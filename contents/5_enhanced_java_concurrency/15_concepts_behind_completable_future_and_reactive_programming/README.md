# 15. Concepts behind CompletableFuture and reactive programming (Java 9)

1. Evolving Java support for expressing concurrency
2. Synchronous and asynchronous APIs
3. The box-and-channel model
4. CompletableFuture and combinators for concurrency
5. Reactive systems vs reactive programming
6. Road map
7. Summary

> ### This chapter covers
>
> - Threads, Futres, 더 풍부한 concurrency API
> - Asynchronous API
> - 동시성 연산에 대한 The boxes-and-channels 관점
> - box를 동적으로 연결하기 위한 CompletableFuture combinator
> - Java 9 Flow API의 기본 : publish-subscribe protocol
> - Reactive programming, Reactive systems

---

### trend

- application을 구동하는 hardware
    - multicore processor의 장점 극대화
- application의 구조 (architecture)
    - monolithic application -> microservices

### _mashup_ application

<img src="img.png"  width="80%"/>

- 1개 이상의 web service를 Internet을 통해 연결
- 응답을 기다리는 시간 (block) 최소화

### Concurrency

<img src="img_1.png"  width="60%"/>

- 동시성을 극대화하기위해 CPU를 최대한 바쁘게 활용해야함
- Java의 방법
    - `Future` interface
    - `CompletableFuture` class (Java 8)
    - `Flow` API (Java 9)

## 1. Evolving Java support for expressing concurrency

- 초기 Java lock : `synchronized` class/method, `java.lang.Runnable`, `java.lang.Thread`
- Java 5 : `java.util.concurrent`
    - `java.util.concurrent.ExecutorService`, `java.util.concurrent.Callable<T>`, `java.util.concurrent.Future<T>`
- Java 7 : `java.util.concurrent.RecurrsiveTask<T>`
    - fork-join framework 구현
- Java 8
    - `Stream` 에 parallelism
    - `Future` 구현체 `java.util.concurrent.CompletableFuture`
- Java 9
    - 명시적인 분산 비동기 프로그래밍 지원
    - **_reactive programming_**, **_publish-subscribe protocol_**
    - `java.util.concurrent.Flow`, `CompletableFuture`

### 1.1 Threads and higher-level abstractions

```
// Java 8 parallel stream
sum = Arrays.stream(stats).parallel().sum();
```

- single CPU computer (concurrent)
    - OS가 process 들을 가상의 주소 공간에 할당
    - process들이 CPU를 공유해서 process user마다 독립된 CPU를 사용하는 것처럼 보임
- multi-core (concurrent + parallel)
    - 단순하게, 4-core는 4배 빠름
    - process, thread를 활용하지 않으면 CPU 효율이 떨어짐 (극대화 못함)

````
// single thread
long sum = 0;
for (int i = 0; i < 1_000_000; i++) {
 sum += stats[i];
}

// mutli-thread : lower-level
// 4 thread (parallel)
long sum = 0;
long sum1 = 0;
...
long sum4 = 0;

// thread 1
for(int i = 0; i < 250_000; i++) {
 sum1 += stats[i];
}

...

// thread 4
for(int i = 750_000; i < 1_000_000; i++) {
 sum4 += stats[i];
}

sum = sum1 + ... + sum4;
````

### 1.2 Executors and thread pools (Java 5)

- Java 5의 Executor framework, thread pools
- Java 개발자들이 task를 만들게 함

#### PROBLEMS WITH THREADS

- Java thread가 OS thread에 **직접** 접근
- OS Thread는 생성/제거 비용이 비쌈, thread 개수가 제한됨
- Java 코드가 hardware spec에 의존 (program이 portable하지 않음)

#### THREAD POOLS AND WHY THEY’RE BETTER

````
// thread pool을 생성하는 factory method
ExecutorService newFixedThreadPool(int nThreads)
````

- `java.util.concurrent.ExecutorService` interface
    - 구현체가 thread pool을 관리
    - factory method로 thread pool을 생성
    - FCFS 알고리즘으로 pool 운용
- programmer는 **_task_** 를 만들고, **_task_** 를 thread pool에 제출
    - **_task_** : `java.lang.Runnable`, `Callable<T>`

#### THREAD POOLS AND WHY THEY’RE WORSE

<img src="img_2.png"  width="70%"/>

- blocking이 가능한 유한한 thread 개수
    - 최대 thread pool의 개수만큼만 동시성 확보 가능
    - 10개의 thread를 관리하는 pool은 10개 task만 동시성 확보 가능
    - blocking이 발생하면, _waiting_ 상태에서 thread가 놀고 있음
    - deadlock 가능성
- 프로그램 종료 전 모든 thread pool을 종료시켜야함
    - Java는 기본적으로 모든 thread가 완료될 때까지 block됨
    - 종료되지 않은 worker thread가 있으면 다른 task를 기다리고 있음
    - main thread가 block되지 않아도 되는 상황에서는 비효율

### 1.3 Other abstractions of threads : non nested with method calls

<img src="img_3.png"  width="70%"/>

<img src="img_4.png"  width="70%"/>

<img src="img_5.png"  width="50%"/>

- **_strict fork/join_** : sub-task가 완료될 때까지 method call이 block됨
- **_relaxed fork/join_** : sub-task는 외부 method call로부터 `join`되어 반환 가능
    - mehtod caller는 해당 동작을 모름
- asynchronous method (Java 8, 9)
    - ongoing thread (실행 상태의 thread) 가 동시적으로 실행되므로 데이터 race condition에 유의
    - Java `main()`이 ongoing thread 종료보다 먼저 return 된다면? (2가지 가능)
        - return 전 모든 ongoing thread의 종료를 기다림 (block)
            - **무한 blocking 가능성**
        - 모든 ongoing thread를 종료
            - **불완전 종료에 따른 문제 발생 가능성**
    - 따라서 thread를 잘 추적해야함

#### **_daemon_**, **_non-daemon_**

- `setDaemon(boolean on)`으로 결정
- Daemon thread : thread 종료 시 killed
    - `main`에서 반환 시, 다른 non-daemon thread의 종료를 기다림

### 1.4 What do you want for threads?

- task switching 비용을 고려하는 한도 내에서, 최대한 많은 thread를 동시성으로 실행
- thread 조작 중복 코드 없이 thread를 조작

## 2. Synchronous and asynchronous APIs

## 3. The box-and-channel model

## 4. CompletableFuture and combinators for concurrency

## 5. Reactive systems vs reactive programming

## 6. Road map

## 7. Summary


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

## 2. Synchronous and asynchronous APIs

## 3. The box-and-channel model

## 4. CompletableFuture and combinators for concurrency

## 5. Reactive systems vs reactive programming

## 6. Road map

## 7. Summary


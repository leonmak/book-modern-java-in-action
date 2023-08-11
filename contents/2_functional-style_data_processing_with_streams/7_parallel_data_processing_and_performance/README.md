# 7. Parallel data processing and performance

1. Parallel streams
2. The fork/join framework
3. Spliterator
4. Summary

> ### This chapter covers
>
> - parallel streams을 활용하여 병렬로 data processing을 하는 방법
> - parallel stream의 성능 분석
> - The fork/join framework
> - `Spliterator`을 사용해서 Data splitting

---

### Java 7 이전의 병렬 처리

- 매우 귀찮음
- 자료구조를 명시적으로 분할
- thread를 직접 생성하고 분할된 자료구조 할당
- 동시성 문제를 피하기 위한 동기화 작업 필요
- 분할된 결과를 합치는 작업 필요
- **Java 7에 fork/join framework가 추가됨**

## 1. Parallel streams

- `parallelStream()` : collection을 기반으로 parallel stream 생성
- parallel stream : 2개 이상의 집합으로 element를 나눈 stream
    - 각 집합을 서로 다른 thread에서 병렬로 처리

#### 병렬 실행 주의점

- 병렬화는 공짜가 아님
- 집합을 나누고 다시 하나로 합치는 작업 필요
- core 간의 communication (값의 이동)이 필요
- 병렬리 빠르지 않은 경우도 있으므로, 정확하게 분석한 뒤 병렬화를 적용할 것

````
public long interativeSum(long n) {
    long result = 0;
    for (long i = 1L; i <= n; i++) {
        result += i;
    }
    return result;
}


public lojng sequentialSum(long n) {
    return Stream.iterate(1L, i -> i + 1)
        .limit(n)
        .reduce(0L, Long::sum);
}
````

### 1.1 Turning a sequential stream into a parallel one

<img src="img.png"  width="80%"/>

````
public long parallelSum(long n) {
    return Stream.iterate(1L, i -> i + 1)
        .limit(n)
        .parallel() // parallel stream으로 변환
        .reduce(0L, Long::sum);
}
````

- `parallel()` : parallel stream으로 변환
- `sequential()` : sequential stream으로 변환

````
stream.parallel() // parallel stream으로 변환
    .filter(...)
    .sequential() // sequential stream으로 변환
    .map(...)
    .parallel() // parallel stream으로 변환
    .reduce(...);
````

#### Configuring the thread pool used by parallel streams

- parallel stream은 default로 `ForkJoinPool`을 사용
- return `Runtime.getRuntime().availableProcessors()` : 사용 가능한 프로세서 수
- 사용할 프로세서 수 직접 설정 : `System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "12")`
    - global 설정₩
    - **비추**

### 1.2 Measuring stream performance

- JMH, Java Microbenchmark Harness : microbenchmark를 위한 framework
- JMH 를 통해 parallel stream과 sequential stream의 성능 비교

````
@Benchmark
public long sequentialSum() {
    return Stream.iterate(1L, i -> i + 1)
            .limit(N)
            .reduce(0L, Long::sum);
}

@Benchmark
public long iterativeSum() {
    long result = 0;
    for (long i = 1L; i <= N; i++) {
        result += i;
    }
    return result;
}

@TearDown(Level.Invocation) // 각각의 벤치마크 실행 이후에 실행
public void tearDown() {
    System.gc();
}




````

<img src="img_1.png"  width="60%"/>

- `iterate()` : **parallel이 더 느림**
    - 서로 독립적인 집합으로 나누기 힘듦
    - `iterate()`는 이전 element를 이용해서 다음 element를 생성

#### USING MORE SPECIALIZED METHODS

````
@Benchmark
public long rangedSum() {
 return LongStream.rangeClosed(1, N)
 .reduce(0L, Long::sum);
}
````

```bash
Benchmark                          Mode  Cnt     Score    Error  Units
ParallelStreamBenchmark.rangedSum  avgt   10  3362.089 ± 22.867  us/op
```

- `sequentialSum()` 보다 빠름
    - autoboxing, auto-unboxing이 없음

````
@Benchmark
public long parallelRangedSum() {
    return LongStream.rangeClosed(1, N)
            .parallel()
            .reduce(0L, Long::sum);
}
````

```bash
Benchmark                                  Mode  Cnt    Score    Error  Units
ParallelStreamBenchmark.parallelRangedSum  avgt    10  496.916 ± 11.518  us/op
```

- 6배 빠름
- 병렬를 위해 독립적인 집합으로 나누기 쉬움

### 1.3  Using parallel streams correctly

- mutalbe state를 병렬 실행 시 사용하면 문제 발생

````
public long sideEffectSum(long n) {
  Accumulator accumulator = new Accumulator();
  
  LongStream.rangeClosed(1, n)
             .parallel() // 문제 원인
             .forEach(accumulator::add);
  
  return accumulator.total;
}

public class Accumulator {
  public long total = 0; // 상태값
  public void add(long value) { total += value; }
}

...

System.out.println("SideEffect parallel sum :"+ sideEffectParallelSum(10_000_000));
System.out.println("SideEffect parallel sum :"+ sideEffectParallelSum(10_000_000));
System.out.println("SideEffect parallel sum :"+ sideEffectParallelSum(10_000_000));

````

```baash
SideEffect parallel sum :6448280623056
SideEffect parallel sum :5006590315172
SideEffect parallel sum :5188848063192
```

- 병렬로 실행 시 `total`에 경합 발생
- 값이 매번 다름

### 1.4 Using parallel streams effectively

| Source            | Decomposability |
|-------------------|-----------------|
| `ArrayList`       | Excellent       |
| `LinkedList`      | Poor            |
| `IntStream.range` | Excellent       |
| `Stream.iterate`  | Poor            |
| `HashSet`         | Good            |
| `TreeSet`         | Good            |

- 성능측정 : parallel stream을 사용할 때와 사용하지 않을 때를 비교
    - parallel이 항상 빠르지 않음
    - parallel이 예상과 다르게 동작 할 수 있음
- boxing 지양
    - auto-boxing/unboxing은 성능에 영향을 줌
    - Java 8의 primitive streams 사용 e.g. `IntStream`, `DoubleStream`, `LongStream`
- 일반적으로 parallel 성능이 안좋은 operation
    - `limit()`, `findFirst()`, `findFirst()` : element의 순서에 의존
    - `findAny()` : element의 순서에 의존하지 않음, 병렬로 실행 시 빠름
    - `unordered()` : 순서 정렬되어있는 stream을 unordered stream으로 변환
- stream 파이프라인의 총 연산 비용 고려
    - N : element의 수, Q : 각 element를 처리하는데 걸리는 시간 일 때,
        - Q가 상대적으로 더 크다면 parallel stream 적합
- 데이터 양이 적은 경우, parallel stream 사용하지 않는 것이 좋음
    - parallel stream overhead가 더 큼
- parallel stream 아래에서 자료구조가 잘 분할 될 수 있는지
    - `ArrayList`는 잘 분할 될 수 있지만, `LinkedList`는 잘 분할 될 수 없음
    - `range()`로 반환된 primitive streams은 잘 분할됨
    - 커스텀 `Spliterator`를 사용하는 stream은 잘 분할됨
- Stream의 특성과 파이프라인을 지나며 어떻게 수정되는지 파악
    - `filter()`는 stream의 크기를 알 수 없음 (분할이 어려움)
    - 사이즈를 아는 stream은 분할이 쉬움
- 병합 비용 고려
    - 병합 비용이 크다면 parallel stream overhead가 더 커짐
    - e.g. `Collector.combiner()` 비용

## 2. The fork/join framework

<img src="img_2.png"  width="80%"/>

- 병렬화 가능한 task를 재귀적으로 작은 task로 분할하고, 결과를 합치는 프레임워크
- `ExecutorService`의 구현체
    - `ForkJoinPool`의 thread pool에게 subtask를 할당

### 2.1 Working with RecursiveTask

- `RecursiveTask<R>`의 subclass를 생성
    - `R` : task (subtask)의 결과 타입

````
protected abstract R compute();

...

// pseudo code
if( task is small enough or no longer divisible ) {
    sequentially solve the task
} else {
    split the task into two subtasks
    recursively invoke compute() on subtasks
    combine the results of the subtasks
}
````

````java
public class ForkJoinSumCalculator extends RecursiveTask<Long> { // fork/join framework 사용

    private final long[] numbers; // 합계를 구할 배열

    // subtask의 array 범위
    private final int start;
    private final int end;

    // subtask로 분할할 최소 배열 크기
    public static final long THRESHOLD = 10_000;

    // main task 생성시 사용
    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    // subtask 생성시 사용
    public ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    // recursive task의 compute 메서드 구현
    @Override
    protected Long compute() {
        int length = end - start;

        // 기준값보다 작으면 순차적으로 계산
        if (length <= THRESHOLD) {
            return computeSequentially();
        }

        // subtask 생성
        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length / 2);
        leftTask.fork(); // 비동기 실행

        // subtask 생성
        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length / 2, end);
        Long rightResult = rightTask.compute(); // 동기 실행
        Long leftResult = leftTask.join();//  block : left task의 결과가 나올때까지 기다림

        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++)
            sum += numbers[i];
        return sum;
    }
}

````

````
private static Long forkJoinSum(long n) {
    long[] numbers = LongStream.rangeClosed(1, n).toArray();
    ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
    return new ForkJoinPool().invoke(task);
}

... 

Long result = forkJoinSum(10000000L);
````

- 같은 app에서 2개 이상의 `ForkJoinPool`을 사용하지 않는 것이 좋음
- `ForkJoinPool`은 인스턴스화 해서 static field에 관리하는 것이 좋음 (singleton)
- `new ForkJonPool()` : `Runtime.getRuntime().availableProcessors()` 만큼의 thread를 생성

#### RUNNING THE FORKJOINSUMCALCULATOR

<img src="img_3.png"  width="80%"/>

- 각 thread들이 task의 `compute()` 실행
- subtask로 이루어진 binary tree 형태로 task들이 생성
    - 실행이 완료되면 root 노드로 결과가 합쳐짐

### 2.2 Best practices for using the fork/join framework

- `join()`은 blocking operation
    - 반드시 2개의 subtask가 시작된 후에 호출
- `invoke()`은 `RecursiveTask` 안에서 사용되면 안됨
    - 안에서는 `compute()`, `fork()`를 호출
- sequential code만 `invoke()`를 호출해야함
- subtask에서 `fork()` : `ForkJoinPool`에 스케쥴
    - 같은 thread 재활용
    - thread pool에 불필요한 task 할당 방지
- fork/join framework를 활용한 parallel computation은 디버깅이 어려움
    - `compute()`가 `fork()`를 실행한 thread와 다른 thread에서 실행
- fork/join framework의 multi-core 연산이 sequential보다 빠름을 보장히지 않음
    - `warm-up`이 필요 (JIT compiler)
- subtask 분할 기준 필요

### 2.3 Work stealing

- 많은 subtask를 생성하는 것이 일반적으로 성능상 유리
- 실제 상황에선 Core수 외에 많은 변수가 존재
    - e.g. I/O bound, CPU bound

#### _work stealing_

<img src="img_4.png.png"  width="80%"/>

- task들이 고르게 분배되지 않는 문제를 해결
- `ForkJoinPool`의 thread들에게 고르게 task를 할당할 수 있음
- thread를 doubly linked queue를 가짐
    - 작업이 완료되면 queue에서 다음 작업을 가져와 실행
    - 자신의 queue가 비면 다른 thread의 queue에서 작업을 가져와 실행 **(steal)**
    - 모든 thread의 queue가 빌때까지 반복

## 3. Spliterator

## 4. Summary

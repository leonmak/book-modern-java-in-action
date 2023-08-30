# 16. CompletableFuture: composable asynchronous programming

1. Simple use of Futures
2. Implementing an asynchronous API
3. Making your code nonblocking
4. Pipelining asynchronous tasks
5. Reacting to a CompletableFuture completion
6. Read map
7. Summary

> ### This chapter covers
>
> - 비동기 연산을 만드는 방법과 결과 반환
> - nonblocking 연산을 만들어 생산량 향상
> - 비동기 API 설계, 구현
> - 동기 API를 비동기적으로 사용
> - 2개 이상의 비동기 연산 pipelining, merging
> - 비동기 연산 결과에 react

---

## 1. Simple use of Futures

- `java.util.concurrent.Future` : 미래 특정 시점에 result를 반환하는 연산
- request 즉시 result를 반환하지 않음
- 비동기 연산 설계, 연산 수행 result에 대한 참조 제공
- 시간이 걸리는 연산을 `Future`에게 위임하고, 다른 작업 수행
    - e.g. 세탁기를 돌리는 동안 다른 일을 함

````
// Java 8 이전
ExecutorService executor = Executors.newCachedThreadPool();

// 비동기 연산 시작
Future<Double> future = executor.submit(new Callable<Double>() {
    @Override
    public Double call() throws Exception {
        return doSomeLongComputation(); // 시간이 걸리는 연산
    }
});

doSomethingElse(); // 다른 작업 수행

try {
  Double result = future.get(1, TimeUnit.SECONDS); // 연산 결과를 가져옴 (blocking)
} catch (ExecutionException e) {
    // 계산 중 예외 발생
} catch (InterruptedException e) {
    // 현재 스레드에서 대기 중 인터럽트 발생
} catch (TimeoutException e) {
    // TimeUnit.SECONDS 동안 결과가 없음
}
````

<img src="img.png"  width="70%"/>

### 1.1 Understanding Futures and their limitations

#### 복잡한 비동기 연산 요구사항 (`Future`로 해결 불가)

- 2가지 비동기 연산이 서로 독립적이거나 아닐 수 있음
- `Future`s 연산들이 모두 완료되기를 기다림
- `Future`s 연산들 중 가장 빨리 완료되는 연산만을 기다림
- 비동기 연산 결과를 수동으로 반환하여 `Future`를 완료시킴
- `Future` 연산 결과에 react
    - 완료되면 알림을 받고 추가 action
- `Future`의 구현체 `CompletableFuture`는 이러한 요구사항을 declarative하게 표현, 지원

### 1.2 Using CompletableFuture to build an asynchronous application

#### 구현 예시 : 최저가 찾기 Application

- 사용자에게 비동기 API를 제공하는 방법
- 동기 API 사용자일때 nonblocking code를 만드는 방법
    - 2개의 동기 연산을 pipelining 하고 결과를 하나의 동기 연산으로 merge
- 비동기 연산이 완료되었을 때 reactive하게 동작
    - e.g. 상품별 최저가를 지속적으로 update

#### Synchronous vs asynchronous API

- synchronous API (_blocking_ call)
    - caller는 result를 기다림
    - caller 와 callee가 다른 threa에 있어도, caller는 기다림
- asynchronous API (_nonblocking_ call)
    - 계산이 완료되기 전에 return (남은 계산은 thread에게 위임)
    - callback method를 통해 caller와 소통
    - I/O system programmion에 적합

## 2. Implementing an asynchronous API

````java
public class Shop {

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    private double calculatePrice(String product) {
        delay(); // 1초간 delay
        return new Random().nextDouble() * product.charAt(0) + product.charAt(1);
    }
}

````

- 사용자가 `getPrice()`를 호출하면, blocking되어 1초 이상 소요 후 결과 반환
- 100개의 상품을 조회하면 100초 이상 소요

### 2.1 Converting a synchronous method into an asynchronous one

```
// public double getPrice(String product) { .. .}
public Future<Double> getPriceAsync(String product) { ... }
```

````
public Future<Double> getPriceAsync(String product) {
    CompletableFuture<Double> futurePrice = new CompletableFuture<>();
    new Thread(() -> {
        double price = calculatePrice(product);
        futurePrice.complete(price);
    }).start();
    return futurePrice;
}
````

- `getPrice()`를 비동기로 만듦

```

@Test
public void tst1() throws ExecutionException, InterruptedException {
    Shopp shop = new Shopp("Best Shop");

    long start = System.nanoTime();
    Future<Double> futurePrice = shop.getPriceAsync("IPhone 12"); // 즉시 return & 비동기 연산 시작
    long invocationTime = ((System.nanoTime() - start) / 1_000_000);
    System.out.println("Invocation returned after " + invocationTime + " msecs");

    double price = futurePrice.get(); // blocking (연산이 완료될 때까지 기다림)
    System.out.printf("Price is %.2f%n", price);

    long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
    System.out.println("Price returned after " + retrievalTime + " msecs");
}

```

<details>
<summary>실행 결과</summary>

```bash
Invocation returned after 0 msecs
Price is 112.66
Price returned after 1020 msecs
```

</details>

### 2.2 Dealing with errors

```
public Future<Double> getPriceAsync(String product) {
    CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread( () -> {
            try {
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch (Exception ex) {
                futurePrice.completeExceptionally(ex); // 에러 발생시 exception을 포함하여 complete
            }
        }).start();
    return futurePrice;
}
```

- `completeExceptionally()` : `CompletableFuture`에 에러를 포함하여 complete
- client는 `get()`을 호출할 때 `ExecutionException`을 받음

<details>
<summary>예외 발생시 console</summary>

```bash
java.util.concurrent.ExecutionException: java.lang.RuntimeException: [에러 메시지 예시]
  at java.base/java.util.concurrent.CompletableFuture.reportGet(CompletableFuture.java:396)
  at java.base/java.util.concurrent.CompletableFuture.get(CompletableFuture.java:2073)
...
```

</details>

#### CREATING A COMPLETABLEFUTURE WITH THE SUPPLYASYNC FACTORY METHOD

````
public Future<Double> getPriceAsync(String product) {
    return CompletableFuture.supplyAsync(() -> calculatePrice(product));
}
````

- `supplyAsync()` : `Supplier`를 인수로 받아 `CompletableFuture`를 rturn
- 비동기로 Supplier를 실행
- `Supplier`는 ForkJoinPool의 `Executor`를 사용하여 실행
- 예외처리도 동일하게 구현되어있음

## 3. Making your code nonblocking

````
private List<Shop> shops = List.of(new Shop("11번가"), new Shop("G마켓"), new Shop("옥션"), new Shop("쿠팡"), new Shop("위메프"));
public List<String> findPrices(String product) {
    return shops.stream()
            .map(shop -> String.format("%s price is %.2f",
                    shop.getName(), shop.getPrice(product)))
            .collect(toList());
}

@Test
public void tst2() {
    long start = System.nanoTime();
    System.out.println(findPrices("Aespa - MY WORLD (정규 3집)"));
    long duration = (System.nanoTime() - start) / 1_000_000;
    System.out.println("Done in " + duration + " msecs");
}
````

<details>
<summary>실행 결과</summary>

```bash
[11번가 price is 156.03 
, G마켓 price is 131.27 
, 옥션 price is 142.56 
, 쿠팡 price is 133.57 
, 위메프 price is 101.21 
]
Done in 5044 msecs
```

</details>
- `calculatePrice()`를 동기적으로 실행
- 5초 이상 (shop 수 만큼) 소요

### 3.1 Parallelizing requests using a parallel Stream

````
public List<String> findPrices(String product) {
    return shops.parallelStream()
            .map(shop -> String.format("%s price is %.2f",
                    shop.getName(), shop.getPrice(product)))
            .collect(toList());
}

````

- `Done in 1038 msecs`

### 3.2 Making asynchronous requests with CompletableFuture

````
public List<String> findPrices(String product) {

    List<CompletableFuture<String>> priceFutures = shops.stream()
            .map(shop -> CompletableFuture.supplyAsync(() -> String.format("%s price is %.2f",
                    shop.getName(), shop.getPrice(product))))
            .collect(toList())
            .stream()
            // .map(CompletableFuture::join) // join을 사용하면 supplyAsync() 완료될 때까지 블록킹
            .collect(toList());

    return priceFutures.stream()
            .map(CompletableFuture::join)
            .collect(toList());
}
````

<img src="img_1.png"  width="70%"/>

### 3.3 Looking for the solution that scales better

#### thread 수에 따른 성능 변화 (thread 5개 가정)

| shop | synchrnous | parallelStream | CompletableFuture |
|------|------------|----------------|-------------------|
| 2    | 2 sec      | 1 sec ~        | 1 sec ~           |
| 5    | 5 sec      | 1 sec ~        | 1 sec ~           |
| 6    | 6sec       | 2 sec ~        | 2 sec ~           |

- parallelStream, `CompletableFuture` 모두 `Runtime.getRuntime().availableProcessors()` 수만큼 thread를 사용
- `CompletableFuture`은 직접 executor를 지정 할 수 있음

### 3.4 Using a custom Executor

> #### thread pool 사이즈 정하는 공식 (book _Java Concurrency in Practice_ Addison-Wesley, 2006)
>
> N<sup>threads</sup> = N<sup>CPU</sup> * U<sub>CPU</sub> * (1 + W/C)
>
> - N<sup>CPU</sup> : CPU 수 (코어 수, `Runtime.getRuntime().availableProcessors()`)
> - U<sub>CPU</sub> : CPU 사용률 (0 ~ 1)
> - W/C : wait time / compute time
> - e.g. 4 core | 100% CPU 사용 | 대기 50ms, 계산 5ms
> - N<sup>threads</sup> = 4 * 1 * (1 + 50/5) = **44**

````
private final Executor customExecutor 
  = Executors.newFixedThreadPool(Math.min(shops.size(), 44) // shop size < thread pool size < 44
    , (Runnable r) -> {
        Thread t = new Thread(r);
        t.setDaemon(true); // daemon thread : main thread가 종료되면 함께 종료되는 thread
        return t;
    });
    
...

CompletableFuture.supplyAsync(() -> String.format("%s price is %.2f",
                    shop.getName(), shop.getPrice(product))
                    , customExecutor); // custom executor 지정

````

##### Parallelism: via Streams or CompletableFutures?

|     | Stream                              | CompletableFuture                          |
|-----|-------------------------------------|--------------------------------------------|
| 사용처 | I/O 없는 무거운 계산                       | I/O waiting이 포함된 연산                        |
| 특징  | Stream API 사용, 간단한 구현               | thread pool 사이즈 지정 가능                      |
| 단점  | I/O 가 있으면 Stream의 lazy로 인해 디버깅이 어려움 | 코드가 길어짐, hardware 의존적 (thread pool 사이즈 지정) |

## 4. Pipelining asynchronous tasks

## 5. Reacting to a CompletableFuture completion

## 6. Read map

## 7. Summary
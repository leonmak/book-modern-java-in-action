# 21. Conclusions and where next for Java

1. Review of Java 8 features
2. The Java 9 module system
3. Java 10 local variable type inference
4. What's ahead for Java?
5. Moving Java forward faster
6. The final word

> ### This chapter covers
>
> - Java 8 features, programming style
> - Java 9 module system
> - 6개월 주기 Java release cycle
> - Java 10
> - Java 에 추가될 기능들


---

## 1. Review of Java 8 features

- 코드가 빠르게 실행되어야함 (병렬)
    - multicore processor 필요
- 데이터 collection을 declarative style로 다룸

### 1.1 Behavior parameterization (lamdas and method references)

- `filter()`에 필터 조건을 파라미터로서 넘겨줌
    - 필터 조건 : 메서드로 표현됨
- `Function<T, R>`, `Predicate<T>`, `Consumer<T>`, `Supplier<T>` 등의 함수형 인터페이스를 사용

````
// lamda
idol -> idol.getAge() > 20;

// method reference
Idol::isOlderThan20
````

### 1.2 Streams

- lamda를 사용하여 db query와 같이 collection을 다룸
- Streams는 pipeline을 만들어 여러 연산을 한번의 탐색으로 처리
- stream을 parallel로 처리할 수 있음

### 1.3 CompletableFutures

- Java 5의 `Future`
    - `get()` : 결과를 기다림
- `CompletableFutures` : Java 8의 `Future` 구현체
    - lamda 사용
- `thenCompose()`, `thenCombine()`, `allOf()` 등으로 함수형 프로그래밍 가능

### 1.4 Optional

- `Optional<T>` : `T`를 리턴하거나 없으면, `Optional.empty()`를 리턴
- `null` pointer : null check를 해야함
- `map()`, `filter()`, `ifPresent()` 등의 메서드를 사용하여 함수형 프로그래밍 가능

### 1.5 Flow API (Java 9)

- reactive streams, backpressure 프로토콜을 표준화
- `Publisher<T>`, `Subscriber<T>`, `Subscription`, `Processor<T, R>` 인터페이스

### 1.6 Default methods

- 인터페이스에 메서드를 추가할 수 있음
- 라이브러리 개발자들에게 유용

## 2. The Java 9 module system

- `Stream`의 `takeWhile()`, `dropWhile()` 등의 메서드 추가
- `CompletableFuture`의 `completeOnTimeout()` 등의 메서드 추가

### 메인은 module system을 제공한 것

- 패키지를 모듈로 묶어 캡슐화
- 패키지의 접근제어는 오직 `public`이었음
    - 시스템을 적절하게 모듈화하는데 힘들었음 (무엇이 public 패키지이고, 무엇이 private 패키지인지)
- 언어 수준에서 Java Runtime을 IoT, 클라우드 등의 환경에서 실행될 수 있도록 도와줌

### Java Module system의 장점

- _Reliable configuration_ : 명시적으로 모듈간의 의존성을 정의
    - runtime이 아닌 build time에 의존성을 체크
- _Strong encapsulation_ : 모듈은 자신의 API를 정의하고, 다른 모듈은 해당 API에만 접근 가능
- _Imporved security_ : 사용자가 모듈의 특정 부분을 맘대로 호출 불가능
- _Better performance_ : 모듈간의 의존성을 정의하므로, 불필요한 모듈을 로드하지 않음
- _Scalability_ : Java SE platform이 더 작은 부분으로 나눠질 수 있음

## 3. Java 10 local variable type inference

- compiler 가 타입을 추론해내는 것

````
Map<String, List<String>> myMap = new HashMap<String, List<String>>();
Map<String, List<String>> myMap = new HashMap<>(); // Java 7

Function<Integer, Boolean> myFunction = (Integer x) -> x > 10;
Function<Integer, Boolean> myFunction = (x) -> x > 10;

// Java 10
// local variable type inference
var myMap = new HashMap<String, List<String>>();

var car = new Car(); // Car 타입으로 추론

public class Vehicle {
    private int wheels;

    public Vehicle(int wheels) {
        this.wheels = wheels;
    }
}

public class Car extends Vehicle {
    public Car() {
        super(4);
    }
}
````

## 4. What's ahead for Java?

## 5. Moving Java forward faster

## 6. The final word


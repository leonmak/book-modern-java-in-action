# 1. Java 8, 9, 10, and 11 : what's happening?

1. So, what's the big story?
2. Why is Java still changing?
3. Functions In Java
4. Streams
5. Default methods and Java M
6. Other good iedas from functional programming
7. Summary

> ### This chapter covers
> - Java의 지속적인 변화
> - computing의 변화
> - Java를 진화하게 만든 이유
> - Java 8, 9의 새로운 기능

---

## 1. So, what's the big story?

java 8의 변화는 java 역사상 가장 크고 중요한 변화

### 더 간결한 코드

````
// java 8 이전, 익명 클래스를 사용하여 정렬
Collections.sort(inventory, new Comparator<Apple>() {
    public int compare(Apple a1, Apple a2) {
        return a1.getWeight().compareTo(a2.getWeight());
    }
});

// java 8
inventory.sort(comparing(Apple::getWeight));
````

### multicore processor

- multi-core가 많아졌으나, Java 프로그램들은 여전히 하나의 core만 사용
- Java 8부터 2개 이상의 core를 사용하는 thread를 사용하도록 권장
- 동시성을 높이고 error 유발을 낮추도록 진화 중
    - Java 5 : thread pool, concurrent collections
    - Java 7 : fork/join framework
    - Java 8 : 더 쉬운 parallelism 구현 방법 제공
    - Java 9 : concurrency-reactive programming 도입

### 변화 요약

- The Streams API
- Techniques for passing code to methods
- Default methods in interfaces

#### Streams API

- Java 8의 새로운 API
- 데이터를 병렬로 실행 + 데이터베이스 query와 비슷한 코드
- `synchronized` 키워드 없이 thread-safe

#### Passing code to methods

- parameterization : method에 코드를 전달하는 기법
    - code 몇 줄만 다르게 구현해야하는 메서드 2개 -> parameterization을 통해 하나의 메서드로 구현 가능
    - 익명 클래스 -> 람다 표현식
- functional-style programming : 함수를 값으로 취급하는 프로그래밍 기법
    
## 2. Why is Java still changing?

## 3. Functions In Java

## 4. Streams

## 5. Default methods and Java M

## 6. Other good iedas from functional programming

## 7. Summary



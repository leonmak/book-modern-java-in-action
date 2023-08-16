# 10. Domain-specific languages using lambdas

1. A specific language for your domain
2. Small DSLs in modern Java APIs
3. Patterns and techniques to create DSLs in Java
4. Real world Java 8 DSL
5. Summary

> ### This chapter covers
>
> - domain-specific languages (DSLs) 의 개념과 형태
> - DSL을 추가하는 것의 장단점
> - 순수 Java 기반 DSL에 대한 JVM의 대안
> - Modern Java의 인터페이스와 클래스에서 볼 수 있는 DSL
> - 효과적인 Java 기반 DSL의 패턴과 기술
> - Java 라이브러리로 효과적인 패턴을 사용하는 법

---

### Domain specific languages (DSLs)

- 특정 도메인에 특화되어있는 목적의 작은 언어
- 특정 도메인 관련 용어 사용
- e.g. Maven, Ant build tools, HTML
- Java 8은 람다를 지원하기 떄문에 비전문가들도 쉽게 DSL을 만들 수 있음

```
// system level code
while (block != null) {
    read(block, buffer)
    for (every record in buffer) {
        if (record.calorie < 400) {
            System.out.println (record.name);
        }
    }
    block = buffer.next();
}

// application level DSL (Java 8)
menu.stream()
    .filter(d -> d.getCalories() < 400)
    .map(Dish::getName)
    .forEach(System.out::println)
```

## 1. A specific language for your domain

- DSL : 특정 비스니스 도메인의 문제해결을 위해 설계된 언어
- 연산과 단어를 특정 도메인에 특화, 제한
- user-friendly DSL
    - 사용자는 lower-level 구현을 하지않음
    - 도메인의 복잡도를 해결
- **_communiciation is king_**
    - 코드의 의도가 명백하게 비 개발자에게 전달 가능해야함
- **_Code is written once but read many times_**
    - 가독성은 유지보수성을 높임

### 1.1 Pros and cons of DSLs

#### DSL의 장점

| 장점                                | 설명                                             |
|-----------------------------------|------------------------------------------------|
| **_Conciseness_**                 | 비즈니스 로직을 캡슐화한 API 코드를 간결하게 함                   |
| **_Readability_**                 | 도메인 특화된 용어들로 가독성이 좋음<br/>조직의 동료들에게 도메인 지식이 공유됨 |
| **_Maintainability_**             | 도메인 특화된 용어들로 유지보수가 쉬움<br/>변경이 잦은 app의 코드에서 유용  |
| **_Higher level of abstraction_** | 도메인과 동일한 수준으로 추상화되어있음<br/>도메인과 관련되지 않은 부분은 숨김  |
| **_Focus_**                       | 특정 도메인에 집중되어있음<br/>비즈니스 로직에만 집중 가능             |
| **_Separation of concerns_**      | 비즈니스 로직과 인프라적인 부분을 분리<br/>유지보수성을 높임            |

### DSL의 단점

| 단점                                 | 설명                                                 |
|------------------------------------|----------------------------------------------------|
| **_Difficulty of DSL design_**     | 도메인 지식을 습득해서 간결한 언어로 풀어내야함                         |
| **_Development cost_**             | DSL을 코드베이스에 추가하는데에 비용이 많이 듦                        |
| **_Additional indirection layer_** | DSL을 사용하면 추가적인 계층이 생김<br/>성능을 위해 계층은 최대한 얇아야함      |
| **_Another language to learn_**    | 새로운 언어를 배워야함                                       |
| **_Hosting-language limitations_** | user-friendly DSL을 설계하는대에 Hosting language의 제약이 있음 |

### 1.2 Different DSL solutions available on the JVM

- Internal DSL (embedded DSL) : hosting language의 최상위 구현 레벨에서 사용
- External DSL (stand-alone DSL) : hosting language와 독립적으로 사용

#### INTERNAL DSL

- Lamda가 도입되면서 Java 8에서는 internal DSL을 쉽게 만들 수 있음
- signal / noise ratio가 높음
- pain java로 DSL을 작성하는 이점
    - Java 로 DSL을 구현하는 것은 외부 DSL을 구현하는 것보다 쉬움
    - 추가적인 compiler 필요 없음
    - 다른 언어를 배울 필요 없음
    - Java IDE의 기능을 그대로 사용
    - 다른 plain java DSL과 호환 가능

````
// Java 8 이전
List<String> numbers = Arrays.asList("one", "two", "three");
numbers.forEach(new Consumer<String>() { // signal
    @Override
    public void accept(String s) {
        System.out.println(s); // signal
    }
});

// Java 8
numbers.forEach((String s) -> System.out.println(s)); // signal
numbers.forEach(System.out::println); // signal
````

### POLYGLOT DSL

- JVM에서는 100개 이상의 언어가 사용 가능 e.g. Groovy, Scala, Kotlin, Clojure
- 단점
    - 공부해야함
    - 2개 이상의 언어로 작성된 소스 빌드를 위한 통합 컴파일러 필요
    - Java가 100 % 호환성을 주장하지만, 약간의 호환성 문제가 있음
        - e.g. Scala와 Java의 Colelction은 호환 안됨

```scala
// Scala 는 `i`이 커도 Stack over flow가 발생하지 않음
def times(i: Int, f: => Unit) = {
   f
   if (i > 1) times(i - 1, f) 
}

// currying
def times(i: Int)(f: => Unit) = {
   f
   if (i > 1) times(i - 1)(f) 
}

...

times(3) {
   println("hello")
}

// Int의 익명클래스 암시적 형변환
implicit def intToTimes(i: Int) = new {
   def times(f: => Unit) = {
      f
      if (i > 1) (i - 1).times(f)
   }
}

...

3 times {
   println("hello")
}
```

#### EXTERNAL DSL

- 완전히 새로운 언어를 처음부터 설계하는 것
- 문법, 의미 정의
- parsing과 parser를 구현하고, 별도의 인프라 구축
- ANTLR : Java와 함께 사용되는 parser 생성기
- 장점
    - 무한한 유연성
    - 도메인에 완전히 특화된 언어 설계
    - 가독성 올라감
    - Java 인프라 코드와 완벽한 독립

## 2. Small DSLs in modern Java APIs

## 3. Patterns and techniques to create DSLs in Java

## 4. Real world Java 8 DSL

## 5. Summary
# 9. Refactoring, testing and debugging

1. Refactoring for improved readability and flexibility
2. Refactoring object-oriented design patterns with lambdas
3. Testing lamdas
4. Debugging
5. Summary

> ### This chapter covers
>
> - 람다 표현식을 이용한 refactoring
> - object-oriented design patterns과 람다 표현식
> - 람다 표현식 테스트
> - 람다와 Streams API의 Debugging

- 기존의 코드를 람다 표현식을 활용해서 refactoring 하는 방법
- lamda를 활용한 디자인 패턴
    - strategy, template method, observer, chain of responsibility, factory
- lamda expression과 Stream API의 테스트, 디버깅

---

## 1. Refactoring for improved readability and flexibility

- refactoring을 통해 코드의 가독성과 유연성을 향상시키는 방법
- refactoring 도구 : lambda expression, method reference, Stream API

### 1.1 Improving code readability

- 가독성은 주관적
- 가독성 : 다른 사람이 얼마나 쉽게 이해 가능한가
- **Java 8 을 사용하여 refactoring 하기**
    - anonymous class -> lambda expression
    - lambda expression -> method reference
    - imperative data processing -> Stream API

### 1.2 From anonymous classes to lambda expressions

- anonymous class and 1개의 asbstract method -> lambda expression
- anonymous class는 장황하고, 에러가 발생하기 쉬움

````
// anonymous class
Runnable r1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello World");
    }
};

// lamda
Runnable r2 = () -> System.out.println("Hello World");
````

#### lamda로 대체하기 어려운 경우

- `this`, `super` 키워드
- `this` : anonymous class의 인스턴스 자신, lamda에선 lamda를 감싸는 클래스의 인스턴스
- enclosing class와 동일한 변수명 사용
- overloading

````
int a = 10;
Runnable r1 = new Runnable() {
    @Override
    public void run() {
        int a = 20;
        System.out.println("Hello World! : "+ a); // Hello World! : 20
    }
};

Runnable r2 = () -> {
    int a = 30; // compile error : Variable 'a' is already defined in the scope
    System.out.println("Hello World");
};

public interface Task {
    public void execute();
}

public static void doSomething(Runnable r) { r.run(); }
public static void doSomething(Task a) { a.execute(); }

// anonymous class
doSomething(new Task() {
    @Override
    public void execute() {
        System.out.println("Danger danger!!");
    }
});

// lamda
doSomething(() -> System.out.println("Danger danger!!")); // compile error : reference to doSomething is ambiguous
doSomething((Task)() -> System.out.println("Danger danger!!")); // OK
````

### 1.3 From lamda expressions to method references

````
// lamda
Map<Member.AgeLevel, List<Member>> byTeam = memberList.stream()
    .collect(groupingBy(member -> {
        member.getAgeLevel();
    }));
    
// method reference
Map<Member.AgeLevel, List<Member>> byTeam = memberList.stream()
    .collect(groupingBy(Member::getAgeLevel));
    
// lamda
memberList.sort((Member m1, Member m2) -> m1.getAge().compareTo(m2.getAge()));

// method reference
memberList.sort(comparing(Member::getAge));

// lamda
int totalAge = memberList.stream()
  .map(Member::getAge).reduce(0, (a, b) -> a + b);
  
// method reference + reduction operation + built-in helper method
int totalAge = memberList.stream()
 .collect(summingInt(Member::getAge));
````

- helper static method : `comparing()`, `maxBy()`, ...
- reduction operation : `sum()`, `maximum()`, `average()`, ...
    - built-in helper method : `summingInt()`, `maxBy()`, `averagingInt()`, ...

### 1.4 From imperative data processing to Streams

- collection을 활용해서 데이터를 처리 -> Stream API
- 의도가 명확, Streams API 내부적으로 이미 최적화 되어있음
    - short-circuiting, lazy evaluation, parallel processing
- 제어 흐름을 못함 (`break()`, `continue()`, `return`)

````
// imperative data processing
List<String> memberAespa = new ArrayList<>();
for(Member member : memberList) {
    if(member.getTeam().equals(Aespa)) {
        memberAespa.add(member.getName());
    }
}

// Streams API
memberList.stream()
    .filter(member -> member.getTeam().equals(Aespa))
    .map(Member::getName)
    .collect(toList());
````

### 1.5 Improving code flexibility

#### ADOPTING FUNCTIONAL INTERFACES

- lamda는 functional interface 없이 사용 불가능
- lamda 표현식을 위한 코딩 패턴
    - conditional deferred execution
    - execute around

#### CONDITIONAL DEFERRED EXECUTION

````
// 문제점 : client code에서 logging 조건 확인 (코드 중복)
if(logger.isLoggable(Log.FINER)) {
    logger.finer("Problem: " + generateDiagnostic());
}

// 문제점 : logging하지 않아도, generateDiagnostic() 메서드는 실행
logger.log(Level.FINER, "Problem: " + generateDiagnostic());

// 내부적으로 conditional deferred execution
logger.log(Level.FINER, () -> "Problem: " + generateDiagnostic()); // Supplier<String>를 인자로 받음
````

#### EXECUTE AROUND

````

@FunctionalInterface
public interface MemberProcessor {
    String process(Member member);
}

public static String processMember(MemberProcessor p) {
    Member member = memberService.getFavoriteMember(); // DB Connection
    return p.process(member);
}

String nameWtihTeam = processMember((Member member) -> member.getName() + " | " + member.getTeam());
String nameWithAge = processMember((Member member) -> member.getName() + " | " + member.getAge());
String nameWIthTeanAndAge = processMember((Member member) -> member.getName() + " | " + member.getTeam() + " | " + member.getAge());
````

## 2. Refactoring object-oriented design patterns with lambdas

## 3. Testing lamdas

## 4. Debugging

## 5. Summary
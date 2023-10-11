# 20. BLending OOP and FP: comparing Java and Scala

1. Introduction to Scala
2. Functions
3. Classes and traits
4. Summary

> ### This chapter covers
>
> - Scala 소개
> - Scala와 Java
> - Scala의 함수
> - Scala의 클래스와 트레이트

---

### Scala?

- Scala는 객체 지향과 functional programming을 섞은 언어
- Java 보다 많은 기능 지원 (type 추론, 패턴 매칭)
- Scala code에서 Java library 사용 가능

## 1. Introduction to Scala

### 1.1 Hello beer

#### IMPERATIVE-SYTLE SCALA

```scala
object Beer {
    def main(args: Array[String]){
        var n : Int = 2
        while( n <= 6){
            println(s"Hello ${n} bottles of beer")
            n += 1
        }
    }
}
```

#### FUNCTIONAL-STYLE SCALA

```java
public class Foo {
    public static void main(String[] args) {
        IntStream.rangeClosed(2, 6)
                .forEach(n -> System.out.println("Hello " + n +
                        " bottles of beer"));
    }
}
```

```scala
object Beer {
    def main(args: Array[String]){
        2 to 6 foreach { n => println(s"Hello ${n} bottles of beer") }
    }
}
```

- Scala에는 기본형이 없음 (primitive type)

### 1.2 Basic data structures : List, Set, Map, Tuple, Stream, Option

#### CREATING COLLECTIONS

```
// Java
Map<String, Integer> idolAge = new HashMap<>();
idolAge.put("IU", 25);
idolAge.put("Karina", 21);
idolAge.put("Winter", 20);

// Java 9
Map<String, Integer> idolAge = Map.ofEntries(
    Map.entry("IU", 25),
    Map.entry("Karina", 21),
    Map.entry("Winter", 20)
);

// scala
val idolAge = Map("IU" -> 25, "Karina" -> 21, "Winter" -> 20)
val idols = List("IU", "Karina", "Winter")
val ages = Set(25, 21, 20)
```

- `val` : read-only variable (Java `final`)
- `var` : read-write variable

#### IMMUTABLE vs MUTABLE

- Scala의 `val` 타입은 _peristent_ data structure
- `val` 수정 시 새로운 객체를 생성하고 참조를 변경

```scala
val aespa = List("Karina", "Winter", "Ningning", "Giselle")
val favorites = aespa + "IU"
println(aespa) // List(Karina, Winter, Ningning, Giselle)
println(favorites) // List(Karina, Winter, Ningning, Giselle, IU)  
```

#### Unmmodifiable vs immutable (Java)

````
Set<Integer> numbers = new HashSet<>();
Set<Integer> unmodifiableNumbers = Collections.unmodifiableSet(numbers);
````

- `unmodifiableNumbers` 에 새로운 element를 추가하려고 하면 `UnsupportedOperationException` 발생
- `numbers`에는 추가 가능

#### WORKING WITH COLLECTIONS

- Scala 의 colleciton은 Java Stream API 처럼 동작

```scala
val fileLines = Source.fromFile("data.txt").getLines.toList()

val linesLongUpper
    = fileLines.filter(l => l.length() > 10) // filter : 10글자 이상인 line만 남김
        .map(l => l.toUpperCase()) // map : line을 대문자로 변환
        
val linesLongUpper
    = fileLines filter (_.length() > 10) map(_.toUpperCase())
```

- `_` : anonymous function의 parameter를 대체
- `_.length()` = `x => x.length()`

<img src="img.png"  width="70%"/>

#### TUPLES

- Java는 Tuple 지원 안해서, `Pair` class를 직접 만들어야 함

````
public class Pair<X, Y> {
    public final X x;
    public final Y y;

    public Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }
}

Pair<String, Integer> karina = new Pair<>("Karina", 21);
Pair<String, Integer> winter = new Pair<>("Winter", 20);
````

- _tuple literals_ : scala가 지원하는 tuple

```scala
val karina = ("Karina", 21)
val winter = ("Winter", 20)

val karinaWithHeight = ("Karina", 21, 168)
val witherWithGroup = ("Winter", 20, "aespa")

println(karina._1) // Karina
println(karinaWithHeight._3) // 168
```

#### STREAM

- Scala Stream은 Java 보다 더 많은 기능을 지원
- 이전에 연산된 element를 기억하고 접근 가능 (cached)
- list 처럼 index를 통해 접근 가능
- trade-off : Java 의 Stream 보다 메모리 효율이 떨어짐

#### OPTION

- Java의 `Optional`과 비슷

````
// Java
public String getCarName (Optional<Idol> optIdol){
    return optIdol
    .flatMap(Idol::getLeader)
    .flatMap(Leader::getCar) 
    .map(Car::getName) 
    .orElse("Unknown");
} 
    
// Scala
def getCarName(optIdol: Option[Idol]): String = {
    optIdol
    .flatMap(_.getLeader)
    .flatMap(_.getCar)
    .map(_.getName)
    .getOrElse("Unknown")
}
````

## 2. Functions

- Java 의 _method_
- Scala _Function types_
- Anonymous functions
- _currying_ : 함수를 여러개의 argument를 받는 함수로 분리

### 2.1 First-class functions in Scala

- Scala에서 Function은 _first-class value_
    - paramter로 전달, 변수에 저장 가능

```scala
def isMentionedKarina(tweet: String): Boolean = tweet.contains("Karina")
def isMentionedShort(tweet: String): Boolean = tweet.length() < 20

// built-in filter
val tweets = List("Karina is so cute"
  , "Winter is so cool"
  , "We all love aespa wow!")

twwets.filter(isMentionedKarina).foreach(println)
twwets.filter(isMentionedShort).foreach(println) 
```

```scala
// filter의 signature
def filter[T](p: (T) => Boolean): List[T]
```

- paramter `p` : _function type_ `(T) => Boolean`
    - Java의 `Predicate<T>` 와 비슷

### 2.2 Anonymous functions and closures

- Java의 lamda expression과 비슷

```scala
//Java
Function<Stirng, Boolean> isMentionedKarinaObj
  = tweet -> tweet.contains("Karina")
isMentionedKarinaObj.apply("Karina is so cute") // true

//Scala
val isMentionedKarinaObj : String => Bollean
  = new Function1[String, Boolean] {
    def apply(tweet: String): Boolean = tweet.contains("Karina")
}

isMentionedKarinaObj.apply("Karina is so cute") // true
```

- trait : Java의 functional interface와 비슷
    - `Function0` : parameter가 없는 function
    - `Function22` : parameter가 22개인 function

```scala
val isMentionedKarina : String => Boolean = tweet => tweet.contains("Karina")

isMentionedKarina("Karina is so cute") // true
```

- compiler가 묵시적으로 `apply` method를 추가

#### CLOSURES

- `closure` : function의 instance
    - non-local variable을 참조 가능
- Java의 lamda expression은 method local variable 수정 불가능

```scala
def main(args: Array[String]) {
  var count = 0
  val inc = () => count+=1
  inc()
  println(count) // 1
  inc()
  println(count)  
}

// Java
public static void main(String[] args) {
    int count = 0;
    Runnable inc = () -> count++; // compile error : count must be final or effectively final
    inc.run();
    System.out.println(count); 
    inc.run();
    System.out.println(count); 
}
```

### 2.3 Currying

````
// Java
static int multiply(int x, int y) {
  return x * y;
}
int r = multiply(2, 10);

// currying
static Function<Integer, Integer> multiply(int x) {
  return y -> x * y;
}

Stream.of(1, 3, 5, 7)
  .map(multiply(2))
  .forEach(System.out::println); // 2, 6, 10, 14
````

```scala
def multiply(x : Int, y: Int) = x * y
val r = multiply(2, 10)

// currying
def multiplyCurry(x : Int)(y : Int) = x * y
val r = multiplyCurry(2)(10) 
println(r) // 20
```

- `multiplyCurry(2)` : 매개변수 y를 받아서 x를 곱하는 함수를 반환
- `multiplyCurry(2)(10)` : 2를 곱하는 함수에 10을 적용

## 3. Classes and traits

## 4. Summary
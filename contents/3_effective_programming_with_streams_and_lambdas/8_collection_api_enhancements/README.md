# 8. Collection API enhancements (Java 9)

1. Collection factories
2. Working with List and Set
3. Working with Map
4. Improved ConcurrentHashMap
5. Summary

> ### This chapter covers
>
> - collection factory 사용
> - List, Set을 사용한 새로운 idomatic pattern
> - Map을 사용한 idomatic pattern

---

- Java 9에 추가된 Collection factory
    - 작은 list, set, map을 생성하는데 사용
- idomatic removal / replacement pattern
- map을 편리하게 사용하는 방법

##                                                                      

````
List<String> aespa = new ArrayList<>();
aespa.add("Karina");
aespa.add("Winter");
aespa.add("Giselle");
aespa.add("Ningning");

// Arrays.asList()를 사용하여 더 간단하게 작성
List<String> aespa = Arrays.asList("Karina", "Winter", "Giselle", "Ningning");

aespa.set(0, "KARINA"); 
aeps.add("newMember"); // thorws UnsupportedOperationException

Set<String> ive = new HashSet<>(Arrays.asList("Yujin", "Wonyoung"));
ive.add("newMember"); // mutalbe
````

- `Arrays.asList()` : 고정된 사이즈의 list를 생성
    - `add()`, `remove()` 등 사용할 수 없음
    - `set()` 사용 가능
- Set은 mutable

### 1.1 List factory

````
List<String> aespa = List.of("Karina", "Winter", "Giselle", "Ningning");
aespa.add("newMember"); // throws UnsupportedOperationException
````

- `List.of()` : immutable list 생성
    - `add()`, `remove()`, `set()` 시 예외 발생

#### Overloading vs varargs

```java

package java.util;

public interface List<E> extends Collection<E> {
    // ...
    static <E> List<E> of(E e1) {
        // ...
    }

    static <E> List<E> of(E e1, E e2) {
        // ...
    }

    //...

    static <E> List<E> of(E... elements) {
        return new ImmutableCollections.ListN<>(elements);
    }

}

````

- `List.of()`, `Set.of()`, `Map.of()`는 varargs, overloading 둘 다 사용
- 10개의 element까지는 각각 overloading된 method를 사용
    - array 할당, 초기화, GC 비용을 줄이기 위함

### 1.2 Set factory

````
Set<String> aespa = Set.of("Karina", "Winter", "Giselle", "Ningning");
aespaSet.add("newMember"); // UnsupportedOperationException

Set<String> aespaSet = Set.of("Karina", "Winter", "Giselle", "Ningning", "Karina");// IllegalArgumentException
````

### 1.3 Map factory

````
Map<String, Integer> aespaAge1 = Map.of("Karina", 21, "Winter", 21, "Giselle", 20, "Ningning", 18);

aespaAge1.put("새로운 멤버", 22); // UnsupportedOperationException
aespaAge1.put("Karina", 22); // UnsupportedOperationException

Map<String, Integer> aespaAge2 = Map.ofEntries(
        Map.entry("Karina", 21),
        Map.entry("Winter", 21),
        Map.entry("Giselle", 20),
        Map.entry("Ningning", 18)
);
````

- `java.util.Map.entry()` : `Map.ofEntries()`를 사용하기 위한 helper method
    - Map element가 많을 때 유용

## 2. Working with List and Set

Java 8에서 Colelction 스스로 수정하는 method를 추가

- `rmoveIf()` : predicate를 사용하여 element를 제거
    - `List`, `Set`을 구현한 모든 class에서 사용 가능
- `replaceAll()` : `UnaryOperator`를 사용하여 element를 대체
- `sort()` : list를 정렬
    - `List`를 구현한 모든 class에서 사용 가능

### 2.1 removeIf

````
List<String> memberList = new ArrayList<>();
memberList.add("Karina");
memberList.add("Giselle");
memberList.add("Winter");
memberList.add("Ningning");

// ConcurrentModificationException
for (String memberName : memberList) {
    if (memberName.equals("Karina")) {
        memberList.remove(memberName);
    }
}

// ConcurrentModificationException : 내부적으로 아래와 같이 동작
for (Iterator<String> iterator = memberList.iterator();
     iterator.hasNext(); ) {
    String memberName = iterator.next();
    if (memberName.equals("Karina")) {
        memberList.remove("Karina");
    }
}

// Iterator로 제거
for (Iterator<String> iterator = memberList.iterator();
     iterator.hasNext(); ) {
    String memberName = iterator.next();
    if (memberName.equals("Karina")) {
        iterator.remove();
    }
}

// Java 8
memberList.removeIf(memberName -> memberName.equals("Karina"));
````

- `for ~ loop` 은 내부적으로 `Iterator`를 사용
- `ConcurrentModificationException` 발생
    - Iterator를 사용하면서, `List` object를 통해 수정하려하기 때문
- `Iterator`를 사용하여 제거하면 해결
- Java 8 부터는 `removeIf()`를 사용하여 간단하게 제거 가능

### 2.2 replaceAll

````
// 새로운 collection을 생성
memberList.stream().map(memberName -> "original : " + memberName + " | upper : " + memberName.toUpperCase())
        .collect(Collectors.toList())
        .forEach(System.out::println);
````

```bash
original : Karina | upper : KARINA
original : Giselle | upper : GISELLE
original : Winter | upper : WINTER
original : Ningning | upper : NINGNING
```

````
// 기존 collection을 수정
for (ListIterator<String> iterator = memberList.listIterator();
 iterator.hasNext(); ) {
    String memberName = iterator.next();
    iterator.set("original : " + memberName + " | upper : " + memberName.toUpperCase());
}

// Java 8
memberList.replaceAll(memberName -> "original : " + memberName + " | upper : " + memberName.toUpperCase());
````

## 3. Working with Map

- Java 8 에 추가된 `Map` interface의 default method
- 코드를 간결하고, 가독성 좋은 idiomatic pattern으로 작성

### 3.1 forEach

````
Map<String, Integer> aespaAge = Map.of("Karina", 21, "Winter", 21, "Giselle", 20, "Ningning", 18);

// forEach
for (Map.Entry<String, Integer> entry : aespaAge.entrySet()) {
    System.out.println(entry.getKey() + " : " + entry.getValue());
}

// Java 8 : BiConsumer
aespaAge.forEach((memberName, age) -> System.out.println(memberName + " : " + age));
````

- `Map` interface에 `forEach()` method 추가
    - `BiConsumer`를 사용하여 `Map.Entry`를 처리

### 3.2 Sorting

- `Entry.comparingByValue()`
- `Entry.comparingByKey()`

````
aespaAge.entrySet().stream()
  .sorted(Map.Entry.comparingByValue())
  .forEachOrdered(System.out::println);
````

```bash
Ningning=18
Giselle=20
Karina=21
Winter=21
````

#### HashMap and Performance

- Java 8부터 `HashMap` 의 성능이 향상
- `HashMap` 의 `Entry` 는 `LinkedList` (bucket) 로 구현 (O(n))
    - hashcode로 접근
    - bucket의 size가 커지면 자동으로 sorted map으로 변경 (O(log n))
        - entiry가 많아지면 성능 저하될 가능성이 있기 때문
    - sorted map은 key가 Comparable 인 경우에만 가능

### 3.3 getOrDefault

```
System.out.println(aespaAge.get("hani").toString()); // NullPointerException
System.out.println(aespaAge.getOrDefault("hani", -1).toString());
```

### 3.4 Compute patterns

- `computeIfAbsent()` : key가 없으면 새로운 value를 생성해서 Map에 추가
    - key가 없거나, value가 null인 경우
- `computeIfPresent()` : key가 있으면 새로운 value를 생성해서 Map에 추가
    - key가 있고, value가 null이 아닌 경우
- `compute()` : 새로운 value를 생성해서 Map에 추가

````
Map<String, byte[]> dataToHash = new HashMap<>();
MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

lines.forEach(line -> 
  datatToHash.computeIfAbsent(line, this::caculateDigest));

private byte[] caculateDigest(String line) {
    return messageDigest.digest(line.getBytes(StandardCharsets.UTF_8));
}
````

````
Map<String, List<String>> idolWithMember = new HashMap<>();
idolWithMember.put("NewJeans", List.of("Minzi", "Hani", "Haerin"));
idolWithMember.put("RedVelvet", List.of("Irene", "Seulgi", "Wendy", "Joy", "Yeri"));
...

String aespa = "Aespa";
List<String> aespaMemberList = idolWithMember.get(aespa);
if(aespaMemberList == null) {
    aespaMemberList = new ArrayList<>();
    idolWithMember.put(aespa, aespaMemberList);
}
aespaMemberList.add("Karina");
aespaMemberList.add("Winter");
aespaMemberList.add("Giselle");
aespaMemberList.add("Ningning");

// Java 8 : computeIfAbsent()

idolWithMember.computeIfAbsent("Aespa", groupName -> 
  new ArrayList<>()).addAll(List.of("Karina", "Winter", "Giselle", "Ningning"));

````

### 3.5 Remove patterns

- `remove()` : key의 Entry를 제거
- Java 8부터 key가 특정 value와 일치하는 경우에만 제거 가능한 오버로드 메서드 추가
- `remove(key, value)` : key의 Entry를 제거

````
Map<String, String> idolWithLeader = new HashMap<>();
idolWithLeader.put("NewJeans", "Minzi");
idolWithLeader.put("Aespa", "Karina");

String nameKarina = "Karina";
String nameAespa = "Aespa";

if (idolWithLeader.containsKey(nameAespa)
        && Objects.equals(idolWithLeader.get(nameAespa), nameKarina)) {
    System.out.println("Aespa는 더 이상 리더 직급이 없습니다.");
    idolWithLeader.remove(nameAespa);
}

// Java 8
idolWithLeader.remove(nameAespa, nameKarina);
````

### 3.6 Replacement patterns

- `replaceAll()` : 각 Entry value를 `BiFunction`을 사용하여 새로운 value로 대체
    - `List`의 `replaceAll()`과 유사
- `Replace()` : key가 있다면 key의 value를 새로운 value로 대체

````
idolWithLeader.replaceAll((groupName, leaderName) -> leaderName.toUpperCase());
````

### 3.7 Merge

- `merge()` : Map의 merge에 추가적인 기능을 제공
- given mapping function을 사용하여 새로운 value로 대체
    - function result가 null이면 key를 제거
- key의 value가 없거나, null 인 경우
    - 새로운 value를 추가

````
Map<String, String> nickNameIdol = Map.ofEntries(
        Map.entry("유지민", "Karina"),
        Map.entry("애리", "GiSelle"),
        Map.entry("김민지", "민지")
);

Map<String, String> nickNameActor = Map.ofEntries(
        Map.entry("공지철", "공유"),
        Map.entry("유지민", "지민") // duplicate key
);

Map<String, String> everyOne1 = new HashMap<>(nickNameIdol);
everyOne1.putAll(nickNameActor);
System.out.println(everyOne1);

// Java 8
Map<String, String> everyOne2 = new HashMap<>(nickNameIdol);
nickNameActor.forEach((name, age) -> everyOne2.merge(name, age, (nick1, nick2) -> nick1 + " & " + nick2));
System.out.println(everyOne2);;
````

```bash
{유지민=지민, 김민지=민지, 애리=GiSelle, 공지철=공유}
{유지민=Karina & 지민, 김민지=민지, 애리=GiSelle, 공지철=공유}
```

```
Map<String, Integer> memberAge = new HashMap<>();
memberAge.put("Karina", 20);
memberAge.put("Giselle", 20);
memberAge.put("Winter", 19);

String nameKarina = "Karina";
Integer age = memberAge.get(nameKarina);
if (age == null) {
    memberAge.put(nameKarina, 20);
} else {
    memberAge.put(nameKarina, age + 1);
}

// Java 8
memberAge.merge(nameKarina, 1, (k, v) -> v + 1);
```

## 4. Improved ConcurrentHashMap

## 5. Summary
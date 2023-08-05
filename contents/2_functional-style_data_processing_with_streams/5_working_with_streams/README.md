# 5. Working with streams

1. Filtering
2. Slicing a stream
3. Mapping
4. Finding and matching
5. Reducing
6. Putting it all into practice
7. Numeric streams
8. Building streams
9. Overview
10. Summary

> ### This chapter covers
> - Filtering, slicing, and mapping
> - Finding, matching, and reducing
> - Using numeric streams (primitive streams specializations)
> - Creating streams from multiple source
> - Infinite streams

---

````
// external iteration
List<Member> memberAespa = new ArrayList<>();
for(Member member : members) {
    if (member.getTeam() == Team.AESPA) {
        memberAespa.add(member);
    }
}

// internal iteration
import static java.util.stream.Collectors.toList;
List<Member> memberAespa = members.stream()
                                .filter(member -> member.getTeam() == Team.AESPA)
                                .collect(toList());
````

| external iteration | internal iteration    |
|--------------------|-----------------------|
| single-thread      | parallel   가능         |
| 명시적으로 구현           | Streams API 내부적으로 최적화 |

## 1. Filtering

### 1.1 Filtering with a predicate

<img src="img.png"  width="70%"/>

````
List<Member> memberIsDebut = members.stream()
                                    .filter(Member::isDebut)
                                    .collect(toList());
````

- `filter` : `Predicate`를 인수로 받아서 새로운 스트림을 반환
    - 반환 stream의 요소는 `Predicate`를 만족하는 원본 stream의 element

### 1.2 Filtering unique elements

<img src="img_1.png"  width="70%"/>

````
List<String> members = Arrays.asList("KARINA", "WINTER", "GISELLE", "NINGNING", "KARINA");

members.stream()
        .distinct()
        .forEach(System.out::println);
````

````log
member = KARINA
member = WINTER
member = GISELLE
member = NINGNING
````

- `distinct` : 중복을 제거한 스트림을 반환
- `hashCode`, `equals`를 사용

## 2. Slicing a stream (Since Java 9)

- 특정 element를 drop하거나 skip 하는 방법

### 2.1 Slicing using a predicate

- `takeWhile()`, `dropWhile()`
- **이미 정렬된 stream에서 적합**

#### USING TAKEWHILE

- 크기가 큰 stream에 적합
- `Predicate` 에 부합한 조건이 아닌게 나올 때까지 요소를 반환

````
// fitler
List<Member> aespa1 = memberList.stream().filter(member -> {
    System.out.println("filter: " + member.getName());
    return member.getTeam() == Member.Team.AESPA;
}).toList();

// takeWhile
List<Member> aespa2 = memberList.stream().takeWhile(member -> {
    System.out.println("takeWhile: " + member.getName());
    return member.getTeam() == Member.Team.AESPA;
}).toList();
````

```log
filter: karina
filter: winter
filter: gisele
filter: ningning
filter: irene
filter: seulgi
filter: wendy
filter: joy
filter: yeri
filter: hani
filter: hyerin
filter: minzi

takeWhile: karina
takeWhile: winter
takeWhile: gisele
takeWhile: ningning
takeWhile: irene
```

#### USING DROPWHILE

- `Predicate` 에 부합한 조건이 나올 때까지 요소를 제외
- **element가 무한대여도 사용 가능**
- `takeWhile`과 반대

````
List<Member> notAespa = memberList.stream().dropWhile(member -> {
    System.out.println("dropWhile: " + member.getName());
    return member.getTeam() == Member.Team.AESPA;
}).toList();
````

### 2.2 Truncating a stream

- `limit(n)` : size가 n인 stream 반환
- 순서 정렬에 상관없이 사용 가능

````
List<Member> aespaTwoMember = memberList.stream()
                                        .filter(member -> member.getTeam() == Member.Team.AESPA)
                                        .limit(2)
                                        .toList();
````

### 2.3 Skipping elements

- `skip(n)` : 시작 element부터 n개의 element를 버리고 반환
- n이 stream 사이즈보다 크면 빈 stream 반환

````
List<Member> aespaTwoMember = memberList.stream()
                                        .filter(member -> member.getTeam() == Member.Team.AESPA)
                                        .skip(2)
                                        .toList();
````

## 3. Mapping

## 4. Finding and matching

## 5. Reducing

## 6. Putting it all into practice

## 7. Numeric streams

## 8. Building streams

## 9. Overview

## 10. Summary
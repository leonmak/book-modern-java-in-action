# 14. The Java Module System (Java 9)

1. The driving force: reasoning about software
2. Why the java Module System was designed
3. Java modules : the big picture
4. Developing an application with the Java Module System
5. Working with several modules
6. Compiling and packaging
7. Automatic modules
8. module declaration and clauses
9. A bigger example and where to learn more
10. Summary

> ### This chapter covers
>
> - Java가 module system을 도입한 이유
> - 핵심 구조 : module declaration, requires, exports
> - legacy JARs를 위한 자동 모듈
> - JDK library의 모듈화
> - Modules and Maven builds
> - `requires`, `exports` 이상의 module 지시문

---

- Java 9에 추가된 가장 큰 기능 중 하나
- 몇 십년 걸려 project Jigsaw를 통해 개발

## 1. The driving force: reasoning about software

### 1.1 Separation of concerns (SoC)

> #### SOC의 장점
>
> - 개별 작업 분리 가능 (팀 협업 향상)
> - 분리된 part의 재사용성
> - 전체 시스템의 유지 보수성

- SoC : 관심사의 분리
    - 아키텍쳐를 이해하기 쉬움 e.g. Model vs View vs Controller
    - low-level (비즈니스 로직을 분리)
- **_module_** : 관련된 코드를 모아놓은 것
    - class를 그룹화해서 class들 간의 관계에 대한 가시성을 높임

### 1.2 Information hiding <sup>정보 은닉</sup>

- **_information hiding_** : 구현 세부사항을 숨기는 것
- **_encapsulation_** : 코드 조각을 다른 부분으로부터 분리하여, 의존성을 줄임
    - e.g. class 안의 component를 캡슐화하는 `private` 접근제어자
- 변하는 요구사항에 대해 코드 수정 최소화

### 1.3 Java software

<img src="img.png"  width="70%"/>

- SOC
    - grouping : package, class, interface 단위로 관심사를 그룹화
    - UML을 통해 관심사, 의존성을 시각화 가능
- Information hiding
    - visibility modifier를 통해 method, field, class의 접근성을 제어
        - e.g. `public`, `protected`, `private`
    - Java 9 module system 이전에는 충분하지 않았음

## 2. Why the java Module System was designed

## 3. Java modules : the big picture

## 4. Developing an application with the Java Module System

## 5. Working with several modules

## 6. Compiling and packaging

## 7. Automatic modules

## 8. module declaration and clauses

## 9. A bigger example and where to learn more

## 10. Summary


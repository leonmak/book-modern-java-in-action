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

### 2.1 Modularity limitations

- classes < packages < JARs < modules (Java 9)
- class의 접근제어와 캡슐화는 제공되지만, package, JAR 단위의 캡슐화는 제공되지 않음
- Java의 접근제어 `public`, `protected`, `package-level`, `private`

#### LIMITED VISIBILITY CONTROL

- **package 단위**의 접근제어가 불가능
- class나 interface가 다른 package에 보이려면 `public`으로 선언해야 함

#### CLASS PATH

- 실행할 class를 모두 compile해서 JAR에 담아야 함
- JVM은 **class path**로부터 동적으로 class를 load
- 단점 1 : class의 버전을 명시할 수 없음 (버전 호환성)
- 단점 2 : class path에서 class간의 의존성을 명시적으로 선언할 수 없음
    - JAR Hell, Class Path Hell
    - compile-time에 의존성을 확인할 수 없어, runtime에 `ClassNotFoundException` 발생
    - Maven, Gradle 과 같은 build-tool 필요

### 2.2 Monolithic JDK

- **_Java Development Kit_** (JDK) : Java compiler, Java runtime, Java API
    - `javac` : Java compiler
    - `java` : Java load and run
    - `java.lang.*` : Java API
- CORBA와 같은 라이브러리는 사용유무에 관계없이 JDK에 제공
- Java의도와 달리 `sun.misc.Unsafe` class를 다른 API에서 공개적으로 사용 e.g. Netty, Spring
- Java 8에 compact profiles 개념이 도입되었지만, 미흡
    - compact profiles : JDK 어떤 라이브러리에 관심있는지 지정

### 2.3 Comparison with OSGi

- Open Service Gateway initiative (OSGi)
- Java 9 개발 이전에 OSGi가 Java의 모듈 시스템으로 사용됨
- OSGi, Java 9 module system은 같은 app에서 공존 가능
- **_bundle_** : OSGi의 module
- bundle은 6가지 상태로 OSGi framework에 존재
    - INSTALLED, RESOLVED, STARTING, ACTIVE, STOPPING, UNINSTALLED
- OSGi 의 특징
    - 재기동 없이 hot-swapping
    - 동시에 다른 버전의 같은 bundle 설치 가능
        - OSGi는 각 bundle마다 class loader를 가짐

## 3. Java modules : the big picture

<img src="img_1.png"  width="70%"/>

- `module-info.java` : module descriptor
- `requires` : module dependency
- `exports` : 다른 module에 대한 접근제어

<img src="img_2.png"  width="70%"/>

## 4. Developing an application with the Java Module System

### 4.1 Setting up an application

- 비용 지출 데이터 종합 application

#### 요구사항

- file이나 URL로부터 비용지출을 read
- 비용관련 문자열 parsing
- 통계 데이터 계산
- 동계 데이터 출력
- task 실행 / 종료 조건 제공

#### 관심사

- source로부터 data를 read `Reader`, `HttpReader`, `FileReader`
- data를 parsing `Parser`, `JSONParser`, `ExpenseJSONParser`
- domain object `Expense`
- 통계 계산 `SummaryCalculator`, `SummaryStatistics`
- 관심사 통합 `ExpenseApplication`

#### 모듈화 (Fine-grained modularization)

- expenses.readers
    - expenses.readers.http
    - expenses.readers.file
- expenses.parsers
    - expenses.parsers.json
- expenses.model
- expenses.statistics
- expenses.application

### 4.2 Fine-grained and coarse-grained modularization

- fine-grained scheme : pacakge마다 module을 가짐
- coarse-grained scheme : 모든 package를 하나의 module로 묶음

### 4.3 java Module System basics

```
|─ expenses.application
    |─ module-info.java
    |─ com
        |─ example
            |─ expenses
                |─ application
                    |─ ExpensesApplication.java
```

- `module-info.java` : module descriptor
    - moodule source file의 root에 위치해야함

```java
module expenses.application {
    // ...
}
```

#### compile, package, run

```bash
javac module-info.java com/example/expenses/application/ExpensesApplication.java -d target
jar cvfe expenses-application.jar com.example.expenses.application.ExpensesApplication -C target

java --module-path expenses-applicaion.jar \
  --module expenses/com.example.expenses.application.ExpensesApplication

```

- `--module-path` : load 대상 module 경로
- `--module` : 실행할 module

## 5. Working with several modules

### 5.1 The exports clause

```java
module expenses.readers {
    exports com.example.expenses.readers;
    exports com.example.expenses.readers.file;
    exports com.example.expenses.readers.http;
}
```

- `exports` 절 : 다른 module에 대한 접근제어
- whitelist : 기본적으로, 모든 것을 module 내에 캡슐화

````

|─ expenses.application
   |─ module-info.java
   |─ com
       |─ example
           |─ expenses
               |─ application
                  |─ ExpensesApplication.java
                  
|─ expenses.readers
   |─ module-info.java
       |─ com
           |─ example
               |─ expenses
                   |─ readers
                      |─ Reader.java
                   |─ file
                      |─ FileReader.java
                   |─ http
                      |─ HttpReader.java
````

### 5.2 The requires clause

```java
module expenses.readers {
    requires java.base; // 기본적으로 포함

    exports com.example.expenses.readers;
    exports com.example.expenses.readers.file;
    exports com.example.expenses.readers.http;
}
```

- `requires` 절 : module dependency
    - module 이 의존하는 것을 명시
- 기본적으로 module platform `java.base`을 포함
    - e.g. `java.net`, `java.io`, `java.util`, ...

| class visibility                          | Before Java 9 | Java 9 |
|-------------------------------------------|---------------|--------|
| 모든 public class를 모두에게 public으로 공개         | O             | O      |
| 특정 public class를 모두에게 public으로 공개         | X             | O      |
| 모든 public class를 module 안에서만 사용           | X             | O      |
| `protected`, `pacakge-private`, `private` | O             | O      |

### 5.3 Naming

- **_Oracle_** : Domain Naming e.g. com.example.expenses
    - module 이름이 주요 `export` API package 이름과 일치해야함

## 6. Compiling and packaging

**_Maven_** build tool을 사용

````
|─ pom.xml
|─ expenses.application
   |─ pom.xml
   |─ src
       |─ main
           |─ java
           |─ module-info.java
             |─ com
              ...
              
|─ expenses.readers
   |─ pom.xml
   |─ src
       |─ main
           |─ java
           |─ module-info.java
               |─ com
                ...
````

````
<!-- global pom.xml -->
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
http://maven.apache.org/xsd/maven-4.0.0.xsd">
  
...

  <modules>
    <module>expenses.application</module>
    <module>expenses.readers</module>
  </modules>

  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.7.0</version>
          <configuration>
            <source>9</source>
            <target>9</target>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>

  </build>
</project>

<!-- expenses.readers pom.xml -->

...
<groupId>com.example</groupId>
<artifactId>expenses.readers</artifactId>
<version>1.0</version>
<packaging>jar</packaging>

<dependencies>
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>expenses</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>

<!-- expenses.application pom.xml -->

...

<groupId>com.example</groupId>
<artifactId>expenses.application</artifactId>
<version>1.0</version>
<packaging>jar</packaging>

<parent>
    <groupId>com.example</groupId>
    <artifactId>expenses</artifactId>
    <version>1.0</version>
</parent>

<dependencies>
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>expenses.readers</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
    
...
````

## 7. Automatic modules

```java
module expenses.readers {
    requires java.base;
    requires org.apache.httpcomponents.httpclient;

    exports com.example.expenses.readers;
    exports com.example.expenses.readers.file;
    exports com.example.expenses.readers.http;
}
```

```xml
<!--expenses.readers pom.xml-->

<dependencies>
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.3</version>
    </dependency>
</dependencies>

````

- `httpclient`에 대한 의존성을 `pom.xml`에 명시
- Maven은 pom.xml을 읽어 묵시적으로 module로 변환하여 의존성 주입
- `--describe-module` : module 정보를 출력
- `--module-path` : module 경로를 지정

```bash
jar --file=./expenses.readers/target/dependency/httpclient-4.5.3.jar \
  --describe-module
httpclient@4.5.3 automatic

java --module-path \
  ./expenses.application/target/expenses.application-1.0.jar: ./expenses.readers/target/expenses.readers-1.0.jar \
  ./expenses.readers/target/dependency/httpclient-4.5.3.jar \
  --module \
  expenses.application/com.example.expenses.application.ExpensesApplication

```

## 8. module declaration and clauses

### 8.1 requires

```java
module com.iteratrlearning.application {
    requires com.iteratrlearning.ui;
}
````

- compile, runtime에 필요한 module 의존성 명시

### 8.2 exports

```java
module com.iteratrlearning.ui {
    requires com.iteratrlearning.core;
    exports com.iteratrlearning.ui.panels;
    exports com.iteratrlearning.ui.widgets;
}
```

- 기본적으로 package는 export되지 않음 (white list)
- `exports` 절을 통해 export할 수 있음

### 8.3 requires transitive

````
module com.iteratrlearning.ui {
   requires transitive com.iteratrlearning.core;
   
   exports com.iteratrlearning.ui.panels;
   exports com.iteratrlearning.ui.widgets;
}

module com.iteratrlearning.application {
  requires com.iteratrlearning.ui;
}
````

- `requires` 한 다른 module이 pulbic type으로 사용하도록 함
- `com.iteratrlearning.application`은 `com.iteratrlearning.core`를 명시하지 않아도 됨

### 8.4 exports to

```java
module com.iteratrlearning.ui {
    requires com.iteratrlearning.core;
    exports com.iteratrlearning.ui.panels;

    exports com.iteratrlearning.ui.widgets to
            com.iteratrlearning.ui.widgetuser;
}
```

- 특정 user에게만 `export`할 수 있음

### 8.5 opens and opens

````java
open module com.iteratrlearning.ui {

}
````

- 다른 module이 해당 모듈 package에 reflective access 할 수 있게 함
- Java 9 전에는 private state 에 reflection을 이용하여 검사 가능
    - e.g. Hibernate와 같은 ORM tool이 이용
- `opens` : module 선언에서 특정 package에 대해 reflective access를 허용
- `opens to` : 특정 module에 대해 reflective access를 허용

### 8.6 uses and provides

- module을 service provider로 선언
- `provides` : service를 제공
- `uses` : service를 사용

## 9. A bigger example and where to learn more

## 10. Summary


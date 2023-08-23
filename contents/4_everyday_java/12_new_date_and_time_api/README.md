# 12. New Date and Time API

1. LocalDate, LocalTime, LocalDateTime, Instant, Duration, and Period
2. Manipulating, parsing, and formatting dates
3. Working with different time zones and calendars
4. summary

> ### This chapter covers
>
> - Java 8의 새로운 date, time library 의 장점
> - date와 time을 인간과 기계를 위해 표시하는 방법
> - time 간격의 정의
> - Manipulating, parsing, and formatting dates
> - 다른 time zone과 calendar를 다루는 방법

---

- Java 1.0 : `java.util.Date` 제공
    - Year는 1900년에서 시작, Month는 0부터 시작
    - 21 September 2017 : `Date date = new Date(117, 8, 21);`
- Java 1.1 : `java.util.Date`의 많은 메서드가 deprecated, `java.util.Calendar` 으로 대체
    - Month는 0부터 시작
    - `Date`, `Calendar` 두 존재가 개발자에게 혼동 유발, 둘다 mutable
    - `DateFormat` 은 not thread-safe
- third-party library를 쓰기 시작 e.b. Joda-Time
- **Java 8 : `java.time` package에 Joda-Time library의 기능을 추가**

## 1. LocalDate, LocalTime, LocalDateTime, Instant, Duration, and Period

- `java.time` package
- `LocalDate`, `LocalTime`, `LocalDateTime`, `Instant`, `Duration`, `Period` 등의 클래스 제공

### 1.1 Working with LocalDate and LocalTime

- `LocalDate` : 날짜를 표현하는 클래스, 불변
    - `of()` : 날짜를 생성하는 static method
    - `java.time.temporal`의 구현체 `java.time.ChronoField` 를 이용해 날짜의 필드에 접근 가능
- `LocalDateTime` : 시간을 표현하는 클래스, 불변

````
LocalDate date = LocalDate.of(2023, 8, 23); // 2023-08-23

int year = date.getYear(); // 2023
year = date.get(ChronoField.YEAR); // 2023

Month month1 = date.getMonth(); // AUGUST
int month2 = date.get(ChronoField.MONTH_OF_YEAR); // 8
int moth3 = date.getMonthValue(); // 8

int day = date.getDayOfMonth(); // 23
day = date.get(ChronoField.DAY_OF_MONTH); // 23

DayOfWeek dow = date.getDayOfWeek(); // WEDNESDAY
int len = date.lengthOfMonth(); // 31 (days in August)
boolean leap = date.isLeapYear(); // false (not a leap year)

LocalDate today = LocalDate.now();// 오늘 날짜 (e.g. 2023-08-23)

LocalTime time = LocalTime.of(13, 45, 20); // 13:45:20
int hour = time.getHour(); // 13
int minute = time.getMinute(); // 45
int second = time.getSecond(); // 20
````

### 1.2 Combining a date and a time

- `LocalDateTime` : `LocalDate` + `LocalTime`
- time zone 없이 날짜와 시간을 표현하는 클래스
- `atTime()`, `atDate()` : `LocalDate`와 `LocalTime`을 합쳐 `LocalDateTime`을 생성하는 method
- `toLocalDate()`, `toLocalTime()` : `LocalDateTime`을 `LocalDate`와 `LocalTime`으로 변환하는 method

````
LocalDateTime dt1 = LocalDateTime.of(2023, Month.AUGUST, 23, 13, 45, 20); // 2023-08-23T13:45:20

LocalDate date = LocalDate.of(2023, 8, 23); // 2023-08-23
LocalTime time = LocalTime.of(13, 45, 20); // 13:45:20
LocalDateTime dt2 = LocalDateTime.of(date, time); // 2023-08-23T13:45
LocalDateTime dt3 = date.atTime(13, 45, 20); // 2023-08-23T13:45:20

LocalDateTime dt4 = date.atTime(time); // 2023-08-23T13:45:20
LocalDateTime dt5 = time.atDate(date); // 2023-08-23T13:45:20

LocalDate date1 = dt1.toLocalDate(); // 2023-08-23
LocalTime time1 = dt1.toLocalTime(); // 13:45:20
````

### 1.3 Instant: a date and time for machines

- `java.time.Instant` : 기계가 이해할 수 있는 날짜와 시간을 표현하는 클래스
    - machine 을 위한 클래스
    - Unix epoch time부터 흐른 시간을 초 단위로 표현
- `ofEpochSecond()` : static factory method
    - nano-second 정밀로 표현 가능
- `now()` : 현재 시간을 표현하는 static factory method

````
Instant.ofEpochSecond(3); // 1970-01-01 00:00:03
Instant.ofEpochSecond(3, 0); // 1970-01-01 00:00:03
Instant.ofEpochSecond(2, 1_000_000_000); //1970-01-01 00:00:03 = 2 sec + 1 billion nanoseconds
Instant.ofEpochSecond(4, -1_000_000_000); // 1970-01-01 00:00:03 = 4 sec - 1 billion nanoseconds
````

### 1.4 Defining a duration or a period

- `Duration`, `Period`, 불변
- 두 `Temporal` 클래스 간의 시간 간격을 표현하는 클래스
- `between()` : 두 `Temporal` 클래스 간의 시간 간격을 표현하는 static factory method

````
// Duration : 시간 간격
LocalTime time1 = LocalTime.of(13, 45, 20); // 13:45:20
LocalTime time2 = LocalTime.of(14, 45, 42); // 14:45:20
Duration d1 = Duration.between(time1, time2);

LocalDate date1 = LocalDate.of(2023, 8, 23); // 2023-08-23
LocalDate date2 = LocalDate.of(2024, 8, 23); // 2024-08-23
Period p1 = Period.between(date1, date2);

Instant instant1 = Instant.ofEpochSecond(3);
Instant instant2 = Instant.ofEpochSecond(4);
Duration d2 = Duration.between(instant1, instant2);

// Period : 날짜 간격 (년, 월, 일 단위)
Period tenDays = Period.between(LocalDate.of(2023, 8, 23)
    , LocalDate.of(2023, 9, 2));

Duration threeMinutes1 = Duration.ofMinutes(3);
Duration threeMinutes2 = Duration.of(3, ChronoUnit.MINUTES);
Period tendDays = Period.ofDays(10);
Period threeWeeks = Period.ofWeeks(3);
Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);
````

## 2. Manipulating, parsing, and formatting dates

## 3. Working with different time zones and calendars

## 4. summary


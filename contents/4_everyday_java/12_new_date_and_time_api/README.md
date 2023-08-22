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

## 2. Manipulating, parsing, and formatting dates

## 3. Working with different time zones and calendars

## 4. summary


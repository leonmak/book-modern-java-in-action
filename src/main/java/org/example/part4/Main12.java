package org.example.part4;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.*;

public class Main12 {

    public static void main(String[] args) {

//        ch1();
        ch2();
    }

    private static void ch2() {
        LocalDate date1 = LocalDate.of(2023, 8, 23); // 2023-08-23
        LocalDate date2 = date1.withYear(2024); // 2024-08-23
        LocalDate date3 = date2.withDayOfMonth(25); // 2024-08-25
        LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 9); // 2024-09-25

        // declarative manner
        LocalDate date5 = date1.plusWeeks(1); // 2023-08-30
        LocalDate date6 = date5.minusYears(3); // 2020-08-30
        LocalDate date7 = date6.plus(6, ChronoUnit.MONTHS); // 2021-02-28


        LocalDate date = LocalDate.of(2014, 3, 18);
        date = date.with(ChronoField.MONTH_OF_YEAR, 9);
        date = date.plusYears(2).minusDays(10);
        date.withYear(2011); // LocalDate는 불변임
        System.out.println(date); // 2016-09-08

        ch21();
        ch22();


    }

    private static void ch22() {
        LocalDate date1 = LocalDate.parse("20230823", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate date2 = LocalDate.parse("2023-08-23", DateTimeFormatter.ISO_LOCAL_DATE);

        // pattern 지정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date3 = LocalDate.of(2023, 8, 23);
        String formattedDate = date3.format(formatter); // 23/08/2023
        LocalDate date4 = LocalDate.parse(formattedDate, formatter);

        // java.util.Locale 이용
        DateTimeFormatter formatterKorean = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일", java.util.Locale.KOREAN);
        LocalDate date5 = LocalDate.of(2023, 8, 23);
        String formattedDateKorean = date5.format(formatterKorean); // 2023년 08월 23일
        LocalDate date6 = LocalDate.parse(formattedDateKorean, formatterKorean);

        // builer pattern : DateTimeFormatterBuilder
        DateTimeFormatter formatterKoreanManner = new DateTimeFormatterBuilder().appendText(ChronoField.YEAR)
                .appendLiteral("년 ")
                .appendText(ChronoField.MONTH_OF_YEAR)
                .appendLiteral("월 ")
                .appendText(ChronoField.DAY_OF_MONTH)
                .appendLiteral("일")
                .parseCaseInsensitive() // 대소문자 구분 없이 (Mar == MAR == mAr)
                .toFormatter(java.util.Locale.KOREAN);
    }

    private static void ch21() {
        LocalDate date1 = LocalDate.of(2023, 8, 23); // 2023-08-23
        LocalDate date2 = date1.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)); // 2023-08-27
        LocalDate date3 = date2.with(TemporalAdjusters.lastDayOfMonth()); // 2023-08-31

        LocalDate nextWorkingDate = date1.with(new NextWorkingDay());
    }

    public static class NextWorkingDay implements TemporalAdjuster {
        @Override
        public Temporal adjustInto(Temporal temporal) {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK)); // 현재 요일
            int dayToAdd = 1; // 기본으로 +1 일
            if (dow == DayOfWeek.FRIDAY) dayToAdd = 3; // 금요일이면 +3 일
            else if (dow == DayOfWeek.SATURDAY) dayToAdd = 2; // 토요일이면 +2 일
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
        }
    }

    private static void ch1() {

        ch11();
        ch12();
        ch13();
        ch14();
    }

    private static void ch14() {
        LocalTime time1 = LocalTime.of(13, 45, 20); // 13:45:20
        LocalTime time2 = LocalTime.of(14, 45, 42); // 14:45:20
        Duration d1 = Duration.between(time1, time2);

        LocalDate date1 = LocalDate.of(2023, 8, 23); // 2023-08-23
        LocalDate date2 = LocalDate.of(2024, 8, 23); // 2024-08-23
        Period p1 = Period.between(date1, date2);

        Instant instant1 = Instant.ofEpochSecond(3);
        Instant instant2 = Instant.ofEpochSecond(4);
        Duration d2 = Duration.between(instant1, instant2);

        Period tenDays = Period.between(LocalDate.of(2023, 8, 23), LocalDate.of(2023, 9, 2));

        Duration threeMinutes1 = Duration.ofMinutes(3);
        Duration threeMinutes2 = Duration.of(3, ChronoUnit.MINUTES);
        Period tendDays = Period.ofDays(10);
        Period threeWeeks = Period.ofWeeks(3);
        Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);

    }

    private static void ch13() {
        Instant.ofEpochSecond(3); // 1970-01-01 00:00:03
        Instant.ofEpochSecond(3, 0); // 1970-01-01 00:00:03
        Instant.ofEpochSecond(2, 1_000_000_000); //1970-01-01 00:00:03 = 2 sec + 1 billion nanoseconds
        Instant.ofEpochSecond(4, -1_000_000_000); // 1970-01-01 00:00:03 = 4 sec - 1 billion nanoseconds
    }

    private static void ch12() {
        LocalDateTime dt1 = LocalDateTime.of(2023, Month.AUGUST, 23, 13, 45, 20); // 2023-08-23T13:45:20

        LocalDate date = LocalDate.of(2023, 8, 23); // 2023-08-23
        LocalTime time = LocalTime.of(13, 45, 20); // 13:45:20
        LocalDateTime dt2 = LocalDateTime.of(date, time); // 2023-08-23T13:45
        LocalDateTime dt3 = date.atTime(13, 45, 20); // 2023-08-23T13:45:20

        LocalDateTime dt4 = date.atTime(time); // 2023-08-23T13:45:20
        LocalDateTime dt5 = time.atDate(date); // 2023-08-23T13:45:20

        LocalDate date1 = dt1.toLocalDate(); // 2023-08-23
        LocalTime time1 = dt1.toLocalTime(); // 13:45:20
    }

    private static void ch11() {
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
    }
}

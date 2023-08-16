package org.example.part3;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class Main9 {
    public static void main(String[] args) {
//        ch1();
        ch3();
    }

    private static void ch3() {

        List<Point> points = Arrays.asList(new Point(12, 2), null);
//        points.stream().map(p -> p.getX()).forEach(System.out::println);


        List<Integer> numbers = Arrays.asList(1, 2, 3);
//        numbers.stream().map(Main9::divideByZero).forEach(System.out::println);

        numbers.stream()
                .map(x -> x + 17)
                .filter(x -> x % 2 == 0)
                .limit(3)
                .forEach(System.out::println);

        numbers.stream()
                .peek(x -> System.out.println("from stream : " + x))
                .map(x -> x + 17)
                .peek(x -> System.out.println("after map : " + x))
                .filter(x -> x % 2 == 0)
                .peek(x -> System.out.println("after filter : " + x))
                .limit(3)
                .peek(x -> System.out.println("after limit : " + x))
                .forEach(System.out::println);

    }

    public static int divideByZero(int n) {
        return n / 0;
    }


    @Test
    public void testMoveRightBy() {
        Point p1 = new Point(5, 5);
        Point p2 = new Point(10, 5);
//        Point p2 = p1.moveRightBy(10);


//        assertEquals(15, p2.getX());
//        assertEquals(5, p2.getY());

        int result = Point.compareByXAndThenY.compare(p1, p2);
        assertTrue(result < 0);
    }

    @Test
    public void testMoveAllPointsRightBy() {
        List<Point> points = Arrays.asList(new Point(5, 5), new Point(10, 5));
        List<Point> expectedPoints = Arrays.asList(new Point(15, 5), new Point(20, 5));

        List<Point> newPoints = moveAllPointsRightBy(points, 10);
        assertEquals(expectedPoints, newPoints);

    }

    public static List<Point> moveAllPointsRightBy(List<Point> points, int x) {
        return points.stream().map(p -> new Point(p.getX() + x, p.getY())).collect(Collectors.toList());
    }

    @Test
    public void testFilter() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        List<Integer> even = numbers.stream().filter(i -> i % 2 == 0).collect(Collectors.toList());
        List<Integer> smallerThanThree = numbers.stream().filter(i -> i < 3).collect(Collectors.toList());

        assertEquals(Arrays.asList(2, 4), even);
        assertEquals(Arrays.asList(1, 2), smallerThanThree);
    }


    private static class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Point moveRightBy(int x) {
            return new Point(this.x + x, this.y);
        }

        public final static Comparator<Point> compareByXAndThenY = Comparator.comparing(Point::getX).thenComparing(Point::getY);

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            return y == point.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    private static void ch1() {
        int a = 10;
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                int a = 20;
                System.out.println("Hello World! : " + a);
            }
        };

        // lamda
        Runnable r2 = () -> {
//            int a = 30; // compile error : Variable 'a' is already defined in the scope
            System.out.println("Hello World");
        };

    }
}

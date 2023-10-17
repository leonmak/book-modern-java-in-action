package org.example.part6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Main21 {

    public class Vehicle {
        private int wheels;

        public Vehicle(int wheels) {
            this.wheels = wheels;
        }
    }

    public class Car extends Vehicle {
        public Car() {
            super(4);
        }
    }

    public class Motorcycle extends Vehicle {

        public Motorcycle(int wheels) {
            super(wheels);
        }
    }

    @Test
    @DisplayName("상속 타입추론 가능여부")
    void test() {
        var car = new Car();
        var motorcycle = new Motorcycle(2);
        System.out.println(car);
        System.out.println(motorcycle);
    }

    public void tst1() {
        List<? extends Number> numbers = new ArrayList<Integer>();
//        List<Number> numbers2 = new ArrayList<Integer>(); // compile error: incompatible types: ArrayList<Integer> cannot be converted to List<Number>
    }

    @Test
    @DisplayName("Boxing, Unboxing")
    public void tst2() {
        // return R (Boolean)
        Function<Integer, Boolean> function = x -> x > 0;
        System.out.println(function.apply(2)); // true

        // return boolean (primitive)
        Predicate<Integer> predicate = x -> x > 0;
        System.out.println(predicate.test(2)); // true

        Supplier<String> supplier1 = () -> "karina!";
    }

    public void tst3() {
        final int[] arr = {1, 2, 3};
        // arr = new int[]{1, 2, 3, 4}; // compile error: cannot assign a value to final variable arr
        arr[0] = 4;

        final List<Integer> list = new ArrayList<>();
//        list = new ArrayList<>(); // compile error: cannot assign a value to final variable list
        list.add(1);

    }

    static class Complex {
        public final double re;
        public final double im;

        public Complex(double re, double im) {
            this.re = re;
            this.im = im;
        }

        public static Complex add(Complex a, Complex b) {
            return new Complex(a.re + b.re, a.im + b.im);
        }
    }

    @Test
    @DisplayName("primitive, object type")
    public void tst4() {
        double d1 = 3.14;
        double d2 = d1;

        Double o1 = d1;
        Double o2 = d2;
        Double ox = o1;

        System.out.println(d1 == d2 ? "yes" : "no"); // yes
        System.out.println(o1 == o2 ? "yes" : "no"); // no
        System.out.println(o1 == ox ? "yes" : "no"); // yes
    }

    @Test
    @DisplayName("Java 12 switch")
    public void tst5() {
        List<String> names = List.of("karina", "sana", "ningning", "winter");

        names.stream().forEach(name -> {
            switch (name) {
                case "karina", "ningning", "winter" -> System.out.println("aespa");
                case "sana" -> System.out.println("twice");

            }
        });
    }
}

package org.example.part6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}

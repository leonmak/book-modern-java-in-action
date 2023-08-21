package org.example.part4;

import java.util.Optional;

public class Leader {

    private Car car;

    private String name;

    public Leader(Car car, String name) {
        this.car = car;
        this.name = name;
    }

    public Leader(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Optional<Car> getCar() {
        return Optional.ofNullable(car);
    }
}

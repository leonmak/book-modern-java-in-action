package org.example.part4;

import java.util.Comparator;
import java.util.List;

public class Main13 {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3);
        numbers.sort(Comparator.naturalOrder());

        numbers.stream();

    }
}

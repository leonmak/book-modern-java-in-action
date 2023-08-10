package org.example.part2;

import java.util.stream.LongStream;

public class Main7 {
    public static void main(String[] args) {
        ch71();
    }

    private static void ch71() {


        System.out.println("SideEffect parallel sum :"+ sideEffectParallelSum(10_000_000));
        System.out.println("SideEffect parallel sum :"+ sideEffectParallelSum(10_000_000));
        System.out.println("SideEffect parallel sum :"+ sideEffectParallelSum(10_000_000));

    }

    private static long sideEffectParallelSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n)
                .parallel()
                .forEach(accumulator::add);
        return accumulator.total;
    }

    private static class Accumulator {
        public long total = 0;

        public void add(long value) {
            total += value;
        }
    }

}

package org.example.part2;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

public class Main7 {
    public static void main(String[] args) {
//        ch71();
        ch72();
    }

    private static void ch72() {
        Long result1 = forkJoinSum(10L);
        System.out.println("result1 = " + result1);
    }

    private static Long forkJoinSum(long n) {
        long[] numbers = LongStream.rangeClosed(1, n).toArray();
        ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
        return new ForkJoinPool().invoke(task);
    }

    private static void ch71() {


        System.out.println("SideEffect parallel sum :" + sideEffectParallelSum(10_000_000));
        System.out.println("SideEffect parallel sum :" + sideEffectParallelSum(10_000_000));
        System.out.println("SideEffect parallel sum :" + sideEffectParallelSum(10_000_000));

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

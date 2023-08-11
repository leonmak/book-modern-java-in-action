package org.example.part2;

import java.util.concurrent.RecursiveTask;

public class ForkJoinSumCalculator extends RecursiveTask<Long> { // fork/join framework 사용

    private final long[] numbers; // 합계를 구할 배열

    // subtask의 array 범위
    private final int start;
    private final int end;

    // subtask로 분할할 최소 배열 크기
    public static final long THRESHOLD = 10_000;

    // main task 생성시 사용
    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    // subtask 생성시 사용
    public ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    // recursive task의 compute 메서드 구현
    @Override
    protected Long compute() {
        int length = end - start;

        // 기준값보다 작으면 순차적으로 계산
        if (length <= THRESHOLD) {
            return computeSequentially();
        }

        // subtask 생성
        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length / 2);
        leftTask.fork(); // 비동기 실행

        // subtask 생성
        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length / 2, end);
        Long rightResult = rightTask.compute(); // 동기 실행
        Long leftResult = leftTask.join();//  block : left task의 결과가 나올때까지 기다림

        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++)
            sum += numbers[i];
        return sum;
    }
}

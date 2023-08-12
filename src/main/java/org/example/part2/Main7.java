package org.example.part2;

import java.util.Spliterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Main7 {
    public static void main(String[] args) {
//        ch71();
//        ch72();
        ch73();
    }

    private static void ch73() {
        final String SENTENCE =
                " Nel mezzo del cammin di nostra vita " +
                        "mi ritrovai in una selva oscura" +
                        " ché la dritta via era smarrita ";
        System.out.println("Found " + countWordsIteratively(SENTENCE) + " words");

        Stream<Character> stream = IntStream.range(0, SENTENCE.length())
                .mapToObj(SENTENCE::charAt);
        int result = countWords(stream);
        System.out.println("result = " + result);

        Spliterator<Character> spliterator = new WordCounterSpliterator(SENTENCE);
        Stream<Character> streamParallel = StreamSupport.stream(spliterator, true); // 2nd param: parallel boolean
        System.out.println("result2 = " + countWords(streamParallel));

    }

    private static int countWords(Stream<Character> stream) {
        WordCounter wordCounter = stream.reduce(new WordCounter(0, true),
                WordCounter::accumulate,
                WordCounter::combine);

        return wordCounter.getCounter();
    }

    private static int countWordsIteratively(String s) {


        int counter = 0;
        boolean lastSpace = true;
        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                lastSpace = true;
            } else {
                if (lastSpace) counter++;
                lastSpace = false;
            }
        }
        return counter;
    }

    private static class WordCounterSpliterator implements Spliterator<Character> {
        private final String str;
        private int currentChar = 0;

        public WordCounterSpliterator(String str) {
            this.str = str;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Character> action) {
            action.accept(str.charAt(currentChar++));
            return currentChar < str.length(); // 다음 문자가 남아있으면 true
        }

        // 문자열 분리 시 어절 단위로 분리
        @Override
        public Spliterator<Character> trySplit() {
            int currentSize = str.length() - currentChar;

            // 10글자 이하면 분리하지 않고 sequential 처리
            if (currentSize < 10)
                return null;

            // 분리할 문자열의 중간을 찾음
            for (int splitPos = currentSize / 2 + currentChar; splitPos < str.length(); splitPos++) {

                // 공백이면 split
                if (Character.isWhitespace(str.charAt(splitPos))) {
                    Spliterator<Character> spliterator =
                            new WordCounterSpliterator(str.substring(currentChar, splitPos));
                    currentChar = splitPos;
                    return spliterator;
                }
            }
            return null;
        }

        @Override
        public long estimateSize() {
            return str.length() - currentChar;
        }

        @Override
        public int characteristics() {
            return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
        }
    }

    private static class WordCounter {
        private final int counter;
        private final boolean lastSpace;

        public WordCounter(int counter, boolean lastSpace) {
            this.counter = counter;
            this.lastSpace = lastSpace;
        }

        // iterative algorithm
        public WordCounter accumulate(Character c) {
            if (Character.isWhitespace(c)) {
                return lastSpace ? this : new WordCounter(counter, true);
            } else {
                return lastSpace ? new WordCounter(counter + 1, false) : this;
            }
        }

        public WordCounter combine(WordCounter wordCounter) {
            return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
        }

        public int getCounter() {
            return counter;
        }
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

package sample;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@BenchmarkMode(Mode.AverageTime) // benchmark mode
@OutputTimeUnit(TimeUnit.MICROSECONDS) // output time unit
@Fork(value = 1, jvmArgs = {"-Xms4G", "-Xmx4G"})// 2번 실행, 4GB Heap size
@State(Scope.Benchmark)
public class ParallelStreamBenchmark {

    private static final long N = 10_000_000L;

//    @Benchmark
//    public long sequentialSum() {
//        return Stream.iterate(1L, i -> i + 1)
//                .limit(N)
//                .reduce(0L, Long::sum);
//    }
//
//    @Benchmark
//    public long iterativeSum() {
//        long result = 0;
//        for (long i = 1L; i <= N; i++) {
//            result += i;
//        }
//        return result;
//    }

//    @Benchmark
//    public long rangedSum() {
//        return LongStream.rangeClosed(1, N)
//                .reduce(0L, Long::sum);
//    }

    @Benchmark
    public long parallelRangedSum() {
        return LongStream.rangeClosed(1, N)
                .parallel()
                .reduce(0L, Long::sum);
    }

    @TearDown(Level.Invocation) // 각각의 벤치마크 실행 이후에 실행
    public void tearDown() {
        System.gc();
    }

}

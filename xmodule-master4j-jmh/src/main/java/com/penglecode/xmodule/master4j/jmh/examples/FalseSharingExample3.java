package com.penglecode.xmodule.master4j.jmh.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import sun.misc.Contended;

import java.util.concurrent.TimeUnit;

/**
 * 伪共享问题
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/31 23:47
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class FalseSharingExample3 {

    @State(Scope.Group)
    public static class InlineState {
        long value1;
        long value2;
    }

    @Benchmark
    @Group("inline")
    @GroupThreads(10)
    public long writer1(InlineState s) {
        return s.value1 = System.currentTimeMillis();
    }

    @Benchmark
    @Group("inline")
    @GroupThreads(10)
    public long writer2(InlineState s) {
        return s.value2 = System.currentTimeMillis();
    }

    @State(Scope.Group)
    public static class PaddedState {
        @Contended
        long value1;
        @Contended
        long value2;
    }

    @Benchmark
    @Group("padded")
    @GroupThreads(10)
    public long writer1(PaddedState s) {
        return s.value1 = System.currentTimeMillis();
    }

    @Benchmark
    @Group("padded")
    @GroupThreads(10)
    public long writer2(PaddedState s) {
        return s.value2 = System.currentTimeMillis();
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(FalseSharingExample3.class.getSimpleName())
                .threads(8)
                .build();
        new Runner(options).run();
    }

}

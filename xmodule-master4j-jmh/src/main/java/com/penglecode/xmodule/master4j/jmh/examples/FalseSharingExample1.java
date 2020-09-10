package com.penglecode.xmodule.master4j.jmh.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

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
public class FalseSharingExample1 {

    @State(Scope.Group)
    public static class InlineState {
        int readOnly;
        int writeOnly;
    }

    @Benchmark
    @Group("inline")
    @GroupThreads(4)
    public int reader(InlineState s) {
        return s.readOnly;
    }

    @Benchmark
    @Group("inline")
    @GroupThreads(4)
    public int writer(InlineState s) {
        return s.writeOnly++;
    }

    @State(Scope.Group)
    public static class PaddedState {
        int readOnly;
        int p01, p02, p03, p04, p05, p06, p07, p08;
        int p11, p12, p13, p14, p15, p16, p17, p18;
        int writeOnly;
        int q01, q02, q03, q04, q05, q06, q07, q08;
        int q11, q12, q13, q14, q15, q16, q17, q18;
    }

    @Benchmark
    @Group("padded")
    @GroupThreads(4)
    public int reader(PaddedState s) {
        return s.readOnly;
    }

    @Benchmark
    @Group("padded")
    @GroupThreads(4)
    public int writer(PaddedState s) {
        return s.writeOnly++;
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(FalseSharingExample1.class.getSimpleName())
                .threads(8)
                .build();
        new Runner(options).run();
    }

}

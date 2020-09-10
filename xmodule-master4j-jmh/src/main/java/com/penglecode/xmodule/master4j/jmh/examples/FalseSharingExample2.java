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
 * 此例添加启动参数：-XX:-RestrictContended 即会消除性能差异
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
public class FalseSharingExample2 {

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
        long value1;
        long p01, p02, p03, p04, p05, p06;
        long p11, p12, p13, p14, p15, p16;
        long value2;
        long q01, q02, q03, q04, q05, q06;
        long q11, q12, q13, q14, q15, q16;
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
        Options options = new OptionsBuilder().include(FalseSharingExample2.class.getSimpleName())
                .threads(8)
                .build();
        new Runner(options).run();
    }

}

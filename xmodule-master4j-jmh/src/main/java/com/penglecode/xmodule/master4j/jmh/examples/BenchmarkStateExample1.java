package com.penglecode.xmodule.master4j.jmh.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @State注解示例
 *
 * 本例使用JMH的多线程模式，来测试ConcurrentHashMap和SynchronizedMap的并发写入性能
 *
 * Scope.Thread     默认状态。实例将不共享分配给运行给定测试的每个线程。
 * Scope.Benchmark	运行相同测试的所有线程将共享实例。可以用来测试状态对象的多线程性能(或者仅标记该范围的基准)。这个配合@Threads(nThreads)注解来设置并发线程数
 * Scope.Group	    实例分配给每个线程组(查看后面的线程组部分)。这个配合@Group("your group name")注解一起使用
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/31 16:58
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(1)
@Threads(4) //多线程并发测试的测试线程数
public class BenchmarkStateExample1 {

    public static final int INIT_CAPACITY = 1200000;

    /**
     * 共享变量
     * 注意@State注解只能作用于类上面，所以才这样写
     */
    @State(Scope.Benchmark)
    public static class ConcurrentMapHolder {

        Map<String,Long> map = new ConcurrentHashMap<>(INIT_CAPACITY);

    }

    /**
     * 共享变量
     * 注意@State注解只能作用于类上面，所以才这样写
     */
    @State(Scope.Benchmark)
    public static class SynchronizedMapHolder {

        Map<String,Long> map = Collections.synchronizedMap(new HashMap<>(INIT_CAPACITY));

    }

    @Benchmark
    public void testConcurrentMapPerformance(ConcurrentMapHolder mapHolder) {
        for(int i = 0; i < 100; i++) {
            long nanoTime = System.nanoTime();
            mapHolder.map.put(String.valueOf(nanoTime), nanoTime);
        }
    }

    @Benchmark
    public void testSynchronizedMapPerformance(SynchronizedMapHolder mapHolder) {
        for(int i = 0; i < 100; i++) {
            long nanoTime = System.nanoTime();
            mapHolder.map.put(String.valueOf(nanoTime), nanoTime);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(BenchmarkStateExample1.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

}

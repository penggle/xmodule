package com.penglecode.xmodule.master4j.jmh.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @BenchmarkMode注解示例
 *
 * Mode.Throughput      计算一个时间单位内操作数量
 * Mode.AverageTime	    计算平均运行时间
 * Mode.SampleTime	    计算一个方法的运行时间(包括百分位)
 * Mode.SingleShotTime	方法仅运行一次(用于冷测试模式)。或者特定批量大小的迭代多次运行(具体查看后面的“@Measurement“注解)——这种情况下JMH将计算批处理运行时间(一次批处理所有调用的总时间)
 * 这些模式的任意组合	    可以指定这些模式的任意组合——该测试运行多次(取决于请求模式的数量)
 * Mode.All	            所有模式依次运行
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/31 16:23
 */
public class BenchmarkModeExample {

    /**
     * Mode.AverageTime - 平均时间：调用一次该方法花费的平均时间
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Warmup(iterations = 5, time = 1)
    @Measurement(iterations = 5, time = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(1)
    public void testAverageTime() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
    }

    /**
     * Mode.Throughput - 吞吐量：单位时间调用该方法的次数
     */
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Warmup(iterations = 5, time = 1)
    @Measurement(iterations = 5, time = 1)
    @OutputTimeUnit(TimeUnit.SECONDS)
    @Fork(1)
    public void testThroughput() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
    }

    /**
     * Mode.SampleTime - 抽样测试：即在每一轮迭代中抽样数据(执行时间)，形成直方图
     */
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @Warmup(iterations = 5, time = 1)
    @Measurement(iterations = 5, time = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(1)
    public void testSampleTime() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
    }

    /**
     * Mode.SingleShotTime - 冷测试：这个跟JUnit测试差不多了，iterations即是测试方法的实际调用次数
     */
    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 0, time = 1)
    @Measurement(iterations = 5, time = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(1)
    public void testSingleShotTime() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
    }

    /**
     * 组合：平均时间 + 吞吐量
     */
    @Benchmark
    @BenchmarkMode({Mode.AverageTime, Mode.Throughput})
    @Warmup(iterations = 5, time = 1)
    @Measurement(iterations = 5, time = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(1)
    public void testAverageTimeAndThroughput() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(10);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(BenchmarkModeExample.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

}

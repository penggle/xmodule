package com.penglecode.xmodule.master4j.jmh.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 结论：batchSize属性只能与@BenchmarkMode(Mode.SingleShotTime)模式配合使用
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/31 21:26
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class BenchSizeExample {

    private List<String> list = new LinkedList<>();

    /**
     * 在预热前和正式测量前清空list内容
     *
     * 如果去掉此步骤，那么正式测量即原封不动的紧接着预热后的状态开始的，
     * 因此在正式测量时，list中已经有很多数据，继续middle-insert的话，性能成绩(score)会越来越差
     */
    @Setup(Level.Iteration)
    public void setup(){
        //System.out.print("-----------------list.clear()---------------"); //此句可以了解setup()执行的时机
        list.clear();
    }

    @Benchmark
    @Warmup(iterations = 5, time = 1)
    @Measurement(iterations = 5, time = 1)
    @BenchmarkMode(Mode.AverageTime)
    public List<String> measureWrong() {
        list.add(list.size() / 2, "something");
        return list;
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 5000)
    @Measurement(iterations = 5, batchSize = 5000)
    @BenchmarkMode(Mode.SingleShotTime)
    public List<String> measureRight() {
        list.add(list.size() / 2, "something");
        return list;
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(BenchSizeExample.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(options).run();
    }

}

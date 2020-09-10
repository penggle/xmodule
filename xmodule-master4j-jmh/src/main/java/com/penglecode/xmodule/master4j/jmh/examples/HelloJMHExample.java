package com.penglecode.xmodule.master4j.jmh.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

/**
 * 简单JMH入门示例
 *
 * https://www.ixigua.com/6612602244040753668/
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/31 14:12
 */
@BenchmarkMode(Mode.AverageTime) //性能测试的关注点(比如吞吐量、平均时间、ALL等)
@State(Scope.Thread) //用于标识程序的状态，其中：Scope.Thread：默认的State，每个测试线程分配一个实例；Scope.Benchmark：所有测试线程共享一个实例，用于测试有状态实例在多线程共享下的性能；Scope.Group：每个线程组共享一个实例；
@OutputTimeUnit(TimeUnit.MICROSECONDS) //性能测试输出结果有关时间的单位
public class HelloJMHExample {

    @Benchmark
    public void hello() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(HelloJMHExample.class.getSimpleName())
                .forks(1) //指定启动的JVM虚拟机数量
                /**
                 * 这个迭代次数并不是说就只运行5次，而是指每次运行10秒(默认时间)，共运行5次
                 * 就上面这个hello()方法来说，运行10秒相当于运行了接近10000次hello()，因此运行5次后，hello()方法大概运行了50000次左右
                 */
                .measurementIterations(5) //测试迭代次数
                .measurementTime(TimeValue.seconds(1)) //设置每个批次的运行时间(默认10秒)
                /**
                 * 同上，不再赘述
                 */
                .warmupIterations(10) //预热迭代次数
                .warmupTime(TimeValue.seconds(1)) //设置每个批次的运行时间(默认10秒)
                .build();

        new Runner(options).run();
    }

}

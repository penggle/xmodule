package com.penglecode.xmodule.master4j.jmh.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @State注解示例
 *
 * 同一组，共享状态
 * 不同组，不共享状态
 *
 * Scope.Thread     默认状态。实例将不共享分配给运行给定测试的每个线程。
 * Scope.Benchmark	运行相同测试的所有线程将共享实例。可以用来测试状态对象的多线程性能(或者仅标记该范围的基准)。这个配合@Threads(nThreads)注解来设置并发线程数
 * Scope.Group	    实例分配给每个线程组(查看后面的线程组部分)。这个配合@Group("your group name")注解一起使用
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/31 18:09
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(1)
public class BenchmarkStateExample3 {

    /**
     * 先声明组态实例，同一组的将共享此实例
     * 此状态实例的定义遵循三个规则：
     * 1、必须声明为public class
     * 2、如果是内部类则必须声明为public static class
     * 3、类中必须含有public的无参构造器
     */
    @State(Scope.Group)
    public static class Test {

        int i = 0;

    }

    @Benchmark
    @Group("test1") //由于testAdd()和testSub()不是同一组的，所以他们的状态参数(Test test)将不是同一个
    @GroupThreads(4)
    public void testAdd(Test test) {
        if(test.i == 0) {
            System.out.println("testAdd======> " + test.hashCode());
        }
        test.i++;
    }

    @Benchmark
    @Group("test2") //由于testAdd()和testSub()不是同一组的，所以他们的状态参数(Test test)将不是同一个
    @GroupThreads(4)
    public void testSub(Test test) {
        if(test.i == 0) {
            System.out.println("testSub======> " + test.hashCode());
        }
        test.i--;
    }

    /**
     * 通过测试可以看出只要@Group(name)中的name一致，那么test就是同一组实例
     */
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(BenchmarkStateExample3.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

}

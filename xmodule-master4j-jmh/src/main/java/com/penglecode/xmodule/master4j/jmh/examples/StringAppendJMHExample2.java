package com.penglecode.xmodule.master4j.jmh.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

/**
 * 使用JMH测试String、StringBuffer、StringBuilder三者的性能
 *
 * JIT的即时编译并不是随心所欲的，JVM会对热点代码进行JIT即时编译生成本地代码，这样加快执行效率
 * 所以我们总是在幻想着JIT所带来的的性能提升是不切实际的
 *
 * 本次基准测试用例是在JIT关闭的情况下
 * 通过注解@CompilerControl(CompilerControl.Mode.EXCLUDE)来关闭JIT编译，
 * 这也是我们在做微基准测试时的正确方法，
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/31 15:26
 */
@BenchmarkMode(Mode.AverageTime) //性能测试的关注点(比如吞吐量、平均时间、ALL等)
@State(Scope.Thread) //用于标识程序的状态，其中：Scope.Thread：默认的State，每个测试线程分配一个实例；Scope.Benchmark：所有测试线程共享一个实例，用于测试有状态实例在多线程共享下的性能；Scope.Group：每个线程组共享一个实例；
@OutputTimeUnit(TimeUnit.MICROSECONDS) //性能测试输出结果有关时间的单位
public class StringAppendJMHExample2 {

    @Benchmark
    @CompilerControl(CompilerControl.Mode.EXCLUDE)
    public String stringAppend() {
        String s = "";
        for (int i = 0; i < 100; i++) {
            s = s + i;
        }
        return s;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.EXCLUDE)
    public StringBuffer stringBufferAppend() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 100; i++) {
            sb.append(i);
        }
        return sb;
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.EXCLUDE)
    public StringBuilder stringBuilderAppend() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append(i);
        }
        return sb;
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(StringAppendJMHExample2.class.getSimpleName())
                .forks(1) //指定启动的JVM虚拟机数量
                .measurementIterations(5) //测试迭代次数
                .measurementTime(TimeValue.seconds(1)) //设置每个批次的运行时间(默认10秒)
                .warmupIterations(10) //预热迭代次数
                .warmupTime(TimeValue.seconds(1)) //设置每个批次的运行时间(默认10秒)
                .build();

        new Runner(options).run();
    }

}

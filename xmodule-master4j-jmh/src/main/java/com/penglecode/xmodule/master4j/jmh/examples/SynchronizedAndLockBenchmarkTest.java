package com.penglecode.xmodule.master4j.jmh.examples;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * synchronized关键字同步与JUC Lock同步的基准测试
 *
 * 经过多番测试，ReentrantLock比synchronzied性能要高出个20%~30%左右
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/1 12:53
 */
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(1)
@Threads(8) //多线程并发测试的测试线程数
public class SynchronizedAndLockBenchmarkTest {

    public static abstract class AbstractCounter {

        private long count;

        public long increment() {
            return ++count;
        }

        @Setup(Level.Iteration)
        public void reset() {
            count = 0;
            System.out.println("reset count : " + count);
        }

    }

    @State(Scope.Benchmark)
    public static class SynchronizedCounter extends AbstractCounter {

        public synchronized long increment() {
            return super.increment();
        }

    }

    @State(Scope.Benchmark)
    public static class ReentrantLockCounter extends AbstractCounter {

        private final Lock lock = new ReentrantLock();

        public long increment() {
            lock.lock();
            try {
                return super.increment();
            } finally {
                lock.unlock();
            }
        }

    }

    @Benchmark
    public long testSynchronizedPerformance(SynchronizedCounter counter) {
        return counter.increment();
    }

    @Benchmark
    public long testReentrantLockPerformance(ReentrantLockCounter counter) {
        return counter.increment();
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(SynchronizedAndLockBenchmarkTest.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

}

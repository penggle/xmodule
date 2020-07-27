package com.penglecode.xmodule.master4j.jvm.chapter13.synchronization;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 不同方式实现的计数器的性能测试
 *
 * 在JDK8下，本例测试synchronized阻塞同步和JUC Atomic非阻塞同步得性能已经不相上下，说明随着JDK版本的越新，JVM对synchronized做了很大的优化
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/26 21:18
 */
public class CounterPerformanceExample {

    interface Counter {

        public int incrementAndGet();

        public int getCount();

    }

    static class SynchronizedCounter implements Counter {

        private volatile int count;

        public synchronized int incrementAndGet() {
            return ++count;
        }

        public int getCount() {
            return count;
        }
    }

    static class AtomicIntegerCounter implements Counter {

        private final AtomicInteger count = new AtomicInteger(0);

        public synchronized int incrementAndGet() {
            return count.incrementAndGet();
        }

        public int getCount() {
            return count.get();
        }

    }

    static class CounterTestThread extends Thread {

        private final Counter counter;

        private final int increments;

        private final CountDownLatch countDownLatch;

        public CounterTestThread(Counter counter, int increments, CountDownLatch countDownLatch) {
            this.counter = counter;
            this.increments = increments;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            for(int i = 0; i < increments; i++) {
                counter.incrementAndGet();
            }
            countDownLatch.countDown();
        }

    }

    public static void testCounter(Counter counter) throws Exception {
        int testThreads = Runtime.getRuntime().availableProcessors() * 2;
        CountDownLatch countDownLatch = new CountDownLatch(testThreads);
        int batchIncrements = 10000000;

        long start = System.currentTimeMillis();
        for(int i = 0; i < testThreads; i++) {
            new CounterTestThread(counter, batchIncrements, countDownLatch).start();
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        System.out.println(String.format(">>> increment from 0 to %s, count = %s, time cost: %s ms", testThreads * batchIncrements, counter.getCount(), (end - start)));
    }

    public static void main(String[] args) throws Exception {
        //testCounter(new SynchronizedCounter()); //16个线程，每个线程各递增10000000次，平均耗时在5600毫秒左右
        testCounter(new AtomicIntegerCounter()); //16个线程，每个线程各递增10000000次，平均耗时在5200毫秒左右
    }

}

package com.penglecode.xmodule.master4j.java.util.concurrent.simplelocks;

import com.penglecode.xmodule.master4j.java.util.concurrent.SimpleLock;

/**
 * 简单自旋锁实现的测试
 *
 */
public class SimpleLockTest {

    public static class TestTask implements Runnable {

        private final SimpleLock lock;

        private final int iterations;

        private int count;

        public TestTask(SimpleLock lock, int iterations) {
            this.lock = lock;
            this.iterations = iterations;
        }

        @Override
        public void run() {
            for(int i = 0; i < iterations; i++) {
                increment();
            }
        }

        protected void increment() {
            lock.lock();
            count++;
            lock.unlock();
        }

        public int getCount() {
            return count;
        }

    }

    public static void testSimpleLock(SimpleLock lock) throws InterruptedException {
        int nThreads = 32;
        int iterations = 1000;
        TestTask task = new TestTask(lock, iterations);

        Thread[] threads = new Thread[nThreads];
        for(int i = 0; i < nThreads; i++) {
            threads[i] = new Thread(task);
        }

        long marked = System.currentTimeMillis();

        for(int i = 0; i < nThreads; i++) {
            threads[i].start();
        }
        for(int i = 0; i < nThreads; i++) {
            threads[i].join();
        }
        System.out.println(lock.getClass().getSimpleName() + " test done, latest count = " + task.getCount() + ", elapsed time = " + (System.currentTimeMillis() - marked) + "ms");
    }

    public static void main(String[] args) throws InterruptedException {
        //testSimpleLock(new SimpleSpinLock());
        for(int i = 0; i < 100; i++) {
            System.gc();
            testSimpleLock(new SimpleCLHLock1());
        }
    }

}

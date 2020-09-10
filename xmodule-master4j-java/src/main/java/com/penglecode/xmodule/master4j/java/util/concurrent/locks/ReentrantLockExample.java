package com.penglecode.xmodule.master4j.java.util.concurrent.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁示例
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/3 18:47
 */
public class ReentrantLockExample {

    /**
     * 悲观锁示例
     */
    public static void lockAndUnlockTest() throws Exception {
        final int iterations = 1000;
        final Lock lock = new ReentrantLock();
        final long[] count = new long[] {0};
        Runnable counter = () -> {
            for(int i = 0; i < iterations; i++) {
                lock.lock();
                try {
                    count[0]++;
                } finally {
                    lock.unlock();
                }
            }
        };

        int nThreads = Runtime.getRuntime().availableProcessors() * 2;
        Thread[] threads = new Thread[nThreads];
        for(int i = 0; i < nThreads; i++) {
            threads[i] = new Thread(counter);
        }

        long marked = System.currentTimeMillis();
        for(int i = 0; i < nThreads; i++) {
            threads[i].start();
        }
        for(int i = 0; i < nThreads; i++) {
            threads[i].join();
        }

        System.out.println("Latest count = " + count[0] + ", elapsed time = " + (System.currentTimeMillis() - marked) + "ms");
    }

    /**
     * 乐观锁示例
     */
    public static void tryLockAndUnlockTest() throws Exception {
        final int iterations = 1000;
        final Lock lock = new ReentrantLock();
        final long[] count = new long[] {0};
        Runnable counter = () -> {
            for(int i = 0; i < iterations; i++) {
                for(;;) {
                    if(lock.tryLock()) {
                        try {
                            count[0]++;
                        } finally {
                            lock.unlock();
                        }
                        break;
                    }
                }
            }
        };

        int nThreads = Runtime.getRuntime().availableProcessors() * 2;
        Thread[] threads = new Thread[nThreads];
        for(int i = 0; i < nThreads; i++) {
            threads[i] = new Thread(counter);
        }

        long marked = System.currentTimeMillis();
        for(int i = 0; i < nThreads; i++) {
            threads[i].start();
        }
        for(int i = 0; i < nThreads; i++) {
            threads[i].join();
        }

        System.out.println("Latest count = " + count[0] + ", elapsed time = " + (System.currentTimeMillis() - marked) + "ms");
    }

    /**
     * 重入测试，记住lock次数一定要与unlock次数一致，否则会导致此锁永远不可用
     */
    public static void reentrantTest1() {
        final ReentrantLock lock = new ReentrantLock();
        int locks = 3;
        for(int i = 0; i < locks; i++) {
            lock.lock();
            System.out.println("【lock】>>> state = " + lock.getHoldCount());
        }

        int unlocks = 3;
        for(int i = 0; i < unlocks; i++) {
            System.out.println("【unlock】<<< state = " + lock.getHoldCount());
            lock.unlock();
        }
    }

    public static void reentrantTest2() {
        final ReentrantLock lock = new ReentrantLock();
        lock.lock();
        System.out.println("lock");
        Thread.currentThread().interrupt();
        System.out.println("unlock");
        lock.unlock();
    }

    public static void main(String[] args) throws Exception {
        //lockAndUnlockTest();
        //tryLockAndUnlockTest();
        //reentrantTest1();
        reentrantTest2();
    }

}

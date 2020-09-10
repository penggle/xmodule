package com.penglecode.xmodule.master4j.java.util.concurrent.aqs;

import com.penglecode.xmodule.master4j.java.util.concurrent.SimpleLock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 基于AQS的简单互斥锁示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/3 14:46
 */
public class SimpleAQSLock implements SimpleLock {

    private final Sync sync = new Sync();

    @Override
    public void lock() {
        sync.acquire(0);
    }

    @Override
    public void unlock() {
        sync.release(0);
    }

    /**
     * 简单互斥锁锁的同步器
     */
    private static class Sync extends AbstractQueuedSynchronizer {

        /**
         * 重写获取锁的模板方法
         * 重写方法所要做的事就是对state变量的维护，这里我们将state定义为当前获得锁的线程数，对于独占锁它的最大值应该是1
         * 通过调用getState()、setState()、compareAndSetState(int expect, int update)方法
         */
        @Override
        protected boolean tryAcquire(int arg) {
            return compareAndSetState(0, 1);
        }

        /**
         * 重写释放锁的模板方法
         * 重写方法所要做的事就是对state变量的维护，这里我们将state定义为当前获得锁的线程数，对于独占锁它的最大值应该是1
         * 通过调用getState()、setState()、compareAndSetState(int expect, int update)方法
         */
        @Override
        protected boolean tryRelease(int arg) {
            return compareAndSetState(1, 0);
        }
    }

}

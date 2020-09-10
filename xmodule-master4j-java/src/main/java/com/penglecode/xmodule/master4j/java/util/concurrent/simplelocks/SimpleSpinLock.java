package com.penglecode.xmodule.master4j.java.util.concurrent.simplelocks;

import com.penglecode.xmodule.master4j.java.util.concurrent.SimpleLock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 简单的自旋锁实现
 */
public class SimpleSpinLock implements SimpleLock {

    private final AtomicReference<Thread> lockOwner = new AtomicReference<>();

    @Override
    public void lock() {
        Thread currentThread = Thread.currentThread();
        // 如果锁未被占用，则设置当前线程为锁的拥有者，设置成功返回true，否则返回false
        while(!lockOwner.compareAndSet(null, currentThread)) {
        }
    }

    @Override
    public void unlock() {
        Thread currentThread = Thread.currentThread();
        // 只有锁的拥有者才能释放锁，也就是说只有锁的拥有者调用此方法才会有效果
        lockOwner.compareAndSet(currentThread, null);
    }

}

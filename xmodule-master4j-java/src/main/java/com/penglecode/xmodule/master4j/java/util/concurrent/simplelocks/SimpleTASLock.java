package com.penglecode.xmodule.master4j.java.util.concurrent.simplelocks;

import com.penglecode.xmodule.master4j.java.util.concurrent.SimpleLock;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * TASLock （Test And Set Lock），测试-设置锁，它的特点是自旋时，每次尝试获取锁时，
 * 采用了CAS操作，不断的设置锁标志位，当锁标志位可用时，一个线程拿到锁，其他线程继续自旋。
 *
 * 缺点是CAS操作一直在修改共享变量的值，会引发缓存一致性流量风暴
 *
 * 缓存一致性协议存在的一个最大的问题是可能引起缓存一致性流量风暴，之前我们看到总线在同一时刻只能被一个处理器使用，
 * 当有大量缓存被修改，或者同一个缓存块一直被修改时，会产生大量的缓存一致性流量，从而占用总线，影响了其他正常的读写请求。
 *
 * 一个最常见的例子就是如果多个线程对同一个变量一直使用CAS操作，那么会有大量修改操作，从而产生大量的缓存一致性流量，
 * 因为每一次CAS操作都会发出广播通知其他处理器，从而影响程序的性能。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/2 1:10
 */
public class SimpleTASLock implements SimpleLock {

    private final AtomicBoolean state = new AtomicBoolean(false);

    @Override
    public void lock() {
        while (state.getAndSet(true)) {};
    }

    @Override
    public void unlock() {
        state.set(false);
    }
}

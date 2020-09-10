package com.penglecode.xmodule.master4j.java.util.concurrent.simplelocks;

import com.penglecode.xmodule.master4j.java.util.concurrent.SimpleLock;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 改进的算法TTASLock（Test Test And Set Lock）测试-测试-设置锁，特点是在自旋尝试获取锁时，分为两步，第一步通过读操作来获取锁状态，当锁可获取时，第二步再通过CAS操作来尝试获取锁，减少了CAS的操作次数。并且第一步的读操作是处理器直接读取自身高速缓存，不会产生缓存一致性流量，不占用总线资源。
 * 缺点是在锁高争用的情况下，线程很难一次就获取锁，CAS的操作会大大增加。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/2 1:10
 */
public class SimpleTTASLock implements SimpleLock {

    private final AtomicBoolean state = new AtomicBoolean(false);

    @Override
    public void lock() {
        while (true) {
            //第一步使用读操作，尝试获取锁，当mutex为false时退出循环，表示可以获取锁
            while (state.get()) {}
            //第二部使用getAndSet方法来尝试获取锁
            if (!state.getAndSet(true)) {
                return;
            }
        }
    }

    @Override
    public void unlock() {
        state.set(false);
    }
}

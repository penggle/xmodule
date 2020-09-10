package com.penglecode.xmodule.master4j.java.util.concurrent.simplelocks;

import java.util.concurrent.atomic.AtomicBoolean;

/**
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

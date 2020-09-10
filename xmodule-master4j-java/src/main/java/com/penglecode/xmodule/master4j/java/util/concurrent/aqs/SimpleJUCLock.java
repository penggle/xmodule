package com.penglecode.xmodule.master4j.java.util.concurrent.aqs;

import com.penglecode.xmodule.master4j.java.util.concurrent.SimpleLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/3 15:15
 */
public class SimpleJUCLock implements SimpleLock {

    private final Lock lock = new ReentrantLock();

    @Override
    public void lock() {
        lock.lock();
    }

    @Override
    public void unlock() {
        lock.unlock();
    }
}

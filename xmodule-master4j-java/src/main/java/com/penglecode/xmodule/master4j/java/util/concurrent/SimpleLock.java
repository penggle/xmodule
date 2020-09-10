package com.penglecode.xmodule.master4j.java.util.concurrent;

/**
 * 简单的独占锁接口
 */
public interface SimpleLock {

    /**
     * 获取锁
     */
    public void lock();

    /**
     * 释放锁
     */
    public void unlock();

}

package com.penglecode.xmodule.master4j.integrates.example.redis.delayqueue;

import java.util.concurrent.Delayed;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/11 0:33
 */
public interface DelayQueue<T extends Delayed> {

    public void put(T element) throws Exception;

    public T take() throws Exception;

}

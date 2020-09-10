package com.penglecode.xmodule.master4j.java.util.concurrent.simplelocks;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 简单的CLH锁实现
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/1 22:21
 */
public class SimpleCLHLock3 implements SimpleLock {

    private final AtomicReference<Node> tail;

    private final ThreadLocal<Node> nodeHolder;

    public SimpleCLHLock3() {
        this.nodeHolder = new ThreadLocal<Node>();
        this.tail = new AtomicReference<>(new Node());
    }

    @Override
    public void lock() {
        Node node = new Node();
        nodeHolder.set(node);
        node.queuing = true;
        Node prev = tail.getAndSet(node); //将当前节点设为tail
        while(prev.queuing) {
        }
        System.out.println(">>> " + node.hashCode());
    }

    @Override
    public void unlock() {
        Node node = nodeHolder.get();
        node.queuing = false;
        System.out.println("<<< " + node.hashCode());
    }

    private class Node {

        private volatile boolean queuing;

    }

}

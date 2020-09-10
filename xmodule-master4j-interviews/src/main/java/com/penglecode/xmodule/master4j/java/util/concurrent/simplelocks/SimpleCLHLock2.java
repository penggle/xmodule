package com.penglecode.xmodule.master4j.java.util.concurrent.simplelocks;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 简单的CLH锁实现
 *
 * https://zhuanlan.zhihu.com/p/197840259?utm_source=wechat_session
 */
public class SimpleCLHLock2 implements SimpleLock {

    //指向最后排队获取锁的线程
    private final AtomicReference<Node> tail;

    //当前线程持有的节点,使用ThreadLocal实现了变量的线程隔离
    private final ThreadLocal<Node> nodeHolder;

    //当前线程持有的节点的前驱节点，使用ThreadLocal实现了变量的线程隔离
    private final ThreadLocal<Node> prevHolder;

    public SimpleCLHLock2() {
        this.nodeHolder = new ThreadLocal<>();
        this.prevHolder = new ThreadLocal<>();
        //初始化tail，指向一个node，类似一个head节点，并且该节点locked属性为false
        this.tail = new AtomicReference<>(new Node());
    }

    @Override
    public void lock() {
        //当前线程进来了，初始化一个代表当前线程的Node节点
        Node node = new Node();
        node.locked = true;
        nodeHolder.set(node);
        //作为一个公平锁，当前线程需要排队，排到队尾去
        Node prev = tail.getAndSet(node);
        //旧的队尾(prev)将会是当前节点的前驱节点
        prevHolder.set(prev);
        /**
         * 只关注前驱节点的状态即可，就像排队一样，只关心前面的人到哪了，后面排队的人不需要管，与我无关。
         * 第一个获取锁的线程，不需要自旋等待毫无吹灰之力滴获取到了锁，因为初始化时tail中放的就是一个无获取锁需求的Node（见上面构造器中的最后一句）
         */
        while(prev.locked) {
            /**
             * 释放CPU，防止出现"CPU竞争死锁"：
             * 即当前线程正要执行unlock()方法里面的代码时没有多余的空闲CPU响应(执行代码)，总共N个CPU全部被排在当前线程之后的N个线程占用(全部CPU都在执行忙等)，进而形成CPU级别的竞争死锁
             */
            Thread.yield(); //加上这句，此锁的并发性能大打折扣
        }
    }

    @Override
    public void unlock() {
        //释放锁也很简单，就是将当前线程持有的Node的locked标志位设为false，标识当前线程已经使用完了锁了
        Node node = nodeHolder.get();
        node.locked = false;
    }

    private class Node {

        //默认不需要获取锁
        private volatile boolean locked = false;

    }

}

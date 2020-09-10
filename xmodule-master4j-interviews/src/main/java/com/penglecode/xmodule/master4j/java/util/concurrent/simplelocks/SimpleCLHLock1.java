package com.penglecode.xmodule.master4j.java.util.concurrent.simplelocks;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 简单的CLH锁实现
 *
 * https://zhuanlan.zhihu.com/p/197840259?utm_source=wechat_session
 */
public class SimpleCLHLock1 implements SimpleLock {

    //指向最后排队获取锁的线程
    private final AtomicReference<Node> tail;

    //当前线程持有的节点,使用ThreadLocal实现了变量的线程隔离
    private final ThreadLocal<Node> nodeHolder;

    //当前线程持有的节点的前驱节点，使用ThreadLocal实现了变量的线程隔离
    private final ThreadLocal<Node> prevHolder;

    public SimpleCLHLock1() {
        this.nodeHolder = ThreadLocal.withInitial(() -> new Node(true));
        this.prevHolder = new ThreadLocal<>();
        //初始化tail，指向一个node，类似一个head节点，并且该节点locked属性为false
        this.tail = new AtomicReference<>(new Node(false));
    }

    @Override
    public void lock() {
        //当前线程进来了，初始化一个代表当前线程的Node节点
        Node node = nodeHolder.get();
        /**
         * 设置当前线程所持有的Node节点，将其设为true代表当前线程期望获得锁
         * 注意此句不能放在ThreadLocal的initialValue()方法中，
         * 例如：ThreadLocal.withInitial(() -> new Node(true));
         * 这样会发生重排序，也即构造器内指令溢出：
         * 例如线程A执行上面new Node(true)时，true值还未设置到locked上时，
         * 紧接着排队在线程A后面的线程B已经走到了下面的
         * while(..)阻塞语句处了，发现它前面的node.locked为false，因而出现插队现象。
         */
        node.locked = true; //禁止指令重排序，不能省略此句

        //作为一个公平锁，当前线程需要排队，排到队尾去
        Node prev = tail.getAndSet(node);
        //旧的队尾(prev)将会是当前节点的前驱节点
        prevHolder.set(prev);
        /**
         * 只关注前驱节点的状态即可，就像排队一样，只关心前面的人到哪了，后面排队的人不需要管，与我无关。
         * 第一个获取锁的线程，不需要自旋等待毫无吹灰之力滴获取到了锁，
         * 因为初始化时tail中放的就是一个无获取锁需求的Node（见上面构造器中的最后一句）
         */
        while(prev.locked) {
            /**
             * 释放CPU，防止出现"CPU竞争暂歇性死锁"：
             * 即当前线程正要执行unlock()方法里面的代码时没有多余的空闲CPU响应(执行代码)，
             * 总共N个CPU全部被排在当前线程之后的N个线程占用(全部CPU都在执行忙等)
             * 进而造成unlock()中的代码很长时间获得不到CPU时间，造成暂歇性死锁
             * (可以注掉下面yield语句，同时解开两处关于node.hashCode()的打印语句，即可观察到现象)
             */
            Thread.yield(); //加上这句，此锁的并发性能大打折扣
        }
        //System.out.println(">>> " + node.hashCode());
    }

    @Override
    public void unlock() {
        //释放锁也很简单，就是将当前线程持有的Node的locked标志位设为false，标识当前线程已经使用完了锁了
        Node node = nodeHolder.get();
        node.locked = false;
        /**
         * 但是考虑到当前线程连续成功获取锁的情况，就会出现lock()方法中while()永远独占忙等，
         * 即出现死锁，解决办法以下两者选其一
         */
        //nodeHolder.set(new Node());
        nodeHolder.set(prevHolder.get()); //相比于上一句代码，此句更省内存及减轻GC压力
        //System.out.println("<<< " + node.hashCode());
    }

    private class Node {

        //默认不需要获取锁
        private volatile boolean locked;

        public Node(boolean locked) {
            this.locked = locked;
        }

    }

}

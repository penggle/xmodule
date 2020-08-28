package com.penglecode.xmodule.master4j.java.lang.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * synchronized 是非公平锁。ReentrantLock 默认是非公平锁，但可设置为公平锁。
 *
 * 那么线程通过Object.nofity() 和 Condition.signal() 被唤醒时是否是公平的呢？
 * 先说结果，在Java 1.8 HotSpot下，两者都是公平的。
 *
 * Object.nofity() 的API文档明确说一个随机的线程将被唤醒，但具体情况将由实现者决定，因为Object.nofity()是一个native方法。
 *
 * Condition.signal() 的API文档则说一个被选定的线程将被唤醒。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/27 16:36
 */
public class NotifyOrderExample {

    public static abstract class AbstractNotifyOrderThread extends Thread {

        private final Integer threadOrder;

        private final List<Integer> waitOrders;

        private final List<Integer> notifyOrders;

        protected AbstractNotifyOrderThread(Integer threadOrder, List<Integer> waitOrders, List<Integer> notifyOrders) {
            this.threadOrder = threadOrder;
            this.waitOrders = waitOrders;
            this.notifyOrders = notifyOrders;
        }

        public Integer getThreadOrder() {
            return threadOrder;
        }

        public List<Integer> getWaitOrders() {
            return waitOrders;
        }

        public List<Integer> getNotifyOrders() {
            return notifyOrders;
        }

        protected void randomSleep(int maxMillis) {
            try {
                Thread.sleep(new Random().nextInt(maxMillis));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static class ObjectNotifyOrderThread extends AbstractNotifyOrderThread {

        private final Object monitor;

        public ObjectNotifyOrderThread(Integer threadOrder, List<Integer> waitOrders, List<Integer> notifyOrders, Object monitor) {
            super(threadOrder, waitOrders, notifyOrders);
            this.monitor = monitor;
        }

        @Override
        public void run() {
            randomSleep(100); //注意不能放在下面的synchronized块中，否则无法打算线程顺序

            synchronized (monitor) {
                try {
                    getWaitOrders().add(getThreadOrder()); //各个线程进入wait的顺序
                    monitor.wait();
                    getNotifyOrders().add(getThreadOrder()); //各个线程被notify的顺序
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void objectNotifyOrderTest(String notifyType) throws Exception {
        Object monitor = new Object();
        List<Integer> waitOrders = new ArrayList<>();
        List<Integer> notifyOrders = new ArrayList<>();

        int nThreads = 30;
        System.out.println(String.format("【%s】启动%s个线程，它们即将运行并陆续进入wait等待状态...", Thread.currentThread().getName(), nThreads));
        for(int i = 0; i < nThreads; i++) { //启动30个线程
            new ObjectNotifyOrderThread(i, waitOrders, notifyOrders, monitor).start();
        }

        while(waitOrders.size() < nThreads) {
            Thread.sleep(50); //小等一下
        }
        /**
         * 通过while()检测，说明ObjectNotifyOrderThread.run()方法中的同步块中nThreads个线程都已经进去了
         * 但不代表monitor.wait();被完全执行，它们被完全执行的的标志是下面synchronized (monitor)能获得到monitor锁
         * (要知道monitor.wait();执行完毕后会暂时释放对象锁的)
         */
        synchronized (monitor) {
            System.out.println(String.format("【%s】%s个线程，都进入wait等待状态...", Thread.currentThread().getName(), nThreads));
        }

        if("notify".equals(notifyType)) {
            //Object.notify()是按FIFO方式唤醒的
            for(int i = 0; i < nThreads; i++) { //接着做30次唤醒操作
                Thread.sleep(50);
                synchronized (monitor) {
                    monitor.notify();
                }
            }
        } else if("notifyAll".equals(notifyType)) {
            //Object.notifyAll()是按LIFO方式唤醒的
            synchronized (monitor) {
                monitor.notifyAll();
            }
        } else {
            throw new IllegalStateException("Unsupported notifyType: " + notifyType);
        }

        while(notifyOrders.size() < nThreads) {
            Thread.sleep(50); //小等一下
        }
        //同理,不再叙述
        synchronized (monitor) {
            System.out.println(String.format("【%s】%s个线程，都被%s唤醒了...", Thread.currentThread().getName(), nThreads, notifyType));
        }

        System.out.println(String.format("【%s】wait休眠顺序 : %s", Thread.currentThread().getName(), waitOrders));
        System.out.println(String.format("【%s】%s唤醒顺序 = %s", Thread.currentThread().getName(), notifyType, notifyOrders));
    }

    public static class SignalNotifyOrderThread extends AbstractNotifyOrderThread {

        private final ReentrantLock lock;

        private final Condition condition;

        public SignalNotifyOrderThread(Integer threadOrder, List<Integer> waitOrders, List<Integer> notifyOrders, ReentrantLock lock, Condition condition) {
            super(threadOrder, waitOrders, notifyOrders);
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            randomSleep(100); //注意不能放在下面的synchronized块中，否则无法打算线程顺序
            try {
                lock.lock();
                getWaitOrders().add(getThreadOrder()); //各个线程进入wait的顺序
                condition.await();
                getNotifyOrders().add(getThreadOrder()); //各个线程被notify的顺序
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void signalNotifyOrderTest(String signalType) throws Exception {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        List<Integer> waitOrders = new ArrayList<>();
        List<Integer> notifyOrders = new ArrayList<>();

        int nThreads = 30;
        System.out.println(String.format("【%s】启动%s个线程，它们即将运行并陆续进入wait等待状态...", Thread.currentThread().getName(), nThreads));
        for(int i = 0; i < nThreads; i++) { //启动30个线程
            new SignalNotifyOrderThread(i, waitOrders, notifyOrders, lock, condition).start();
        }

        while(waitOrders.size() < nThreads) {
            Thread.sleep(50); //小等一下
        }
        //同理,不再叙述
        try {
            lock.lock();
            System.out.println(String.format("【%s】%s个线程，都进入wait等待状态...", Thread.currentThread().getName(), nThreads));
        } finally {
            lock.unlock();
        }

        if("signal".equals(signalType)) {
            //Condition.signal()是按FIFO方式唤醒的
            for(int i = 0; i < nThreads; i++) { //接着做30次唤醒操作
                Thread.sleep(50);
                try {
                    lock.lock();
                    condition.signal(); //signal()需要获得lock
                } finally {
                    lock.unlock();
                }
            }
        } else if("signalAll".equals(signalType)) {
            //Condition.signalAll()是按FIFO方式唤醒的
            try {
                lock.lock();
                condition.signalAll(); //signalAll()需要获得lock
            } finally {
                lock.unlock();
            }
        } else {
            throw new IllegalStateException("Unsupported signalType: " + signalType);
        }

        while(notifyOrders.size() < nThreads) {
            Thread.sleep(50); //小等一下
        }
        //同理,不再叙述
        try {
            lock.lock();
            System.out.println(String.format("【%s】%s个线程，都被%s唤醒了...", Thread.currentThread().getName(), nThreads, signalType));
        } finally {
            lock.unlock();
        }

        System.out.println(String.format("【%s】wait休眠顺序 : %s", Thread.currentThread().getName(), waitOrders));
        System.out.println(String.format("【%s】%s唤醒顺序 = %s", Thread.currentThread().getName(), signalType, notifyOrders));
    }

    public static void main(String[] args) throws Exception {
        //objectNotifyOrderTest("notify");
        //objectNotifyOrderTest("notifyAll");
        //signalNotifyOrderTest("signal");
        signalNotifyOrderTest("signalAll");
    }

}

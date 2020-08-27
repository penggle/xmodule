package com.penglecode.xmodule.master4j.java.lang.thread;

/**
 * Thread.sleep(..)方法要点：
 *
 * 1、sleep(..)方法只针对当前线程，即让当前线程进入休眠状态，传入的休眠时间并不是很精准。
 * 2、如果在synchronized块里进行sleep操作，或者在已获得锁的线程中执行sleep操作，都不会释放已获得对象锁，这点与Object.wait(..)不一样。
 * 3、当前A线程进入sleep睡眠之后，其他线程可以调用A.interrupt()方法中断A线程，解除睡眠状态并抛出InterruptedException
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/26 20:08
 */
public class ThreadSleepExample {

    /**
     * 如果在synchronized块里进行sleep操作，或者在已获得锁的线程中执行sleep操作，都不会释放已获得对象锁，这点与Object.wait(..)不一样。
     *
     * sleep操作不会释放对象锁的示例：
     * thread1获得到对象锁lock之后进入同步块，thread2在同步块外等待获取对象锁，thread1休眠5秒后退出同步块后，
     * thread2获得到对象锁进入同步块中休眠10秒后退出同步块
     */
    public static void sleepObjectMonitorTest() {
        final long[] duration = new long[2];
        final Object lock = new Object();
        Thread thread1 = new Thread(() -> {
            int sleeps = 5000;
            System.out.println(String.format("【%s】等待获得对象锁...", Thread.currentThread().getName()));
            synchronized (lock) {
                System.out.println(String.format("【%s】获得到对象锁[%s]进入了同步块中，即将进入休眠...", Thread.currentThread().getName(), lock));
                try {
                    Thread.sleep(sleeps);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(String.format("【%s】获得到对象锁[%s]并已经休眠了%s毫秒，即将退出同步块...", Thread.currentThread().getName(), lock, sleeps));
            }
        }, "Thread1");

        Thread thread2 = new Thread(() -> {
            int sleeps = 10000;
            System.out.println(String.format("【%s】等待获得对象锁...", Thread.currentThread().getName()));
            synchronized (lock) {
                System.out.println(String.format("【%s】获得到对象锁[%s]进入了同步块中，即将进入休眠...", Thread.currentThread().getName(), lock));
                try {
                    Thread.sleep(sleeps);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(String.format("【%s】获得到对象锁[%s]并已经休眠了%s毫秒，即将退出同步块...", Thread.currentThread().getName(), lock, sleeps));
            }
        }, "Thread2");

        thread1.start();
        thread2.start();
    }

    /**
     * 当前A线程进入sleep睡眠之后，其他线程可以调用A.interrupt()方法中断A线程，解除睡眠状态并抛出InterruptedException
     */
    public static void sleepInterruptTest() throws InterruptedException {
        Thread thread = new Thread(() -> {
            int sleepMillis = 5000;
            for(int i = 0; i < 10; i++) { //预期循环10次
                try {
                    Thread.sleep(sleepMillis);
                    System.out.println(String.format("【%s】处理第%s个任务耗时：%s毫秒...", Thread.currentThread().getName(), i + 1, sleepMillis));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(String.format("【%s】处理第%s个任务时发生中断...", Thread.currentThread().getName(), i + 1));
                    return; //接受到中断信号时，停止运行
                }
            }
        });
        thread.start(); //启动子线程
        Thread.sleep(12000); //主线程休眠
        System.out.println(String.format("【%s】主线程：等待12秒后发送中断信号, 当前子线程的中断状态：%s", Thread.currentThread().getName(), thread.isInterrupted()));
        thread.interrupt(); //发送中断信号
    }

    /**
     * sleep(0)并非指的是让当前线程睡眠0秒，它的意义是使当前线程让出该轮CPU执行机会。意义上与yield()方法相同
     */
    public static void sleep0AndYieldTest1() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(0);
                System.out.println("yield cpu time");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();

        for(int i = 0; i < 100; i++) {
            System.out.println("main thread");
        }
    }

    /**
     * sleep(0)并非指的是让当前线程睡眠0秒，它的意义是使当前线程让出该轮CPU执行机会。意义上与yield()方法相同
     */
    public static void sleep0AndYieldTest2() {
        Thread thread = new Thread(() -> {
            Thread.yield();
            System.out.println("yield cpu time");
        });
        thread.setDaemon(true);
        thread.start();

        for(int i = 0; i < 100; i++) {
            System.out.println("main thread");
        }
    }

    public static void main(String[] args) throws Exception {
        //sleepObjectMonitorTest();
        //sleepInterruptTest();
        //sleep0AndYieldTest1();
        sleep0AndYieldTest2();
    }

}

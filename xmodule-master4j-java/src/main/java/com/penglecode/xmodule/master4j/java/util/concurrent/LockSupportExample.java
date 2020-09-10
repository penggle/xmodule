package com.penglecode.xmodule.master4j.java.util.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport基本介绍与基本使用
 *
 * LockSupport是JDK中比较底层的类，用来创建锁和其他同步工具类的基本线程阻塞原语。
 * java锁和同步器框架的核心 AQS: AbstractQueuedSynchronizer，就是通过调用 LockSupport.park()和 LockSupport.unpark()实现线程的阻塞和唤醒的。
 *
 * LockSupport 很类似于二元信号量(只有1个许可证可供使用)，如果这个许可还没有被占用，当前线程获取许可并继续执行；
 * 如果许可已经被占用，当前线程阻塞，等待获取许可。
 *
 * http://www.tianshouzhi.com/api/tutorials/mutithread/303
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/30 19:49
 */
public class LockSupportExample {

    /**
     * 许可默认是被占用的 ，调用park()时获取不到许可，所以进入阻塞状态。
     */
    public static void parkTest() {
        System.out.println("阻塞前");
        LockSupport.park();
        System.out.println("阻塞后");
    }

    /**
     * 先释放许可，再获取许可，线程将不会被阻塞并正常终止
     */
    public static void parkAndUnparkTest1() {
        System.out.println("先调用unpark()为当前线程提供许可");
        LockSupport.unpark(Thread.currentThread());
        System.out.println("再调用park()为当前线程获取许可");
        LockSupport.park();
        System.out.println("当前线程获取到了许可，不阻塞当前线程");
    }

    /**
     * LockSupport是不可重入的，如果一个线程连续2次调用LockSupport.park()，那么该线程一定会一直阻塞下去。
     */
    public static void parkAndUnparkTest2() {
        System.out.println("1、调用unpark()为当前线程提供许可");
        LockSupport.unpark(Thread.currentThread());
        System.out.println("2、调用park()为当前线程获取许可");
        LockSupport.park();
        System.out.println("3、当前线程获取到了许可，不阻塞当前线程");
        LockSupport.park();
        System.out.println("4、前一次park()已经用掉了许可，再次连续park()肯定被阻塞");
    }

    /**
     * LockSupport是不可重入的，如果一个线程连续2次调用LockSupport.park()，那么该线程一定会一直阻塞下去。
     */
    public static void parkAndUnparkTest3() {
        final Thread mainThread = Thread.currentThread();
        System.out.println("1、调用unpark()为当前线程提供许可");
        LockSupport.unpark(mainThread);
        System.out.println("2、调用park()为当前线程获取许可");
        LockSupport.park();
        System.out.println("3、当前线程获取到了许可，不阻塞当前线程");

        Thread uparkHelper = new Thread(() -> {
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10)); //当前子线程休眠10秒钟
            LockSupport.unpark(mainThread); //休眠10秒过后帮主线程获取许可
        });
        uparkHelper.start();

        LockSupport.park();
        System.out.println("4、前一次park()已经用掉了许可，再次连续park()肯定被阻塞，除非有另一个线程为当前线程获取许可");
    }

    /**
     * 如果多次unpark只会获得一次许可，也就是说只能够一次park使用
     */
    public static void parkAndUnparkTest4() {
        Thread mainThread = Thread.currentThread();
        System.out.println("1、第一次调用unpark()为当前线程提供许可");
        LockSupport.unpark(mainThread);
        System.out.println("2、第二次调用unpark()为当前线程提供许可");
        LockSupport.unpark(mainThread);
        System.out.println("3、调用park()为当前线程获取许可");
        LockSupport.park();
        System.out.println("4、当前线程获取到了许可，不阻塞当前线程");
        LockSupport.park();
        System.out.println("5、前一次park()已经用掉了许可，再次连续park()肯定被阻塞");
    }

    /**
     * 使用线程的interrupt机制打断park()等待
     */
    public static void interruptParkTest() {
        final Thread mainThread = Thread.currentThread();
        System.out.println(String.format("【%s】调用park()为当前线程获取许可", mainThread.getName()));

        Thread parkInterrupter = new Thread(() -> {
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10)); //当前子线程休眠10秒钟
            mainThread.interrupt(); //休眠10秒过后帮主线程触发中断信号
            System.out.println(String.format("【%s】调用mainThread.interrupt()中断主线程", Thread.currentThread().getName()));
        });
        parkInterrupter.start();

        System.out.println(String.format("【%s】调用park()前，isInterrupted = %s", mainThread.getName(), mainThread.isInterrupted()));
        LockSupport.park();

        System.out.println(String.format("【%s】调用park()后，isInterrupted = %s", mainThread.getName(), mainThread.isInterrupted()));
        System.out.println(String.format("【%s】主线程运行完毕", mainThread.getName()));
    }

    public static void main(String[] args) {
        //parkTest();
        //parkAndUnparkTest1();
        //parkAndUnparkTest2();
        //parkAndUnparkTest3();
        //parkAndUnparkTest4();
        interruptParkTest();
    }

}

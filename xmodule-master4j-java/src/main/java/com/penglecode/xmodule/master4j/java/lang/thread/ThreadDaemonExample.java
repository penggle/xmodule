package com.penglecode.xmodule.master4j.java.lang.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * Thread.setDaemon(true/false)方法
 *
 * 1、默认情况下，即未显示调用thread.setDaemon(..)方法的情况下(即setDaemon(false))，该thread不是守护线程
 *    这就意味着，在主线程运行完毕之后，只有thread的run()方法运行完毕之后，整个JVM虚拟机才会关闭退出
 *    例如下面的defaultDaemon()示例
 *
 * 2、显示调用thread.setDaemon(false)，即thread不是守护线程，跟上面的默认情况一样
 *    例如下面的setDaemonFalse()示例
 *
 * 3、显示调用thread.setDaemon(true)，即thread是守护线程，此时一旦主线程运行结束，那么子线程就好立即强制结束退出
 *    例如下面的setDaemonTrue()示例
 *
 * 4、thread.setDaemon(..)必须在thread.start()方法之前被调用
 *
 * 5、在Daemon线程中产生的新线程也是Daemon的。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/16 22:34
 */
public class ThreadDaemonExample {

    public static void defaultDaemon() {
        Thread thread = new Thread(() -> {
            System.out.println("run start");
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
            System.out.println("run end");
        });
        //thread.setDaemon(true);
        thread.start();
    }

    public static void setDaemonFalse() {
        Thread thread = new Thread(() -> {
            System.out.println("run start");
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
            System.out.println("run end");
        });
        thread.setDaemon(false);
        thread.start();
    }

    public static void setDaemonTrue() {
        Thread thread = new Thread(() -> {
            System.out.println("run start");
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
            System.out.println("run end");
        });
        thread.setDaemon(true);
        thread.start();
    }

    public static void main(String[] args) {
        System.out.println("main start");
        //defaultDaemon();
        //setDaemonFalse();
        setDaemonTrue();
        System.out.println("main end");
    }

}

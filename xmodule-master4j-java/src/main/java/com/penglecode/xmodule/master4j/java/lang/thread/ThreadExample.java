package com.penglecode.xmodule.master4j.java.lang.thread;

import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * java.lang.Thread示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/26 13:42
 */
public class ThreadExample {

    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println(String.format("【%s】创建并运行线程的方式1：继承Thread类并重写run()方法", Thread.currentThread().getName()));
        }
    }

    /**
     * 严格意义上讲，创建线程的方式只有一种，那就是new Thread(..)，
     * 但是实现多线程的方式却有三种
     *
     * 创建线程的方式1：通过继承Thread类并重写run()方法
     */
    public static void implementThreadMode1() {
        new MyThread().start();
        System.out.println(String.format("【%s】主线程运行...", Thread.currentThread().getName()));
    }

    /**
     * 严格意义上讲，创建线程的方式只有一种，那就是new Thread(..)，
     * 但是实现多线程的方式却有三种
     *
     * 创建线程的方式2：实现Runnable接口重写run()方法
     */
    public static void implementThreadMode2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(String.format("【%s】创建线程的方式2：实现Runnable接口重写run()方法", Thread.currentThread().getName()));
            }
        }).start();
        System.out.println(String.format("【%s】主线程运行...", Thread.currentThread().getName()));
    }

    /**
     * 严格意义上讲，创建线程的方式只有一种，那就是new Thread(..)，
     * 但是实现多线程的方式却有三种
     *
     * 创建线程的方式3：实现Runnable接口重写run()方法
     */
    public static void implementThreadMode3() {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println(String.format("【%s】创建线程的方式3：实现Callable接口重写call()方法并配合线程池使用", Thread.currentThread().getName()));
                return null;
            }
        });
        System.out.println(String.format("【%s】主线程运行...", Thread.currentThread().getName()));
    }

    /**
     * Thread.sleep(..)线程休眠：即让正在运行的当前线程休眠一段时间，
     * 处于sleep状态的线程将会让出CPU资源，但是不会释放对象锁(monitor)，
     * 因为要保证sleep时间一到后立马能够运行就不能让出对象锁
     */
    public static void threadSleepTest() throws InterruptedException {
        System.out.println(String.format("【%s】主线程开始运行...", Thread.currentThread().getName()));
        System.out.println(String.format("【%s】主线程即将休眠10秒...", Thread.currentThread().getName()));
        Thread.sleep(10000);
        System.out.println(String.format("【%s】主线程休眠10秒结束...", Thread.currentThread().getName()));
        System.out.println(String.format("【%s】主线程结束运行...", Thread.currentThread().getName()));
    }

    /**
     * 从java.lang.Thread源码中可以看出：
     * 如果Thread构造器中没有显示指定ThreadGroup那么就使用java.lang.SecurityManager.threadGroup
     * 最后使用调用者(父线程)的threadGroup来兜底
     */
    public static void threadGroupTest() {
        SecurityManager securityManager = System.getSecurityManager();
        Thread mainThread = Thread.currentThread();

        System.out.println(String.format("【%s】 Thread.currentThread() = %s", mainThread.getName(), mainThread));
        System.out.println(String.format("【%s】 System.getSecurityManager().getThreadGroup() = %s", mainThread.getName(), securityManager == null ? null : securityManager.getThreadGroup()));
        System.out.println(String.format("【%s】 Thread.currentThread().getThreadGroup() = %s", mainThread.getName(), mainThread.getThreadGroup()));
        System.out.println(String.format("【%s】 Thread.currentThread().getContextClassLoader() = %s", mainThread.getName(), mainThread.getContextClassLoader()));
        Thread myThread = new Thread(() -> {
            Thread curThread = Thread.currentThread();
            System.out.println(String.format("【%s】 threadGroup = %s", curThread.getName(), curThread.getThreadGroup()));
            System.out.println(String.format("【%s】 Thread.currentThread().getContextClassLoader() = %s", curThread.getName(), curThread.getContextClassLoader()));
        });
        myThread.start();
    }

    /**
     * Thread的默认UncaughtExceptionHandler实现来自ThreadGroup实现的uncaughtException(..)方法
     */
    public static void uncaughtExceptionHandlerTest1() {
        Thread mainThread = Thread.currentThread();
        ThreadGroup mainThreadGroup = mainThread.getThreadGroup();
        Thread.UncaughtExceptionHandler mainThreadExceptionHandler = mainThread.getUncaughtExceptionHandler();
        System.out.println(String.format("【%s】 Thread.currentThread() = %s", mainThread.getName(), mainThread));
        System.out.println(String.format("【%s】 Thread.currentThread().getThreadGroup() = %s", mainThread.getName(), mainThreadGroup));
        System.out.println(String.format("【%s】 Thread.currentThread().getUncaughtExceptionHandler() = %s", mainThread.getName(), mainThreadExceptionHandler));
        System.out.println(String.format("【%s】 mainThreadGroup == mainThreadExceptionHandler ? %s", mainThread.getName(), mainThreadGroup == mainThreadExceptionHandler));
        Collections.emptyList().get(2); //触发异常，并观察默认的异常堆栈与java.lang.ThreadGroup.uncaughtException(..)中的最后默认异常堆栈输出是佛匹配
    }

    /**
     * 自定义UncaughtExceptionHandler
     */
    public static void uncaughtExceptionHandlerTest2() {
        Thread mainThread = Thread.currentThread();
        mainThread.setUncaughtExceptionHandler((t, e) -> {
            System.err.print("线程【" + t.getName() + "】运行过程中发生异常：");
            e.printStackTrace(System.err);
        });
        ThreadGroup mainThreadGroup = mainThread.getThreadGroup();
        Thread.UncaughtExceptionHandler mainThreadExceptionHandler = mainThread.getUncaughtExceptionHandler();
        System.out.println(String.format("【%s】 Thread.currentThread() = %s", mainThread.getName(), mainThread));
        System.out.println(String.format("【%s】 Thread.currentThread().getThreadGroup() = %s", mainThread.getName(), mainThreadGroup));
        System.out.println(String.format("【%s】 Thread.currentThread().getUncaughtExceptionHandler() = %s", mainThread.getName(), mainThreadExceptionHandler));
        System.out.println(String.format("【%s】 mainThreadGroup == mainThreadExceptionHandler ? %s", mainThread.getName(), mainThreadGroup == mainThreadExceptionHandler));
        Collections.emptyList().get(2);
    }

    public static void main(String[] args) throws Exception {
        //implementThreadMode1();
        //implementThreadMode2();
        //implementThreadMode3();
        threadSleepTest();
        //threadGroupTest();
        //uncaughtExceptionHandlerTest1();
        //uncaughtExceptionHandlerTest2();
    }

}

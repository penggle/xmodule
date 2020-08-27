package com.penglecode.xmodule.master4j.java.lang.thread;

/**
 * join()方法的使用
 *
 * Thread类有一个join方法，其作用是：在A线程中调用了另外一个线程对象B的join方法时，那么A线程必须等待B线程执行完才能继续往下执行。
 *
 * thread.join()方法必须在thread.start()方法之后调用
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/26 22:36
 */
public class ThreadJoinExample {

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(() -> {
            try {
                int sleeps = 5000;
                Thread.sleep(sleeps);
                System.out.println(String.format("【%s】子线程执行结束，耗费%s毫秒", Thread.currentThread().getName(), sleeps));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start(); //子线程启动
        thread.join(); //主线程等待子线程
        System.out.println(String.format("【%s】主线程等待子线程结束", Thread.currentThread().getName()));
    }

}

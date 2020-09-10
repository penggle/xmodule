package com.penglecode.xmodule.master4j.java.util.concurrent;

import java.util.concurrent.*;

/**
 * 线程池的典型实现ThreadPoolExecutor
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/6 21:03
 */
public class ThreadPoolExecutorExample {

    private static final int MAX_CONCURRENCY = 1000;

    private static final ThreadFactory DEFAULT_THREAD_FACTORY = Executors.defaultThreadFactory();

    public static ExecutorService newFixedThreadPool(int nThreads) {
        return newFixedThreadPool(nThreads, DEFAULT_THREAD_FACTORY);
    }

    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        /**
         * 新建一个线程池：
         * 1、使用固定的线程大小、且保持线程一直存活的
         * 2、使用固定容量的等待队列来存储积压的任务
         * 3、如果连等待队列都满了，则直接在当前提交任务的线程上执行
         */
        return new ThreadPoolExecutor(nThreads, nThreads, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(MAX_CONCURRENCY), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 测试corePoolSize及maximumPoolSize等参数的合法性
     */
    public static void newThreadPoolSizeTest() {
        ExecutorService executorService = new ThreadPoolExecutor(0, 1, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(MAX_CONCURRENCY), DEFAULT_THREAD_FACTORY, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static void prestartAllCoreThreads() throws InterruptedException {
        int nThreads = 4;
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(nThreads, nThreads, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(MAX_CONCURRENCY), DEFAULT_THREAD_FACTORY, new ThreadPoolExecutor.CallerRunsPolicy());
        executorService.prestartAllCoreThreads();

        Thread.sleep(120000);

        /**
         * 然后dump出线程堆栈：jstack -l 17784 > d:/threadpool-prestart.dump
         * 可以发现有4个线程在ThreadPoolExecutor#getTask()方法中的take()处WAITING
         */
    }

    public static void main(String[] args) throws Exception {
        //newThreadPoolSizeTest();
        prestartAllCoreThreads();
    }

}

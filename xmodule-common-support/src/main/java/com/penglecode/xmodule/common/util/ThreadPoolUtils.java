package com.penglecode.xmodule.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import org.springframework.util.Assert;

/**
 * 线程池工具类
 * 
 * @author 	pengpeng
 * @date 	2020年2月19日 下午5:06:56
 */
public class ThreadPoolUtils {

	/**
	 * 默认的并发能力
	 */
	public static final int DEFAULT_CONCURRENTCY_CAPACITY = 1000;
	
	/**
	 * 创建具有单个线程的线程池
	 * 注：该线程池设置了最大等待队列大小(50)，reject策略为#CallerRunsPolicy (即超过等待队列已满时将任务直接在调用者线程上执行)
	 * @return
	 */
	public static ExecutorService newSingleThreadExecutor() {
        return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(50), new CallerRunsPolicy());
    }
	
	/**
	 * 创建具有单个线程的线程池
	 * 注：该线程池设置了最大等待队列大小(50)，reject策略为#CallerRunsPolicy (即超过等待队列已满时将任务直接在调用者线程上执行)
	 * @return
	 */
	public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(50), threadFactory, new CallerRunsPolicy());
    }
	
	/**
	 * 创建具有固定数量的线程池
	 * 注：该线程池设置了最大等待队列大小(1000)，reject策略为#CallerRunsPolicy (即超过等待队列已满时将任务直接在调用者线程上执行)
	 * @param nThreads	- 线程池数量
	 * @return
	 */
	public static ExecutorService newFixedThreadPool(int nThreads) {
		Assert.isTrue(nThreads > 0, "Parameter 'nThreads' must be > 0");
		return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(DEFAULT_CONCURRENTCY_CAPACITY), new CallerRunsPolicy());
	}
	
	/**
	 * 创建具有固定数量的线程池
	 * 注：该线程池设置了最大等待队列大小(1000)，reject策略为#CallerRunsPolicy (即超过等待队列已满时将任务直接在调用者线程上执行)
	 * @param nThreads	- 线程池数量
	 * @return
	 */
	public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
		Assert.isTrue(nThreads > 0, "Parameter 'nThreads' must be > 0");
		return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(DEFAULT_CONCURRENTCY_CAPACITY), threadFactory, new CallerRunsPolicy());
	}
	
}

package com.penglecode.xmodule.java.examples.concurrent.util;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 使用信号量来来控制线程池的最大执行任务数，当提交的任务数大于阈值的时候，submit方法将会阻塞
 * 
 * @author 	pengpeng
 * @date	2017年11月6日 上午10:56:22
 */
public class SemaphoreExample2 {

	public static void main(String[] args) {
		Random random = new Random();
		int threadPoolSize = 4;
		ThreadPoolExecutor executor = new BlockingThreadPoolExecutor(threadPoolSize); //最多允许threadPoolSize个线程同时执行
		for(int i = 0; i < 10; i++) {
			Runnable worker = new Worker(i + 1, random.nextInt(60));
			executor.submit(worker); //如果向线程池中添加的任务数大于threadPoolSize则会导致submit等待
			System.out.println(String.format("[%s] >>> 成功向线程池中添加了一个任务：%s", Thread.currentThread().getName(), worker));
		}
		executor.shutdown();
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(60));
		System.out.println("主线程结束...");
	}
	
	public static class Worker implements Runnable {

		private final int id;
		
		private final int workload;
		
		public Worker(int id, int workload) {
			super();
			this.id = id;
			this.workload = workload;
		}

		public void run() {
			System.out.println(String.format("[%s] >>> %s开始工作...", Thread.currentThread().getName(), this));
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(workload));
			System.out.println(String.format("[%s] <<< %s完成工作...", Thread.currentThread().getName(), this));
		}

		public String toString() {
			return "Worker [id=" + id + ", workload =" + workload + "]";
		}
		
	}
	
	public static class BlockingThreadPoolExecutor extends ThreadPoolExecutor {

		private final Semaphore semaphore;
		
		public BlockingThreadPoolExecutor(int threadPoolSize) {
			super(threadPoolSize, threadPoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
			this.semaphore = new Semaphore(threadPoolSize);
		}
		
		public BlockingThreadPoolExecutor(int threadPoolSize, ThreadFactory threadFactory) {
			super(threadPoolSize, threadPoolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
			this.semaphore = new Semaphore(threadPoolSize);
		}
		
		/**
		 * 如果提交的任务大于threadPoolSize时,则submit方法阻塞
		 */
		public Future<?> submit(Runnable task) {
			acquire();
			return super.submit(task);
		}

		/**
		 * 如果提交的任务大于threadPoolSize时,则submit方法阻塞
		 */
		public <T> Future<T> submit(Runnable task, T result) {
			acquire();
			return super.submit(task, result);
		}

		/**
		 * 如果提交的任务大于threadPoolSize时,则submit方法阻塞
		 */
		public <T> Future<T> submit(Callable<T> task) {
			acquire();
			return super.submit(task);
		}
		
		protected void acquire() {
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		protected void afterExecute(Runnable r, Throwable t) {
			super.afterExecute(r, t);
			semaphore.release();
		}
		
	}
	
}

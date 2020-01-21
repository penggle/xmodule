package com.penglecode.xmodule.java.examples.concurrent.util;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * CountDownLatch基于AQS实现的一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
 * 
 * CountDownLatch原理：首先任务分为N个子线程去执行，state也初始化为N（注意N要与线程个数一致）。
 * 这N个子线程是并行执行的，每个子线程执行完后countDown()一次，state会CAS减1。
 * 等到所有子线程都执行完后(即state=0)，会unpark()主调用线程，然后主调用线程就会从await()函数返回，继续后余动作。
 * 
 * @author 	pengpeng
 * @date	2017年11月2日 上午10:36:27
 */
public class CountDownLatchExample {

	public static class Worker implements Runnable {
		
		private final CountDownLatch countDown;

		public Worker(CountDownLatch countDown) {
			super();
			this.countDown = countDown;
		}

		public void run() {
			int workTimes = new Random().nextInt(60);
			System.out.println(String.format("[%s] >>> 作业开始！ workTimes = %s", Thread.currentThread().getName(), workTimes));
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(workTimes));
			System.out.println(String.format("[%s] >>> 作业完成！ workTimes = %s", Thread.currentThread().getName(), workTimes));
			countDown.countDown();
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		int workers = 5;
		CountDownLatch countDown = new CountDownLatch(workers);
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
		for(int i = 0; i < workers; i++) {
			executorService.submit(new Worker(countDown));
		}
		executorService.shutdown();
		countDown.await();
		System.out.println(String.format("[%s] >>> 所有作业都完成了！", Thread.currentThread().getName()));
	}

}

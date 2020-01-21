package com.penglecode.xmodule.java.examples.concurrent.sync;

import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Java中的每一个对象都可以作为锁。
 * 对于同步的静态方法，锁是当前对象的Class对象。
 * 
 * @author  pengpeng
 * @date 	 2014年5月21日 下午1:09:07
 * @version 1.0
 */
public class SynchronizedStaticMethodExample {

	private static long value = 0;
	
	/**
	 * 静态方法级的同步(synchronized),所指定的锁就是当前类型Class(SynchronizedStaticMethodExample.class),也就等价于同步块：
	 * synchronized(SynchronizedStaticMethodExample.class){
	 * 		...
	 * }
	 * @param incr
	 */
	public static synchronized void increment(int incr) {
		value = value + incr;
	}
	
	public static long getValue() {
		return value;
	}
	
	/**
	 * 经测试平均耗时135毫秒左右
	 * @throws Exception
	 */
	public void test1() throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		int threads = 1000;
		int step = 1;
		CountDownLatch countDownLatch = new CountDownLatch(threads);
		Runnable command = new SynchronizedStaticMethodCommand(countDownLatch, step);
		ExecutorService executorService = Executors.newCachedThreadPool();
		for(int i = 0; i < threads; i++){
			executorService.execute(command);
		}
		executorService.shutdown();
		countDownLatch.await();
		long endTimeMillis = System.currentTimeMillis();
		System.out.println(MessageFormat.format("do concurrency increment from 0 to {0} by step {1}, cost {2} milliseconds", SynchronizedStaticMethodExample.getValue(), step, (endTimeMillis - beginTimeMillis)));
	}
	
	/**
	 * 经测试平均耗时260毫秒左右
	 * 此方法也就相当于test1方法中threads=2000时的执行效果
	 * @throws Exception
	 */
	public void test2() throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		int threads1 = 1000;
		int threads2 = 1000;
		int step = 1;
		CountDownLatch countDownLatch = new CountDownLatch(threads1 + threads2);
		Runnable command1 = new SynchronizedStaticMethodCommand(countDownLatch, step);
		Runnable command2 = new SynchronizedStaticMethodCommand(countDownLatch, step);
		ExecutorService executorService = Executors.newCachedThreadPool();
		for(int i = 0; i < threads1; i++){
			executorService.execute(command1);
		}
		for(int i = 0; i < threads2; i++){
			executorService.execute(command2);
		}
		executorService.shutdown();
		countDownLatch.await();
		long endTimeMillis = System.currentTimeMillis();
		//do concurrency increment from 0 to 20,000,000 by step 1, cost 262 milliseconds
		System.out.println(MessageFormat.format("do concurrency increment from 0 to {0} by step {1}, cost {2} milliseconds", SynchronizedStaticMethodExample.getValue(), step, (endTimeMillis - beginTimeMillis)));
	}
	
	/**
	 * 注意这里并不是在同一个对象上调用加锁的increment方法,经测试平均耗时260毫秒左右
	 * @throws Exception
	 */
	public void test3() throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		int threads1 = 1000;
		int threads2 = 1000;
		int step = 1;
		CountDownLatch countDownLatch = new CountDownLatch(threads1 + threads2);
		Runnable command1 = new SynchronizedStaticMethodCommand(countDownLatch, step);
		Runnable command2 = new SynchronizedStaticMethodCommand(countDownLatch, step);
		ExecutorService executorService = Executors.newCachedThreadPool();
		for(int i = 0; i < threads1; i++){
			executorService.execute(command1);
		}
		for(int i = 0; i < threads2; i++){
			executorService.execute(command2);
		}
		executorService.shutdown();
		countDownLatch.await();
		long endTimeMillis = System.currentTimeMillis();
		//do concurrency increment from 0 to 2,000,000 by step 1, cost 268 milliseconds
		System.out.println(MessageFormat.format("do concurrency increment from 0 to {0} by step {1}, cost {2} milliseconds", SynchronizedStaticMethodExample.getValue(), step, (endTimeMillis - beginTimeMillis)));
	}
	
	/**
	 * 对比test2和test3，主要想说明：静态方法级的锁是加在当前类型Class上的，因此test2的平均耗时和test3基本一样
	 * 对象级的锁和class级的锁的粒度不一样,class级的锁使用起来锁的范围最大
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SynchronizedStaticMethodExample example = new SynchronizedStaticMethodExample();
		//example.test1();
		//example.test2();
		example.test3();
	}

}

class SynchronizedStaticMethodCommand implements Runnable {
	
	private final CountDownLatch countDownLatch;
	
	private int step = 1;
	
	public SynchronizedStaticMethodCommand(CountDownLatch countDownLatch, int step) {
		this(countDownLatch);
		this.step = step;
	}
	
	public SynchronizedStaticMethodCommand(CountDownLatch countDownLatch) {
		super();
		this.countDownLatch = countDownLatch;
	}

	public void run() {
		try {
			for (int i = 0; i < 10000; i++) {
				SynchronizedStaticMethodExample.increment(step);
			}
		} finally {
			countDownLatch.countDown();
		}
	}
	
}
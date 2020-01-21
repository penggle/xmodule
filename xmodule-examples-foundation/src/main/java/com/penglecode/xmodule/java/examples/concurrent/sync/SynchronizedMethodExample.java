package com.penglecode.xmodule.java.examples.concurrent.sync;

import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Java中的每一个对象都可以作为锁。
 * 对于同步的普通方法，锁是当前实例对象。
 * 
 * @author  pengpeng
 * @date 	 2014年5月21日 下午1:07:28
 * @version 1.0
 */
public class SynchronizedMethodExample {

	private long value = 0;
	
	/**
	 * 普通方法级的同步(synchronized),所指定的锁就是当前对象this,也就等价于同步块：
	 * synchronized(this){
	 * 		...
	 * }
	 * @param incr
	 */
	public synchronized void increment(int incr) {
		value = value + incr;
	}
	
	public long getValue() {
		return value;
	}
	
	public void test1() throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		int threads = 100;
		int step = 1;
		CountDownLatch countDownLatch = new CountDownLatch(threads);
		Runnable command = new SynchronizedMethodCommand(this, countDownLatch, step);
		ExecutorService executorService = Executors.newCachedThreadPool();
		for(int i = 0; i < threads; i++){
			executorService.execute(command);
		}
		executorService.shutdown();
		countDownLatch.await();
		long endTimeMillis = System.currentTimeMillis();
		System.out.println(MessageFormat.format("do concurrency increment from 0 to {0} by step {1}, cost {2} milliseconds", this.getValue(), step, (endTimeMillis - beginTimeMillis)));
	}
	
	/**
	 * 注意这里是在同一个对象(this)上调用加锁的increment方法,经测试平均耗时105毫秒左右
	 * 此方法也就相当于test1方法中threads=200时的执行效果
	 * @throws Exception
	 */
	public void test2() throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		int threads1 = 100;
		int threads2 = 100;
		int step = 1;
		CountDownLatch countDownLatch = new CountDownLatch(threads1 + threads2);
		Runnable command1 = new SynchronizedMethodCommand(this, countDownLatch, step);
		Runnable command2 = new SynchronizedMethodCommand(this, countDownLatch, step);
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
		//do concurrency increment from 0 to 2,000,000 by step 1, cost 106 milliseconds
		System.out.println(MessageFormat.format("do concurrency increment from 0 to {0} by step {1}, cost {2} milliseconds", this.getValue(), step, (endTimeMillis - beginTimeMillis)));
	}
	
	/**
	 * 注意这里并不是在同一个对象上调用加锁的increment方法,经测试平均耗时78毫秒左右
	 * @throws Exception
	 */
	public void test3() throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		int threads1 = 100;
		int threads2 = 100;
		int step = 1;
		CountDownLatch countDownLatch = new CountDownLatch(threads1 + threads2);
		SynchronizedMethodExample example1 = new SynchronizedMethodExample();
		SynchronizedMethodExample example2 = new SynchronizedMethodExample();
		Runnable command1 = new SynchronizedMethodCommand(example1, countDownLatch, step);
		Runnable command2 = new SynchronizedMethodCommand(example2, countDownLatch, step);
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
		//do concurrency increment from 0 to 2,000,000 by step 1, cost 78 milliseconds
		System.out.println(MessageFormat.format("do concurrency increment from 0 to {0} by step {1}, cost {2} milliseconds", example1.getValue() + example2.getValue(), step, (endTimeMillis - beginTimeMillis)));
	}
	
	/**
	 * 对比test2和test3，主要想说明：普通方法(非静态方法)级的锁是加在当前对象(this)上的，因此test2的平均耗时要比test3多一些
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SynchronizedMethodExample example = new SynchronizedMethodExample();
		example.test1();
		//example.test2();
		//example.test3();
	}

}

class SynchronizedMethodCommand implements Runnable {
	
	private final SynchronizedMethodExample example;
	
	private final CountDownLatch countDownLatch;
	
	private int step = 1;
	
	public SynchronizedMethodCommand(SynchronizedMethodExample example, CountDownLatch countDownLatch, int step) {
		this(example, countDownLatch);
		this.step = step;
	}
	
	public SynchronizedMethodCommand(SynchronizedMethodExample example, CountDownLatch countDownLatch) {
		super();
		this.example = example;
		this.countDownLatch = countDownLatch;
	}

	public void run() {
		try {
			for (int i = 0; i < 10000; i++) {
				example.increment(step);
			}
		} finally {
			countDownLatch.countDown();
		}
	}
	
}
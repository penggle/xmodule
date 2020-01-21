package com.penglecode.xmodule.java.examples.concurrent.sync;

import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Java中的每一个对象都可以作为锁。
 * 对于同步方法块，锁是Synchonized括号里配置的对象。
 * 
 * @author  pengpeng
 * @date 	 2014年5月21日 下午1:09:52
 * @version 1.0
 */
public class SynchronizedBlockExample {

	private long value = 0;
	
	/**
	 * 同步块
	 * synchronized(mutex){
	 * 		...
	 * }
	 * 关于mutex的选择要慎重：
	 * 当mutex为：XXX.class、静态域等全局元素时,锁住的范围最大,因此并发效率最差
	 * 当mutex为：当前对象this、普通成员域等局部元素时,锁住的范围小,因此并发效率较好
	 * 当mutex为：诸如new Object()等变量时,就相当于没加锁的效果
	 * @param incr
	 */
	public void increment(int incr) {
		synchronized(this){
			value = value + incr;
		}
		/*synchronized(SynchronizedBlockExample.class){
			value = value + incr;
		}*/
	}
	
	public long getValue() {
		return value;
	}
	
	public void test1() throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		int threads = 100;
		int step = 1;
		CountDownLatch countDownLatch = new CountDownLatch(threads);
		Runnable command = new SynchronizedBlockCommand(this, countDownLatch, step);
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
		Runnable command1 = new SynchronizedBlockCommand(this, countDownLatch, step);
		Runnable command2 = new SynchronizedBlockCommand(this, countDownLatch, step);
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
	 * @throws Exception
	 */
	public void test3() throws Exception {
		long beginTimeMillis = System.currentTimeMillis();
		int threads1 = 100;
		int threads2 = 100;
		int step = 1;
		CountDownLatch countDownLatch = new CountDownLatch(threads1 + threads2);
		SynchronizedBlockExample example1 = new SynchronizedBlockExample();
		SynchronizedBlockExample example2 = new SynchronizedBlockExample();
		Runnable command1 = new SynchronizedBlockCommand(example1, countDownLatch, step);
		Runnable command2 = new SynchronizedBlockCommand(example2, countDownLatch, step);
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
		//do concurrency increment from 0 to 2,000,000 by step 1, cost 60 milliseconds
		System.out.println(MessageFormat.format("do concurrency increment from 0 to {0} by step {1}, cost {2} milliseconds", example1.getValue() + example2.getValue(), step, (endTimeMillis - beginTimeMillis)));
	}
	
	/**
	 * synchronized(mutex){
	 * 		value = value + incr;
	 * }
	 * 
	 * 通过mutex为this或是SynchronizedBlockExample.class
	 * 来对比test2和test3，主要想说明：锁(mutex)的选择非常重要,当选择为当前类型Class时锁住的范围最大因此效率最低
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		SynchronizedBlockExample example = new SynchronizedBlockExample();
		//example.test1();
		//example.test2();
		example.test3();
	}

}

class SynchronizedBlockCommand implements Runnable {
	
	private final SynchronizedBlockExample example;
	
	private final CountDownLatch countDownLatch;
	
	private int step = 1;
	
	public SynchronizedBlockCommand(SynchronizedBlockExample example, CountDownLatch countDownLatch, int step) {
		this(example, countDownLatch);
		this.step = step;
	}
	
	public SynchronizedBlockCommand(SynchronizedBlockExample example, CountDownLatch countDownLatch) {
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
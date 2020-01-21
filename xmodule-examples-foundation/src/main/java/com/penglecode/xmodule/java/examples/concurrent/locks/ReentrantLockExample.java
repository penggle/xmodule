package com.penglecode.xmodule.java.examples.concurrent.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * ReentrantLock - 基于AQS实现的可重入锁,它拥有和synchronized关键字基本行为和语义,而且功能更为强大,而且比synchronized有着更好的性能
 * 该类的构造器有一个参数fair,如果为true,则内部采用的是公平锁,当某个线程等待时间较长时,下次获得锁的机会越大
 * 默认使用不公平锁,使用公平锁并发性能差一些(吞吐量低)
 * 
 * ReentrantLock工作原理：state初始化为0，表示未锁定状态。A线程lock()时，会调用tryAcquire()独占该锁并将state+1。
 * 此后，其他线程再tryAcquire()时就会失败，直到A线程unlock()到state=0（即释放锁）为止，其它线程才有机会获取该锁。
 * 当然，释放锁之前，A线程自己是可以重复调用lock获取此锁的（state会累加），这就是可重入的概念。
 * 但要注意，获取多少次锁(调用多少次lock)就要调用释放多么次锁(调用多少次unlock)，这样才能保证state是能回到零态的。
 * 
 * 
 * @author 	pengpeng
 * @date	2017年11月1日 上午10:14:54
 */
public class ReentrantLockExample {

	public static void main(String[] args) {
		Counter counter = new Counter();
		CounterTester tester = new CounterTester(counter);
		for(int i = 0; i < 10; i++){
			new Thread(tester).start();
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(String.format("[%s] >>> count = %s", Thread.currentThread().getName(), counter.getCount()));
	}

	public static class Counter {
		
		private final Lock lock = new ReentrantLock();
		
		private long count = 0;
		
		public long getCount(){
			return count;
		}
		
		public void increment(){
			lock.lock();
			try {
				count++;
				System.out.println(String.format("[%s] >>> count = %s", Thread.currentThread().getName(), count));
			} finally {
				lock.unlock();
			}
		}
		
	}
	
	public static class CounterTester implements Runnable {

		private final Counter counter;
		
		public CounterTester(Counter counter) {
			super();
			this.counter = counter;
		}

		public void run() {
			for(int i = 0; i < 10; i++){
				counter.increment();
			}
		}
		
	}
	
}

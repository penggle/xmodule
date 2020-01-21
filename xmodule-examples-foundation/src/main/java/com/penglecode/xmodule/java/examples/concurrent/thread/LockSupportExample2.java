package com.penglecode.xmodule.java.examples.concurrent.thread;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport很类似于二元信号量(只有1个许可证可供使用)
 * 
 * @author 	pengpeng
 * @date	2017年10月31日 下午6:00:12
 */
public class LockSupportExample2 {

	/**
	 * 调用LockSupport.park()将导致当前线程等待
	 */
	public static void test1() {
		LockSupport.park();
	    System.out.println("block.");
	}
	
	/**
	 * 调用LockSupport.unpark(thread)：如果给定线程的许可尚不可用，则使其可用。如果线程在 park 上受阻塞，则它将解除其阻塞状态。否则，保证下一次调用 park 不会受阻塞。如果给定线程尚未启动，则无法保证此操作有任何效果。
	 */
	public static void test2() {
		Thread thread = Thread.currentThread();
	    LockSupport.unpark(thread); //预先释放许可
	    LockSupport.park(); // 获取许可
	    System.out.println("a");
	}
	
	/**
	 * 调用LockSupport.unpark(thread)：如果给定线程的许可尚不可用，则使其可用。如果线程在 park 上受阻塞，则它将解除其阻塞状态。否则，保证下一次调用 park 不会受阻塞。如果给定线程尚未启动，则无法保证此操作有任何效果。
	 * 
	 * 此例说明unpark调用超过1次以上的都是多余的 （因为LockSupport很类似于二元信号量：只有1个许可证可供使用）
	 */
	public static void test3() {
		Thread thread = Thread.currentThread();
	    LockSupport.unpark(thread); //预先释放许可 (许可置为可用状态)
	    LockSupport.unpark(thread); //预先释放许可 (多余的调用,因为只有1个许可证可供使用)
	    LockSupport.park(); // 获取许可
	    System.out.println("a");
	    LockSupport.park(); // 获取许可
	    System.out.println("b");
	}
	
	/**
	 * LockSupport是不可重入的，如果一个线程连续2次调用LockSupport.park()，那么该线程 【一定】 会一直阻塞下去。
	 */
	public static void test4() {
		Thread thread = Thread.currentThread();
	    LockSupport.unpark(thread); //预先释放许可
	    LockSupport.park(); // 获取许可
	    System.out.println("a");
	    LockSupport.park(); // 获取许可
	    System.out.println("b"); //此处不会打印出b，因为第二次调用park的时候，线程无法获取许可出现等待。
	}
	
	public static void main(String[] args) {
		//test1();
		//test2();
		//test3();
		test4();
	}

}

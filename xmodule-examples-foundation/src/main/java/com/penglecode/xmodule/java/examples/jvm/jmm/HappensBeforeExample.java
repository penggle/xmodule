package com.penglecode.xmodule.java.examples.jvm.jmm;

/**
 * 新的内存模型语义在内存操作（读取字段，写入字段，锁，解锁）以及其他线程的操作（start 和 join）中创建了一个部分排序，在这些操作中，一些操作被称为happen before其他操作。当一个操作在另外一个操作之前发生，第一个操作保证能够排到前面并且对第二个操作可见。这些排序的规则如下：
 *
 * 1、线程中的每个操作happens before该线程中在程序顺序上后续的每个操作。
 * 2、解锁一个监视器的操作happens before随后对相同监视器进行锁的操作。
 * 3、对volatile字段的写操作happens before后续对相同volatile字段的读取操作。
 * 4、线程上调用start()方法happens before这个线程启动后的任何操作。
 * 5、一个线程中所有的操作都happens before从这个线程join()方法成功返回的任何其他线程。（注意思是其他线程等待一个线程的jion()方法完成，那么，这个线程中的所有操作happens before其他线程中的所有操作）
 * 
 * @author 	pengpeng
 * @date	2017年10月27日 下午3:24:14
 */
public class HappensBeforeExample {

	private static final Object mutex = new Object();
	
	/**
	 * 1、线程中的每个操作happens before该线程中在程序顺序上后续的每个操作。
	 * 这句话的意思就是当前线程进入如下方法内：实际执行与语句顺序是一致的
	 */
	public void happensBeforeInThread() {
		System.out.println("step-1");
		System.out.println("step-2");
		System.out.println("step-3");
		System.out.println("step-4");
		System.out.println("step-5");
	}
	
	/**
	 * 2、解锁一个监视器的操作happens before随后对相同监视器进行锁的操作。
	 * 这句话的意思就是线程1解锁离开同步块之后线程2才能获锁进入同步块中
	 */
	public void happensBeforeInSync() {
		System.out.println("线程：" + Thread.currentThread().getName() + " 等待进入同步块中...");
		synchronized(mutex) {
			happensBeforeInThread();
		}
		System.out.println("线程：" + Thread.currentThread().getName() + " 从同步块中出来了...");
	}
	
}

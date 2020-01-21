package com.penglecode.xmodule.java.examples.concurrent.threadpool;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 线程池类ThreadPoolExecutor的常用构造方法说明
 * ThreadPoolExecutor(
 * 		int corePoolSize, 					//线程池中线程的最少数量
 * 		int maximumPoolSize,				//线程池中线程的最大数量
 * 		long keepAliveTime, 				//当线程池中的线程数量大于corePoolSize时,如果某线程空闲时间超过keepAliveTime,线程将被终止
 * 		TimeUnit unit,						//keepAliveTime的单位
 * 		BlockingQueue<Runnable> workQueue,	//阻塞队列,等待被线程池中线程处理的新任务的存储队列
 * 		RejectedExecutionHandler handler	//核心线程corePoolSize、任务队列workQueue、最大线程maximumPoolSize,如果三者都满了,使用handler处理被拒绝的任务
 * )
 * 
 * 排队说明：
 * 
 * 所有 BlockingQueue 都可用于传输和保持提交的任务。可以使用此队列与池大小进行交互：
 * A. 如果运行的线程少于 corePoolSize，则 Executor 始终首选添加新的线程，而不进行排队。
 * B. 如果运行的线程等于或多于 corePoolSize，则 Executor 始终首选将请求加入队列，而不添加新的线程。
 * C. 如果无法将请求加入队列，则创建新的线程，除非创建此线程超出 maximumPoolSize，在这种情况下，任务将被拒绝。
 * 
 * 另外Executors工厂类中提供了一些列关于线程池的工厂方法
 * 
 * @author 	pengpeng
 * @date	2017年11月6日 下午1:18:33
 */
public class ThreadPoolExecutorExample {

	/**
	 * 使用默认的拒绝策略+LinkedBlockingQueue
	 */
	public static void testExecuteByLinkedBlockingQueue() {
		int nThreads = 4;
		ThreadPoolExecutor executor = new ThreadPoolExecutor(nThreads, nThreads, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		for(int i = 0; i < 100; i++){
			executor.execute(new Task("任务-" + (i + 1)));
			System.out.println(executor.getQueue().size());
		}
		executor.shutdown();
	}
	
	/**
	 * 使用默认的拒绝策略+ArrayBlockingQueue，注意此时如果添加的任务数大于ArrayBlockingQueue队列的大小则会触发拒绝策略
	 */
	public static void testExecuteByArrayBlockingQueue() {
		int nThreads = 4;
		ThreadPoolExecutor executor = new ThreadPoolExecutor(nThreads, nThreads, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(50));
		for(int i = 0; i < 100; i++){
			executor.execute(new Task("任务-" + (i + 1)));
			System.out.println(executor.getQueue().size());
		}
		executor.shutdown();
	}
	
	/**
	 * 1. 对于submit(task)的任务，框架会将异常保持在future里，并包装在ExecutionException里，当调用Future.get()时，再次throw，
	 *    这时可以调用ExecutionException.getCause()获取包装的exception，这种情况下，设置UncaughtExceptionHandler也不会被调用，
	 *    这种情况下只能通过继承ThreadPoolExecutor重写afterExecute方法来处理异常信息。
	 *    
     * 2. 对于execute(task)的任务，会直接throw，可以通过ThreadFactory在new Thread的时候设置一个UncaughtExceptionHandler来处理
	 */
	public static void testExceptionHandle() {
		int nThreads = 4;
		ThreadPoolExecutor executor = new ThreadPoolExecutor(nThreads, nThreads, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		for(int i = 0; i < 10; i++){
			//executor.execute(new UnstableTask("任务-" + (i + 1)));
			executor.submit(new UnstableTask("任务-" + (i + 1)));
		}
		executor.shutdown();
	}
	
	public static void main(String[] args) {
		//testExecuteByLinkedBlockingQueue();
		//testExecuteByArrayBlockingQueue();
		testExceptionHandle();
	}

	public static class Task implements Runnable {

		private final String name;
		
		public Task(String name) {
			super();
			this.name = name;
		}

		public void run() {
			for(int i = 0; i < 6; i++){
				Date date = new Date();
				System.out.println(String.format("[%s][%s] >>> nowTime = %tY %tT", Thread.currentThread().getName(), getName(), date, date));
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));
			}
		}
		
		public String getName() {
			return name;
		}
		
	}
	
	public static class UnstableTask implements Runnable {

		private final String name;
		
		public UnstableTask(String name) {
			super();
			this.name = name;
		}
		
		public void run() {
			Random r = new Random();
			int n = r.nextInt(10);
			System.out.println(String.format("[%s][%s] >>> random = %s", Thread.currentThread().getName(), getName(), n));
			if(n % 3 == 0) {
				throw new IllegalStateException("3333333333333333333");
			}
			LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(n));
		}
		
		public String getName() {
			return name;
		}
		
	}
	
}

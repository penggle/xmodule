package com.penglecode.xmodule.java.examples.concurrent.thread;

/**
 * 在某些情况下，我们需要等待线程的终结。例如，我们可能会遇到程序在执行前需要初始化资源。在执行剩下的代码之前，我们需要等待线程完成初始化任务。
 *
 * 为达此目的, 我们使用Thread 类的join() 方法。当前线程调用某个线程的这个方法时，它会暂停当前线程，直到被调用线程执行完成。
 * 
 * @author 	pengpeng
 * @date	2017年10月31日 下午2:46:43
 */
public class ThreadWaitByJoinExample {

	public static class MyAppBootstrap implements Runnable {

		public void run() {
			for(int i = 10; i > 0; i--) {
				System.out.println(String.format("[%s] >>> MyApp启动倒计时 : %s", Thread.currentThread().getName(), i));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(String.format("[%s] >>> 主线程启动了", Thread.currentThread().getName()));
		Thread bootstrap = new Thread(new MyAppBootstrap());
		bootstrap.start();
		System.out.println(String.format("[%s] >>> 主线程即将等待子线程", Thread.currentThread().getName()));
		bootstrap.join(); //当前线程(主线程)调用bootstrap的join方法，导致当前线程暂停，等待bootstrap线程执行完成后继续执行
		System.out.println(String.format("[%s] >>> 主线程等待子线程结束", Thread.currentThread().getName()));
		System.out.println(String.format("[%s] >>> 主线程结束了", Thread.currentThread().getName()));
	}

}

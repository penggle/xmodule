package com.penglecode.xmodule.java.examples.concurrent.thread;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 
 * 在一个线程 对象的 run() 方法里抛出一个检查异常，我们必须捕获并处理他们。因为 run() 方法不接受 throws 子句。当一个非检查异常被抛出，默认的行为是在控制台写下stack trace并退出程序。
 * 幸运的是, Java 提供我们一种机制可以捕获和处理线程对象抛出的未检测异常来避免程序终结。
 * 
 * @author 	pengpeng
 * @date	2017年10月31日 下午3:26:58
 */
public class UncaughtExceptionHandlerExample {

	public static class Task implements Runnable {

		public void run() {
			Integer.parseInt("T_T");
		}
		
	}
	
	public static void main(String[] args) {
		Thread thread = new Thread(new Task());
		thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void uncaughtException(Thread t, Throwable e) {
				System.err.println(String.format("The thread[%s] throw an uncaught exception : %s", t.getName(), e.getMessage()));
				System.err.println(String.format("The thread[%s] state is : %s", t.getName(), t.getState()));
			}
		});
		thread.start();
	}

}

package com.penglecode.xmodule.java.examples.concurrent.thread;

import java.util.Date;

/**
 * 线程中断的例子
 * 
 * Thread 类还有一个boolean类型的属性来表明线程是否被中断。
 * 当你调用线程的interrupt() 方法，就代表你把这个属性设置为 true。
 * 而isInterrupted() 方法仅返回属性值。
 * 
 * @author 	pengpeng
 * @date	2017年10月31日 上午9:45:21
 */
public class ThreadInterruptExample2 extends Thread {

	public void run() {
		try {
			while(true) {
				Date now = new Date();
				System.out.println(String.format("%tF %tT", now, now));
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			System.err.println("The current thread has been interrupted!");
		}
	}
	
	public static void main(String[] args) {
		Thread task = new ThreadInterruptExample2();
		task.start();
		try {
			Thread.sleep(10500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/**
		 * 调用线程的interrupt()方法,如果该线程当前正在如下操作之一都将会抛出InterruptedException：
		 * 等待IO读写、sleep中、wait中 等
		 */
		task.interrupt();
	}
	
}

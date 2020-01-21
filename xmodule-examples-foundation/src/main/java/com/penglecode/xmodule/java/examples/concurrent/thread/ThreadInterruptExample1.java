package com.penglecode.xmodule.java.examples.concurrent.thread;

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
public class ThreadInterruptExample1 extends Thread {

	public void run() {
		long number = 1;
		while(!isInterrupted()) {
			if(isPrime(number)) {
				System.out.println(String.format("Number %d is Prime", number));
			}
			number++;
		}
		System.out.println("The current thread has been Interrupted");
	}
	
	protected boolean isPrime(long number) {
		if (number <= 2) {
			return true;
		}
		for (long i = 2; i < number; i++) {
			if ((number % i) == 0) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		Thread task = new ThreadInterruptExample1();
		task.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		task.interrupt();
	}
	
}

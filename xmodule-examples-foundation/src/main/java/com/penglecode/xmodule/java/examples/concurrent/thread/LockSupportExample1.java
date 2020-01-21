package com.penglecode.xmodule.java.examples.concurrent.thread;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class LockSupportExample1 {

	public static class Timer implements Runnable {

		public void run() {
			while(!Thread.currentThread().isInterrupted()) {
				Date now = new Date();
				System.out.println(String.format("%tF %tT", now, now));
				LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
			}
		}
		
	}
	
	public static void main(String[] args) {
		Thread thread = new Thread(new Timer());
		thread.start();
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(15));
		thread.interrupt();
	}

}

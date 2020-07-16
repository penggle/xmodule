package com.penglecode.xmodule.master4j.jvm.chapter4.jstack;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * jstack工具的应用示例
 * 
 * @author 	pengpeng
 * @date 	2020年7月8日 下午5:08:20
 */
@Component
public class JstackAgainstUsageExample {

	private final Object monitor1 = new Object();
	
	private final Object monitor2 = new Object();
	
	protected void tryLock1() {
		synchronized (monitor1) {
			/*
			 * 暂停一下，以确保tryLock1()中获取到锁monitor1的同时tryLock2()中获取到锁monitor2
			 * 这样在一次运行中就能产生出个死锁示例
			 */
			LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(200));
			synchronized (monitor2) {
				System.out.println("do something after acquired lock : monitor1 and monitor2");
			}
		}
	}
	
	protected void tryLock2() {
		synchronized (monitor2) {
			/*
			 * 暂停一下，以确保tryLock2()中获取到锁monitor2的同时tryLock1()中获取到锁monitor1
			 * 这样在一次运行中就能产生出个死锁示例
			 */
			LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(200));
			synchronized (monitor1) {
				System.out.println("do something after acquired lock : monitor2 and monitor1");
			}
		}
	}
	
	/**
	 * 触发一个死锁
	 */
	public void tryDeadLock() {
		System.out.println("try deadlock for jstack usage...");
		new Thread(this::tryLock1).start();
		new Thread(this::tryLock2).start();
	}
	
	/**
	 * 触发一个死循环
	 */
	@Async
	public void tryEndlessLoop() {
		System.out.println("try endlessloop for jstack usage...");
		long lastTimeMillis = 0;
		do {
			lastTimeMillis = System.currentTimeMillis();
		} while (lastTimeMillis <= System.currentTimeMillis());
		System.err.println("clock is moving backwards...");
	}
	
}

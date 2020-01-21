package com.penglecode.xmodule.java.examples.concurrent.locks;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLock读写锁,在没有线程获取写锁的情况下允许多个线程同时读,而当有线程获取了写锁时,多个读锁需要阻塞等待.
 * 
 * 这个类提供两把锁，一把用于读操作和一把用于写操作。同时可以有多个线程执行读操作，但只有一个线程可以执行写操作。当一个线程正在执行一个写操作，不可能有任何线程执行读操作。
 * 
 * @author 	pengpeng
 * @date	2017年11月1日 上午10:14:33
 */
public class ReadWriteLockExample {

	public static void main(String[] args) {
		Cache<Object> cache = new Cache<Object>();
		CacheWriter writer = new CacheWriter(cache);
		CacheReader reader = new CacheReader(cache);
		ThreadGroup threadGroup = new ThreadGroup("Read-Write-Threads");
		new Thread(threadGroup, reader, "Cache-Reader-1").start();
		new Thread(threadGroup, reader, "Cache-Reader-2").start();
		new Thread(threadGroup, writer, "Cache-Writer-1").start();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadGroup.interrupt();
		System.out.println(String.format("[%s] >>> get %s = %s", Thread.currentThread().getName(), "nowTime", cache.get("nowTime")));
	}
	
	public static class CacheWriter implements Runnable {

		private final Cache<Object> cache;
		
		public CacheWriter(Cache<Object> cache) {
			super();
			this.cache = cache;
		}

		public void run() {
			while(!Thread.currentThread().isInterrupted()){
				Date date = new Date();
				String key = "nowTime";
				String value = String.format("%tF %tT", date, date);
				cache.set(key, value);
				System.out.println(String.format("[%s] >>> set %s = %s", Thread.currentThread().getName(), key, value));
				LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100)); //写的频度小一点
			}
		}
		
	}
	
	public static class CacheReader implements Runnable {

		private final Cache<Object> cache;
		
		public CacheReader(Cache<Object> cache) {
			super();
			this.cache = cache;
		}

		public void run() {
			while(!Thread.currentThread().isInterrupted()){
				String key = "nowTime";
				String value = (String)cache.get(key);
				System.out.println(String.format("[%s] >>> get %s = %s", Thread.currentThread().getName(), key, value));
				LockSupport.parkNanos(100L); //读的频度大一点
			}
		}
		
	}

	public static class Cache<T> {
		
		private final ReadWriteLock lock = new ReentrantReadWriteLock();
		
		private final Map<String,T> cacheMap = new HashMap<String,T>();
		
		public T set(String key, T value){
			lock.writeLock().lock();
			try {
				T oldValue = cacheMap.put(key, value);
				return oldValue;
			} finally {
				lock.writeLock().unlock();
			}
		}
		
		public T remove(String key){
			lock.writeLock().lock();
			try {
				return cacheMap.remove(key);
			} finally {
				lock.writeLock().unlock();
			}
		}
		
		public T get(String key){
			lock.readLock().lock();
			try {
				T value = cacheMap.get(key);
				return value;
			} finally {
				lock.readLock().unlock();
			}
		}
		
	}
	
}

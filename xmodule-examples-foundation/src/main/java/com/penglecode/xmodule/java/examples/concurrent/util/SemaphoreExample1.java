package com.penglecode.xmodule.java.examples.concurrent.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 使用信号量来实现生产者消费者问题
 * 
 * 信号量semaphore计数器的值大于0表示有可以使用的资源,
 * 相反,信号量semaphore计数器的值等于0表示没有可以使用的资源,当前线程在调用acquire()时将会阻塞.
 * semaphore.acquire(),申请一个可用资源,semaphore计数器的值减一
 * semaphore.release(),释放一个可用资源,semaphore计数器的值加一
 * 
 * @author 	pengpeng
 * @date	2017年11月6日 上午10:39:59
 */
public class SemaphoreExample1 {

	public static void main(String[] args) {
		ProductBuffer buffer = new ProductBuffer(50);
		Producer producer1 = new Producer(buffer, 500);
		Producer producer2 = new Producer(buffer, 500);
		Consumer consumer1 = new Consumer(buffer, 750);
		Consumer consumer2 = new Consumer(buffer, 250);
		
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);
		executorService.execute(producer1);
		executorService.execute(producer2);
		executorService.execute(consumer1);
		executorService.execute(consumer2);
		executorService.shutdown();
	}
	
	public static class Product {
		
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String toString() {
			return "Product [name=" + name + "]";
		}
		
	}
	
	public static class ProductBuffer {
		
		private final Semaphore putSemaphore;
		
		private final Semaphore takeSemaphore;
		
		private final Product[] buffer;
		
		private int currentSize = 0;
		
		private int serialNo = 0;
		
		public ProductBuffer(int capacity) {
			this.buffer = new Product[capacity];
			this.putSemaphore = new Semaphore(capacity);
			this.takeSemaphore = new Semaphore(0);
		}
		
		public void put(Product product) {
			try {
				putSemaphore.acquire(); //如果buffer中可放入的Product数量为0则acquire()方法会导致等待
				synchronized (buffer) {
					product.setName("Product-" + (++serialNo));
					buffer[currentSize++] = product;
					System.out.println(String.format(">>>[%s] 生产者生产一个商品: %s", Thread.currentThread().getName(), product));
				}
				takeSemaphore.release(); //可取出的Product数量+1
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		
		public Product take() {
			Product product = null;
			try {
				takeSemaphore.acquire(); //如果buffer中可取出的Product数量为0则acquire()方法会导致等待
				synchronized(buffer){
					product = buffer[--currentSize];
					System.out.println(String.format(">>>[%s] 消费者消费一个商品: %s", Thread.currentThread().getName(), product));
				}
				putSemaphore.release(); //可放入的Product数量+1
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			return product;
		}
		
	}
	
	public static class Producer implements Runnable {

		private final ProductBuffer buffer;
		
		private final int total;
		
		public Producer(ProductBuffer buffer, int total) {
			super();
			this.buffer = buffer;
			this.total = total;
		}

		public void run() {
			for(int i = 0; i < total; i++){
				buffer.put(new Product());
			}
		}
		
	}
	
	public static class Consumer implements Runnable {

		private final ProductBuffer buffer;
		
		private final int total;
		
		public Consumer(ProductBuffer buffer, int total) {
			super();
			this.buffer = buffer;
			this.total = total;
		}

		public void run() {
			for(int i = 0; i < total; i++){
				buffer.take();
			}
		}
		
	}
	
}

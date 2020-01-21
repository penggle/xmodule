package com.penglecode.xmodule.java.examples.concurrent.locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用Lock、Condition来实现生产者消费者问题的例子(ArrayBlockingQueue就是基于这个实现的)
 * 
 * Condition 将 Object 监视器方法（wait、notify 和 notifyAll）分解成截然不同的对象，
 * 以便通过将这些对象与任意 Lock 实现组合使用，为每个对象提供多个等待 set（wait-set）。
 * 其中，Lock 替代了 synchronized 方法和语句的使用，Condition 替代了 Object 监视器方法的使用。
 * 
 * @author 	pengpeng
 * @date	2017年11月1日 上午9:50:08
 */
public class ConditionExample {

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
		
		private final Lock lock = new ReentrantLock();
		
		private final Condition notFull = lock.newCondition();
		
		private final Condition notEmpty = lock.newCondition();
		
		private final Product[] buffer;
		
		private final int capacity;
		
		private int currentSize = 0;
		
		private int serialNo = 0;
		
		public ProductBuffer(int capacity) {
			this.buffer = new Product[capacity];
			this.capacity = capacity;
		}
		
		public void put(Product product) {
			lock.lock();
			try {
				while(currentSize >= capacity) {
					try {
						notFull.await(); //缓冲区满了,生产者需要等待
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				product.setName("Product-" + (++serialNo));
				buffer[currentSize++] = product;
				notEmpty.signal(); //唤醒某一个消费者
				System.out.println(String.format(">>>[%s] 生产者生产一个商品: %s", Thread.currentThread().getName(), product));
			} finally {
				lock.unlock();
			}
		}
		
		public Product take() {
			lock.lock();
			try {
				while(currentSize == 0) {
					try {
						notEmpty.await(); //缓冲区空了,消费者需要等待
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Product product = buffer[--currentSize];
				notFull.signal(); //唤醒某一个生产者
				System.out.println(String.format("<<<[%s] 消费者消费一个商品: %s", Thread.currentThread().getName(), product));
				return product;
			} finally {
				lock.unlock();
			}
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

}

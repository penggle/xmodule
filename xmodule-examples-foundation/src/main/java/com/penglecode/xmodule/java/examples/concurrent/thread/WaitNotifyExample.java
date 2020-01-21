package com.penglecode.xmodule.java.examples.concurrent.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 一个线程一旦调用了任意对象的wait()方法，就会变为非运行状态，直到另一个线程调用了同一个对象的notify()方法。为了调用wait()或者notify()，
 * 线程必须先获得那个对象监视器的锁。也就是说，线程必须在同步块里调用wait()或者notify()。
 * 在此例中,充当对象监视器的就是ProductBuffer本身
 * 
 * @author 	pengpeng
 * @date	2017年10月31日 下午5:10:57
 */
public class WaitNotifyExample {

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
		
		private final Product[] buffer;
		
		private final int capacity;
		
		private int currentSize = 0;
		
		private int serialNo = 0;
		
		public ProductBuffer(int capacity) {
			super();
			this.capacity = capacity;
			this.buffer = new Product[capacity];
		}
		
		public synchronized void put(Product product) {
			while(currentSize >= capacity) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			product.setName("Product-" + (++serialNo));
			buffer[currentSize++] = product;
			/**
			 * 此处由于是仅仅放入1个Product所以用notify(),如果一次放入多个Product则需要调用notifyAll()了
			 * notify() : 唤醒在此对象监视器上等待的单个线程,具体唤醒哪个是随机的
			 */
			notify();
			System.out.println(String.format(">>>[%s] 生产者生产一个商品: %s", Thread.currentThread().getName(), product));
		}
		
		public synchronized Product take() {
			while(currentSize == 0) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Product product = buffer[--currentSize];
			/**
			 * 此处由于是仅仅放入1个Product所以用notify(),如果一次放入多个Product则需要调用notifyAll()了
			 * notify() : 唤醒在此对象监视器上等待的单个线程,具体唤醒哪个是随机的
			 */
			notify();
			System.out.println(String.format("<<<[%s] 消费者消费一个商品: %s", Thread.currentThread().getName(), product));
			return product;
		}

		public int getCurrentSize() {
			return currentSize;
		}

		public void setCurrentSize(int currentSize) {
			this.currentSize = currentSize;
		}

		public Product[] getBuffer() {
			return buffer;
		}

		public int getCapacity() {
			return capacity;
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

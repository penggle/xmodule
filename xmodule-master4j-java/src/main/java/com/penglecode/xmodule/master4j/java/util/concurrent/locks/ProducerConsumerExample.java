package com.penglecode.xmodule.master4j.java.util.concurrent.locks;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于ReentrantLock及Condition实现的生产者消费者示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/4 10:48
 */
public class ProducerConsumerExample {

    public static interface BlockingQueue<E> {

        public void put(E element) throws InterruptedException;

        public E take() throws InterruptedException;

    }

    public static class BoundedBuffer<E> implements BlockingQueue<E> {

        private final Queue<E> queue;

        private final int capacity;

        private final Lock lock;

        //与生产者绑定的条件
        private final Condition notFull;

        //与消费者绑定的条件
        private final Condition notEmpty;

        public BoundedBuffer(int capacity) {
            this.capacity = capacity;
            this.queue = new LinkedList<>();
            this.lock = new ReentrantLock();
            this.notFull = this.lock.newCondition();
            this.notEmpty = this.lock.newCondition();
        }

        @Override
        public void put(E element) throws InterruptedException {
            lock.lock();
            try {
                while (queue.size() == capacity) { //缓冲区满了，则阻塞调用线程(生产者)使其等待
                    notFull.await();
                }
                queue.add(element); //某个生产者向缓冲区中新增一个元素
                notEmpty.signal(); //唤醒一个消费者来消费新增的元素
            } finally {
                lock.unlock();
            }
        }

        @Override
        public E take() throws InterruptedException {
            lock.lock();
            try {
                while (queue.size() == 0) { //缓冲区空了，则阻塞调用线程(消费者)使其等待
                    notEmpty.await();
                }
                E element = queue.poll(); //某个消费者从缓冲区中拿走一个元素
                notFull.signal(); //唤醒一个生产者来生产新的元素
                return element;
            } finally {
                lock.unlock();
            }
        }

    }

    static class Product {

        private Integer id;

        private String name;

        public Product(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Product{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    static class Producer implements Runnable {

        private final int produceCount;

        private final AtomicInteger idGenerator;

        private final BlockingQueue<Product> blockingQueue;

        public Producer(int produceCount, AtomicInteger idGenerator, BlockingQueue<Product> blockingQueue) {
            this.produceCount = produceCount;
            this.idGenerator = idGenerator;
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            for(int i = 0; i < produceCount; i++) {
                try {
                    int id = idGenerator.incrementAndGet();
                    Product product = new Product(id, "商品" + id);
                    blockingQueue.put(product);
                    System.out.println(String.format("【%s】>>> 生产了一个商品：%s", Thread.currentThread().getName(), product));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    static class Consumer implements Runnable {

        private final BlockingQueue<Product> blockingQueue;

        private final AtomicInteger remainingCount;

        public Consumer(AtomicInteger remainingCount, BlockingQueue<Product> blockingQueue) {
            this.remainingCount = remainingCount;
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    /**
                     * 预先锁住一个名额，如果不这样，main方法结束不了
                     * (通过jstack查看线程栈，consumer线程全WAITING在take()方法处，而此时producer线程已经全部运行完毕并结束了)
                     */
                    int newRemainings = remainingCount.decrementAndGet();
                    if(newRemainings >= 0) {
                        Product product = blockingQueue.take();
                        System.out.println(String.format("【%s】<<< 消费了一个商品：%s", Thread.currentThread().getName(), product));
                    } else {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        AtomicInteger idGenerator = new AtomicInteger(0);
        int bufferSize = 10; //缓冲区大小
        int totalProduceCount = 0; //总生产量
        BlockingQueue<Product> blockingQueue = new BoundedBuffer<>(bufferSize);

        int pThreads = 4; //生产者个数
        int cThreads = 8; //消费者个数
        Thread[] producers = new Thread[4];
        Thread[] consumers = new Thread[8];

        for(int i = 0; i < pThreads; i++) {
            int produceCount = 200 + random.nextInt(50);
            producers[i] = new Thread(new Producer(produceCount, idGenerator, blockingQueue));
            totalProduceCount += produceCount;
        }

        for(int i = 0; i < pThreads; i++) {
            producers[i].start(); //启动生产者线程
        }

        AtomicInteger remainingCount = new AtomicInteger(totalProduceCount); //剩余生产量

        for(int i = 0; i < cThreads; i++) {
            consumers[i] = new Thread(new Consumer(remainingCount, blockingQueue));
        }

        for(int i = 0; i < cThreads; i++) {
            consumers[i].start(); //启动消费者线程
        }

        for(int i = 0; i < pThreads; i++) {
            producers[i].join();
        }
        for(int i = 0; i < cThreads; i++) {
            consumers[i].join();
        }
        System.out.println("all done，totalProduceCount = " + totalProduceCount);
    }

}

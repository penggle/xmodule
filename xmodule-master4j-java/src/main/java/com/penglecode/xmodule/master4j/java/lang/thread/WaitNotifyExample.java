package com.penglecode.xmodule.master4j.java.lang.thread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通过Object.wait()、notify()、notifyAll()实现线程间通信的例子
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/27 21:03
 */
public class WaitNotifyExample {

    public static class Product {

        private final Integer productId;

        private final String productName;

        public Product(Integer productId, String productName) {
            this.productId = productId;
            this.productName = productName;
        }

        public Integer getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        @Override
        public String toString() {
            return "Product{" +
                    "productId=" + productId +
                    ", productName='" + productName + '\'' +
                    '}';
        }

    }

    public static class ProductBuffer {

        private final Queue<Product> queue;

        private final int capacity;

        public ProductBuffer(int capacity) {
            this.capacity = capacity;
            this.queue = new LinkedList<>();
        }

        public boolean isEmpty() {
            return queue.size() == 0;
        }

        public boolean isFull() {
            return queue.size() == capacity;
        }

        protected Product take() {
            return queue.poll();
        }

        protected void put(Product product) {
            queue.add(product);
        }
    }

    public static class Producer extends Thread {

        /**
         * 商品缓冲区
         */
        private final ProductBuffer buffer;

        /**
         * 总的生产指标
         */
        private final AtomicInteger totalProduceCount;

        public Producer(String threadName, ProductBuffer buffer, AtomicInteger totalProduceCount) {
            super(threadName);
            this.buffer = buffer;
            this.totalProduceCount = totalProduceCount;
        }

        @Override
        public void run() {
            while(true) { //在此处的while中设置动态条件都是徒劳的，跟"wait()方法为什么都放在while()循环中进行检测而不是放在if()中?"是一个道理：wait醒了之后条件有可能变了
                synchronized (buffer) {
                    while (buffer.isFull()) { //队列满了，停止生产
                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //总的生产指标还未完成?继续生产下一个
                    if(totalProduceCount.get() > 0) {
                        buffer.put(produce());
                        buffer.notifyAll(); //唤醒所有消费者继续消费
                    } else { //产量达标立马结束任务
                        break;
                    }
                }

                Thread.yield(); //打乱生产次序
            }
        }

        protected Product produce() {
            Product product = new Product(totalProduceCount.getAndDecrement(), "华为P40 Pro");
            System.out.println(String.format("生产者[%s]生产了一个商品：%s", Thread.currentThread().getName(), product.toString()));
            return product;
        }
    }

    public static class Consumer extends Thread {

        /**
         * 商品缓冲区
         */
        private final ProductBuffer buffer;

        /**
         * 总的消费数量
         */
        private final AtomicInteger totalConsumeCount;

        public Consumer(String threadName, ProductBuffer buffer, AtomicInteger totalConsumeCount) {
            super(threadName);
            this.buffer = buffer;
            this.totalConsumeCount = totalConsumeCount;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    synchronized (buffer) {
                        while(buffer.isEmpty()) { //队列空了，停止消费
                            buffer.wait();
                        }
                        consume(buffer.take());
                        totalConsumeCount.incrementAndGet();
                        buffer.notifyAll(); //唤醒所有生产者继续生产
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
            System.out.println(String.format("【%s】done", Thread.currentThread().getName()));
        }

        protected void consume(Product product) {
            System.out.println(String.format("消费者[%s]消费了一个商品：%s", Thread.currentThread().getName(), product.toString()));
        }

    }

    /**
     * 生产者消费者示例
     *
     * 实现了一个由任意数量的生产者和任意数量的消费者组成的生产-消费系统，来生产(消费)任意个商品
     */
    public static void producerConsumerTest() throws Exception {
        int pThreads = 8; //8个生产者
        int cThreads = 4; //4个消费者
        ProductBuffer buffer = new ProductBuffer(20);

        int totalCount = 231;
        //总的1000个生产指标，分配给pThreads个生产者去完成
        AtomicInteger totalProduceCount = new AtomicInteger(totalCount);
        //总的消费数量
        AtomicInteger totalConsumeCount = new AtomicInteger(0);

        List<Thread> producers = new ArrayList<>();
        for(int i = 0; i < pThreads; i++) {
            Thread thread = new Producer("Producer-" + i, buffer, totalProduceCount);
            producers.add(thread);
            thread.start();
        }

        List<Thread> consumers = new ArrayList<>();
        for(int i = 0; i < cThreads; i++) {
            Thread thread = new Consumer("Consumer-" + i, buffer, totalConsumeCount);
            thread.start();
            consumers.add(thread);
        }

        while(totalProduceCount.get() > 0) { //总产量指标还没完成?
            Thread.sleep(50); //小等一下
        }
        for(Thread producer : producers) {
            System.out.println(String.format("【%s】%s", producer.getName(), producer.getState()));
        }
        System.out.println(String.format("【%s】所有生产者一起完成了产量指标(%s)...", Thread.currentThread().getName(), totalCount));

        while(totalConsumeCount.get() < totalCount) { //所有产量还没消费完?
            Thread.sleep(50); //小等一下
        }

        boolean allConsumerWaiting = true;
        for(Thread consumer : consumers) {
            allConsumerWaiting = allConsumerWaiting && consumer.getState() == Thread.State.WAITING;
        }

        if(allConsumerWaiting) { //所有生产者完成了任务，所有消费者在干等着?
            System.out.println(String.format("【%s】所有消费者都在干等着，即将中断他们...", Thread.currentThread().getName()));
            for(Thread consumer : consumers) {
                consumer.interrupt();
            }
        }

    }

    public static void main(String[] args) throws Exception {
        producerConsumerTest();
    }

}

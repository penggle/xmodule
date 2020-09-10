package com.penglecode.xmodule.master4j.java.util.concurrent;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * 信号量Semaphore示例
 *
 * Semaphore翻译成字面意思为 信号量，Semaphore可以控同时访问的线程个数，
 * 通过 acquire() 获取一个许可，如果没有就等待，而 release() 释放一个许可。
 * 适用于控制并发个数的场景，如控制某个资源池的并发使用情况
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/5 8:19
 */
public class SemaphoreExample {

    static class TicketSaleTask implements Runnable {

        private final Random random;

        private final Semaphore semaphore;

        public TicketSaleTask(Semaphore semaphore) {
            this.semaphore = semaphore;
            this.random = new Random();
        }

        @Override
        public void run() {
            try {
                semaphore.acquire(); //获取一个许可
                saleTicket(); //执行售票
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release(); //释放一个许可
            }
        }

        protected void saleTicket() throws InterruptedException {
            Thread.sleep(random.nextInt(5) * 1000);
            System.out.println(String.format("【%s】购得1张票...", Thread.currentThread().getName()));
        }

    }

    public static void main(String[] args) throws InterruptedException {
        int passengerCount = 50;
        int ticketSellers = 4; //售票员个数(或者售票窗口数)
        TicketSaleTask saleTask = new TicketSaleTask(new Semaphore(ticketSellers));

        Thread[] passengers = new Thread[passengerCount];
        for(int i = 0; i < passengerCount; i++) {
            passengers[i] = new Thread(saleTask, "乘客-" + (i + 1));
        }

        for(int i = 0; i < passengerCount; i++) {
            passengers[i].start();
        }

        for(int i = 0; i < passengerCount; i++) {
            passengers[i].join();
        }

        System.out.println(String.format("【%s】所有乘客都买到了票...", Thread.currentThread().getName()));
    }

}

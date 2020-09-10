package com.penglecode.xmodule.master4j.java.util.concurrent;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * CountDownLatch示例
 *
 * CountDownLatch它可以实现类似计数器的功能。
 * 比如有一个任务A，它要等待其他4个任务执行完毕之后才能执行，此时就可以利用CountDownLatch来实现这种功能了。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/4 20:57
 */
public class CountDownLatchExample {

    static class MyThread extends Thread {

        private final CountDownLatch countDownLatch;

        public MyThread(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                System.out.println(String.format("【%s】>>> 任务开始...", Thread.currentThread().getName()));
                int seconds = new Random().nextInt(10);
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
                System.out.println(String.format("【%s】<<< 任务结束，耗时%s秒...", Thread.currentThread().getName(), seconds));
            } finally {
                countDownLatch.countDown();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int taskSize = 3;
        final CountDownLatch countDownLatch = new CountDownLatch(taskSize);
        for(int i = 0; i < taskSize; i++) {
            new MyThread(countDownLatch).start();
        }
        countDownLatch.await();
        System.out.println(String.format("【%s】所有任务都已完成，主线程结束...", Thread.currentThread().getName()));
    }

}

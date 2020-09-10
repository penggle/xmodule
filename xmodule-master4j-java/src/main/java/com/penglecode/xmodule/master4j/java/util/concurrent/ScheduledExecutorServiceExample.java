package com.penglecode.xmodule.master4j.java.util.concurrent;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/9 13:51
 */
public class ScheduledExecutorServiceExample {

    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
        return new ScheduledThreadPoolExecutor(corePoolSize, Executors.defaultThreadFactory());
    }

    /**
     * ScheduledExecutorService#schedule(..)：任务仅在指定延迟时间到来时执行一次
     */
    public static void scheduleTest() throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = newScheduledThreadPool(8);
        scheduledExecutorService.schedule(() -> {
            Thread currentThread = Thread.currentThread();
            System.out.println(String.format("【%s】isDaemon() = %s", currentThread.getName(), currentThread.isDaemon()));
            System.out.println(String.format("【%s】现在时间是：%s", currentThread.getName() ,LocalDateTime.now()));
        }, 5000, TimeUnit.MILLISECONDS);

        System.out.println(String.format("【%s】done", Thread.currentThread().getName()));
    }

    /**
     * scheduleAtFixedRate()，是以上一个任务开始的时间计时，delay时间过去后，检测上一个任务是否执行完毕，
     * 如果上一个任务执行完毕，则当前任务立即执行，如果上一个任务没有执行完毕，则需要等上一个任务执行完毕后立即执行。
     */
    public static void scheduleAtFixedRateTest() throws InterruptedException {
        System.out.println("=====================scheduleAtFixedRate()====================");
        ScheduledExecutorService scheduledExecutorService = newScheduledThreadPool(8);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Thread currentThread = Thread.currentThread();
            System.out.println(String.format("【%s】现在时间是：%s", currentThread.getName() ,LocalDateTime.now()));
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(8));
        }, 1000, 5000, TimeUnit.MILLISECONDS);

        System.out.println(String.format("【%s】现在时间是：%s", Thread.currentThread().getName(), LocalDateTime.now()));
    }

    /**
     * scheduleWithFixedDelay()，是以上一个任务结束时开始计时，period时间过去后，立即执行，而不管前一个任务是否执行完毕。
     */
    public static void scheduleWithFixedDelayTest() throws InterruptedException {
        System.out.println("=====================scheduleWithFixedDelay()====================");
        ScheduledExecutorService scheduledExecutorService = newScheduledThreadPool(8);
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            Thread currentThread = Thread.currentThread();
            System.out.println(String.format("【%s】现在时间是：%s", currentThread.getName() ,LocalDateTime.now()));
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(8));
        }, 1000, 5000, TimeUnit.MILLISECONDS);

        System.out.println(String.format("【%s】现在时间是：%s", Thread.currentThread().getName(), LocalDateTime.now()));
    }

    public static void main(String[] args) throws Exception {
        //scheduleTest();
        scheduleAtFixedRateTest();
        //scheduleWithFixedDelayTest();
    }

}

package com.penglecode.xmodule.master4j.java.util.concurrent;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

/**
 * CyclicBarrier字面意思回环栅栏，通过它可以实现让一组线程等待至某个状态之后再全部同时执行。
 * 叫做回环是因为当所有等待线程都被释放以后，CyclicBarrier可以被重用。我们暂且把这个状态就叫做barrier，
 * 当调用await()方法之后，线程就处于barrier了。
 *
 * 使用场景：当所有任务线程都达到同一状态点时再同时运行，就像百米赛跑同一起跑线一样，这个起跑线就可以看做栅栏(barrier)
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/5 10:56
 */
public class CyclicBarrierExample {

    static class Meeting implements Runnable {

        private final CyclicBarrier meetingBarrier;

        private final Random random;

        public Meeting(CyclicBarrier meetingBarrier) {
            this.meetingBarrier = meetingBarrier;
            this.random = new Random();
        }

        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(5) * 1000);
                System.out.println(String.format("【%s】【%tT】<<< 来到会场，等待其他与会人员到场，broken = %s", Thread.currentThread().getName(), new Date(), meetingBarrier.isBroken()));
                meetingBarrier.await();
                System.out.println(String.format("【%s】【%tT】>>> 开始开会了，broken = %s", Thread.currentThread().getName(), new Date(), meetingBarrier.isBroken()));
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 打印与会者的到场时间情况
     */
    public static void runMeetingBarrierExample() {
        int participants = 10;
        Meeting meeting = new Meeting(new CyclicBarrier(participants));
        for(int i = 0; i < participants; i++) {
            new Thread(meeting).start();
        }
    }

    static class Running implements Runnable {

        private final CyclicBarrier runningBarrier;

        //总回合数
        private final int roundCount;

        private final Random random;

        public Running(CyclicBarrier runningBarrier, int roundCount) {
            this.runningBarrier = runningBarrier;
            this.roundCount = roundCount;
            this.random = new Random();
        }

        @Override
        public void run() {
            for(int i = 0; i < roundCount; i++) {
                try {
                    //百米赛跑开始
                    long start = System.currentTimeMillis();
                    running();
                    long end = System.currentTimeMillis();
                    //到达终点
                    runningBarrier.await();
                    System.out.println(String.format("【%s】【%tT】>>> 百米赛跑第%s轮成绩：%s秒", Thread.currentThread().getName(), new Date(), (i + 1), (end - start) / 1000.0));
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }

        protected void running() {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10000 + random.nextInt(5000)));
        }

    }

    /**
     * 打印连续三轮百米赛跑的成绩
     */
    public static void runRunningBarrierExample() {
        int runners = 10;
        int rounds = 3;
        Running running = new Running(new CyclicBarrier(runners), rounds);
        for(int i = 0; i < runners; i++) {
            new Thread(running).start();
        }
    }

    static class Enplaning1 implements Runnable {

        private final CyclicBarrier enplaningBarrier;

        private final Random random;

        //离飞机起飞剩余时间
        private final int leftTime = 10;

        public Enplaning1(CyclicBarrier enplaningBarrier) {
            this.enplaningBarrier = enplaningBarrier;
            this.random = new Random();
        }

        @Override
        public void run() {
            int takeTime = takeBoarding();
            /**
             * 由CyclicBarrier.dowait()方法可知：
             * 1、waitTime为负数肯定会导致TimeoutException
             * 2、waitTime为正数有可能会导致TimeoutException
             */
            int waitTime = leftTime - takeTime;
            try {
                enplaningBarrier.await(waitTime, TimeUnit.SECONDS);
                System.out.println(String.format("【%s】【%tT】>>> 及时赶上飞机，broken = %s，waitTime = %s", Thread.currentThread().getName(), new Date(), enplaningBarrier.isBroken(), waitTime));
            } catch (Exception e) {
                if(e instanceof TimeoutException) { //导致TimeoutException的线程只有一个，而且它是导致屏障整体broken的罪魁祸首
                    System.err.println(String.format("【%s】【%tT】>>> 本人未能及时赶上飞机，broken = %s，waitTime = %s，exception = %s", Thread.currentThread().getName(), new Date(), enplaningBarrier.isBroken(), waitTime, e.getClass()));
                } else if (e instanceof BrokenBarrierException) {
                    System.err.println(String.format("【%s】【%tT】>>> 某人未能及时赶上飞机，broken = %s，waitTime = %s，exception = %s", Thread.currentThread().getName(), new Date(), enplaningBarrier.isBroken(), waitTime, e.getClass()));
                }
            }
        }

        /**
         * 赶去登机
         * @return 赶去登机所耗时间
         */
        protected int takeBoarding() {
            //随机构造有些人能准时赶到并登上飞机，有些人则不行
            int takeTime = random.nextInt((int) (leftTime * 1.5));
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(takeTime));
            return takeTime;
        }

    }

    /**
     * 本例通过模拟等待超时TimeoutException来导致屏障的整体broken
     * 导致TimeoutException的线程只有一个，而且它是导致屏障整体broken的罪魁祸首
     */
    public static void runEnplaning1BarrierExample() {
        int passengers = 10;
        Enplaning1 enplaning = new Enplaning1(new CyclicBarrier(passengers));
        for(int i = 0; i < passengers; i++) {
            new Thread(enplaning).start();
        }
    }

    static class Enplaning2 implements Runnable {

        private final CyclicBarrier enplaningBarrier;

        private final Random random;

        //最大赶去登机所耗时间（单位秒）
        private final int maxTakeTime;

        public Enplaning2(CyclicBarrier enplaningBarrier, int maxTakeTime) {
            this.enplaningBarrier = enplaningBarrier;
            this.maxTakeTime = maxTakeTime;
            this.random = new Random();
        }

        @Override
        public void run() {
            takeBoarding();
            try {
                enplaningBarrier.await();
                System.out.println(String.format("【%s】【%tT】>>> 及时赶上飞机，broken = %s", Thread.currentThread().getName(), new Date(), enplaningBarrier.isBroken()));
            } catch (Exception e) {
                if(e instanceof InterruptedException) { //导致InterruptedException的线程只有一个，而且它是导致屏障整体broken的罪魁祸首
                    System.err.println(String.format("【%s】【%tT】>>> 本人未能及时赶上飞机，broken = %s，exception = %s", Thread.currentThread().getName(), new Date(), enplaningBarrier.isBroken(), e.getClass()));
                } else if (e instanceof BrokenBarrierException) {
                    System.err.println(String.format("【%s】【%tT】>>> 某人未能及时赶上飞机，broken = %s，exception = %s", Thread.currentThread().getName(), new Date(), enplaningBarrier.isBroken(), e.getClass()));
                }
            }
        }

        /**
         * 赶去登机
         * @return 赶去登机所耗时间
         */
        protected int takeBoarding() {
            int takeTime = random.nextInt(maxTakeTime);
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(takeTime));
            return takeTime;
        }

    }

    /**
     * 本例通过模拟等待中断InterruptedException来导致屏障的整体broken
     * 导致InterruptedException的线程只有一个，而且它是导致屏障整体broken的罪魁祸首
     */
    public static void runEnplaning2BarrierExample() throws InterruptedException {
        int passengerCount = 10;
        int takeTime = 10;
        Enplaning2 enplaning = new Enplaning2(new CyclicBarrier(passengerCount), takeTime);

        Thread[] passengers = new Thread[passengerCount];
        for(int i = 0; i < passengerCount; i++) {
            passengers[i] = new Thread(enplaning);
            passengers[i].start();
        }

        Thread.sleep(5000); //5秒后随便中断一个线程，即会导致整体broken
        passengers[3].interrupt();
    }

    public static void main(String[] args) throws Exception {
        //runMeetingBarrierExample();
        //runRunningBarrierExample();
        //runEnplaning1BarrierExample();
        runEnplaning2BarrierExample();
    }

}

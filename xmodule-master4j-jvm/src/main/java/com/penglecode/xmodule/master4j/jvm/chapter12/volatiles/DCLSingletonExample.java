package com.penglecode.xmodule.master4j.jvm.chapter12.volatiles;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * 段标准的双锁检测（Double Check Lock，DCL）单例代码
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/25 11:47
 */
public class DCLSingletonExample {

    public static class Singleton {

        private static volatile Singleton instance;

        private Singleton() {}

        public static Singleton getInstance() {
            if(instance == null) {
                synchronized (Singleton.class) {
                    if(instance == null) {
                        instance = new Singleton();
                    }
                }
            }
            return instance;
        }

    }

    public static void main(String[] args) throws Exception {
        System.out.println(">>> 并发获取单例开始");
        int threads = Runtime.getRuntime().availableProcessors();
        final Set<Singleton> singletons = new HashSet<>();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threads);
        CountDownLatch countDownLatch = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            new SingletonTestThread(cyclicBarrier, countDownLatch, singletons).start();
        }
        countDownLatch.await();
        System.out.println(String.format("<<< 并发获取单例结束：%s", singletons));
    }

    static class SingletonTestThread extends Thread {

        private final CyclicBarrier cyclicBarrier;

        private final CountDownLatch countDownLatch;

        private final Set<Singleton> singletons;

        public SingletonTestThread(CyclicBarrier cyclicBarrier, CountDownLatch countDownLatch, Set<Singleton> singletons) {
            this.cyclicBarrier = cyclicBarrier;
            this.countDownLatch = countDownLatch;
            this.singletons = singletons;
        }

        @Override
        public void run() {
            try {
                cyclicBarrier.await(); //等待所有运动员准备完毕
                System.out.println(String.format("【%s】>>> 准备获取单例...", getName()));
                singletons.add(Singleton.getInstance());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown(); //当前测试任务跑完了
            }
        }
    }

}

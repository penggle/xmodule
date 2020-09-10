package com.penglecode.xmodule.master4j.java.util.concurrent.atomics;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * AtomicStampedReference使用示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/9 22:31
 */
public class AtomicStampedReferenceExample {

    static class UserCounter {

        private final AtomicStampedReference<Integer> counter;

        public UserCounter(int initialCount) {
            this.counter = new AtomicStampedReference<>(initialCount, 0);
        }

        public final int[] getBoth() {
            int[] holder = new int[2];
            holder[1] = counter.get(holder);
            return holder;
        }

        public int online() {
            int[] stamp = new int[1];
            while(true) {
                //注意，此处value一定不能用原始类型，因为经历一次拆箱与装箱就有问题了，不是同一个引用,==比较总是为false
                Integer value = counter.get(stamp); //同时获取时间戳和数据，防止获取到数据和版本不是一致的
                int newValue = value + 1;
                boolean success = counter.compareAndSet(value, newValue, stamp[0], stamp[0] + 1);
                if(success) {
                    return newValue;
                }
            }
        }

        public int offline() {
            int[] stamp = new int[1];
            while(true) {
                //注意，此处value一定不能用原始类型，因为经历一次拆箱与装箱就有问题了，不是同一个引用,==比较总是为false
                Integer value = counter.get(stamp); //同时获取时间戳和数据，防止获取到数据和版本不是一致的
                int newValue = value - 1;
                boolean success = counter.compareAndSet(value, newValue, stamp[0], stamp[0] + 1);
                if(success) {
                    return newValue;
                }
            }
        }

    }

    static class UserCounterTestTask implements Runnable {

        private final UserCounter userCounter;

        private final String action;

        private final int iterations;

        public UserCounterTestTask(UserCounter userCounter, String action, int iterations) {
            this.userCounter = userCounter;
            this.action = action;
            this.iterations = iterations;
        }

        @Override
        public void run() {
            for(int i = 0; i < iterations; i++) {
                if("online".equals(action)) {
                    userCounter.online();
                } else if ("offline".equals(action)) {
                    userCounter.offline();
                } else {
                    throw new IllegalArgumentException("Unkown action: " + action);
                }
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        int coreSize = Runtime.getRuntime().availableProcessors();
        final UserCounter userCounter = new UserCounter(0);

        ExecutorService executorService = Executors.newFixedThreadPool(coreSize);

        int count = 100, iterations = 100;
        UserCounterTestTask onlineTask = new UserCounterTestTask(userCounter, "online", iterations);
        UserCounterTestTask offlineTask = new UserCounterTestTask(userCounter, "offline", iterations);

        for(int i = 0; i < count; i++) {
            executorService.submit(onlineTask);
            executorService.submit(offlineTask);
        }

        while (true) {
            Thread.sleep(100);
            int[] both = userCounter.getBoth();
            System.out.println(Arrays.toString(both));
            if(both[0] == iterations * count * 2) {
                break;
            }
        }
        executorService.shutdown();
    }

}

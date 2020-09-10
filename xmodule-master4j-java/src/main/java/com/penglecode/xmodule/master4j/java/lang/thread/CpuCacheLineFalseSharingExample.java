package com.penglecode.xmodule.master4j.java.lang.thread;

import sun.misc.Contended;

/**
 * 缓存行（Cache line）伪共享问题
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/29 19:09
 */
public class CpuCacheLineFalseSharingExample {

    public static final class PaddingValue {

        // 前置填充对象
        protected long p1, p2, p3, p4, p5, p6, p7;
        // value 值
        protected volatile long value = 0L;
        // 后置填充对象
        protected long p9, p10, p11, p12, p13, p14, p15;

    }

    /**
     * 如果将NoPaddingValue.value字段加上@Contended注解，并且加上JVM参数-XX:-RestrictContended再运行：效果跟PADDING一样
     */
    public static final class NoPaddingValue {

        // value 值
        //@Contended
        protected volatile long value = 0L;

    }

    public static final class PaddingData {

        protected static PaddingValue[] values;

    }

    public static final class NoPaddingData {

        protected static NoPaddingValue[] values;

    }

    public static enum PaddingType {
        PADDING, NO_PADDING;
    }

    /**
     * 测试在nThreads个线程的并发读写情况下的性能
     */
    public static void runTest(PaddingType type, int nThreads) throws InterruptedException {
        Thread[] threads = new Thread[nThreads];
        //初始化
        switch (type) {
            case PADDING:
                PaddingData.values = new PaddingValue[nThreads];
                for(int i = 0; i < nThreads; i++) {
                    PaddingData.values[i] = new PaddingValue();
                }
                break;
            case NO_PADDING:
                NoPaddingData.values = new NoPaddingValue[nThreads];
                for(int i = 0; i < nThreads; i++) {
                    NoPaddingData.values[i] = new NoPaddingValue();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        for(int i = 0; i < nThreads; i++) {
            threads[i] = new Thread(new FalseSharing(i, type));
        }
        for(Thread thread : threads) {
            thread.start();
        }
        for(Thread thread : threads) {
            thread.join();
        }
    }

    public static class FalseSharing implements Runnable {

        public final static long ITERATIONS = 500L * 1000L * 100L;

        private final int arrayIndex;

        private final PaddingType type;

        public FalseSharing(int arrayIndex, PaddingType type) {
            this.arrayIndex = arrayIndex;
            this.type = type;
        }

        @Override
        public void run() {
            long i = ITERATIONS + 1;
            // 读取共享变量中指定的下标对象，并对其value变量不断修改
            // 由于每次读取数据都会写入缓存行，如果线程间有共享的缓存行数据，就会导致伪共享问题发生
            // 如果对象已填充，那么线程每次读取到缓存行中的对象就不会产生伪共享问题
            switch (type) {
                case NO_PADDING:
                    while (0 != --i) {
                        NoPaddingData.values[arrayIndex].value = 0L;
                    }
                    break;
                case PADDING:
                    while (0 != --i) {
                        PaddingData.values[arrayIndex].value = 0L;
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            }
        }
    }

    /**
     * 测试结果1：
     *
     * [PADDING]Thread num 1 duration = 438
     * [PADDING]Thread num 2 duration = 501
     * [PADDING]Thread num 3 duration = 527
     * [PADDING]Thread num 4 duration = 516
     * [PADDING]Thread num 5 duration = 702
     * [PADDING]Thread num 6 duration = 761
     * [PADDING]Thread num 7 duration = 735
     * [PADDING]Thread num 8 duration = 684
     * [PADDING]Thread num 9 duration = 794
     * [NO_PADDING] Thread num 1 duration = 436
     * [NO_PADDING] Thread num 2 duration = 1484
     * [NO_PADDING] Thread num 3 duration = 1729
     * [NO_PADDING] Thread num 4 duration = 1669
     * [NO_PADDING] Thread num 5 duration = 3206
     * [NO_PADDING] Thread num 6 duration = 3758
     * [NO_PADDING] Thread num 7 duration = 3357
     * [NO_PADDING] Thread num 8 duration = 3653
     * [NO_PADDING] Thread num 9 duration = 2796
     *
     *
     * 如果将NoPaddingValue.value字段加上@Contended注解，并且加上JVM参数-XX:-RestrictContended再运行：效果跟PADDING一样：
     *
     * [PADDING]Thread num 1 duration = 431
     * [PADDING]Thread num 2 duration = 446
     * [PADDING]Thread num 3 duration = 525
     * [PADDING]Thread num 4 duration = 556
     * [PADDING]Thread num 5 duration = 718
     * [PADDING]Thread num 6 duration = 909
     * [PADDING]Thread num 7 duration = 704
     * [PADDING]Thread num 8 duration = 678
     * [PADDING]Thread num 9 duration = 791
     * [NO_PADDING] Thread num 1 duration = 427
     * [NO_PADDING] Thread num 2 duration = 450
     * [NO_PADDING] Thread num 3 duration = 553
     * [NO_PADDING] Thread num 4 duration = 617
     * [NO_PADDING] Thread num 5 duration = 557
     * [NO_PADDING] Thread num 6 duration = 591
     * [NO_PADDING] Thread num 7 duration = 628
     * [NO_PADDING] Thread num 8 duration = 671
     * [NO_PADDING] Thread num 9 duration = 797
     */
    public static void main(String[] args) throws Exception {
        for (int i = 1; i < 10; i++) {
            System.gc();
            final long start = System.currentTimeMillis();
            runTest(PaddingType.PADDING, i); //依次增加并发线程数，测试读写PADDING型共享变量的性能
            System.out.println("[PADDING]Thread num " + i + " duration = " + (System.currentTimeMillis() - start));
        }
        for (int i = 1; i < 10; i++) {
            System.gc();
            final long start = System.currentTimeMillis();
            runTest(PaddingType.NO_PADDING, i); //依次增加并发线程数，测试读写NO_PADDING型共享变量的性能
            System.out.println("[NO_PADDING] Thread num " + i + " duration = " + (System.currentTimeMillis() - start));
        }
    }

}

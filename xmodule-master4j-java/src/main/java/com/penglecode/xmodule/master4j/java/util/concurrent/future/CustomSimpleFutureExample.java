package com.penglecode.xmodule.master4j.java.util.concurrent.future;

import com.penglecode.xmodule.common.support.NamedThreadFactory;

import java.util.concurrent.*;

/**
 * 手写一个简单的Future实现
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/10 10:54
 */
public class CustomSimpleFutureExample {

    public static void main(String[] args) throws Exception {
        FutureService futureService = new FutureService();
        Future<Long> future = futureService.submit(() -> {
            System.out.println(String.format("【%s】>>> task start ...", Thread.currentThread().getName()));
            long start = System.currentTimeMillis();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            System.out.println(String.format("【%s】<<< task end ...", Thread.currentThread().getName()));
            return end - start;
        });
        System.out.println(String.format("【%s】>>> task submited ...", Thread.currentThread().getName()));
        Long cost = future.get();
        System.out.println(String.format("【%s】<<< task done, result = %s", Thread.currentThread().getName(), cost));
    }

    /**
     * 代表的是一个未来的凭据
     * @param <T>
     */
    public static interface Future<T> {

        public boolean isDone();

        public T get() throws InterruptedException;

        public T get(long timeout, TimeUnit unit)  throws InterruptedException;

    }

    /**
     * 具体的任务逻辑
     * @param <T>
     */
    public interface FutureTask<T> {

        public T call();

    }

    public static class AsynFuture<T> implements Future<T> {

        private volatile boolean done = false;

        private T result;

        @Override
        public boolean isDone() {
            return done;
        }

        @Override
        public T get() throws InterruptedException {
            synchronized (this) {
                while (!done) {
                    this.wait();
                }
            }
            return result;
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws InterruptedException {
            synchronized (this) {
                while (!done) {
                    this.wait(unit.toMillis(timeout));
                }
            }
            return result;
        }

        protected void done(T result) {
            synchronized (this) {
                this.result = result;
                this.done = true;
                this.notifyAll();
            }
        }

    }

    public static class FutureService {

        private final ThreadFactory threadFactory;

        public FutureService() {
            this(new NamedThreadFactory("my-future-task"));
        }

        public FutureService(ThreadFactory threadFactory) {
            this.threadFactory = threadFactory;
        }

        public <T> Future<T> submit(FutureTask<T> task) {
            final AsynFuture<T> future = new AsynFuture<>();
            threadFactory.newThread(() -> future.done(task.call())).start();
            return future;
        }

    }

}

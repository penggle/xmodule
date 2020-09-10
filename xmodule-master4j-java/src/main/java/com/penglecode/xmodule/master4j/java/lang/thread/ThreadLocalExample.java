package com.penglecode.xmodule.master4j.java.lang.thread;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * ThreadLocal原理解析
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/6 14:24
 */
public class ThreadLocalExample {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    static class LoggerThreadLocal extends ThreadLocal<Logger> {
        @Override
        protected Logger initialValue() {
            return new Logger();
        }
    }

    static class Logger {

        private final String loggerId;

        public Logger() {
            this.loggerId = UUID.randomUUID().toString();
        }

        public void log(String message, Object... args) {
            System.out.println("【" + loggerId + "】>>> " + String.format(message, args));
        }
    }

    static class DataProcessor extends Thread {

        private final LoggerThreadLocal loggerThreadLocal;

        public DataProcessor(LoggerThreadLocal loggerThreadLocal) {
            this.loggerThreadLocal = loggerThreadLocal;
        }

        @Override
        public void run() {
            beforeProcess();
            postProcess();
            afterProcess();
        }

        protected void beforeProcess() {
            loggerThreadLocal.get().log("do something before process...");
        }

        protected void postProcess() {
            loggerThreadLocal.get().log("do something post process...");
        }

        protected void afterProcess() {
            loggerThreadLocal.get().log("do something after process...");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LoggerThreadLocal loggerThreadLocal = new LoggerThreadLocal();

        Thread[] threads = new Thread[4];
        for(int i = 0; i < threads.length; i++) {
            threads[i] = new DataProcessor(loggerThreadLocal);
        }

        for(int i = 0; i < threads.length; i++) {
            threads[i].start();
        }

        for(int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println(loggerThreadLocal);

    }

}

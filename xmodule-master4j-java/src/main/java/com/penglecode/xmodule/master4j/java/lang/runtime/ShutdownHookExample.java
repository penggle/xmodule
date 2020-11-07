package com.penglecode.xmodule.master4j.java.lang.runtime;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * Java程序经常也会遇到进程挂掉的情况，一些状态没有正确的保存下来，这时候就需要在JVM关掉的时候执行一些清理现场的代码。JAVA中的ShutdownHook提供了比较好的方案。
 *
 * JDK提供了Java.Runtime.addShutdownHook(Thread hook)方法，可以注册一个JVM关闭的钩子，这个钩子可以在一下几种场景中被调用：
 *
 * 1.程序正常退出
 * 2.使用System.exit()
 * 3.终端使用Ctrl+C触发的中断
 * 4.系统关闭
 * 5.OutOfMemory宕机
 * 6.使用Kill pid命令干掉进程（注：在使用kill -9 pid时，是不会被调用的）
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/30 14:31
 */
public class ShutdownHookExample {

    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            String curThreadName = Thread.currentThread().getName();
            int exitSeconds = 30;
            for(int i = 0; i < exitSeconds; i++) {
                System.out.println(String.format("【%s】>>> JVM即将在%s秒后退出...", curThreadName, exitSeconds - i));
                LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
            }
        }));
        System.out.println(String.format("【%s】>>> 服务启动了...", Thread.currentThread().getName()));
    }

    public static void main(String[] args) {
        new ShutdownHookExample().start();
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(10));
        System.out.println(String.format("【%s】<<< 服务即将关闭...", Thread.currentThread().getName()));
    }

}

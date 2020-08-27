package com.penglecode.xmodule.master4j.java.lang.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * 面试题：一个Java程序启动至少启动几个线程？
 * 回答：抛开JVM内部的管理线程(例如垃圾回收线程)来说，那至少有一个线程，即主线程
 *      如果想具体知道到底实际有几个线程在运行的话，我们可以使用JMX API下的ThreadMXBean实例dump出所有的正在运行的线程集合
 *
 *
 * 6 - Monitor Ctrl-Break       //监控用户Ctrl + Break强制退出动作的线程
 * 5 - Attach Listener
 * 4 - Signal Dispatcher        //分发处理发送给JVM信号的线程
 * 3 - Finalizer                //调用对象的finalize方法的线程，就是垃圾回收的线程
 * 2 - Reference Handler        //清除reference的线程
 * 1 - main                     //主线程
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/26 10:50
 */
public class JvmActualRunningThreadsExample {

    public static void main(String[] args) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        for(ThreadInfo threadInfo : threadInfos) {
            System.out.println(threadInfo.getThreadId() + " - " + threadInfo.getThreadName());
        }
    }

}

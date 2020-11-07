package com.penglecode.xmodule.master4j.jvm.chapter3.gc;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.concurrent.locks.LockSupport;

/**
 * 使用MemoryPoolMXBean来监控GC信息
 *
 * -Xmx2048m -Xms2048m -Xmn1024m -XX:+UseConcMarkSweepGC -XX:+PrintFlagsFinal -XX:+PrintCommandLineFlags
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/12 21:03
 */
public class GcMXBeanExample {

    public static void main(String[] args) throws Exception {
        byte[] array = new byte[700*1024*1024];

        for(MemoryPoolMXBean memoryPoolMXBean: ManagementFactory.getMemoryPoolMXBeans()){
            System.out.println(memoryPoolMXBean.getName()
                    + "   total:"+memoryPoolMXBean.getUsage().getCommitted()
                    + "   used:"+memoryPoolMXBean.getUsage().getUsed());
        }
        LockSupport.park();
    }

}

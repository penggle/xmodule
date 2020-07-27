package com.penglecode.xmodule.master4j.jvm.chapter8.localvariables;

/**
 * 本地变量曹影响GC的示例2
 *
 * 缩小placeholder的作用域，可能会成功的即时回收掉placeholder的内存空间，为什么是可能？
 * 1、如果JVM执行的是传统的解释执行的话那么此处placeholder内存空间仍然不能被GC回收，
 * 但是如果我们添加代码（placeholder = null）强制指示JVM回收placeholder，也是能达到即时回收的目的的
 *
 * -XX:InitialHeapSize=266553600 -XX:MaxHeapSize=4264857600 -XX:+PrintCommandLineFlags -XX:+PrintGC -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC
 * [B@1ddc4ec2
 * [GC (System.gc())  72090K->66800K(251392K), 0.0014161 secs]
 * [Full GC (System.gc())  66800K->1118K(251392K), 0.0101097 secs] //加入placeholder = null;强制JVM回收的效果
 *
 *
 * 2、如果JVM执行的是JIT即时编译的话那么此处placeholder内存空间将会被GC回收，
 *
 * VM Args：-XX:+PrintGC -XX:+PrintCommandLineFlags
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/21 10:13
 */
public class LocalVariablesSlotEffectGCExample3 {

    public static void main(String[] args) {
        //将placeholder放入代码块中，缩小其作用域，使得后面的gc能够回收placeholder
        {
            byte[] placeholder = new byte[64 * 1024 * 1024];
            System.out.println(placeholder);
            /**
             * 手动强制JVM回收placeholder，在JVM基于解释执行时有效，
             * 基于JIT即时编译执行时下面这句代码将会被忽略，但placeholder也会被即时回收掉
             * 所以该语句可以算是个保险的hack兼容写法，因为这段代码到底是基于解释执行还是基于JIT即时编译执行无法确定！
             */
            placeholder = null;
        }
        System.gc();
    }

}

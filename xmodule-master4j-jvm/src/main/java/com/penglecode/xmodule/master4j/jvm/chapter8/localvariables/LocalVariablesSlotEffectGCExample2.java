package com.penglecode.xmodule.master4j.jvm.chapter8.localvariables;

/**
 * 本地变量曹影响GC的示例2
 *
 * 缩小placeholder的作用域，可能会成功的即时回收掉placeholder的内存空间，为什么是可能？
 * 1、如果JVM执行的是传统的解释执行的话那么此处placeholder内存空间仍然不能被GC回收，如下面结果：
 * -XX:InitialHeapSize=266553600 -XX:MaxHeapSize=4264857600 -XX:+PrintCommandLineFlags -XX:+PrintGC -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC
 * [B@1ddc4ec2
 * [GC (System.gc())  72090K->66784K(251392K), 0.0012992 secs]
 * [Full GC (System.gc())  66784K->66654K(251392K), 0.0069660 secs]
 *
 * 2、如果JVM执行的是JIT即时编译的话那么此处placeholder内存空间将会被GC回收，
 *
 * VM Args：-XX:+PrintGC -XX:+PrintCommandLineFlags
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/21 10:13
 */
public class LocalVariablesSlotEffectGCExample2 {

    public static void main(String[] args) {
        //将placeholder放入代码块中，缩小其作用域，使得后面的gc能够回收placeholder
        {
            byte[] placeholder = new byte[64 * 1024 * 1024];
            System.out.println(placeholder);
        }
        System.gc();
    }

}

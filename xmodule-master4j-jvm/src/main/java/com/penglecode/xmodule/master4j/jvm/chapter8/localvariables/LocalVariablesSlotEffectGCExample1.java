package com.penglecode.xmodule.master4j.jvm.chapter8.localvariables;

/**
 * 本地变量曹影响GC的示例1
 *
 * 下面代码没有回收掉placeholder所占的内存是能说得过去，因为在执行System.gc()时，
 * 变量placeholder还处于作用域之内，虚拟机自然不敢回收掉placeholder的内存。
 *
 * -XX:InitialHeapSize=266553600 -XX:MaxHeapSize=4264857600 -XX:+PrintCommandLineFlags -XX:+PrintGC -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC
 * [B@1ddc4ec2
 * [GC (System.gc())  72090K->66880K(251392K), 0.0028194 secs]
 * [Full GC (System.gc())  66880K->66654K(251392K), 0.0080563 secs] //可以看出手动调用GC并没有回收掉placeholder大对象
 *
 * VM Args：-XX:+PrintGC -XX:+PrintCommandLineFlags
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/21 10:13
 */
public class LocalVariablesSlotEffectGCExample1 {

    public static void main(String[] args) {
        byte[] placeholder = new byte[64 * 1024 * 1024];
        /**
         * 必须加上这句才能复现JVM不回收placeholder的情况(这一点与《深入理解Java虚拟机》中所述示例代码有出入)
         * 注释这句的结果是：
         *
         * -XX:InitialHeapSize=266553600 -XX:MaxHeapSize=4264857600 -XX:+PrintCommandLineFlags -XX:+PrintGC -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC
         * [GC (System.gc())  72090K->66820K(251392K), 0.0034652 secs]
         * [Full GC (System.gc())  66820K->1117K(251392K), 0.0066167 secs]
         *
         * 注释该句，通过jclasslib bytecode viewer插件可以看到main方法的LocalVariablesTable中并没有placeholder这个变量
         * 相反加上该句，则main方法的LocalVariablesTable中就有placeholder这个变量
         */
        System.out.println(placeholder);
        System.gc();
    }

}

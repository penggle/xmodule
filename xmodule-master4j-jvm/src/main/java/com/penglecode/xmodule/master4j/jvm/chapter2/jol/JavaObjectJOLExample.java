package com.penglecode.xmodule.master4j.jvm.chapter2.jol;

import org.openjdk.jol.info.ClassLayout;

/**
 * 如何正确计算Java对象所占内存？
 * 此例我们使用官方推荐的jol工具来计算一个最简单的Object对象的内存大小布局
 * 当前硬件环境：64位机器，16GB内存，JDK8，因此默认是启用指针压缩的，而且是起效的
 *
 * jol工具的maven依赖：
 *    <dependency>
 *         <groupId>org.openjdk.jol</groupId>
 *         <artifactId>jol-core</artifactId>
 *         <version>0.11</version>
 *     </dependency>
 *
 * VM Args：-XX:+PrintCommandLineFlags
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/24 15:15
 */
public class JavaObjectJOLExample {

    /**
     * 输出：
     * java.lang.Object object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
     *       0    12        (object header)                           N/A
     *      12     4        (loss due to the next object alignment)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     *
     * 解释：
     * 本机为64位机器，总内存16GB，默认最大堆内存为总内存的1/4，未超过32GB，故-XX:+UseCompressedOops参数起效。
     * JAVA对象由三部分组成：
     * |---------------------------|-----------------|---------|
     * |       Object Header       |  Instance Data  | Padding |
     * |-----------|---------------|-----------------|---------|
     * | Mark Word | Klass Pointer | field1|filed2|  | Padding |
     * |-----------|---------------|-----------------|---------|
     *
     * java.lang.Object为非数组的普通对象，对象头仅有Mark Word和Klass Pointer组成，
     * 32位机器中，Mark Word和Klass Pointer各占32位，即4字节
     * 64位机器中，在未启用压缩指针的情况下(-XX:-UseCompressedOops)，Mark Word和Klass Pointer各占64位，即8字节
     * 64位机器中，在启用压缩指针的情况下(-XX:+UseCompressedOops)，Mark Word和Klass Pointer各占64位和32位，即8字节和4字节
     *
     *
     * 所以java.lang.Object对象占：8字节 + 4字节 + 4字节(对齐补白)
     *
     * 最后4字节为对齐补白加上去的，对齐使得对象的大小为8字节(由-XX:ObjectAlignmentInBytes决定，默认为8)的整数倍
     */
    public static void main(String[] args) {
        System.out.println(ClassLayout.parseClass(Object.class).toPrintable());
        System.out.println("--------------------------------------------------------------");
        System.out.println(ClassLayout.parseInstance(new Object()).toPrintable());
    }

}

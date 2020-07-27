package com.penglecode.xmodule.master4j.jvm.chapter2.jol;

import org.openjdk.jol.info.ClassLayout;

/**
 * Java复杂对象的内存大小布局示例
 * 当前硬件环境：64位机器，16GB内存，JDK8，因此默认是启用指针压缩的，而且是起效的
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/24 17:11
 */
public class JavaComplexObjectJOLExample {

    static class A {

    }

    static class B {

        private long timestamp;

        private Boolean enabled;

        private int age;

    }

    static class C {

        private byte b;

        private long l;

    }

    static class D {

        private double price;

        /**
         * 数组引用和其他普通对象引用一样占4或8个字节(跟指针压缩有关)
         * 但是数组对象本身则另论
         */
        private int[] states;

        /**
         * 数组引用和其他普通对象引用一样占4或8个字节(跟指针压缩有关)
         * 但是数组对象本身则另论
         */
        private String[] hobies;

    }

    /**
     * 没什么好解释的，跟java.lang.Object差不多
     *
     * com.penglecode.xmodule.master4j.jvm.chapter2.jol.JavaComplexObjectJOLExample$A object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
     *       0    12        (object header)                           N/A
     *      12     4        (loss due to the next object alignment)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     */
    public static void printA() {
        System.out.println(ClassLayout.parseClass(A.class).toPrintable());
    }

    /**
     * 这个复杂的例子中，通过了智能分配多个字段，省下内对齐的4个字节！为什么这么说，请尝试着调换字段的声明顺序，而输出一点不变！
     * 为什么会这样？其中通过智能分配多个字段，省下了long类型的内对齐4个字节，因为有些资料或网友说了这么一个对象内字段对齐规则：
     * 对象内字段之间排序的优先级为： long = double > int = float > char = short > byte > boolean > object reference。
     *
     * 倘若以这条规则为准，那么必定出现：第一个内对齐long与对象头之间需要补白4个字节，可是这里没有，或许是JDK版本的不同导致的
     *
     * com.penglecode.xmodule.master4j.jvm.chapter2.jol.JavaComplexObjectJOLExample$B object internals:
     *  OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
     *       0    12                     (object header)                           N/A
     *      12     4                 int B.age                                     N/A
     *      16     8                long B.timestamp                               N/A
     *      24     4   java.lang.Boolean B.enabled                                 N/A
     *      28     4                     (loss due to the next object alignment)
     * Instance size: 32 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     */
    public static void printB() {
        System.out.println(ClassLayout.parseClass(B.class).toPrintable());
    }

    /**
     * 相比于printB()示例，又进一步证实了，的确做到了智能顺序优化，使得内对齐是3个字节而不是4个字节
     *
     * com.penglecode.xmodule.master4j.jvm.chapter2.jol.JavaComplexObjectJOLExample$C object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
     *       0    12        (object header)                           N/A
     *      12     1   byte C.b                                       N/A
     *      13     3        (alignment/padding gap)
     *      16     8   long C.l                                       N/A
     * Instance size: 24 bytes
     * Space losses: 3 bytes internal + 0 bytes external = 3 bytes total
     */
    public static void printC() {
        System.out.println(ClassLayout.parseClass(C.class).toPrintable());
    }

    /**
     *
     * com.penglecode.xmodule.master4j.jvm.chapter2.jol.JavaComplexObjectJOLExample$D object internals:
     *  OFFSET  SIZE                 TYPE DESCRIPTION                               VALUE
     *       0    12                      (object header)                           N/A
     *      12     4                int[] C.states                                  N/A
     *      16     8               double C.price                                   N/A
     *      24     4   java.lang.String[] C.hobies                                  N/A
     *      28     4                      (loss due to the next object alignment)
     * Instance size: 32 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     */
    public static void printD() {
        System.out.println(ClassLayout.parseClass(D.class).toPrintable());
    }

    /**
     * java.lang.String object internals:
     *  OFFSET  SIZE     TYPE DESCRIPTION                               VALUE
     *       0    12          (object header)                           N/A
     *      12     4   char[] String.value                              N/A
     *      16     4      int String.hash                               N/A
     *      20     4          (loss due to the next object alignment)
     * Instance size: 24 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     */
    public static void printString() {
        System.out.println(ClassLayout.parseClass(String.class).toPrintable());
    }

    public static void printStringObject() {
        String str = "hello world!";
        System.out.println(ClassLayout.parseInstance(str).toPrintable());
    }

    public static void main(String[] args) {
        //printA();
        //printB();
        //printC();
        //printD();
        printString();
        printStringObject();
    }

}

package com.penglecode.xmodule.master4j.jvm.chapter2.jol;

import org.openjdk.jol.info.ClassLayout;

/**
 * Java数组类型的内存大小布局示例
 * 当前硬件环境：64位机器，16GB内存，JDK8，因此默认是启用指针压缩的，而且是起效的
 *
 * 数组类型的引用相对于普通对象最大的区别是在对象头上上多了一个数组长度存储区域，即：
 * 数组类型的引用其对象头由：Mark Word, Klass Word, 数组长度三部分组成，
 * 其中Mark Word与Klass Word的占用大小与普通对象一致，这里就不说了，而数组长度的占用大小是多少呢？
 * 数组长度是int类型，因此是固定死的4个字节无疑了
 *
 * 综上所述，数组类型的引用，在64位机器中，开启指针压缩的情况下，占16个字节，不开启指针压缩的情况下是24个字节(含对齐补白4个字节)
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/25 9:53
 */
public class JavaArrayJOLExample {

    /**
     * [I object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
     *       0    16        (object header)                           N/A
     *      16     0    int [I.<elements>                             N/A
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     */
    public static void printIntArrayType() {
        System.out.println(ClassLayout.parseClass(int[].class).toPrintable());
    }

    /**
     * [I object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
     *       0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
     *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4        (object header)                           6d 01 00 f8 (01101101 00000001 00000000 11111000) (-134217363)
     *      12     4        (object header)                           0a 00 00 00 (00001010 00000000 00000000 00000000) (10)
     *      16    40    int [I.<elements>                             N/A
     * Instance size: 56 bytes
     * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     */
    public static void printIntArrayObject() {
        int[] a = new int[10];
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
    }

    /**
     * 从类的角度来看任何类型的数组其所占的大小都是16或者24个字节
     *
     * [Ljava.lang.Integer; object internals:
     *  OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
     *       0    16                     (object header)                           N/A
     *      16     0   java.lang.Integer Integer;.<elements>                       N/A
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     */
    public static void printIntegerArrayType() {
        System.out.println(ClassLayout.parseClass(Integer[].class).toPrintable());
    }

    /**
     * 数组中的元素装的是Integer的引用，引用占4字节或8字节，故下面出现：16    40   java.lang.Integer Integer;.<elements>
     * 那你可能要怀疑了，Integer数组中装的是Integer引用还是原型int值？不妨关闭指针压缩-XX:-UseCompressedOops。来瞧瞧
     *
     * 开启指针压缩：-XX:+UseCompressedOops ：
     *
     * [Ljava.lang.Integer; object internals:
     *  OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
     *       0     4                     (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
     *       4     4                     (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4                     (object header)                           65 62 00 f8 (01100101 01100010 00000000 11111000) (-134192539)
     *      12     4                     (object header)                           0a 00 00 00 (00001010 00000000 00000000 00000000) (10)
     *      16    40   java.lang.Integer Integer;.<elements>                       N/A
     * Instance size: 56 bytes
     * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     *
     * 关闭指针压缩：-XX:-UseCompressedOops ：
     * [Ljava.lang.Integer; object internals:
     *  OFFSET  SIZE                TYPE DESCRIPTION                               VALUE
     *       0     4                     (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
     *       4     4                     (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4                     (object header)                           b8 81 d0 1b (10111000 10000001 11010000 00011011) (466649528)
     *      12     4                     (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *      16     4                     (object header)                           0a 00 00 00 (00001010 00000000 00000000 00000000) (10)
     *      20     4                     (alignment/padding gap)
     *      24    80   java.lang.Integer Integer;.<elements>                       N/A
     * Instance size: 104 bytes
     * Space losses: 4 bytes internal + 0 bytes external = 4 bytes total
     */
    public static void printIntegerArrayObject() {
        Integer[] a = new Integer[10];
        System.out.println(ClassLayout.parseInstance(a).toPrintable());
    }

    public static void main(String[] args) {
        //printIntArrayType();
        //printIntArrayObject();
        //printIntegerArrayType();
        printIntegerArrayObject();
    }
}

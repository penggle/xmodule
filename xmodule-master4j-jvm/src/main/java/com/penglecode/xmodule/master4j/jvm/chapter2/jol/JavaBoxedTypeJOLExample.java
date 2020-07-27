package com.penglecode.xmodule.master4j.jvm.chapter2.jol;

import org.openjdk.jol.info.ClassLayout;

/**
 * Java包装类型的内存大小布局示例
 * 当前硬件环境：64位机器，16GB内存，JDK8，因此默认是启用指针压缩的，而且是起效的
 *
 * VM Args：-XX:+PrintCommandLineFlags
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/24 15:45
 */
public class JavaBoxedTypeJOLExample {

    /**
     * java.lang.Byte object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
     *       0    12        (object header)                           N/A
     *      12     1   byte Byte.value                                N/A
     *      13     3        (loss due to the next object alignment)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 3 bytes external = 3 bytes total
     */
    public static void printByte() {
        System.out.println(ClassLayout.parseClass(Byte.class).toPrintable());
    }

    /**
     * java.lang.Boolean object internals:
     *  OFFSET  SIZE      TYPE DESCRIPTION                               VALUE
     *       0    12           (object header)                           N/A
     *      12     1   boolean Boolean.value                             N/A
     *      13     3           (loss due to the next object alignment)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 3 bytes external = 3 bytes total
     */
    public static void printBoolean() {
        System.out.println(ClassLayout.parseClass(Boolean.class).toPrintable());
    }

    /**
     * java.lang.Short object internals:
     *  OFFSET  SIZE    TYPE DESCRIPTION                               VALUE
     *       0    12         (object header)                           N/A
     *      12     2   short Short.value                               N/A
     *      14     2         (loss due to the next object alignment)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 2 bytes external = 2 bytes total
     */
    public static void printShort() {
        System.out.println(ClassLayout.parseClass(Short.class).toPrintable());
    }

    /**
     * java.lang.Character object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
     *       0    12        (object header)                           N/A
     *      12     2   char Character.value                           N/A
     *      14     2        (loss due to the next object alignment)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 2 bytes external = 2 bytes total
     */
    public static void printCharacter() {
        System.out.println(ClassLayout.parseClass(Character.class).toPrintable());
    }

    /**
     * java.lang.Integer object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
     *       0    12        (object header)                           N/A
     *      12     4    int Integer.value                             N/A
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     */
    public static void printInteger() {
        System.out.println(ClassLayout.parseClass(Integer.class).toPrintable());
    }

    /**
     * java.lang.Float object internals:
     *  OFFSET  SIZE    TYPE DESCRIPTION                               VALUE
     *       0    12         (object header)                           N/A
     *      12     4   float Float.value                               N/A
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
     */
    public static void printFloat() {
        System.out.println(ClassLayout.parseClass(Float.class).toPrintable());
    }

    /**
     * 注意这里出现了4字节的内对齐，
     * 那是因为对象中的字段也需要对齐，对齐规则是：
     * 1、如果一个字段占据 C 个字节，那么该字段的偏移量需要对齐至 NC。这里偏移量指的是字段地址与对象的起始地址差值。
     *
     * 而Double类中只有一个名为value的原始类型double，其本身占8字节，因此value按照8N来对齐，也即：value是8byte并按照8byte对齐，
     * 而对象头12byte从0byte~11byte，所以double的实际存放地址是16~23byte，在double value和对象头之间需要4byte的padding
     * （但这个padding不是对象头的，是后面的double value根据第一条对齐规则导致的，Integer例子中int value是4byte对齐，4byte是前面对象头12byte的整数倍，就不需要额外的padding了）。
     * 填充之后，对象大小为24byte，是默认对齐8byte的整倍数，对象间不需要额外的padding。
     * ————————————————
     * 参考链接：https://blog.csdn.net/reliveit/java/article/details/101161173
     *
     * java.lang.Double object internals:
     *  OFFSET  SIZE     TYPE DESCRIPTION                               VALUE
     *       0    12          (object header)                           N/A
     *      12     4          (alignment/padding gap)
     *      16     8   double Double.value                              N/A
     * Instance size: 24 bytes
     * Space losses: 4 bytes internal + 0 bytes external = 4 bytes total
     */
    public static void printDouble() {
        System.out.println(ClassLayout.parseClass(Double.class).toPrintable());
    }

    /**
     * 内对齐，同Double
     *
     * java.lang.Long object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
     *       0    12        (object header)                           N/A
     *      12     4        (alignment/padding gap)
     *      16     8   long Long.value                                N/A
     * Instance size: 24 bytes
     * Space losses: 4 bytes internal + 0 bytes external = 4 bytes total
     */
    public static void printLong() {
        System.out.println(ClassLayout.parseClass(Long.class).toPrintable());
    }

    public static void main(String[] args) {
        //printByte();
        //printBoolean();
        //printShort();
        //printCharacter();
        //printInteger();
        //printFloat();
        //printDouble();
        printLong();
    }

}

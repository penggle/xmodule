package com.penglecode.xmodule.master4j.java.basics.datatype;

/**
 * Integer类的API示例
 *
 * 1、所谓原码就是二进制定点表示法，即最高位为符号位，“0”表示正，“1”表示负，其余位表示数值的大小。
 * 2、反码表示法规定：正数的反码与其原码相同；负数的反码是对其原码逐位取反，但符号位除外。 原码10010= 反码11101
 * （10010，1为符号码，故为负） (11101) 二进制= -13 十进制
 * 3、补码表示法规定：正数的补码与其原码相同；负数的补码是在其反码的末位加1。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/29 11:10
 */
public class IntegerApiUsageExample {

    /**
     * Integer的toString方面的示例
     *
     * 计算机中负数的运算原理，计算机中的运算是通过二进制的补码形式进行的
     */
    public static void toStrings() {
        Integer i1 = -131;
        System.out.println(i1.toString()); //-131
        System.out.println(Integer.toString(i1)); //-131
        System.out.println(Integer.toString(i1, 2)); //-10000011
        //Java中正数的二进制表示形式与原码相同，负数的表示形式是其反码 + 1
        System.out.println(Integer.toBinaryString(i1)); //11111111111111111111111101111101
        System.out.println(Integer.toString(i1, 16)); //-83
        System.out.println(Integer.toHexString(i1)); //ffffff7d
        System.out.println(Integer.toUnsignedString(i1)); //4294967165
        System.out.println((long)Integer.MAX_VALUE - (long)Integer.MIN_VALUE + 1 - 131); //4294967165
        System.out.println(Integer.toUnsignedString(i1, 2)); //11111111111111111111111101111101
    }

    /**
     * Integer的parse方面的示例
     * Integer.parseXxx()方法基本上与Integer.toString系列相对应
     */
    public static void parses() {
        System.out.println(Integer.parseInt("-131")); //-131
        System.out.println(Integer.parseInt("-10000011", 2)); //-131
        System.out.println(Integer.parseInt("-83", 16)); //-131
        System.out.println(Integer.parseUnsignedInt("4294967165")); //-131
        System.out.println(Integer.parseUnsignedInt("11111111111111111111111101111101", 2)); //-131
        System.out.println(Integer.parseUnsignedInt("ffffff7d", 16)); //-131
    }

    /**
     * Integer的valueOf()方面的示例
     * Integer.valueOf(String)带String参数的其内部直接调用了parseInt(String)
     */
    public static void valueOfs() {
        System.out.println(Integer.valueOf(-131)); //首先走缓存，否则new Integer(int);
        System.out.println(Integer.valueOf("-131")); //-131
        System.out.println(Integer.valueOf("-10000011", 2)); //-131
    }

    /**
     * Integer的xxxValues()方面的示例
     */
    public static void xxxValues() {
        Integer i1 = -131;
        System.out.println(i1 & 0xff);
        System.out.println(i1.byteValue()); //125 <==> i1 << 24 >> 24，即先右移(32-8)24位，再左移(32-8)24位移回来
        System.out.println(i1.shortValue());
        System.out.println(i1.intValue());
        System.out.println(i1.longValue());
        System.out.println(i1.floatValue());
        System.out.println(i1.doubleValue());
    }

    /**
     * Integer的getInteger(String propertyName,[...])方面的示例
     * 即以int类型获取系统参数
     */
    public static void getIntegers() {
        System.out.println(Integer.getInteger("java.version")); //注意会吞NumberFormatException
        System.out.println(Integer.getInteger("os.version"));
        System.out.println(Integer.decode("0x7f"));;
    }

    public static void others() {
        System.out.println(Integer.reverse(123));
        System.out.println(Integer.signum(123));
    }

    public static void main(String[] args) {
        //toStrings();
        //parses();
        //valueOfs();
        //xxxValues();
        //getIntegers();
        others();
    }

}

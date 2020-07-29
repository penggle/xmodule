package com.penglecode.xmodule.master4j.java.basics.datatype;

/**
 * 自动装箱与缓存
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/29 9:18
 */
public class AutoBoxingCacheExample {

    /**
     * 确切地说，Java原始类型中除了浮点型(float, double)以外，在自动装箱时都有缓存效应，当然缓存是有界限的。具体缓存值范围是：
     *
     * - 整型：-128至127之间的整数，共256个数值。
     *
     * - 布尔型：true 和 false的布尔值，共true/false两个。
     *
     * - 字符型：‘\u0000’至 ‘\u007f’之间的字符，共128个字符。
     *
     * 当然这个范围也是可以指定的，通过-XX:AutoBoxCacheMax参数指定，默认为128
     */
    public static void main(String[] args) {
        Boolean bool1 = true;
        Boolean bool2 = true;
        System.out.println(String.format("【%s】%s == %s : %s", Boolean.class.getSimpleName(), bool1, bool2, bool1 == bool2));

        bool1 = false;
        bool2 = false;
        System.out.println(String.format("【%s】%s == %s : %s", Boolean.class.getSimpleName(), bool1, bool2, bool1 == bool2));

        Byte b1 = 1;
        Byte b2 = 1;
        System.out.println(String.format("【%s】%s == %s : %s", Byte.class.getSimpleName(), b1, b2, b1 == b2));

        b1 = 123;
        b2 = 123;
        System.out.println(String.format("【%s】%s == %s : %s", Byte.class.getSimpleName(), b1, b2, b1 == b2));

        Short s1 = 1;
        Short s2 = 1;
        System.out.println(String.format("【%s】%s == %s : %s", Short.class.getSimpleName(), s1, s2, s1 == s2));

        s1 = 127;
        s2 = 127;
        System.out.println(String.format("【%s】%s == %s : %s", Short.class.getSimpleName(), s1, s2, s1 == s2));

        s1 = 128;
        s2 = 128;
        System.out.println(String.format("【%s】%s == %s : %s", Short.class.getSimpleName(), s1, s2, s1 == s2));

        Integer i1 = 1;
        Integer i2 = 1;
        System.out.println(String.format("【%s】%s == %s : %s", Integer.class.getSimpleName(), i1, i2, i1 == i2));

        i1 = 124;
        i2 = 124;
        System.out.println(String.format("【%s】%s == %s : %s", Integer.class.getSimpleName(), i1, i2, i1 == i2));

        i1 = 138;
        i2 = 138;
        System.out.println(String.format("【%s】%s == %s : %s", Integer.class.getSimpleName(), i1, i2, i1 == i2));

        Long l1 = 1L;
        Long l2 = 1L;
        System.out.println(String.format("【%s】%s == %s : %s", Long.class.getSimpleName(), l1, l2, l1 == l2));

        l1 = 125L;
        l2 = 125L;
        System.out.println(String.format("【%s】%s == %s : %s", Long.class.getSimpleName(), l1, l2, l1 == l2));

        l1 = 139L;
        l2 = 139L;
        System.out.println(String.format("【%s】%s == %s : %s", Long.class.getSimpleName(), l1, l2, l1 == l2));

        Character c1 = '\u0001';
        Character c2 = '\u0001';
        System.out.println(String.format("【%s】%s == %s : %s", Character.class.getSimpleName(), c1, c2, c1 == c2));

        c1 = '\u007f'; //十进制127
        c2 = '\u007f'; //十进制127
        System.out.println(String.format("【%s】%s == %s : %s", Character.class.getSimpleName(), c1, c2, c1 == c2));

        c1 = '\u008f'; //十进制143
        c2 = '\u008f'; //十进制143
        System.out.println(String.format("【%s】%s == %s : %s", Character.class.getSimpleName(), c1, c2, c1 == c2));
    }

}

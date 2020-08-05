package com.penglecode.xmodule.master4j.java.lang.string;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * String常用API使用示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/31 10:17
 */
public class StringApiUsageExample {

    /**
     * String.codePointAt(int index)
     * 返回指定索引处的字符(Unicode代码点)
     */
    public static void codePointAt() {
        String str = "abc";
        int cp = str.codePointAt(1);
        System.out.println(cp);
    }

    /**
     * 在JDK5.0中,Java引入了对Unicode4.0中所谓"增补码"的支持,这种字符的长度大于2B,因此用char类型无法表示(Java中char类型占2B).
     * Java的设计者于是利用两个char变量空间来表示这样的一个字符。于是,假如字符串中有这样的字符,则length方法无法精确的表示字符长度 (length方法返回的是String中char的个数) ,
     * 于是有了codePointCount方法,利用这个方法可以精确统计出String中字符的数量,考虑到了String中可能含有的"增补码"
     *
     * 汉字 Unicode 编码范围表：https://www.qqxiuzi.cn/zh/hanzi-unicode-bianma.php
     *
     * char的范围是\u0000 ~ \uffff，也即0~65535
     */
    public static void codePointCount() {
        String str = "\uD841\uDC74\uD841\uDC75";
        System.out.println(str); //输出的是两个汉字，但是上面却出现4个Unicode码
        int length = str.length();
        System.out.println(length); //理论上length应该为2,但是实际是输出4，也就印证了上面的所述："length方法无法精确的表示字符长度"
        int cpcount = str.codePointCount(0, length); //输出2，得到正确的结果
        System.out.println(cpcount);
    }

    /**
     * 字符串比较，默认按照字符序列挨个比较char值的大小
     */
    public static void compareTo() {
        String[] langs = new String[] { "Java", "Go", "c", "C++", ".net", "python", "SQL" };
        Arrays.sort(langs, String::compareTo);
        System.out.println(Arrays.toString(langs));
    }

    /**
     * String.replace(CharSequence target, CharSequence replacement)方法是基于字面值的替换,
     * 而没有广义上的正则模式匹配，进入源代码可以看到：Pattern.compile(target.toString(), Pattern.LITERAL),
     * 使用的是Pattern.LITERAL(字面值)
     */
    public static void replace() {
        String str = "Hello Java. Java is a language.";
        System.out.println(str.replace("Java.", "c++"));//打印 Hello c++ Java is a language.
    }

    /**
     * replaceAll是真正基于正则表达式的
     */
    public static void replaceAll() {
        String str = "Hello Java. Java is a language.";
        System.out.println(str.replaceAll("Java.", "c++"));//打印 Hello c++ c++is a language.
    }

    public static void contentEquals() {
        String str = "hello world";
        StringBuilder sb = new StringBuilder().append("hello").append(" ").append("world");
        System.out.println(str.contentEquals(sb));
    }

    public static void split() {
        List<String> list = null;
        String str = "Hello Java.   Java is a language. ";

        String[] strs1 = str.split(" "); //匹配单个空格
        list = Arrays.stream(strs1).map(s -> "\"" + s + "\"").collect(Collectors.toList());
        System.out.println(list);

        String[] strs2 = str.split("\\s+"); //匹配多个空格
        list = Arrays.stream(strs2).map(s -> "\"" + s + "\"").collect(Collectors.toList());
        System.out.println(list);
    }

    /**
     * 拼接字符串
     */
    public static void join() {
        String[] strs1 = new String[] {"java", "lang", "String"};
        System.out.println(String.join(".", strs1));

        List<String> strs2 = Arrays.asList("java", "util", "List");
        System.out.println(String.join(".", strs2));
    }

    /**
     * 拼接字符串
     */
    public static void concat() {
        String str = "hello";
        str = str.concat(" ");
        str = str.concat("world");
        System.out.println(str);
    }

    /**
     * 字符串格式化
     */
    public static void format() {
        System.out.println(String.format("姓名：%s", "阿三"));
        System.out.println(String.format("性别：%c", '男'));
        System.out.println(String.format("年龄：%d", 28));
        System.out.println(String.format("体温：%.2f", 35.136));
        System.out.println(String.format("活力：%e", Math.PI * 100000000000L));
        System.out.println(String.format("日期：%tF", new Date()));
        System.out.println(String.format("时间：%tT", new Date()));
    }

    public static void reverseString() {
        String str = "java.lang.String0";

        char[] chars = str.toCharArray();
        int length = chars.length;
        for(int ascIndex = 0, descIndex = length - 1; ascIndex < descIndex; ascIndex++, descIndex--) {
            char temp = chars[ascIndex];
            chars[ascIndex] = chars[descIndex];
            chars[descIndex] = temp;
        }
        System.out.println(new String(chars));
    }

    public static void main(String[] args) {
        //replace();
        //replaceAll();
        //reverseString();
        //codePointCount();
        //compareTo();
        //split();
        //join();
        format();
    }

}

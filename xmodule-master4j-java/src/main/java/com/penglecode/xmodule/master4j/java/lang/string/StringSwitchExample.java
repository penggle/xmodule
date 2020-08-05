package com.penglecode.xmodule.master4j.java.lang.string;

/**
 * Java字符串类型String对switch语句的支持
 *
 * witch对String的支持是通过String.hashCode和equals两个方法来实现的。
 * 这一点可以通过jad反编译工具来反编译该class文件得知。
 *
 * 总结一下我们可以发现，其实switch只支持一种数据类型，那就是整型，例如像那些可以自动转换为int的byte,short,char等
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/31 10:05
 */
public class StringSwitchExample {

    /**
     * witch对String的支持是通过String.hashCode和equals两个方法来实现的。
     */
    public static void stringSwitch() {
        String str = "world";
        switch (str) {
            case "hello":
                System.out.println("hello");
                break;
            case "world":
                System.out.println("world");
                break;
            default:
                break;
        }
    }

    /**
     * witch对enum的支持是通过enum的ordinal()来实现的。
     */
    public static void enumSwith() {
        Language lan = Language.JAVA;
        switch (lan) {
            case PYTHON:
                System.out.println("python");
                break;
            case GO:
                System.out.println("go");
                break;
            default:
                System.out.println("java");
                break;
        }
    }

    public static enum Language {

        JAVA, GO, PYTHON;

    }

    public static void main(String[] args) {
        stringSwitch();
        enumSwith();
    }

}
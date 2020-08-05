package com.penglecode.xmodule.master4j.java.lang.string;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/31 17:00
 */
public class StingBuilderExample {

    public static void setLength() {
        StringBuilder sb = new StringBuilder();
        System.out.println(">>> length = " + sb.length() + ", capacity = " + sb.capacity() + ", value = " + sb.toString());
        sb.append("java.lang.String");
        System.out.println(">>> length = " + sb.length() + ", capacity = " + sb.capacity() + ", value = " + sb.toString());
        sb.setLength(5); //setLength方法仅仅是改变了StringBuilder.count，并没有改变StringBuilder.value
        System.out.println(">>> length = " + sb.length() + ", capacity = " + sb.capacity() + ", value = " + sb.toString());
        sb.setLength(10);
        System.out.println(">>> length = " + sb.length() + ", capacity = " + sb.capacity() + ", value = " + sb.toString());
    }

    public static void delete() {
        StringBuilder sb = new StringBuilder("java.lang.String");
        System.out.println(">>> length = " + sb.length() + ", capacity = " + sb.capacity() + ", value = " + sb.toString());
        sb.delete(5, 10);
        System.out.println(">>> length = " + sb.length() + ", capacity = " + sb.capacity() + ", value = " + sb.toString());
    }

    public static void main(String[] args) {
        //setLength();
        delete();
    }

}

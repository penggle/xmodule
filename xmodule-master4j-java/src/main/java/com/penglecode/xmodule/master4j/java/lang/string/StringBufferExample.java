package com.penglecode.xmodule.master4j.java.lang.string;

import com.penglecode.xmodule.common.util.ReflectionUtils;

import java.util.Arrays;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/31 14:24
 */
public class StringBufferExample {

    /**
     * 扩容示例
     */
    public static void ensureCapacity() {
        StringBuffer sb = new StringBuffer();
        trace(sb);

        sb.append("java");
        trace(sb);

        sb.append(".");
        trace(sb);

        sb.append("lang.").append("StringBuffer");
        trace(sb);
    }

    public static void setLength() {
        StringBuffer sb = new StringBuffer("java.lang.StringBuffer");
        int count = sb.length();
        trace(sb);

        sb.append(".append()");
        trace(sb);

        sb.setLength(count);
        trace(sb);

        sb.setLength(0); //逻辑清空
        trace(sb);

        sb.setLength(sb.capacity()); //物理清空
        trace(sb);
    }

    public static void delete() {
        StringBuffer sb = new StringBuffer("java.lang.StringBuffer");
        trace(sb);

        sb.append(".append()");
        trace(sb);

        sb.delete(0, sb.length());
        trace(sb);
    }

    private static void trace(StringBuffer sb) {
        char[] value = getValue(sb);
        int count = sb.length();
        int capacity = sb.capacity();
        System.out.println(String.format("{ count = %s, capacity = %s, value = %s }", count, capacity, Arrays.toString(value)));
    }

    private static char[] getValue(StringBuffer sb) {
        return ReflectionUtils.getFieldValue(sb, "value");
    }

    public static void main(String[] args) {
        //ensureCapacity();
        //setLength();
        delete();
    }

}

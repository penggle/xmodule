package com.penglecode.xmodule.master4j.java.nio.channel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Java字节
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/23 17:44
 */
public class JavaEndianExample {

    public static void main(String[] args) {
        System.out.println("平台默认的字节顺序：" + ByteOrder.nativeOrder());

        ByteBuffer buffer = ByteBuffer.allocate(4);
        ByteOrder byteOrder = buffer.order();
        System.out.println("Java默认的字节序列：" + byteOrder);
    }

}

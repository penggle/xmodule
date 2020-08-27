package com.penglecode.xmodule.master4j.java.nio.buffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/19 18:57
 */
public class CharBufferExample {

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void charsetEncode() throws IOException {
        String message = "jack, 你好吗?";
        ByteBuffer buffer = CHARSET.encode(message);
        System.out.println(message.length());
        inspectBuffer(buffer);
        //[106, 97, 99, 107, 44, 32, -28, -67, -96, -27, -91, -67, -27, -112, -105, 63, 0, 0, 0, 0, 0, 0, 0]
    }

    public static void putChar() throws IOException {
        String message = "jack, 你好吗?";
        System.out.println("-------------------buffer1----------------");
        CharBuffer buffer1 = CharBuffer.allocate(32);
        for(int i = 0; i < message.length(); i++) {
            buffer1.put(message.charAt(i));
        }
        buffer1.flip();
        inspectBuffer(CHARSET.encode(buffer1));
    }

    protected static void inspectBuffer(ByteBuffer buffer) {
        System.out.println(buffer); //此时position发生了变动
        byte[] datas = buffer.array(); //返回该ByteBuffer的底层字节数组
        System.out.println(Arrays.toString(datas));
    }

    protected static void inspectBuffer(CharBuffer buffer) {
        System.out.println(buffer); //此时position发生了变动
        char[] datas = buffer.array(); //返回该ByteBuffer的底层字节数组
        System.out.println(Arrays.toString(datas));
    }

    public static void main(String[] args) throws IOException {
        charsetEncode();
        //putChar();
    }

}

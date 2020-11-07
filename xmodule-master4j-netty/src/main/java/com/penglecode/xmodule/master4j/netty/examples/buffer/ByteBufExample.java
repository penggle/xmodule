package com.penglecode.xmodule.master4j.netty.examples.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ByteBuf基本使用
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/29 21:21
 */
public class ByteBufExample {

    protected static void printBuffer(ByteBuf buffer) {
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("buffer", buffer);
        map.put("isDirect", buffer.isDirect());
        map.put("hasMemoryAddress", buffer.hasMemoryAddress());
        map.put("hasArray", buffer.hasArray());
        map.put("readable", buffer.isReadable());
        map.put("readableBytes", buffer.readableBytes());
        map.put("readerIndex", buffer.readerIndex());
        map.put("writable", buffer.isWritable());
        map.put("writableBytes", buffer.writableBytes());
        map.put("writerIndex", buffer.writerIndex());
        map.put("capacity", buffer.capacity());
        System.out.println(">>> " + map);
    }

    public static void headByteBuf() {
        byte[] bytes = "Netty In Action".getBytes();
        ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
        printBuffer(buffer);
        int readableBytes = buffer.readableBytes();
        for(int i = 0; i < readableBytes; i++) {
            byte b = buffer.readByte(); //读取一个字节，readerIndex递增1
            System.out.println(String.format("【%s】b = %s, readerIndex = %s, writerIndex = %s",
                    i, (char) b, buffer.readerIndex(), buffer.writerIndex()));
        }
        if(buffer.hasArray()) {
            bytes = buffer.array();
            System.out.println(new String(bytes)); //Netty In Action
        }
    }

    public static void directByteBuf() {
        byte[] bytes = "Netty In Action".getBytes();
        ByteBuf buffer = Unpooled.directBuffer(1024);
        printBuffer(buffer);
        buffer.writeBytes(bytes);
        printBuffer(buffer);
        while(buffer.isReadable()) {
            byte b = buffer.readByte(); //读取一个字节，readerIndex递增1
            System.out.println(String.format("b = %s, readerIndex = %s, writerIndex = %s",
                    (char) b, buffer.readerIndex(), buffer.writerIndex()));
        }
        printBuffer(buffer);
        buffer.clear();
        printBuffer(buffer);
    }

    public static void findByteBuf() {
        byte[] bytes = "Netty In Action".getBytes();
        ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
        printBuffer(buffer);
        int spaceIndex = buffer.forEachByte(ByteProcessor.FIND_ASCII_SPACE);
        System.out.println(spaceIndex);
        printBuffer(buffer);

        spaceIndex = 0;
        while(true) {
            //查询所有空格符的位置
            spaceIndex = buffer.forEachByte(spaceIndex, buffer.readableBytes() - spaceIndex, ByteProcessor.FIND_ASCII_SPACE);
            if(spaceIndex == -1) {
                break;
            }
            System.out.println(spaceIndex++); //++是为了前进一个字符，否则就会在某个空格符处无限循环
        }
    }

    public static void copiedBuffer() {
        Charset charset = StandardCharsets.UTF_8;
        ByteBuf buffer = Unpooled.copiedBuffer("Netty in Action rocks!", charset);
        printBuffer(buffer);
        ByteBuf sliced = buffer.slice(0, 14);
        System.out.println(sliced.toString(charset));
        buffer.setByte(0, (byte) 'J'); //buffer和sliced底层共享同一个array,所以设置buffer的字节序列会影响到sliced
        System.out.println(buffer.getByte(0) == sliced.getByte(0)); //true
    }

    public static void main(String[] args) throws Exception {
        //headByteBuf();
        //directByteBuf();
        //findByteBuf();
        copiedBuffer();
    }

}

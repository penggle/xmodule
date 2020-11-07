package com.penglecode.xmodule.master4j.netty.examples.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;

/**
 * Netty的Zero-copy与传统意义的zero-copy不太一样。传统的zero-copy是IO传输过程中，
 * 数据无需中内核态到用户态、用户态到内核态的数据拷贝，减少拷贝次数。而Netty的zero-copy则是完全在用户态，
 * 或者说传输层的zero-copy机制，可以参考下图。由于协议传输过程中，通常会有拆包、合并包的过程，一般的做法就是System.arrayCopy了，
 * 但是Netty通过ByteBuf.slice以及Unpooled.wrappedBuffer等方法拆分、合并Buffer无需拷贝数据。
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/29 21:01
 */
public class ZeroCopyExample {

    public static void zeroCopy1() {
        byte[] b1 = "java.".getBytes();
        byte[] b2 = "lang.".getBytes();
        byte[] b3 = "String".getBytes();
        ByteBuf buffer1 = Unpooled.wrappedBuffer(b1);
        System.out.println(">>> buffer1 = " + buffer1);
        ByteBuf buffer2 = Unpooled.wrappedBuffer(b2);
        System.out.println(">>> buffer2 = " + buffer2);
        ByteBuf buffer3 = Unpooled.wrappedBuffer(b3);
        System.out.println(">>> buffer3 = " + buffer3);
        ByteBuf compositeBuffer = Unpooled.wrappedBuffer(buffer1, buffer2, buffer3);
        System.out.println(">>> compositeBuffer = " + compositeBuffer + ", hasArray = " + compositeBuffer.hasArray());
        int readableBytes = compositeBuffer.readableBytes();
    }

    public static void main(String[] args) {
        zeroCopy1();
    }

}

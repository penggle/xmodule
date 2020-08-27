package com.penglecode.xmodule.master4j.java.nio.buffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static java.nio.file.StandardOpenOption.*;

/**
 * ByteBuffer示例
 *
 * 写数据到Buffer有两种方式：
 *  1、从Channel写到Buffer。
 *  2、通过Buffer的put()方法写到Buffer里。
 *
 * flip()方法将Buffer从写模式切换到读模式。调用flip()方法会将position设回0，并将limit设置成之前position的值。
 *
 * rewind()方法将position设回0，所以你可以重读Buffer中的所有数据。limit保持不变，仍然表示能从Buffer中读取多少个元素（byte、char等）。
 *
 * clear()与compact()方法
 * 一旦读完Buffer中的数据，需要让Buffer准备好再次被写入。可以通过clear()或compact()方法来完成。
 *
 * 如果调用的是clear()方法，position将被设回0，limit被设置成 capacity的值。换句话说，Buffer 被清空了。Buffer中的数据并未清除，只是这些标记告诉我们可以从哪里开始往Buffer里写数据。
 *
 * 如果Buffer中有一些未读的数据，调用clear()方法，数据将“被遗忘”，意味着不再有任何标记会告诉你哪些数据被读过，哪些还没有。
 *
 * 如果Buffer中仍有未读的数据，且后续还需要这些数据，但是此时想要先先写些数据，那么使用compact()方法。
 *
 * compact()方法将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。limit属性依然像clear()方法一样，设置成capacity。现在Buffer准备好写数据了，但是不会覆盖未读的数据。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/18 21:12
 */
public class ByteBufferExample {

    public static void putAndInspect1() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(32);

        inspectBuffer(buffer);

        System.out.println("------------向buffer中放入一个长整型数字----------------------------------------------------");

        buffer.putLong(System.currentTimeMillis()); //向buffer中放入一个long型数据

        inspectBuffer(buffer);

        System.out.println("------------向buffer中继续放入字符数据----------------------------------------------------");

        String words = "hello, this is a example";

        for(int i = 0; i < words.length(); i++) {
            /**
             * 如果没有这一句作为保护，那么当buffer没有空间了(position等于capacity时)
             * 就会抛出java.nio.BufferOverflowException
             */
            if(buffer.hasRemaining()) {
                char c = words.charAt(i);
                buffer.putChar(c); //向buffer中继续放入字符数据
                //System.out.println("put into buffer: words[" + i + "] = " + c);
            }
        }

        inspectBuffer(buffer);

        System.out.println("------------将buffer中的数据写入到通道中去(1)----------------------------------------------------");

        try(FileChannel fileChannel = FileChannel.open(Paths.get("d:/bytebuffer1.data"), READ, WRITE, CREATE)) {
            /**
             * 必须调用flip()方法，用于反转缓冲区，
             * 实际就是将limit调整为当前的position大小，然后将position置为0，你可以将position理解为游标
             * 通常调用flip()方法即意味着接下来要将buffer中的数据排干(写入到某通道中去)
             */
            buffer.flip();
            System.out.println("------------调用ByteBuffer.flip()方法之后----------------------------------------------------");
            inspectBuffer(buffer); //此时观察到position变为0了
            fileChannel.write(buffer);
            System.out.println("------------调用FileChannel.write()方法之后----------------------------------------------------");
            inspectBuffer(buffer);
            System.out.println("------------调用ByteBuffer.clear()方法之后----------------------------------------------------");
            /**
             * 调用clear()方法仅仅是将position置为0，limit置为容量
             * 不会清空底层的数据，不信的话请看下面输出
             */
            buffer.clear();
            inspectBuffer(buffer);
        }

        //休眠30秒，去看看bytebuffer.data的内容，与下面的再次写入做个对比
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(30));

        System.out.println("------------修改buffer的前8个字节的内容----------------------------------------------------");
        buffer.putChar('j');
        buffer.putChar('a');
        buffer.putChar('v');
        buffer.putChar('a');
        inspectBuffer(buffer);

        System.out.println("------------将buffer中的数据写入到通道中去(2)----------------------------------------------------");

        try(FileChannel fileChannel = FileChannel.open(Paths.get("d:/bytebuffer1.data"), READ, WRITE, CREATE)) {
            /**
             * 必须调用flip()方法，用于反转缓冲区，
             * 实际就是将limit调整为当前的position大小，然后将position置为0，你可以将position理解为游标
             * 通常调用flip()方法即意味着接下来要将buffer中的数据排干(写入到某通道中去)
             */
            buffer.flip();
            System.out.println("------------调用ByteBuffer.flip()方法之后----------------------------------------------------");
            inspectBuffer(buffer); //此时观察到position变为0了,limit变为8
            /**
             * 此处的write仅仅是将buffer中的前8个字节写到通道中，
             * 所以ByteBuffer与StringBuffer等在设计上有点相似，
             * 即清除操作并没有把底层数组中的数据给清除掉
             */
            fileChannel.write(buffer);
            System.out.println("------------调用FileChannel.write()方法之后----------------------------------------------------");
            inspectBuffer(buffer);
            System.out.println("------------调用ByteBuffer.clear()方法之后----------------------------------------------------");
            /**
             * 调用clear()方法仅仅是将position置为0，limit置为容量
             * 不会清空底层的数据，不信的话请看下面输出
             */
            buffer.clear();
            inspectBuffer(buffer);
        }
    }

    protected static void inspectBuffer(ByteBuffer buffer) {
        System.out.println(buffer); //此时position发生了变动
        byte[] datas = buffer.array(); //返回该ByteBuffer的底层字节数组
        System.out.println(Arrays.toString(datas));
    }

    /**
     * ByteBuffer的大端存储与小端存储问题
     */
    public static void byteBufferEndian() throws IOException {
        //获取本地操作系统默认存储顺序，本机是windows，默认是LITTLE_ENDIAN
        System.out.println(ByteOrder.nativeOrder()); //LITTLE_ENDIAN
        String message = "hello world!";
        ByteBuffer buffer = ByteBuffer.allocate(64);
        //ByteBuffer默认的存储顺序是大端存储：BIG_ENDIAN
        System.out.println(buffer.order()); //BIG_ENDIAN

        /**
         * 将buffer的ByteOrder顺序调整与操作系统一致，
         * 否则在调用putChar()时会出现因为字节序列对齐方式不一致而出现的NUL字符问题
         */
        buffer.order(ByteOrder.nativeOrder()); //注释掉该句,bytebuffer2.data文件中会出现NUL字符(用notepad++打开查看)
        for(int i = 0; i < message.length(); i++) {
            buffer.putChar(message.charAt(i));
        }

        //buffer.put(message.getBytes()); //如果上面不设置buffer.order(..)，而是使用该句则结果也不会有问题

        try(FileChannel fileChannel = FileChannel.open(Paths.get("d:/bytebuffer2.data"), READ, WRITE, CREATE)) {
            buffer.flip();
            fileChannel.write(buffer);
        }
    }

    public static void compact() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.put("hello, girl".getBytes());
        inspectBuffer(buffer);

        //将position游标指向单词girl的首位'g'上，然后调用compact()方法进行剪切压缩
        buffer.position(7);
        buffer.compact(); //丢掉前面7个字节，而且是真实的反映在底层字节数组上的!
        inspectBuffer(buffer);
    }


    public static void main(String[] args) throws IOException {
        //putAndInspect1();
        //byteBufferEndian();
        compact();
    }

}

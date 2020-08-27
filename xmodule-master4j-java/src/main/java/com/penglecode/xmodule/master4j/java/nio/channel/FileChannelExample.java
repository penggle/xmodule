package com.penglecode.xmodule.master4j.java.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Properties;

/**
 * Java NIO的通道类似流，但又有些不同：
 *  1、既可以从通道中读取数据，又可以写数据到通道。但流的读写通常是单向的。
 *  2、通道可以异步地读写。
 *  3、通道中的数据总是要先读到一个Buffer，或者总是要从一个Buffer中写入。
 *
 * Channel的实现
 * 这些是Java NIO中最重要的通道的实现：
 *
 *  1、FileChannel ：从文件中读写数据。
 *  2、DatagramChannel ：能通过UDP读写网络中的数据。
 *  3、SocketChannel ：能通过TCP读写网络中的数据。
 *  4、ServerSocketChannel ：可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/18 17:39
 */
public class FileChannelExample {

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void write1() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile("d:/system.properties", "rw")) {
            FileChannel fileChannel = file.getChannel();
            Properties properties = System.getProperties();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            String line;
            for(Map.Entry<Object,Object> entry : properties.entrySet()) {
                line = entry.getKey().toString() + "=" + entry.getValue() + LINE_SEPARATOR;
                byte[] bytes = line.getBytes(CHARSET);
                buffer.clear(); //先清理buffer

                for(int i = 0; i < bytes.length; i++) {
                    if(buffer.hasRemaining()) { //当前position与limit之间是否还有空间?
                        buffer.put(bytes[i]); //读入一个字节
                    } else { //遇到line为很长的长行导致没空间了?(bytes.length > buffer.capacity)
                        buffer.flip(); //反转此缓冲区，为将此缓冲区中的数据写入通道之中做准备
                        //此处是多次append追加模式的写入，所以一定要调用FileChannel.write(ByteBuffer src, long position)方法
                        fileChannel.write(buffer); //写缓冲区中的数据进入通道的当前位置
                        buffer.clear(); //清除缓冲区，为bytes中剩余数据的继续读做准备
                    }
                }
                //缓冲区中还有数据未被排干?
                if(buffer.position() > 0) {
                    buffer.flip(); //反转此缓冲区，为将此缓冲区中的数据写入通道之中做准备
                    fileChannel.write(buffer); //写缓冲区中的数据进入通道的当前位置
                    buffer.clear(); //清除缓冲区，为bytes中剩余数据的继续读做准备
                }
            }
            fileChannel.close();
        }
    }

    public static void write2() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile("d:/system.properties", "rw")) {
            FileChannel fileChannel = file.getChannel();
            Properties properties = System.getProperties();
            String line;
            for(Map.Entry<Object,Object> entry : properties.entrySet()) {
                line = entry.getKey().toString() + "=" + entry.getValue() + LINE_SEPARATOR;
                fileChannel.write(ByteBuffer.wrap(line.getBytes(CHARSET)));
            }
            fileChannel.close();
        }
    }

    public static void channelCopy() throws IOException {
        long start = System.currentTimeMillis();
        String srcFilePath = "d:/SOFTWARE/huangshan.zip";
        String destFilePath = "e:/huangshan.zip";
        try(FileInputStream inputStream = new FileInputStream(srcFilePath);
            FileOutputStream outputStream = new FileOutputStream(destFilePath);
            FileChannel inChannel = inputStream.getChannel();
            FileChannel outChannel = outputStream.getChannel()) {
            /**
             * 此例中目标文件大小2.6GB
             * inputStream.available()与inChannel.size()输出的值不一样(后者返回值是正确的)，
             * 前者返回的是int，后者返回的是long，在此例中由于文件太大，前者的返回值已经溢出了达到了Integer.MAX_VALUE上限了
             */
            System.out.println("inputStream.available() = " + inputStream.available() + ", inChannel.size() = " + inChannel.size());
            long totalSize = inChannel.size();
            /**
             * Linux中无需担心最大传输上限问题,而windows单次最大传输上限为Integer.MAX_VALUE - 1，即batchSize < Integer.MAX_VALUE
             * 网上说windows真正启用零拷贝，存在最大阈值，但是经多方查资料不管怎么设都是提高不了速度
             */
            long batchSize = (8 * 1024 * 1024);
            long position = 0;
            while(position < totalSize) {
                position += inChannel.transferTo(position, batchSize, outChannel);
            }
            System.out.println(position);
        }
        long end = System.currentTimeMillis();
        System.out.println("使用FileChannel.transferXxx(..)的零拷贝技术来拷贝文件，cost = " + (end - start) + "ms");
    }

    /**
     * 2.6GB,用时平均在16000毫秒左右
     */
    public static void normalCopy() throws IOException {
        long start = System.currentTimeMillis();
        String srcFilePath = "d:/SOFTWARE/huangshan.zip";
        String destFilePath = "e:/huangshan.zip";

        try(FileInputStream in = new FileInputStream(srcFilePath);
            FileOutputStream out = new FileOutputStream(destFilePath)) {
            byte[] buffer = new byte[5 * 1024 * 1024];
            while(in.read(buffer) != -1) {
                out.write(buffer);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("使用正常文件拷贝技术来拷贝文件，cost = " + (end - start) + "ms");
    }


    public static void main(String[] args) throws IOException {
        //write1();
        //write2();
        //normalCopy();
        channelCopy();
    }

}

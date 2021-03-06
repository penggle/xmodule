package com.penglecode.xmodule.master4j.java.io.chars;

import java.io.*;

/**
 * 基于字符流的终端输出流示例
 * 与之对应的还有基于字节流的终端输出流
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/20 14:37
 */
public class PrintWriterExample {

    /**
     * 获取标准系统输出流
     * @return
     */
    protected static OutputStream getStandardSystemOut(boolean cached) throws UnsupportedEncodingException {
        if(cached) {
            return System.out;
        }
        //以下代码具体见java.lang.System
        String enc = System.getProperty("sun.stdout.encoding", "UTF-8");
        return new PrintStream(new BufferedOutputStream(new FileOutputStream(FileDescriptor.out), 128), true, enc);
    }

    public static void main(String[] args) {
        try (PrintWriter writer = new PrintWriter(getStandardSystemOut(false))) {
            writer.println("hello world!");
            writer.println("现在时间是：");
            writer.println(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

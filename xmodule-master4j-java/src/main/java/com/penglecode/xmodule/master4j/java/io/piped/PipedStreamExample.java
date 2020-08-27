package com.penglecode.xmodule.master4j.java.io.piped;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Java IO中的管道流(PipedOutputStream和PipedInputStream)为同一个JVM中不同线程之间进行IO通信提供的一种流
 *
 * 可以通过Java IO中的PipedOutputStream和PipedInputStream创建管道。一个PipedInputStream流应该和一个PipedOutputStream流相关联。
 * 一个线程通过PipedOutputStream写入的数据可以被另一个线程通过相关联的PipedInputStream读取出来。
 *
 * http://ifeve.com/java-io-%e7%ae%a1%e9%81%93/
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/17 11:11
 */
public class PipedStreamExample {

    public static void main(String[] args) throws IOException {
        final PipedOutputStream output = new PipedOutputStream();
        final PipedInputStream input = new PipedInputStream();
        input.connect(output); //也可以这样建立绑定关系：input = new PipedInputStream(output);

        Thread thread1 = new Thread(() -> {
            try {
                String message = "Hello world, piped!";
                output.write(message.getBytes());
                System.out.println("【写管道流】>>> " + message);
                output.close(); //记得关闭流，否则报错
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                StringBuilder message = new StringBuilder();
                int c, i = 0;
                while((c = input.read()) != -1) {
                    message.append((char) c);
                }
                System.out.println("【读管道流】<<< " + message.toString());
                input.close(); //记得关闭流，否则报错
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();
    }

}

package com.penglecode.xmodule.master4j.java.io.clientserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 参照最简单的Reactor线程模型的TimeClient实现
 *
 * http://www.tianshouzhi.com/api/tutorials/netty/222
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/20 14:32
 */
public class TimeClient {

    public static void main(String[] args) {
        try (Socket client = new Socket("127.0.0.1", 2345);
             BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter writer = new PrintWriter(client.getOutputStream())) {
            while (true){//每隔5秒发送一次请求
                writer.println("GET CURRENT TIME");
                writer.flush();
                String response = reader.readLine(); //readLine()会阻塞直到有数据可读
                System.out.println("Current Time:"+response);
                LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(5000));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

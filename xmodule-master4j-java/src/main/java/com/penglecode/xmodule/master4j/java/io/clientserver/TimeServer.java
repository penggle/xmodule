package com.penglecode.xmodule.master4j.java.io.clientserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

/**
 * 参照最简单的Reactor线程模型的TimeServer实现
 *
 * http://www.tianshouzhi.com/api/tutorials/netty/222
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/20 14:17
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 2345;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("【" + Thread.currentThread().getName() + "】TimeServer started on " + port + "...");
            while (true) {
                Socket client = serverSocket.accept(); //accept()方法将会阻塞，直到有客户端连接到Server
                //每次接收到一个新的客户端连接，启动一个新的线程来处理
                new Thread(new TimeServerHandler(client)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static class TimeServerHandler implements Runnable {

        private final Socket client;

        public TimeServerHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            System.out.println("【" + Thread.currentThread().getName() + "】New Client(" + client + ") connected to the server...");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 PrintWriter writer = new PrintWriter(client.getOutputStream())) {
                ;
                while (true) { //因为一个client可以发送多次请求，这里的每一次循环，相当于接收处理一次请求
                    String command = reader.readLine(); //readLine()阻塞直到有数据可以读取
                    if (!"GET CURRENT TIME".equals(command)) {
                        writer.println("BAD_REQUEST");
                    } else {
                        writer.println(LocalDateTime.now().toString());
                    }
                    writer.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}

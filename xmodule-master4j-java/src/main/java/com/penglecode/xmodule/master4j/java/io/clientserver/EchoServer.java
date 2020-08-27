package com.penglecode.xmodule.master4j.java.io.clientserver;

import org.springframework.util.Assert;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Socket的简单echo示例之服务端
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/16 22:05
 */
public class EchoServer extends Thread {

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private final ServerSocket serverSocket;

    public EchoServer(int port) throws IOException {
        Assert.isTrue(port > 0, "EchoServer port must be > 0");
        this.serverSocket = new ServerSocket(port);
        this.serverSocket.setReuseAddress(true);
        System.out.println("【EchoServer】Echo 服务器启动了...");
    }

    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            try (Socket clientSocket = serverSocket.accept()) { //accept()方法将会阻塞，直到有客户端连接到Server
                SocketAddress clientSocketAddr = clientSocket.getRemoteSocketAddress();
                BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), CHARSET));
                BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), CHARSET));
                String line;
                while((line = clientReader.readLine()) != null) {
                    System.out.println("【EchoServer】收到来自远程客户端(" + clientSocketAddr + ")的Echo消息：" + line);
                    //回写消息给客户端
                    clientWriter.write(line + LINE_SEPARATOR);
                    clientWriter.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new EchoServer(1234).start();
    }

}

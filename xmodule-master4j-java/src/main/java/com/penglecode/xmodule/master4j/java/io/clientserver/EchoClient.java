package com.penglecode.xmodule.master4j.java.io.clientserver;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Socket的简单echo示例之客户端
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/16 22:39
 */
public class EchoClient extends Thread {

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private final Socket clientSocket;

    private volatile BufferedReader clientReader;

    private volatile BufferedWriter clientWriter;

    public EchoClient(String serverHost, int serverPort) throws IOException {
        this.clientSocket = new Socket(serverHost, serverPort);
        this.clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), CHARSET));
        this.clientWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), CHARSET));
    }

    /**
     * 子线程负责接收Echo消息
     */
    @Override
    public void run() {
        while(clientSocket.isConnected()) {
            try {
                SocketAddress serverAddr = clientSocket.getRemoteSocketAddress();
                String line;
                while((line = clientReader.readLine()) != null) {
                    System.out.println("【EchoClient】收到来自服务器(" + serverAddr + ")的Echo消息：" + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 主线程负责发送Echo消息
     */
    public void service() throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while((line = in.readLine()) != null) {
                clientWriter.write(line + LINE_SEPARATOR);
                clientWriter.flush();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        EchoClient echoClient = new EchoClient("127.0.0.1", 1234);
        echoClient.start(); //子线程负责接收Echo消息
        echoClient.service(); //主线程负责发送Echo消息
    }

}

package com.penglecode.xmodule.master4j.java.nio.clientserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 非阻塞模式的Echo Client
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/19 14:43
 */
public class EchoClient extends EchoEndpoint {

    private final SocketChannel socketChannel;

    private final Selector selector;

    private final InetSocketAddress serverAddress;

    //读取来自服务端的数据buffer
    private final ByteBuffer readBuffer;

    public EchoClient(String serverHost, int serverPort) throws IOException {
        this.serverAddress = new InetSocketAddress(serverHost, serverPort);
        this.readBuffer = ByteBuffer.allocate(getBufferSize());
        this.selector = Selector.open();
        this.socketChannel = SocketChannel.open();
    }

    protected void initChannels() throws IOException {
        this.socketChannel.configureBlocking(false);
        this.socketChannel.register(this.selector, SelectionKey.OP_CONNECT);
        this.socketChannel.connect(serverAddress);
    }

    @Override
    public void run() {
        try {
            initChannels();
            while(true) {
                if(selector.select() > 0) {
                    Set<SelectionKey> readyKeys = selector.selectedKeys();
                    for(Iterator<SelectionKey> keyIteratort = readyKeys.iterator(); keyIteratort.hasNext();) {
                        SelectionKey key = keyIteratort.next();
                        try {
                            if (key.isConnectable()) {
                                handleClientConnected(key);
                            } else if (key.isReadable()) {
                                handleClientRead(key);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            key.channel().close(); //IO异常时强制断开连接
                        } finally {
                            keyIteratort.remove();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void handleClientConnected(SelectionKey key) throws IOException {
        while(!socketChannel.finishConnect()) {
            LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(100));
        }
        System.out.println("【EchoClient】>>> EchoClient启动了, 监听端口：" + ((InetSocketAddress) this.socketChannel.getLocalAddress()).getPort());
        System.out.println("【EchoClient】>>> 客户端(" + socketChannel + ")已连接到远程服务器...");
        //请不要注册SelectionKey.OP_WRITE，否则selector.select()基本上不会阻塞,会引起CPU自旋!!!
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    protected void handleClientRead(SelectionKey key) throws IOException {
        StringBuilder sb = new StringBuilder();
        char c;
        /**
         * 此处逻辑跟EchoServer相似，基本一样
         */
        outerLoop:
        while(socketChannel.read(readBuffer) != -1) {
            if(readBuffer.hasRemaining()) {
                readBuffer.flip();
                CharBuffer charBuffer = getCharset().decode(readBuffer);
                while(charBuffer.hasRemaining()) {
                    c = charBuffer.get();
                    sb.append(c);
                    if(c == '\r') {
                        continue;
                    }
                    //解析出一个整行即终止，剩余不完整的留到下一波解析
                    if(c == '\n') {
                        charBuffer.flip();
                        ByteBuffer lineBuffer = getCharset().encode(charBuffer);
                        readBuffer.position(lineBuffer.limit());
                        readBuffer.compact(); //当前\r\n后面的数据整体移到最前面去
                        break outerLoop;
                    }
                }
            }
        }
        String message = sb.toString(); //注意message结尾包含了\r\n
        System.out.print("【EchoClient】>>> 收到来自服务端(" + socketChannel + ")的Echo消息：" + message);
        //到这里一个echo流程走完
    }

    protected void service() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in, getCharset()));
            String message;
            while((message = br.readLine()) != null) {
                socketChannel.write(getCharset().encode(message + getLineSeparator()));
                if("bye".equals(message)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        EchoClient client = new EchoClient("127.0.0.1", 9090);
        client.start();
        client.service();
    }

}

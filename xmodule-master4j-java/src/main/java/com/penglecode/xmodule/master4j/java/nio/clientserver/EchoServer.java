package com.penglecode.xmodule.master4j.java.nio.clientserver;

import org.springframework.util.Assert;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 非阻塞模式的Echo Server
 *
 * 注意事项：
 * 1、不管是Server端还是Client端都不要注册SelectionKey.OP_WRITE事件，如果注册了肯定会导致CPU自旋
 * 2、Server端如果想要广播或组播消息的话，请定义一个Map<SocketChannel,Group> allAcceptedClients全局变量来实现
 * 3、不管是Server端还是Client端,在读取消息时都得考虑消息的不确定性，即：消息可能不是完整的，可能这波消息后面还有下波消息的部分。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/17 10:18
 */
public class EchoServer extends EchoEndpoint {

    private final ServerSocketChannel serverSocketChannel;

    private final Selector selector;

    private final int port;

    public EchoServer(int port) throws IOException {
        Assert.isTrue(port > 0, "EchoServer port must be > 0");
        this.port = port;
        this.serverSocketChannel = ServerSocketChannel.open();
        this.selector = Selector.open();
    }

    protected void initChannels() throws IOException {
        //设置通道为非阻塞模式
        this.serverSocketChannel.configureBlocking(false);
        //重启时重用相同端口
        this.serverSocketChannel.socket().setReuseAddress(true);
        //设置ServerSocket监听的端口
        this.serverSocketChannel.socket().bind(new InetSocketAddress(port));
        this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        System.out.println("【EchoServer】>>> EchoServer启动了，监听端口：" + port);
    }

    @Override
    public void run() {
        try {
            initChannels();
            while (true) {
                if (selector.select() > 0) { //有通道准备就绪了?
                    Set<SelectionKey> readyKeys = selector.selectedKeys();
                    for(Iterator<SelectionKey> keyIterator = readyKeys.iterator(); keyIterator.hasNext();) {
                        SelectionKey key = keyIterator.next();
                        try {
                            if(key.isAcceptable()) { // a connection was accepted by a ServerSocketChannel.
                                handleClientConnected(key);
                            } else if (key.isReadable()) { //a channel is ready for reading
                                handleClientRead(key);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            key.channel().close(); //IO异常时强制断开连接
                        } finally {
                            keyIterator.remove(); //删除已选的key,以防重复处理
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void handleClientConnected(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept(); //接受客户端的请求
        System.out.println("【EchoServer】>>> 收到来在客户端(" + clientChannel + ")的连接...");
        clientChannel.configureBlocking(false); //指定为非阻塞模式
        //在首次收到客户端的连接之后立即注册对该客户端通道的读写事件
        //请不要注册SelectionKey.OP_WRITE，否则selector.select()基本上不会阻塞,会引起CPU自旋!!!
        clientChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(getBufferSize()));
    }

    protected void handleClientRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        StringBuilder sb = new StringBuilder();
        ByteBuffer readBuffer = (ByteBuffer) key.attachment();
        char c;
        /**
         * 此处的读操作值得注意：它只能保证消息的顺序性，无法保证消息是否被部分发送
         * 即clientChannel.read(readBuffer)之后可能出现：
         *      客户端发送两拨数据：1、hello\r\n
         *                      2、how are you\r\n
         *      服务端read(readBuffer)之后,readBuffer中的数据可能是：[h, e, l, l, o, \r, \n, h, o, w]
         *
         * 所以下面要使用compact()将实际上未处理的数据移进行压缩待到下一次再进行处理
         */
        //网上说下面这句channel.read()是非阻塞的，简直扯淡，你要明白I/O的两段(1、准备数据和2、操作数据)过程中，
        //上面select()通知到你数据已经准备好了，现在的read()就是操作数据，除非是真正的异步IO，否则第2步是铁定阻塞的
        outerLoop:
        while(clientChannel.read(readBuffer) != -1) {
            //System.out.println("spining"); //从此处可以看出，到底上面read()是不是阻塞的，如果是非阻塞的那么必定CPU自旋
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
        System.out.print("【EchoServer】>>> 收到来在客户端(" + clientChannel + ")的消息：" + message);
        clientChannel.write(getCharset().encode(message)); //回发给客户端
    }


    public static void main(String[] args) throws IOException {
        new EchoServer(9090).start();
    }

}

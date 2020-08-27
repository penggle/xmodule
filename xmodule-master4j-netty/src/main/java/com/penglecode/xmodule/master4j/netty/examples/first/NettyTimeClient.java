package com.penglecode.xmodule.master4j.netty.examples.first;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 简单的基于netty的时间客户端
 *
 * http://www.tianshouzhi.com/api/tutorials/netty/390
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/20 23:29
 */
public class NettyTimeClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyTimeClient.class);

    private final EventLoopGroup workerGroup;

    private final Bootstrap bootstrap;

    private final InetSocketAddress serverAddress;

    public NettyTimeClient(int serverPort) {
        this("127.0.0.1", serverPort);
    }

    public NettyTimeClient(String serverHost, int serverPort) {
        this.serverAddress = new InetSocketAddress(serverHost, serverPort);
        //客户端的工作线程池
        this.workerGroup = new NioEventLoopGroup();
        //与ServerBootstrap相对应，这表示一个客户端的启动类
        this.bootstrap = new Bootstrap();
    }

    public void start() {
        try {
            /**
             * 前面提到，EventLoopGroup的作用是线程池。前面在创建ServerBootstrap时，
             * 设置了一个bossGroup，一个wrokerGroup，这样做主要是为将接受连接和处理连接请求任务划分开，
             * 以提升效率。对于客户端而言，则没有这种需求，只需要设置一个EventLoopGroup实例即可
             */
            bootstrap.group(workerGroup)
                    //通过channel方法指定了NioSocketChannel，这是netty在nio编程中用于表示客户端的对象实例。
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            //LineBasedFrameDecoder在解析客户端请求时，遇到字符”\n”或”\r\n”时则认为是一个完整的请求报文，
                            //然后将这个请求报文的二进制字节流交给StringDecoder处理。
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            //StringDecoder将字节流转换成一个字符串，交给TimeServerHandler来进行处理。
                            ch.pipeline().addLast(new StringDecoder());
                            //TimeClientHandler是我们自己编写的给服务端发送请求，并接受服务端响应的处理器类。
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            //客户端连接到远程服务器
            ChannelFuture f = bootstrap.connect(serverAddress).sync();
            //等待客户端所持有的的SocketChannel被关闭，start()方法运行结束，否则hang在此处
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyTimeClient(1212).start();
    }

}
package com.penglecode.xmodule.master4j.netty.examples.first;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * 简单的基于netty的时间服务器
 *
 * http://www.tianshouzhi.com/api/tutorials/netty/390
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/20 21:25
 */
public class NettyTimeServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyTimeServer.class);

    /**
     * 用于接受客户端连接的线程池
     */
    private final EventLoopGroup bossGroup;

    /**
     * 处理I/O事件的线程池
     */
    private final EventLoopGroup workerGroup;

    /**
     * 服务端启动类，我们需要给设置一些参数，包括bossGroup和workerGroup
     */
    private final ServerBootstrap serverBootstrap;

    private final int port;

    public NettyTimeServer(int port) {
        this.port = port;
        int cores = Runtime.getRuntime().availableProcessors();
        this.bossGroup = new NioEventLoopGroup(cores);
        this.workerGroup = new NioEventLoopGroup(cores * 2);
        this.serverBootstrap = new ServerBootstrap();
    }

    public void start() {
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    //指定Channel的类型，用于接受客户端连接，对应于java.nio包中的ServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    /**
                     * 上面NioServerSocketChannel接受客户端连接后需要做的初始化动作，
                     * 包括设置读数据的解码器、写数据的编码器以及自定义业务数据处理器
                     *
                     * 需要注意的是LineBasedFrameDecoder对于消息内容本身就含有换行符的情况就无能为力了，必须先用Base64Encoder先行编码，
                     * 再用LineBasedFrameDecoder进行编码方可解决问题，但是在此例中就业务来讲绝对足够不会出问题，因为此例只对客户端发来的
                     * 固定内容"GET NOW TIME"感兴趣，对其他任何不匹配的内容均认为是BAD_REQUEST
                     */
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            //LineBasedFrameDecoder在解析客户端请求时，遇到字符”\n”或”\r\n”时则认为是一个完整的请求报文，
                            //然后将这个请求报文的二进制字节流交给StringDecoder处理。
                            ch.pipeline().addLast(new LineBasedFrameDecoder(1024)); //基于换行符的TCP粘包解码器
                            //StringDecoder将字节流转换成一个字符串，交给TimeServerHandler来进行处理。
                            ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8)); //byte -> String的解码器
                            //TimeServerHandler是我们自己要编写的类，在这个类中，我们要根据用户请求返回当前时间。
                            ch.pipeline().addLast(new TimeServerHandler());
                        }
                    });
            //启动服务并监听在8080端口，接受客户端请求
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            LOGGER.info(">>> Netty Time Server started and listen on {}", port);
            //等待服务端所持有的的NioServerSocketChannel被关闭，start()方法运行结束，否则hang在此处
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyTimeServer(1212).start();
    }

}

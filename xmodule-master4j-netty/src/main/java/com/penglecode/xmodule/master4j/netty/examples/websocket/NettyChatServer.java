package com.penglecode.xmodule.master4j.netty.examples.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 基于Netty的websocket服务器
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/23 21:13
 */
public class NettyChatServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyChatServer.class);

    private final EventLoopGroup bossGroup;

    private final EventLoopGroup workerGroup;

    private final ServerBootstrap bootstrap;

    private final int port;

    public NettyChatServer(int port) {
        Assert.isTrue(port > 0, "Server port must be > 0");
        this.port = port;
        int baseThreads = Runtime.getRuntime().availableProcessors();
        this.bossGroup = new NioEventLoopGroup(baseThreads, new DefaultThreadFactory("bossGroup"));
        this.workerGroup = new NioEventLoopGroup(2 * baseThreads, new DefaultThreadFactory("workerGroup"));
        this.bootstrap = new ServerBootstrap();
    }

    public void start() {
        try {
            bootstrap.group(bossGroup, workerGroup) //设置boss线程组和worker线程组
                    //指定服务端ChannelFactory的生产的Channel类型
                    .channel(NioServerSocketChannel.class)
                    //设置底层socket已接受请求的缓存队列大小，可设最大值与平台有关：windows最大200，其他128
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //是否重用处于TIME_WAIT状态的地址(例如服务重启后是否可以绑定相同端口)，默认false
                    .option(ChannelOption.SO_REUSEADDR, true)
                    //是否启用心跳机制,默认false(鸡肋设置，还是建议应用层自己实现心跳)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            //websocket 基于http协议，所以要有http编解码器 服务端用HttpServerCodec
                            ch.pipeline().addLast("http-codec", new HttpServerCodec());
                            //对写大数据流的支持
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                            //将HTTP消息的多个部分组合成一条完整的HTTP消息的消息聚合器，也就是处理粘包与解包问题
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                            // ====================== 以上是用于支持http协议 , 以下是支持httpWebsocket ======================
                            /**
                             * websocket 服务器处理的协议，用于指定给客户端连接访问的路由 : /ws
                             * 本handler会帮你处理一些繁重的复杂的事
                             * 会帮你处理握手动作： handshaking（close, ping, pong） ping + pong = 心跳
                             * 对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
                             */
                            ch.pipeline().addLast("ws-handler", new WebSocketServerProtocolHandler("/ws"));

                            //处理websocket请求的业务逻辑
                            ch.pipeline().addLast("chat-handler", new ChatServerHandler());
                        }
                    });
            //启动服务并监听在8080端口，接受客户端请求
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            LOGGER.info(">>> Netty WebSocket Server started and listen on {}", port);
            /**
             * 科普解惑：
             * 1、channel().closeFuture().sync()与channel().close().sync()的区别：
             *    前者是当前线程等待并监听别人关闭channel，当前线程并无发出关闭操作；后者是当前线程立即发出关闭操作并等待channel被关闭
             *
             * 2、xxx.sync()与xxx.syncUninterruptibly()的区别：
             *    前者导致当前线程阻塞等待，如果发生异常则会抛出异常，后者导致当前线程等待，且不可通过Interrupt中断操作
             */
            //等待服务端所持有的的NioServerSocketChannel被关闭，start()方法运行结束，否则hang在此处
            channelFuture.channel().closeFuture().sync();
            LOGGER.info("<<< Netty WebSocket Server all ServerSocketChannel has been closed");
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            LOGGER.info("<<< Netty WebSocket Server has been shutdown successfully");
        }

    }

    public static void main(String[] args) {
        new NettyChatServer(1912).start();
    }

}

package com.penglecode.xmodule.master4j.netty.examples;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 通用Netty示例服务端基类
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/30 17:03
 */
public abstract class AbstractNettyServer extends ChannelInitializer<NioSocketChannel> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNettyServer.class);

    private final ServerBootstrap bootstrap;

    private final NioEventLoopGroup bossGroup;

    private final NioEventLoopGroup workerGroup;

    private final int port;

    private volatile boolean running;

    public AbstractNettyServer(int bossThreads, int workerThreads, int port) {
        this.port = port;
        this.bootstrap = new ServerBootstrap();
        this.bossGroup = new NioEventLoopGroup(bossThreads, new DefaultThreadFactory("bossGroup"));
        this.workerGroup = new NioEventLoopGroup(workerThreads, new DefaultThreadFactory("workerGroup"));
    }

    public void start() {
        if(!running) {
            synchronized (this) {
                if(!running) {
                    try {
                        doStart(bootstrap);
                        running = true;
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    protected void doStart(ServerBootstrap bootstrap) throws Exception {
        try {
            initBootstrap(bootstrap);
            //启动服务并监听在8080端口，接受客户端请求
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            LOGGER.info(">>> Netty {} server started and listen on {}", port, getServerName());
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
            LOGGER.info("<<< Netty {} server all ServerSocketChannel has been closed", getServerName());
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            running = false;
            LOGGER.info("<<< Netty {} server has been shutdown successfully", getServerName());
        }
    }

    protected void initBootstrap(ServerBootstrap bootstrap) {
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(this);
    }

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        //入站消息解码handlers：
        //先通过LengthFieldBasedFrameDecoder解码
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2, true));
        //将上一步的字节数组转为String
        ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));

        //出站消息编码handlers：
        //最后前置追加Length报文头
        ch.pipeline().addLast(new LengthFieldPrepender(2, 0, false));
        //先将原始字符串明文转为ByteBuf
        ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));

        //业务处理器：
        List<ChannelHandler> serverHandlers = createServerHandlers();
        for(ChannelHandler serverHandler : serverHandlers) {
            ch.pipeline().addLast(serverHandler);
        }
    }

    protected abstract List<ChannelHandler> createServerHandlers();

    protected abstract String getServerName();

}

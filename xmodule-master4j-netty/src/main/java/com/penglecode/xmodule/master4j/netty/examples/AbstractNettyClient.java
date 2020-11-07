package com.penglecode.xmodule.master4j.netty.examples;

import com.penglecode.xmodule.master4j.netty.examples.heartbeat.NettyEchoClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 通用Netty示例客户端基类
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/30 19:00
 */
public abstract class AbstractNettyClient extends ChannelInitializer<NioSocketChannel> implements ChannelFutureListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyEchoClient.class);

    private final Bootstrap bootstrap;

    private final InetSocketAddress serverAddress;

    private final NioEventLoopGroup workerGroup;

    private volatile ChannelFuture channelFuture;

    private volatile boolean initialized;

    private volatile boolean running;

    public AbstractNettyClient(int serverPort) {
        this("127.0.0.1", serverPort);
    }

    public AbstractNettyClient(String serverHost, int serverPort) {
        this(serverHost, serverPort, Runtime.getRuntime().availableProcessors());
    }

    public AbstractNettyClient(String serverHost, int serverPort, int nThreads) {
        this.serverAddress = new InetSocketAddress(serverHost, serverPort);
        this.workerGroup = new NioEventLoopGroup(nThreads, new DefaultThreadFactory("workerGroup"));
        this.bootstrap = new Bootstrap();
        this.connect();
    }

    protected void initBootstrap() {
        if(!initialized) {
            synchronized (this) {
                if(!initialized) {
                    doInitBootstrap(bootstrap);
                    initialized = true;
                }
            }
        }
    }

    protected void doInitBootstrap(Bootstrap bootstrap) {
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(this);
    }

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
        List<ChannelHandler> clientHandlers = createClientHandlers();
        for(ChannelHandler clientHandler : clientHandlers) {
            ch.pipeline().addLast(clientHandler);
        }
    }

    protected abstract List<ChannelHandler> createClientHandlers();

    protected ChannelFuture connect() {
        initBootstrap();
        if(!running) {
            synchronized (this) {
                if(!running) {
                    channelFuture = doConnect(bootstrap);
                    running = true;
                }
            }
        } else {
            Channel channel = getClientChannel();
            if(!channel.isActive()) { //重连接
                channelFuture = doConnect(bootstrap);
            }
        }
        return channelFuture;
    }

    protected ChannelFuture doConnect(Bootstrap bootstrap) {
        //客户端连接到远程服务器
        ChannelFuture channelFuture = bootstrap.connect(serverAddress).syncUninterruptibly();
        //注册代表当前客户端的通道被关闭的监听器
        return channelFuture.channel().closeFuture().addListener(this);
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        shutdown();
    }

    protected void shutdown() {
        LOGGER.info(">>> Netty {} client will be shutdown now", getClientName());
        workerGroup.shutdownGracefully();
        running = false;
        LOGGER.info("<<< Netty {} client has been shutdown gracefully", getClientName());
    }

    protected NioEventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    protected ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    protected Channel getClientChannel() {
        Assert.isTrue(running, "Client is not running!");
        return channelFuture.channel();
    }

    protected String getClientName() {
        return getClass().getSimpleName();
    }

}

package com.penglecode.xmodule.master4j.netty.examples.echo2;

import com.penglecode.xmodule.master4j.netty.codec.LineEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.base64.Base64Decoder;
import io.netty.handler.codec.base64.Base64Encoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;

/**
 * 基于Netty的Echo服务器实现
 *
 * 采用Length头边长协议进行编码和解码
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/21 20:54
 */
public class NettyEchoServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyEchoServer.class);

    private final EventLoopGroup bossGroup;

    private final EventLoopGroup workerGroup;

    private final ServerBootstrap bootstrap;

    private final int port;

    public NettyEchoServer(int port) {
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
                            //Inbound消息解码handlers：
                            //先通过LengthFieldBasedFrameDecoder解码
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2, true));
                            //将上一步的字节数组转为String
                            ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));

                            //Outbound消息编码handlers
                            //最后前置追加Length报文头
                            ch.pipeline().addLast(new LengthFieldPrepender(2, 0, false));
                            //先将原始字符串明文转为ByteBuf
                            ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));

                            /**
                             * 注意你向对端发送消息(ctx.writeAndFlush(message))的代码写在了哪个ChannelHandler里面了，
                             * 写在哪个ChannelHandler里面就要把它放置到最后(诸如下面这句)，否则上面编码或解码将不生效
                             * @see io.netty.channel.AbstractChannelHandlerContext#findContextOutbound
                             * @see io.netty.channel.AbstractChannelHandlerContext#findContextInbound
                             * 上面两个查找下一个ChannelHandlerContext都是从调用者作为起始点来找的，这个调用者就是我们的业务ChannelHandler
                             */
                            //处理echo请求的业务逻辑
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            //启动服务并监听在8080端口，接受客户端请求
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            LOGGER.info(">>> Netty Echo Server started and listen on {}", port);
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
            LOGGER.info("<<< Netty Echo Server all ServerSocketChannel has been closed");
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            LOGGER.info("<<< Netty Echo Server has been shutdown successfully");
        }

    }

    public static void main(String[] args) {
        new NettyEchoServer(1314).start();
    }

}

package com.penglecode.xmodule.master4j.netty.examples.echo1;

import com.penglecode.xmodule.master4j.netty.codec.LineEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.base64.Base64Decoder;
import io.netty.handler.codec.base64.Base64Encoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * 基于Netty的Echo客户度实现
 *
 * 采用换行分割协议进行编码和解码
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/21 22:24
 */
public class NettyEchoClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyEchoClient.class);

    private final EventLoopGroup workerGroup;

    private final Bootstrap bootstrap;

    private final InetSocketAddress serverAddress;

    public NettyEchoClient(int serverPort) {
        this("127.0.0.1", serverPort);
    }

    public NettyEchoClient(String serverHost, int serverPort) {
        this.serverAddress = new InetSocketAddress(serverHost, serverPort);
        int nThreads = Runtime.getRuntime().availableProcessors() * 2;
        this.workerGroup = new NioEventLoopGroup(nThreads, new DefaultThreadFactory("workerGroup"));
        this.bootstrap = new Bootstrap();
    }

    public void start() {
        try {
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<NioSocketChannel>(){
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            //Inbound消息解码handlers：
                            //先通过LineBasedFrameDecoder解码，解码后的报文是Base64编码的
                            ch.pipeline().addLast(new LineBasedFrameDecoder(Short.MAX_VALUE, true, true));
                            //继续对上一步LineBasedFrameDecoder的解码结果进行二次解码，最终得到明码的字节数组形式
                            ch.pipeline().addLast(new Base64Decoder());
                            //将上一步的字节数组转为String
                            ch.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));

                            //Outbound消息编码handlers
                            //最后追加换行符
                            ch.pipeline().addLast(new LineEncoder());
                            //在通过Base64Encoder对原始明码进行编码(隐去原始明码中存在换行符的问题)
                            ch.pipeline().addLast(new Base64Encoder());
                            //先将原始字符串明文转为ByteBuf
                            ch.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));

                            /**
                             * 注意你向对端发送消息(ctx.writeAndFlush(message))的代码写在了哪个ChannelHandler里面了，
                             * 写在哪个ChannelHandler里面就要把它放置到最后(诸如下面这句)，否则上面编码或解码将不生效
                             * @see io.netty.channel.AbstractChannelHandlerContext#findContextOutbound
                             * @see io.netty.channel.AbstractChannelHandlerContext#findContextInbound
                             * 上面两个查找下一个ChannelHandlerContext都是从调用者作为起始点来找的，这个调用者就是我们的业务ChannelHandler
                             */
                            //处理echo响应的业务逻辑
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            //客户端连接到远程服务器
            ChannelFuture future = bootstrap.connect(serverAddress).sync();
            //等待客户端所持有的的SocketChannel被关闭，start()方法运行结束，否则hang在此处
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyEchoClient(1314).start();
    }

}

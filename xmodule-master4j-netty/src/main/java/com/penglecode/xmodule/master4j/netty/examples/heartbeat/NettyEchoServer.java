package com.penglecode.xmodule.master4j.netty.examples.heartbeat;

import com.penglecode.xmodule.master4j.netty.examples.AbstractNettyServer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Arrays;
import java.util.List;

/**
 * Netty心跳机制的服务端
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/30 17:03
 */
public class NettyEchoServer extends AbstractNettyServer {

    public NettyEchoServer(int port) {
        super(1, 4, port);
    }

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        /**
         * 服务器关注的是READER_IDLE事件，并且服务器的READ_IDLE间隔需要比客户端的ALL_IDLE事件间隔大
         * 最好是2倍/3倍，这样做就无需引入超时次数计数的复杂逻辑。
         */
        ch.pipeline().addLast(new IdleStateHandler(10, 0, 0));
        super.initChannel(ch);
    }

    @Override
    protected List<ChannelHandler> createServerHandlers() {
        return Arrays.asList(new EchoServerHeartbeatHandler(), new EchoServerHandler());
    }

    @Override
    protected String getServerName() {
        return "Echo";
    }

    public static void main(String[] args) {
        new NettyEchoServer(1912).start();
    }

}

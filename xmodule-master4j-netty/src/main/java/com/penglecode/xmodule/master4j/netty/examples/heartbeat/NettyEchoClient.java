package com.penglecode.xmodule.master4j.netty.examples.heartbeat;

import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.master4j.netty.examples.AbstractNettyClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * Netty心跳机制的客户端
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/30 17:19
 */
public class NettyEchoClient extends AbstractNettyClient {

    /**
     * 被服务端断开连接后是否需要重连接?
     * 此标志位测试用，表示是否开启断线重连测试
     */
    private volatile boolean reconnect = true;

    /**
     * 积极的向服务端上报"我还活着"
     * 此标志位测试用，测试客户端不发PING时服务端什么反应
     * 如果keepAlive=true，那么上面的reconnect参数应该没作用了，
     * 因为keepAlive=true表示客户端会积极发送ping给服务端，所以基本上服务端不会关闭连接，也就没有重连的可能。
     */
    private final boolean keepAlive = true;

    public NettyEchoClient(int serverPort) {
        super("127.0.0.1", serverPort, 4);
    }

    @Override
    protected String getClientName() {
        return "Echo";
    }

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        /**
         * 客户端注意关注ALL_IDLE事件，即在5秒钟内，如果没发任何消息给服务端或者没收到服务端任何消息，
         * 那么就会触发ALL_IDLE事件，进入EchoClientHeartbeatHandler#handleAllIdle(..)方法中
         */
        ch.pipeline().addLast(new IdleStateHandler(0, 0, 5));
        super.initChannel(ch);
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if(!reconnect) { //不需要重连则执行shutdown
            shutdown();
        } else {
            connect();
        }
    }

    @Override
    protected List<ChannelHandler> createClientHandlers() {
        return Arrays.asList(new EchoClientHeartbeatHandler(keepAlive), new EchoClientHandler());
    }

    /**
     * 随机间隔滴发送Echo消息
     */
    protected void randomEcho() {
        Random random = new Random();
        int reports = 10;
        int maxIntervals = 15;

        for(int i = 0; i < reports; i++) {
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(random.nextInt(maxIntervals)));
            boolean success = sendMessage(); //发送一次Echo消息
            if(!success && !reconnect) { //不需要重连的情况下，就结束测试吧
                break;
            }
        }
        closeChannel(); //reports次测试完毕后关闭客户端
    }

    protected boolean sendMessage() {
        try {
            Channel channel = getClientChannel();
            channel.writeAndFlush(DateTimeUtils.formatNow());
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    protected void closeChannel() {
        try {
            reconnect = false; //指示直接shutdown
            Channel channel = getClientChannel();
            channel.close();
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        NettyEchoClient client = new NettyEchoClient(1912);
        client.connect();
        client.randomEcho();
    }

}

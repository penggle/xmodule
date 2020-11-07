package com.penglecode.xmodule.master4j.netty.examples.heartbeat;

import com.penglecode.xmodule.common.util.DateTimeUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳基类
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/30 22:20
 */
public abstract class AbstractHeartbeatHandler extends ChannelInboundHandlerAdapter {

    public static final String PING_MESSAGE = "PING";

    public static final String PONG_MESSAGE = "PONG";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(PING_MESSAGE.equals(msg)) { //对端发来ping消息 (对端关注我的状态)
            sendPongMessage(ctx); //回复对端pong消息
            System.out.println(String.format("【%s】>>> Received PING from %s", DateTimeUtils.formatNow(), ctx.channel().remoteAddress()));
        } else if (PONG_MESSAGE.equals(msg)) { //收到对端发来的pong (我关注对端的状态)
            System.out.println(String.format("【%s】>>> Received PONG from %s", DateTimeUtils.formatNow(), ctx.channel().remoteAddress()));
        } else { //除了ping/pong之外的真正的业务信息，继续传播给后续XxxHandler
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 处理读超时，即当在指定的时间间隔内没有从 Channel 读取到数据时
     * @param ctx
     */
    protected void handleReaderIdle(ChannelHandlerContext ctx) {

    }

    /**
     * 处理写超时，即当在指定的时间间隔内没有数据写入到 Channel 时
     * @param ctx
     */
    protected void handleWriterIdle(ChannelHandlerContext ctx) {

    }

    /**
     * 处理读/写超时
     * @param ctx
     */
    protected void handleAllIdle(ChannelHandlerContext ctx) {

    }

    /**
     * 发送心跳包给对端
     * @param ctx
     */
    protected void sendPingMessage(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(PING_MESSAGE);
    }

    /**
     * 回应对端的心跳包
     * @param ctx
     */
    protected void sendPongMessage(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(PONG_MESSAGE);
    }

}

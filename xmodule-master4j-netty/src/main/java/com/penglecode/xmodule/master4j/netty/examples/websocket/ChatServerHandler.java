package com.penglecode.xmodule.master4j.netty.examples.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/23 21:29
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatServerHandler.class);

    private static final EventExecutor EVENT_EXECUTOR = GlobalEventExecutor.INSTANCE;

    /**
     * 用于记录和管理所有保持连接的客户端channle
     */
    private static final ChannelGroup ALL_ALIVE_CLIENTS = new DefaultChannelGroup(EVENT_EXECUTOR);

    static {
        schedulingMessagePush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info(">>> Channel active: {}", ctx.channel());
        ALL_ALIVE_CLIENTS.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info(">>> Channel inactive: {}", ctx.channel());
        ALL_ALIVE_CLIENTS.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String content = msg.text();
        LOGGER.info(">>> Message received: {}", msg);
        ctx.writeAndFlush(new TextWebSocketFrame(content));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error(cause.getMessage(), cause);
    }

    protected static void schedulingMessagePush() {
        EVENT_EXECUTOR.scheduleAtFixedRate(ChatServerHandler::doMessagePush, 0, 60, TimeUnit.SECONDS);
        LOGGER.info(">>> Register global message pusher");
    }

    protected static void doMessagePush() {
        String content = "同学，你还在吗?";
        ALL_ALIVE_CLIENTS.writeAndFlush(new TextWebSocketFrame(content));
    }

}

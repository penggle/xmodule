package com.penglecode.xmodule.master4j.netty.examples.first;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * 时间服务的客户端处理器
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/20 23:48
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info(">>> Channel active : {}", ctx.channel());
        String command = "GET NOW TIME" + System.getProperty("line.separator");
        ByteBuf requestBuffer = Unpooled.copiedBuffer(command.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(requestBuffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info(">>> Channel read : {}", msg);
    }
}

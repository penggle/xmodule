package com.penglecode.xmodule.master4j.netty.examples.first;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * TimeServer核心业务处理器
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/20 23:09
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeServerHandler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info(">>> Channel registered : {}", ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info(">>> Channel unregistered : {}", ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info(">>> Channel active : {}", ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info(">>> Channel inactive : {}", ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.info(">>> Channel read : {}", msg);
        String request = (String) msg; //StringDecoder解码结果
        String response;
        if ("GET NOW TIME".equals(request)) {
            response = "Server now time is " + LocalDateTime.now().toString();
        } else {
            response = "BAD REQUEST";
        }
        response = response + System.getProperty("line.separator");
        //调用了Unpooled.copiedBuffer方法创建了一个缓冲区对象ByteBuf
        ByteBuf responseBuffer = Unpooled.copiedBuffer(response.getBytes());
        ctx.writeAndFlush(responseBuffer); //将消息发送出去
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error(cause.getMessage());
    }
}

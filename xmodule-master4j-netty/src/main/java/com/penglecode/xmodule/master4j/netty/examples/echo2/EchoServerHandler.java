package com.penglecode.xmodule.master4j.netty.examples.echo2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理echo请求的业务处理器
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/21 21:39
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoServerHandler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info(">>> Channel registered : {}", ctx.channel());
        //super.channelRegistered(ctx); //注意注释此句将会导致fireChannelXxx()事件不能传播给后续YyyHandler
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info(">>> Channel unregistered : {}", ctx.channel());
        //super.channelUnregistered(ctx); //注意注释此句将会导致fireChannelXxx()事件不能传播给后续YyyHandler
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info(">>> Channel active : {}", ctx.channel());
        //super.channelActive(ctx); //注意注释此句将会导致fireChannelXxx()事件不能传播给后续YyyHandler
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info(">>> Channel inactive : {}", ctx.channel());
        //super.channelInactive(ctx); //注意注释此句将会导致fireChannelXxx()事件不能传播给后续YyyHandler
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;
        LOGGER.info(">>> Receive message from client : {}", message);
        ctx.writeAndFlush(message); //回写消息给客户端
        //super.channelRead(ctx, msg); //注意注释此句将会导致fireChannelXxx()事件不能传播给后续YyyHandler
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error(cause.getMessage());
        //super.exceptionCaught(ctx, cause); //注意注释此句将会导致fireChannelXxx()事件不能传播给后续YyyHandler
    }

}

package com.penglecode.xmodule.master4j.netty.examples.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Echo服务的客户端处理器
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/21 22:34
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info(">>> Channel active : {}", ctx.channel());
        //super.channelActive(ctx); //注意注释此句将会导致fireChannelXxx()事件不能传播给后续YyyHandler
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;
        LOGGER.info(">>> Echo message from server : {}", message);
        //super.channelRead(ctx, msg); //注意注释此句将会导致fireChannelXxx()事件不能传播给后续YyyHandler
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error(cause.getMessage(), cause);
        //super.exceptionCaught(ctx, cause); //注意注释此句将会导致fireChannelXxx()事件不能传播给后续YyyHandler
    }
}

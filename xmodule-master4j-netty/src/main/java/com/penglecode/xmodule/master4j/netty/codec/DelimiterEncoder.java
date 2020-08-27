package com.penglecode.xmodule.master4j.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 自定义的基于分隔符的编码器
 *
 * 注意如果将delimiter声明为ByteBuf那么在encode方法中需要小心处理，因为在没有任何处理措施的情况下
 * 从第二次开始将读不到任何内容，因为readerIndex已经读到最后了，还未复位，导致第二次开始始终读到的是空白串
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/21 23:55
 */
public class DelimiterEncoder extends MessageToMessageEncoder<ByteBuf> {

    private final String delimiter;

    public DelimiterEncoder(String delimiter) {
        Assert.hasLength(delimiter, "Parameter 'delimiter' must be not empty!");
        this.delimiter = delimiter;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        ByteBuf buffer = Unpooled.directBuffer();
        out.add(buffer.writeBytes(msg).writeBytes(delimiter.getBytes()));
    }

}

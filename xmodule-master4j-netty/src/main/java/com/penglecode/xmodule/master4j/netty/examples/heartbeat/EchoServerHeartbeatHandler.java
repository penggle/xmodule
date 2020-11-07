package com.penglecode.xmodule.master4j.netty.examples.heartbeat;

import com.penglecode.xmodule.common.util.DateTimeUtils;
import io.netty.channel.ChannelHandlerContext;

/**
 * 服务器是接收客户端的PING消息的，因此服务器关注的是READER_IDLE事件，并且服务器的READ_IDLE间隔需要比客户端的ALL_IDLE事件间隔大，最好是2倍/3倍，这样做就无需引入超时次数计数的复杂逻辑。
 * (例如客户端ALL_IDLE是5s没有读写时触发，因此服务器的READER_IDLE可以：设置为10s(代表2个周期内没收到客户端的PING)，或者设置为15s(代表3个周期内没收到客户端的PING))
 * 如果服务端在READER_IDLE间隔内还未收到来自客户端的任何消息，那么可以判定客户端连接已经无效了并关闭客户端连接。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/30 23:54
 */
public class EchoServerHeartbeatHandler extends AbstractHeartbeatHandler {

    /**
     * 例如服务端超过10s(两个客户端的ALL_IDLE时间)内还未收到来自客户端的任何消息，那么可以判定客户端连接已经无效了并关闭客户端连接。
     */
    @Override
    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        ctx.close();
        System.out.println(String.format("【%s】>>> 长时间未收到来自客户端的任何消息，关闭客户端连接", DateTimeUtils.formatNow()));
    }

}

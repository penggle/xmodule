package com.penglecode.xmodule.master4j.netty.examples.heartbeat;

import com.penglecode.xmodule.common.util.DateTimeUtils;
import io.netty.channel.ChannelHandlerContext;

/**
 * 一般是客户端负责发送心跳的PING消息，因此客户端注意关注ALL_IDLE事件，
 * (这个ALL_IDLE可以这么理解：1、对应于写IDLE，即客户端很久没发消息给服务端了，作为客户端需要主动跟服务端保持畅通，需要发一个PING心跳包给服务端；
 * 2、对应于读IDLE，即服务端很久没发来消息了，作为客户端需要主动跟服务端保持畅通，需要发一个PING心跳包给服务端；)
 * 在这个ALL_IDLE事件触发后，客户端需要向服务器发送PING消息，告诉服务器"我还存活着"。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/30 23:54
 */
public class EchoClientHeartbeatHandler extends AbstractHeartbeatHandler {

    /**
     * 积极的向服务端上报"我还活着"
     * 此标志位测试用，测试客户端不发PING时服务端什么反应
     */
    private final boolean keepAlive;

    public EchoClientHeartbeatHandler(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    /**
     * 例如在5s后触发了ALL_IDLE事件，则发送一个PING心跳包给服务端；
     */
    @Override
    protected void handleAllIdle(ChannelHandlerContext ctx) {
        if(keepAlive) {
            sendPingMessage(ctx);
            System.out.println(String.format("【%s】>>> 发送PING心跳包给服务端", DateTimeUtils.formatNow()));
        }
    }

}

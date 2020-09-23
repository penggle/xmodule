package com.penglecode.xmodule.master4j.spring.context.sample.common;

import java.net.InetSocketAddress;

/**
 * Spring ApplicationContext启动过程示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/11 19:13
 */
public class JettyWebServer extends AbstractWebServer {

    public JettyWebServer(InetSocketAddress bindAddress) {
        super(bindAddress);
    }

    public void shutdown() {
        stop();
    }

}

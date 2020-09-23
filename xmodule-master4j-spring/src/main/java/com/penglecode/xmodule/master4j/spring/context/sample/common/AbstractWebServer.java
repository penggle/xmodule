package com.penglecode.xmodule.master4j.spring.context.sample.common;

import java.net.InetSocketAddress;
import java.time.Duration;

/**
 * Spring ApplicationContext启动过程示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/11 18:53
 */
public abstract class AbstractWebServer implements WebServer {

    private Duration sessionTimeout;

    private final InetSocketAddress bindAddress;

    protected AbstractWebServer(InetSocketAddress bindAddress) {
        this.bindAddress = bindAddress;
    }

    @Override
    public void start() {
        System.out.println(String.format("【%s】>>> %s starting ...", getClass().getSimpleName(), getName()));
    }

    @Override
    public void stop() {
        System.out.println(String.format("【%s】>>> %s stoping ...", getClass().getSimpleName(), getName()));
    }

    protected String getName() {
        return getClass().getSimpleName();
    }

    public InetSocketAddress getBindAddress() {
        return bindAddress;
    }

    public Duration getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Duration sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

}

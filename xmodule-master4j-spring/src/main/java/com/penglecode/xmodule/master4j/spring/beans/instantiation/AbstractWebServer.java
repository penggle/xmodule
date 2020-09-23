package com.penglecode.xmodule.master4j.spring.beans.instantiation;

import java.time.Duration;

/**
 * Spring BeanFactory实例化bean过程示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/11 18:53
 */
public abstract class AbstractWebServer implements WebServer {

    private final ServerConfig serverConfig;

    private Duration sessionTimeout;

    protected AbstractWebServer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
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

    protected ServerConfig getServerConfig() {
        return serverConfig;
    }

    public Duration getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Duration sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

}

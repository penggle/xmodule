package com.penglecode.xmodule.master4j.spring.beans.instantiation;

/**
 * Spring BeanFactory实例化bean过程示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/11 18:48
 */
public class ServerConfig {

    private String host;

    private int port;

    public ServerConfig() {
    }

    public ServerConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}

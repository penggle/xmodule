package com.penglecode.xmodule.master4j.spring.beans.injection;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/12 15:30
 */
public class ApiEndpoint {

    private final String host;

    private final int port;

    public ApiEndpoint(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}

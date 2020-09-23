package com.penglecode.xmodule.master4j.spring.beans.instantiation;

/**
 * Spring BeanFactory实例化bean过程示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/11 19:13
 */
public class JettyWebServer extends AbstractWebServer {

    public JettyWebServer(ServerConfig serverConfig) {
        super(serverConfig);
    }

    public void shutdown() {
        stop();
    }

}

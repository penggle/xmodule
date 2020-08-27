package com.penglecode.xmodule.master4j.java.lang.reflect.clazz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 14:07
 */
@Validated
public class TomcatServer extends ServletServer implements ServletServer.ServerInitialization {

    public static final String DEFAULT_TOMCAT_CHARSET = "UTF-8";

    private static final Logger LOGGER = LoggerFactory.getLogger(TomcatServer.class);

    private String uriEnchoding = DEFAULT_CHARSET;

    @Override
    protected void doStart() throws Exception {
        LOGGER.info("{} starting ...", getServerName());
    }

    @Override
    protected void doStop() throws Exception {
        LOGGER.info("{} stoping ...", getServerName());
    }

    @Override
    protected String getServerName() {
        return "Tomcat Server";
    }

    @Override
    public void init() {
        LOGGER.info("{} initializing ...", getServerName());
    }

    public String getUriEnchoding() {
        return uriEnchoding;
    }

    public void setUriEnchoding(String uriEnchoding) {
        this.uriEnchoding = uriEnchoding;
    }

    public static class DefaultTomcatInitializer implements ServletServer.ServerInitialization {

        @Override
        public void init() {

        }
    }

}

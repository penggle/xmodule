package com.penglecode.xmodule.master4j.java.lang.reflect.clazz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

import javax.annotation.concurrent.ThreadSafe;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.Map;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 13:24
 */
@Valid
@AutoRestartable
public abstract class ServletServer implements SmartLifecycle, Serializable {

    public static final String DEFAULT_CHARSET = "UTF-8";

    private static final Logger LOGGER = LoggerFactory.getLogger(ServletServer.class);

    private int port;

    private volatile boolean running;

    @Override
    public void start() {
        if(!running) {
            synchronized (this) {
                if (!running) {
                    try {
                        LOGGER.info(">>> {} start now ...", getServerName());
                        doStart();
                        running = true;
                        LOGGER.info(">>> {} started successfully ...", getServerName());
                    } catch (Exception e) {
                        LOGGER.error(">>> {} start error : {}", getServerName(), e.getMessage());
                    }
                }
            }
        }
    }

    protected abstract void doStart() throws Exception;

    @Override
    public void stop() {
        if(running) {
            synchronized (this) {
                if (running) {
                    try {
                        LOGGER.info(">>> {} stop now ...", getServerName());
                        doStop();
                        running = false;
                        LOGGER.info(">>> {} stoped successfully ...", getServerName());
                    } catch (Exception e) {
                        LOGGER.error(">>> {} stop error : {}", getServerName(), e.getMessage());
                    }
                }
            }
        }
    }

    protected abstract void doStop() throws Exception;

    @Override
    public boolean isRunning() {
        return running;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    protected abstract String getServerName();

    public static Map<String,String> getServerEnvironments() {
        return System.getenv();
    }

    @ThreadSafe
    public interface ServerInitialization {

        public void init();

    }

}

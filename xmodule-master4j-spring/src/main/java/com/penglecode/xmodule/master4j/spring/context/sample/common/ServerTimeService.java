package com.penglecode.xmodule.master4j.spring.context.sample.common;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Spring ApplicationContext启动过程示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/15 14:23
 */
@Component
public class ServerTimeService {

    private volatile LocalDateTime serverStartTime;

    public String getServerStartTime() {
        return serverStartTime.toString();
    }

    public String getServerNowTime() {
        return Instant.now().toString();
    }

    @PostConstruct
    protected void init() {
        this.serverStartTime = LocalDateTime.now();
        System.out.println("【ServerTimeService】>>> serverStartTime = " + serverStartTime);
    }

}

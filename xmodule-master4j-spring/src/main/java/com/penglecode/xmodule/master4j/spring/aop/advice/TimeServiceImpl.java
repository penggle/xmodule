package com.penglecode.xmodule.master4j.spring.aop.advice;

import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/17 23:55
 */
public class TimeServiceImpl implements TimeService {

    @Override
    public String getNowTime(ZoneId zoneId) {
        Assert.notNull(zoneId, "Parameter 'zoneId' can not be null!");
        return LocalDateTime.now(zoneId).toString();
    }

    @Override
    public long getNowInstant(ZoneId zoneId) {
        Assert.notNull(zoneId, "Parameter 'zoneId' can not be null!");
        return LocalDateTime.now().atZone(zoneId).toEpochSecond();
    }

}

package com.penglecode.xmodule.master4j.spring.aop.advice;

import java.time.ZoneId;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/18 20:09
 */
public interface TimeService {

    public static final ZoneId SHANG_HAI = ZoneId.of("Asia/Shanghai");

    public static final ZoneId LONDON = ZoneId.of("Europe/London");

    public static final ZoneId NEW_YORK = ZoneId.of("America/New_York");

    public String getNowTime(ZoneId zoneId);

    public long getNowInstant(ZoneId zoneId);

}

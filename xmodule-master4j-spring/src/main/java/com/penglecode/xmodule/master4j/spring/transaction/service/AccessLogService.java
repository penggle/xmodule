package com.penglecode.xmodule.master4j.spring.transaction.service;

import com.penglecode.xmodule.master4j.spring.transaction.model.AccessLog;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/21 23:17
 */
public interface AccessLogService {

    public void recordAccessLog1(AccessLog accessLog);

    public void recordAccessLog2(AccessLog accessLog);

}

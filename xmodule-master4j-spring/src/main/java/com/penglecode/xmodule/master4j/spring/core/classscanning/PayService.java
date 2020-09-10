package com.penglecode.xmodule.master4j.spring.core.classscanning;

import java.util.Map;

/**
 * 支付服务
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/6 21:45
 */
public interface PayService extends Refoundable {

    /**
     * 获取支付厂商名称
     * @return
     */
    public String getName();

    /**
     * 支付接口
     * @param parameter
     * @return
     */
    public boolean pay(Map<String,Object> parameter);

}

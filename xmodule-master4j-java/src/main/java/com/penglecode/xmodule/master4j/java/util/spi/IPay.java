package com.penglecode.xmodule.master4j.java.util.spi;

import java.util.Map;

/**
 * 支付SPI接口
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/25 17:45
 */
public interface IPay {

    void pay(Map<String,Object> parameter);

}

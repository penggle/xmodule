package com.penglecode.xmodule.master4j.java.util.spi;

import java.util.Map;

/**
 * 微信支付
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/25 17:48
 */
public class WeiXinPay implements IPay {

    @Override
    public void pay(Map<String, Object> parameter) {
        System.out.println("微信支付");
    }

}

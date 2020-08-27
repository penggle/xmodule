package com.penglecode.xmodule.master4j.java.util.spi;

import java.util.Map;

/**
 * 支付宝支付
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/25 17:47
 */
public class AliPay implements IPay {

    @Override
    public void pay(Map<String, Object> parameter) {
        System.out.println("支付宝支付");
    }

}

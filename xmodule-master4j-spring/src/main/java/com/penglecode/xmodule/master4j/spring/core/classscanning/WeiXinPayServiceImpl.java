package com.penglecode.xmodule.master4j.spring.core.classscanning;

import java.util.Map;

/**
 * 微信支付
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/6 22:13
 */
public class WeiXinPayServiceImpl extends AbstractPayService {
    protected WeiXinPayServiceImpl() {
        super("微信");
    }

    @Override
    protected boolean doPay(Map<String, Object> parameter) throws Exception {
        System.out.println("执行" + getName() + "支付");
        return true;
    }

    @Override
    protected boolean doRefound(Map<String, Object> parameter) throws Exception {
        System.out.println("执行" + getName() + "退款");
        return true;
    }
}

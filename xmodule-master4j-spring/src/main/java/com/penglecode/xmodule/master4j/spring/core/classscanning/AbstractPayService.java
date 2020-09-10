package com.penglecode.xmodule.master4j.spring.core.classscanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 支付服务基类
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/6 21:49
 */
public abstract class AbstractPayService implements PayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPayService.class);

    static {
        System.out.println("类加载-初始化AbstractPayService");
    }

    private final String name;

    protected AbstractPayService(String name) {
        this.name = name;
    }

    @Override
    public final boolean pay(Map<String, Object> parameter) {
        try {
            LOGGER.info(">>> {}支付开始: parameter = {}", name, parameter);
            return doPay(parameter);
        } catch (Exception e) {
            LOGGER.error(String.format("<<< %s支付出错: %s", name, e.getMessage()), e);
        }
        return false;
    }

    protected abstract boolean doPay(Map<String,Object> parameter) throws Exception;

    @Override
    public final boolean refound(Map<String, Object> parameter) {
        try {
            LOGGER.info(">>> {}退款开始: parameter = {}", name, parameter);
            return doRefound(parameter);
        } catch (Exception e) {
            LOGGER.error(String.format("<<< %s退款出错: %s", name, e.getMessage()), e);
        }
        return false;
    }

    protected abstract boolean doRefound(Map<String,Object> parameter) throws Exception;

    @Override
    public String getName() {
        return this.name;
    }

    final class NoopPayServiceImpl implements PayService {

        @Override
        public String getName() {
            return "noop";
        }

        @Override
        public boolean pay(Map<String, Object> parameter) {
            return true;
        }

        @Override
        public boolean refound(Map<String, Object> parameter) {
            return true;
        }
    }

    public static class IntegralPayServiceImpl extends AbstractPayService {

        public IntegralPayServiceImpl() {
            super("积分");
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

}

package com.penglecode.xmodule.master4j.spring.beans.instantiation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;

/**
 * Spring BeanFactory实例化bean过程示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/11 19:08
 */
public class TomcatWebServer extends AbstractWebServer implements InitializingBean, DisposableBean,
        BeanNameAware, BeanFactoryAware {

    public TomcatWebServer(ServerConfig serverConfig) {
        super(serverConfig);
    }

    @Override
    public void destroy() throws Exception {
        stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println(String.format("【%s】>>> setBeanFactory(%s)", getClass().getSimpleName(), beanFactory));
    }

    @Override
    public void setBeanName(String name) {
        System.out.println(String.format("【%s】>>> setBeanName(%s)", getClass().getSimpleName(), name));
    }
}

package com.penglecode.xmodule.master4j.springboot.example.conditions;

import org.springframework.beans.factory.InitializingBean;

/**
 * Spring @Conditional注解示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/24 16:38
 */
public class WindowsOperatingSystem implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(String.format("【%s】>>> %s", "Spring @Conditional注解示例", "当前操作系统是：windows"));
    }

}

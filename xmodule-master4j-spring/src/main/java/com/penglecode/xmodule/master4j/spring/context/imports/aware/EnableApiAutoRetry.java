package com.penglecode.xmodule.master4j.spring.context.imports.aware;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * ImportAware接口配合@Import及注解，用于设置注解的值示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 23:05
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ApiAutoRetryConfiguration.class)
public @interface EnableApiAutoRetry {

    int autoRetries() default 0;

}

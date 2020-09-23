package com.penglecode.xmodule.master4j.spring.aop.pointcut;

import java.lang.annotation.*;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/19 23:55
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Proxyable {
}

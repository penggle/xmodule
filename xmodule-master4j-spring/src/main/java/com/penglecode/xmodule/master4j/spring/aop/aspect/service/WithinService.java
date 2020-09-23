package com.penglecode.xmodule.master4j.spring.aop.aspect.service;

import java.lang.annotation.*;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/20 17:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface WithinService {
}

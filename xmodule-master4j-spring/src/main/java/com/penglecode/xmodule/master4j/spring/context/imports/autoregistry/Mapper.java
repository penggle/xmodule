package com.penglecode.xmodule.master4j.spring.context.imports.autoregistry;

import java.lang.annotation.*;

/**
 * 自动注册@Mapper注释的类示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 23:21
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapper {
}

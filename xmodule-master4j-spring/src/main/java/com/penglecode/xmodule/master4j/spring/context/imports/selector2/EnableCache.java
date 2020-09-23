package com.penglecode.xmodule.master4j.spring.context.imports.selector2;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 21:40
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CacheVendorImportSelector.class)
public @interface EnableCache {

    String vendor() default "ehcache";

}

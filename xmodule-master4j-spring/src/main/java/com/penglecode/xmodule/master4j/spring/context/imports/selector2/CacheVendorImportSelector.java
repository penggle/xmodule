package com.penglecode.xmodule.master4j.spring.context.imports.selector2;

import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * ImportSelector实例的selectImports方法的执行时机，是在@Configuration注解中的其他逻辑被处理**之前**，
 * 所谓的其他逻辑，包括对@ImportResource、@Bean这些注解的处理
 * （注意，这里只是对@Bean修饰的方法的处理，并不是立即调用@Bean修饰的方法，这个区别很重要！）
 *
 * DeferredImportSelector实例的selectImports方法的执行时机，是在@Configuration注解中的其他逻辑被处理**完毕之后**
 * DeferredImportSelector的实现类可以用@Order注解，或者实现Ordered接口来对selectImports的执行顺序排序（ImportSelector不支持）
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 21:41
 * @see AdviceModeImportSelector
 */
public class CacheVendorImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableCache.class.getName(), false));
        String vendor = attributes.getString("vendor");
        if("ehcache".equals(vendor)) {
            return new String[] { EhCacheConfiguration.class.getName() };
        } else if ("guavacache".equals(vendor)) {
            return new String[] { GuavaCacheConfiguration.class.getName() };
        }
        return null;
    }
}

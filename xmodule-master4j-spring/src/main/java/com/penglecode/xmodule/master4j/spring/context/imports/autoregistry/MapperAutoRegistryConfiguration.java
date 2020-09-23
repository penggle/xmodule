package com.penglecode.xmodule.master4j.spring.context.imports.autoregistry;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * 自动注册@Mapper注释的类示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 23:26
 */
public class MapperAutoRegistryConfiguration implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;

    private Environment environment;

    private String[] basePackages;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        System.out.println("MapperAutoRegistryConfiguration#registerBeanDefinitions");
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(MapperScan.class.getName()));
        this.basePackages = attributes.getStringArray("basePackages");
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry, false, environment, resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Mapper.class));
        scanner.scan(basePackages);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}

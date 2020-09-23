package com.penglecode.xmodule.master4j.spring.context.imports.aware;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * ImportAware接口配合@Import及注解，用于设置注解的值示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 23:07
 */
@Configuration
public class ApiAutoRetryConfiguration implements ImportAware {

    private int autoRetries;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importMetadata.getAnnotationAttributes(EnableApiAutoRetry.class.getName()));
        this.autoRetries = attributes.getNumber("autoRetries").intValue();
    }

    @Bean
    public ApiClient apiClient() {
        return new ApiClient(autoRetries);
    }

}

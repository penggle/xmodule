package com.penglecode.xmodule.master4j.spring.core.resource.loader;

import com.penglecode.xmodule.common.util.PropertiesUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Properties;

/**
 * ResourceLoader - 资源加载器示例
 *
 * ResourcePatternResolver是ResourceLoader的增强子接口，提供了根据模式匹配获取多个资源的能力
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/17 14:54
 */
public class ResourceLoaderExample {

    public static void main(String[] args) throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:com/penglecode/xmodule/master4j/spring/core/resource/foo.properties");
        Properties properties = PropertiesUtils.loadProperties(resource);
        System.out.println(properties);
    }

}

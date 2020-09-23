package com.penglecode.xmodule.master4j.spring.core.resource.loader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * ResourcePatternResolver是ResourceLoader的增强子接口，提供了根据模式匹配获取多个资源的能力
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/17 16:02
 */
public class ResourcePatternResolverExample {

    public static void main(String[] args) throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources("classpath:**/*.xml");
        for(Resource resource : resources) {
            System.out.println(resource);
        }
    }

}

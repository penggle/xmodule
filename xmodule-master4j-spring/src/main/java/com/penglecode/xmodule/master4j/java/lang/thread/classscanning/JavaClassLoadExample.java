package com.penglecode.xmodule.master4j.java.lang.thread.classscanning;

import com.penglecode.xmodule.BasePackage;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.util.Set;

/**
 * -XX:+TraceClassLoading
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 17:33
 */
public class JavaClassLoadExample {

    public static void main(String[] args) {
        ClassPathScanningCandidateComponentProvider scanningProvider = new ClassPathScanningCandidateComponentProvider(false);


        //scanningProvider.addIncludeFilter();

        Set<BeanDefinition> candidateBeanDefinitions = scanningProvider.findCandidateComponents(BasePackage.class.getName());
        for(BeanDefinition candidateBeanDefinition : candidateBeanDefinitions) {
            System.out.println(candidateBeanDefinition.getBeanClassName());
        }
    }

}

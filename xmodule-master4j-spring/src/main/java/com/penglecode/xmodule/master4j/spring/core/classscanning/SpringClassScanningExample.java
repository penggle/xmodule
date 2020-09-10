package com.penglecode.xmodule.master4j.spring.core.classscanning;

import com.penglecode.xmodule.common.util.ClassScanningUtils.ClassPathScanningCandidateTypeProvider;
import com.penglecode.xmodule.common.util.ClassScanningUtils.JavaCoreResourceClassLoader;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * Spring类扫描
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/6 22:15
 */
public class SpringClassScanningExample {

    /**
     * 查看并打印ClassMetadata对象的各个方法
     */
    public static void inspectClassMetadataByScanning() {
        ClassPathScanningCandidateComponentProvider scanningProvider = new ClassPathScanningCandidateComponentProvider(false);
        scanningProvider.addIncludeFilter(new ClassMetadataInspectTypeFilter());

        String packageName = "com.penglecode.xmodule.master4j.spring.core.classscanning";
        Set<BeanDefinition> candidateBeanDefinitions = scanningProvider.findCandidateComponents(packageName);
        System.out.println("---------------------------------------------------------");
        for(BeanDefinition candidateBeanDefinition : candidateBeanDefinitions) {
            System.out.println(candidateBeanDefinition.getBeanClassName());
        }
    }

    public static void anyTypeFilterClassScanning() {
        ClassPathScanningCandidateComponentProvider scanningProvider = new ClassPathScanningCandidateTypeProvider(true,false);
        scanningProvider.addIncludeFilter(new AnyTypeFilter());

        String packageName = "com.penglecode.xmodule.master4j.spring.core.classscanning";
        Set<BeanDefinition> candidateBeanDefinitions = scanningProvider.findCandidateComponents(packageName);
        System.out.println("---------------------------------------------------------");
        for(BeanDefinition candidateBeanDefinition : candidateBeanDefinitions) {
            System.out.println(candidateBeanDefinition.getBeanClassName());
        }
    }

    public static void assignableTypeFilterClassScanning() {
        ClassPathScanningCandidateComponentProvider scanningProvider = new ClassPathScanningCandidateTypeProvider(true,false);
        scanningProvider.addIncludeFilter(new AssignableTypeFilter(Refoundable.class));

        String packageName = "com.penglecode.xmodule.master4j.spring.core.classscanning";
        Set<BeanDefinition> candidateBeanDefinitions = scanningProvider.findCandidateComponents(packageName);
        System.out.println("---------------------------------------------------------");
        for(BeanDefinition candidateBeanDefinition : candidateBeanDefinitions) {
            String className = candidateBeanDefinition.getBeanClassName();
            try {
                System.out.println(Class.forName(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void javaCoreClassScanning() {
        ClassPathScanningCandidateComponentProvider scanningProvider = new ClassPathScanningCandidateTypeProvider(true, false);
        scanningProvider.setResourceLoader(new DefaultResourceLoader(new JavaCoreResourceClassLoader()));
        scanningProvider.addIncludeFilter(new AssignableTypeFilter(Executor.class));

        String packageName = "java.util";
        Set<BeanDefinition> candidateBeanDefinitions = scanningProvider.findCandidateComponents(packageName);
        System.out.println("---------------------------------------------------------");
        for(BeanDefinition candidateBeanDefinition : candidateBeanDefinitions) {
            System.out.println(candidateBeanDefinition.getBeanClassName());
        }
    }

    public static void main(String[] args) throws IOException {
        //inspectClassMetadataByScanning();
        //anyTypeFilterClassScanning();
        //assignableTypeFilterClassScanning();
        javaCoreClassScanning();
        //testClassLoader();
    }

}

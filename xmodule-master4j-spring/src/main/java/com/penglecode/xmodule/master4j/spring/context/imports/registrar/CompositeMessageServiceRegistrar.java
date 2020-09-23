package com.penglecode.xmodule.master4j.spring.context.imports.registrar;

import com.penglecode.xmodule.common.util.ArrayUtils;
import com.penglecode.xmodule.master4j.spring.context.imports.common.MessageService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 20:38
 */
public class CompositeMessageServiceRegistrar implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

    private ListableBeanFactory beanFactory;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        System.out.println(String.format("【CompositeMessageServiceRegistrar】>>> registerBeanDefinitions(%s, %s)", importingClassMetadata, registry));
        String[] candidateNames = beanFactory.getBeanNamesForType(MessageService.class);
        String beanName = "compositeMessageService";
        if(!ArrayUtils.isEmpty(candidateNames) && !registry.containsBeanDefinition(beanName)) {
            registry.registerBeanDefinition(beanName, new RootBeanDefinition(CompositeMessageService.class));
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println(String.format("【CompositeMessageServiceRegistrar】>>> setBeanFactory(%s)", beanFactory));
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }
}

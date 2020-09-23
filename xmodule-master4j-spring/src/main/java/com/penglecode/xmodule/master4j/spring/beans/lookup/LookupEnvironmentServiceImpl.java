package com.penglecode.xmodule.master4j.spring.beans.lookup;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 基于Spring lookup方法查找bean实例机制的EnvironmentService实现
 * 注意该类是单例的，但是其所依赖的EnvironmentRepository却是prototype的
 *
 * 在该类中getEnvironmentRepository()会被CGLIB重写，每次调用该方法时都会去容器中查找bean
 * 即每次都隐式的调用getBean()方法来查找bean，此例中EnvironmentRepository是prototype，
 * 因此getEnvironmentRepository()方法每次都会返回一个新的实例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/12 19:12
 */
@Service("lookupEnvironmentService")
public class LookupEnvironmentServiceImpl extends EnvironmentService {

    @Override
    public String getEnvironment(String name) {
        return getAllEnvironments().get(name);
    }

    @Override
    public Map<String, String> getAllEnvironments() {
        EnvironmentRepository environmentRepository = getEnvironmentRepository();
        System.out.println("【StandardEnvironmentServiceImpl】>>> environmentRepository = " + environmentRepository);
        return environmentRepository.getAllEnvironments();
    }

    @Lookup
    @Override
    protected EnvironmentRepository getEnvironmentRepository() {
        return null;
    }

}

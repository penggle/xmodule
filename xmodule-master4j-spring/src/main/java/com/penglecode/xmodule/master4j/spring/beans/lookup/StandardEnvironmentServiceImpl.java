package com.penglecode.xmodule.master4j.spring.beans.lookup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 默认的EnvironmentService实现
 * 注意该类是单例的，但是其所依赖的EnvironmentRepository却是prototype的
 *
 * 在该类中虽然EnvironmentRepository是prototype的，但是一旦StandardEnvironmentServiceImpl实例化以后
 * 成员变量environmentRepository也就定了不会变了，如果想getEnvironmentRepository()每次都取一个新的EnvironmentRepository
 * 请参考LookupEnvironmentServiceImpl
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/12 19:12
 */
@Service("standardEnvironmentService")
public class StandardEnvironmentServiceImpl extends EnvironmentService {

    @Autowired
    private EnvironmentRepository environmentRepository;

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

    @Override
    protected EnvironmentRepository getEnvironmentRepository() {
        return environmentRepository;
    }

}

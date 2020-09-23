package com.penglecode.xmodule.master4j.spring.beans.lookup;

import java.util.Map;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/12 19:04
 */
public abstract class EnvironmentService {

    public abstract String getEnvironment(String name);

    public abstract Map<String,String> getAllEnvironments();

    protected abstract EnvironmentRepository getEnvironmentRepository();

}

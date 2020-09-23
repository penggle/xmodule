package com.penglecode.xmodule.master4j.spring.beans.lookup;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/12 19:07
 */
@Repository
@Scope("prototype")
public class EnvironmentRepository {

    public Map<String,String> getAllEnvironments() {
        return System.getenv();
    }

}

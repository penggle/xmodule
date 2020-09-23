package com.penglecode.xmodule.master4j.spring.context.imports.aware;

/**
 * ImportAware接口配合@Import及注解，用于设置注解的值示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 23:11
 */
public class ApiClient {

    private int autoRetries;

    public ApiClient() {
    }

    public ApiClient(int autoRetries) {
        this.autoRetries = autoRetries;
    }

    public int getAutoRetries() {
        return autoRetries;
    }

    public void setAutoRetries(int autoRetries) {
        this.autoRetries = autoRetries;
    }

    @Override
    public String toString() {
        return "ApiClient{" +
                "autoRetries=" + autoRetries +
                '}';
    }
}

package com.penglecode.xmodule.master4j.spring.context.config.property1;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/17 19:10
 */
public class AppConfig {

    @Value("${app.name}")
    private String appName;

    @Value("${app.version:${os.version}}")
    private String appVersion;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Override
    public String toString() {
        return "AppConfig{" +
                "appName='" + appName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                '}';
    }
}

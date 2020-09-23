package com.penglecode.xmodule.master4j.spring.context.config.property2;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/17 20:18
 */
public class DataSourceProperties {

    @Value("${spring.datasource.jdbcUrl}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "DataSourceProperties{" +
                "jdbcUrl='" + jdbcUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

package com.penglecode.xmodule.master4j.springboot.example.profiles;

import org.springframework.beans.factory.InitializingBean;

/**
 * Spring profile文件加载顺序及优先级示例
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/23 18:00
 */
public class AccountConfigProperties implements InitializingBean {

    private String id;

    private String username;

    private String password;

    private Double balance;

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AccountConfigProperties{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(String.format("【%s】>>> %s", "Spring profile文件加载顺序及优先级示例", this));
    }
}

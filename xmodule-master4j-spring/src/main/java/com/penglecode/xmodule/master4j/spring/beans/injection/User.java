package com.penglecode.xmodule.master4j.spring.beans.injection;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/12 15:48
 */
public class User {

    private Long userId;

    private String username;

    private String password;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}

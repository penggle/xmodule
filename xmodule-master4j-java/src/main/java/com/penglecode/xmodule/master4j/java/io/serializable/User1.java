package com.penglecode.xmodule.master4j.java.io.serializable;

import java.io.Serializable;

/**
 * 对象序列化测试数据模型
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 21:47
 */
public class User1 implements Serializable {

    /**
     * 一定要显示指定serialVersionUID的值，不指定即用默认自动生成的
     * 如果User1.java哪怕被改动一点点，都会导致serialVersionUID自动重新生成的，
     * 这样在反序列化时会遇到serialVersionUID不匹配的问题
     */
    private static final long serialVersionUID = 1L;

    private Long userId;

    private String userName;

    private String password;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User1{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

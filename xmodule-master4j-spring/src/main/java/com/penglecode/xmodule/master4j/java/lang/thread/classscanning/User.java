package com.penglecode.xmodule.master4j.java.lang.thread.classscanning;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 17:26
 */
public class User {

    static {
        System.out.println("[" + User.class.getName() + "]类被JVM类加载器[" + Thread.currentThread().getContextClassLoader() + "]加载了");
    }

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

}

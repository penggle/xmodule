package com.penglecode.xmodule.master4j.spring.common.model;

import java.io.Serializable;

/**
 * 公共的示例User模型
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/10 22:32
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private String userName;

    private String password;

    private String userType;

    private String createTime;

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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", userType='" + userType + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}

package com.penglecode.xmodule.master4j.java.io.serializable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

/**
 * 对象序列化测试数据模型
 *
 * 通过两个自定义方法void writeObject(ObjectOutputStream out)和void readObject(ObjectInputStream in)
 * 来更为精细的控制序列化和反序列化，这个是仅通过Serializable接口进行精细控制序列化反序列化的手段
 * 另一个精细控制手段就是实现Externalizable，也是一样的
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 21:47
 */
public class User3 implements Serializable {

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

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeLong(userId);
        out.writeUTF(userName);
        out.writeUTF(Base64.getEncoder().encodeToString(password.getBytes()));
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        userId = in.readLong();
        userName = in.readUTF();
        password = in.readUTF();
    }

    @Override
    public String toString() {
        return "User3{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

package com.penglecode.xmodule.master4j.java.io.serializable;

import java.io.*;

/**
 * 对象序列化测试数据模型
 *
 * 区别于Serializable接口，Externalizable继承了Serializable，该接口中定义了两个抽象方法：writeExternal()与readExternal()。
 * 当使用Externalizable接口来进行序列化与反序列化的时候需要开发人员重写writeExternal()与readExternal()方法才能实现序列化与反序列化。
 *
 * 还有一点值得注意：在使用Externalizable进行序列化的时候，在读取对象时，会调用被序列化类的无参构造器去创建一个新的对象，
 * 然后再将被保存对象的字段的值分别填充到新对象中。所以，实现Externalizable接口的类必须要提供一个public的无参的构造器。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 21:47
 */
public class User2 implements Externalizable {

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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(userId);
        out.writeUTF(userName);
        out.writeUTF(password);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        userId = in.readLong();
        userName = in.readUTF();
        password = in.readUTF();
    }

    @Override
    public String toString() {
        return "User2{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

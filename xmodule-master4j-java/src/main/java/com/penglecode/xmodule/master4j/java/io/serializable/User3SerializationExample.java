package com.penglecode.xmodule.master4j.java.io.serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * User3对象序列化测试示例
 *
 * 通过两个自定义方法void writeObject(ObjectOutputStream out)和void readObject(ObjectInputStream in)
 * 来更为精细的控制序列化和反序列化，这个是仅通过Serializable接口进行精细控制序列化反序列化的手段
 * 另一个精细控制手段就是实现Externalizable，也是一样的
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 21:49
 */
public class User3SerializationExample {

    private static final String USER1_SERIALIZATION_FILE_PATH = "d:/user3.obj";

    public static void serialize() throws Exception {
        User3 user3 = new User3();
        user3.setUserId(123L);
        user3.setUserName("Admin");
        user3.setPassword("123456");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USER1_SERIALIZATION_FILE_PATH))) {
            out.writeObject(user3);
        }
    }

    public static void deserialize() throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(USER1_SERIALIZATION_FILE_PATH))) {
            User3 user3 = (User3) in.readObject();
            System.out.println(user3); //输出的password为MTIzNDU2而不是123456，说明writeObject(..)和readObject(..)方法起作用了
        }
    }

    public static void main(String[] args) throws Exception {
        //serialize();
        deserialize();
    }

}

package com.penglecode.xmodule.master4j.java.io.serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * User1对象序列化测试示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 21:49
 */
public class User1SerializationExample {

    private static final String USER1_SERIALIZATION_FILE_PATH = "d:/user1.obj";

    public static void serialize() throws Exception {
        User1 user1 = new User1();
        user1.setUserId(123L);
        user1.setUserName("Admin");
        user1.setPassword("123456");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USER1_SERIALIZATION_FILE_PATH))) {
            out.writeObject(user1);
        }
    }

    public static void deserialize() throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(USER1_SERIALIZATION_FILE_PATH))) {
            User1 user1 = (User1) in.readObject();
            System.out.println(user1);
        }
    }

    public static void main(String[] args) throws Exception {
        //serialize();
        deserialize();
    }

}

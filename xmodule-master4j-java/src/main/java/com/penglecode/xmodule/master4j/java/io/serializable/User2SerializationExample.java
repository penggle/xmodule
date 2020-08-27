package com.penglecode.xmodule.master4j.java.io.serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * User2对象序列化测试示例
 *
 * 区别于Serializable接口，Externalizable继承了Serializable，该接口中定义了两个抽象方法：writeExternal()与readExternal()。
 * 当使用Externalizable接口来进行序列化与反序列化的时候需要开发人员重写writeExternal()与readExternal()方法才能实现序列化与反序列化。
 *
 * 还有一点值得注意：在使用Externalizable进行序列化的时候，在读取对象时，会调用被序列化类的无参构造器去创建一个新的对象，
 * 然后再将被保存对象的字段的值分别填充到新对象中。所以，实现Externalizable接口的类必须要提供一个public的无参的构造器。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 21:49
 */
public class User2SerializationExample {

    private static final String USER1_SERIALIZATION_FILE_PATH = "d:/user2.obj";

    public static void serialize() throws Exception {
        User2 user2 = new User2();
        user2.setUserId(123L);
        user2.setUserName("Admin");
        user2.setPassword("123456");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(USER1_SERIALIZATION_FILE_PATH))) {
            out.writeObject(user2);
        }
    }

    public static void deserialize() throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(USER1_SERIALIZATION_FILE_PATH))) {
            User2 user2 = (User2) in.readObject();
            System.out.println(user2);
        }
    }

    public static void main(String[] args) throws Exception {
        //serialize();
        deserialize();
    }

}

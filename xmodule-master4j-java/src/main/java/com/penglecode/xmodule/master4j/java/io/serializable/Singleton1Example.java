package com.penglecode.xmodule.master4j.java.io.serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 单例在反序列化时存在的问题：反序列化的对象与Singleton.getInstance()的==操作为false
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 22:37
 */
public class Singleton1Example {

    public static void main(String[] args) throws Exception {
        String filePath = "d:/singleton1.obj";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            Singleton1 singleton1 = Singleton1.getSingleton();
            System.out.println(singleton1);
            out.writeObject(singleton1);
        }

        Thread.sleep(10000);
        System.out.println("---------------------------------------");

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            Singleton1 singleton2 = (Singleton1) in.readObject();
            System.out.println(singleton2);
            Singleton1 singleton1 = Singleton1.getSingleton();
            System.out.println(singleton1);
            System.out.println(singleton1 == singleton2);
        }
    }

}

package com.penglecode.xmodule.master4j.java.io.serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 单例在反序列化时存在的问题：反序列化的对象与Singleton.getInstance()的==操作为false
 *
 * 解决方法，在序列化对象上加一个 Object readResolve(){...}方法，具体为什么要加，请看
 * ObjectInputStream.readObject调用栈中，在readOrdinaryObject()方法中针对这种情况加了
 * 通过以待反序列化对象的readResolve()方法返回值则为反序列化值的情况
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 22:37
 */
public class Singleton2Example {

    public static void main(String[] args) throws Exception {
        String filePath = "d:/singleton2.obj";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            Singleton2 singleton1 = Singleton2.getSingleton();
            System.out.println(singleton1);
            out.writeObject(singleton1);
        }

        Thread.sleep(10000);
        System.out.println("---------------------------------------");

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            Singleton2 singleton2 = (Singleton2) in.readObject();
            System.out.println(singleton2);
            Singleton2 singleton1 = Singleton2.getSingleton();
            System.out.println(singleton1);
            System.out.println(singleton1 == singleton2);
        }
    }

}

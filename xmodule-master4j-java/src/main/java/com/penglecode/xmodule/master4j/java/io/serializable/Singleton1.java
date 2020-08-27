package com.penglecode.xmodule.master4j.java.io.serializable;

import java.io.Serializable;

/**
 * 基于双重DCL检查的单例
 *
 * 单例在反序列化时存在的问题：反序列化的对象与Singleton.getInstance()的==操作为false
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 22:30
 */
public class Singleton1 implements Serializable {

    private static final long serialVersionUID = 1L;

    private static volatile Singleton1 singleton;

    private Singleton1(){}

    public static Singleton1 getSingleton() {
        if(singleton == null) {
            synchronized (Singleton1.class) {
                if(singleton == null) {
                    singleton = new Singleton1();
                }
            }
        }
        return singleton;
    }

}

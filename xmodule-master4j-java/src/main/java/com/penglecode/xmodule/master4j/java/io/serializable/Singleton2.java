package com.penglecode.xmodule.master4j.java.io.serializable;

import java.io.Serializable;

/**
 * 基于双重DCL检查的单例
 *
 * 单例在反序列化时存在的问题：反序列化的对象与Singleton.getInstance()的==操作为false
 *
 * 解决方法，在序列化对象上加一个 Object readResolve(){...}方法，具体为什么要加，请看
 * ObjectInputStream.readObject调用栈中，在readOrdinaryObject()方法中针对这种情况加了
 * 通过以待反序列化对象的readResolve()方法返回值则为反序列化值的情况
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 22:30
 */
public class Singleton2 implements Serializable {

    private static final long serialVersionUID = 1L;

    private static volatile Singleton2 singleton;

    private Singleton2(){}

    public static Singleton2 getSingleton() {
        if(singleton == null) {
            synchronized (Singleton2.class) {
                if(singleton == null) {
                    singleton = new Singleton2();
                }
            }
        }
        return singleton;
    }

    private Object readResolve() {
        return getSingleton();
    }

}

package com.penglecode.xmodule.master4j.jvm.chapter7.classloading.passiverefcase;

/**
 * 被动使用类字段演示一：通过子类引用父类的静态字段，不会导致子类初始化
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/19 14:37
 */
public class SubClass extends SuperClass {

    static {
        System.out.println("SubClass init!");
    }

}

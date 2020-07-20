package com.penglecode.xmodule.master4j.jvm.chapter7.classloading.passiverefcase;

/**
 * 被动使用类字段演示三：
 * 常量在编译阶段会存入调用类的常量池中，本质上没有直接引用到定义常量的类，因此不会触发定义常量的类的初始化
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/19 15:16
 */
public class NotInitializationCase3Example {

    /**
     * 不会输出：ConstClass init!
     */
    public static void main(String[] args) {
        System.out.println(ConstClass.GREETING);
    }

}

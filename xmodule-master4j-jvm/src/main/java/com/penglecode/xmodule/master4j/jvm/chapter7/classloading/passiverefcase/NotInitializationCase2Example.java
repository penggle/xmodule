package com.penglecode.xmodule.master4j.jvm.chapter7.classloading.passiverefcase;

/**
 * 被动使用类字段演示二：
 * 通过数组定义来引用类，不会触发此类的初始化
 *
 * 这也很好理解：因为下面代码仅仅是声明了一个指定长度的空数组，在JVM底层数组实际上进行了类型擦除就是个Object数组
 *
 * VM Args：-XX:+TraceClassLoading
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/19 14:56
 */
public class NotInitializationCase2Example {

    /**
     * 以下不会输出：SuperClass init!
     */
    public static void main(String[] args) {
        SuperClass[] superClasses = new SuperClass[10];
        System.out.println(superClasses.getClass());
    }

}

package com.penglecode.xmodule.master4j.jvm.chapter7.classloading.passiverefcase;

/**
 * 非主动使用类字段演示
 *
 * 在Hotspot虚拟机中加入-XX:+TraceClassLoading参数会打印出：
 *      [Loaded com.penglecode.xmodule.master4j.jvm.chapter7.classloading.passiverefcase1.SuperClass 。。。
 *      [Loaded com.penglecode.xmodule.master4j.jvm.chapter7.classloading.passiverefcase1.SubClass 。。。
 *
 *  请注意打印出Loaded了该类并不代表初始化了该类！！！Loaded该类仅仅是表明JVM加载了该类，并不代表要初始化该类！！！
 *
 * VM Args：-XX:+TraceClassLoading
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/19 14:38
 */
public class NotInitializationCase1Example {

    /**
     * 该例中只初始化了父类(value静态变量直接定义的那个类)
     * 只会输出“SuperClass init！”，而不会输出“SubClass init”
     */
    public static void main(String[] args) {
        System.out.println(SubClass.value);
    }

}

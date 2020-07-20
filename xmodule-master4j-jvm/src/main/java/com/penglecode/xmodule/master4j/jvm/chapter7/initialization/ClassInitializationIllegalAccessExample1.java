package com.penglecode.xmodule.master4j.jvm.chapter7.initialization;

/**
 * 类初始化 - 非法前向引用变量示例
 *
 * <clinit>()方法是由编译器自动收集类中的所有类变量的赋值动作和静态语句块（static{}块）中的语句合并产生的，
 * 编译器收集的顺序是由语句在源文件中出现的顺序决定的，静态语句块中只能访问到定义在静态语句块之前的变量，
 * 定义在它之后的变量，在前面的静态语句块可以赋值，但是不能访问
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/20 9:54
 */
public class ClassInitializationIllegalAccessExample1 {

    static {
        i = 1; //可以前向赋值，但不可以前向访问
        //System.out.println(i); // 不可以前向访问，编译器会报错
    }

    private static int i = 0;

}

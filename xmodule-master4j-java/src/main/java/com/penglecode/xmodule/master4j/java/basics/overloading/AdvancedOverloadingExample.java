package com.penglecode.xmodule.master4j.java.basics.overloading;

/**
 * 重载：
 * 简单说，就是方法有同样的名称，但是参数列表不相同的情形，这样的同名不同参数的方法之间，互相称之为重载方法。
 * 从JVM的角度来说，方法重载是JVM"静态分配"的体现，即JVM在编译阶段，Javac编译器就根据参数的静态类型决定了会使用哪个重载版本了
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/27 17:28
 */
public class AdvancedOverloadingExample {

    static abstract class Human {}

    static class Man extends Human {}

    static class Woman extends Human {}

    public void sayHello(Human guy) {
        System.out.println("hello,guy!");
    }

    public void sayHello(Man guy) {
        System.out.println("hello,gentleman!");
    }

    public void sayHello(Woman guy) {
        System.out.println("hello,lady!");
    }

    public static void main(String[] args) {
        Human man = new Man();
        Human woman = new Woman();
        AdvancedOverloadingExample example = new AdvancedOverloadingExample();
        /**
         * 重载方法在javac编译阶段就根据参数类型确定好了具体调用哪个方法了
         * 这里的"参数类型"指的是参数的声明类型而不是实际类型!
         */
        example.sayHello(man);
        example.sayHello(woman);
    }

}

package com.penglecode.xmodule.master4j.jvm.chapter8.methodinvoke;

/**
 * 静态分派和重载（Overload）示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/21 16:00
 */
public class StaticDispatchExample {

    static abstract class Human {}

    static class Man extends Human {}

    static class Woman extends Human {}

    public void sayHello(Human guy) {
        System.out.println("hello, guy!");
    }

    public void sayHello(Man guy) {
        System.out.println("hello, gentleman!");
    }

    public void sayHello(Woman guy) {
        System.out.println("hello, lady!");
    }

    /**
     * 代码中故意定义了两个静态类型相同，而实际类型不同的变量(man, woman)，
     * 但虚拟机（或者准确地说是编译器）在重载时是通过参数的静态类型而不是实际类型作为判定依据的
     */
    public static void main(String[] args) {
        Human man = new Man();
        Human woman = new Woman();
        StaticDispatchExample sr = new StaticDispatchExample();
        sr.sayHello(man);
        sr.sayHello(woman);
    }

}

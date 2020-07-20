package com.penglecode.xmodule.master4j.jvm.chapter7.initialization;

/**
 * <clinit>()方法执行顺序示例
 *
 * ·<clinit>()方法与类的构造函数（即在虚拟机视角中的实例构造器 <init>()方法）不同，
 * 它不需要显式地调用父类构造器，Java虚拟机会保 证在子类的<clinit>()方法执行前，
 * 父类的<clinit>()方法已经执行完毕。因此在Java虚拟机中第一个被执行的<clinit>()
 * 方法的类型肯定是java.lang.Object。
 *
 * 由于父类的<clinit>()方法先执行，也就意味着父类中定义的静态语句块要优先于子类的变量赋值操作
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/20 10:08
 */
public class ClassInitializationOrderExample1 {

    static class Parent {

        public static int A = 1;

        static {
            A = 2;
        }

    }

    static class Sub extends Parent {

        public static int B = A;

        public static int C = 3;

        static {
            /**
             * 此处输出值为2，说明子类与父类中定义的静态变量与静态块从虚拟机的视角的clinit()上看具有执行隔离性的特征
             * 而且是父类先于子类执行其clinit()方法
             */
            System.out.println(B);
        }

    }

    public static void main(String[] args) {
        System.out.println(Sub.C); //调用Sub.C触发JVM对Sub类进行类加载
    }

}

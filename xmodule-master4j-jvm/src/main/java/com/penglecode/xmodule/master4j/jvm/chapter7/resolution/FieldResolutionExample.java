package com.penglecode.xmodule.master4j.jvm.chapter7.resolution;

/**
 * 字段解析示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/19 22:10
 */
public class FieldResolutionExample {

    interface Interface0 {
        int A = 0;
    }

    interface Interface1 extends Interface0 {
        int A = 1;
    }

    interface Interface2 {
        int A = 2;
    }

    static class Parent implements Interface1 {
        public static int A = 3;
    }

    static class Sub extends Parent implements Interface2 {
        /**
         * 如果注释掉该语句，则下面的Sub.A会报错：
         * Reference to 'A' is ambiguous, both 'Parent.A' and 'Interface2.A' match
         */
        public static int A = 4;
    }

    public static void main(String[] args) {
        /**
         * 如果注释掉Sub类中的A静态变量的声明，则下面的Sub.A会报错：
         * Reference to 'A' is ambiguous, both 'Parent.A' and 'Interface2.A' match
         */
        System.out.println(Sub.A);
    }

}

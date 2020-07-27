package com.penglecode.xmodule.master4j.jvm.chapter8.dynamiclang;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * JDK7引入的java.lang.invoke.MethodHandle使用示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/22 12:22
 */
public class Java7MethodHandleExample {

    static class MyPrinter {

        public void println(String str) {
            System.out.println(str);
        }

    }

    public static MethodHandle getPrintlnMethodHandle(Object receiver) throws Exception {
        /**
         * MethodType：代表“方法类型”，包含了方法的返回值（methodType()的第一个参数）
         * 和具体参数（methodType()第二个及以后的参数）。
         */
        MethodType methodType = MethodType.methodType(void.class, String.class);
        /**
         * lookup()方法来自于MethodHandles.lookup，这句的作用是在指定类中查找符合给定的方法名称、
         * 方法类型，并且符合调用权限的方法句柄。
         *
         * 因为这里调用的是一个虚方法，按照Java语言的规则，方法第一个参数是隐式的，代表该方法的接收者，
         * 也即this指向的对象，这个参数以前是放在参数列表中进行传递，现在提供了bindTo()方法来完成这件事情。
         */
        return MethodHandles.lookup().findVirtual(receiver.getClass(), "println", methodType).bindTo(receiver);
    }

    public static void main(String[] args) throws Throwable {
        Object obj = System.currentTimeMillis() % 2 == 0 ? System.out : new MyPrinter();
        MethodHandle methodHandle = getPrintlnMethodHandle(obj);
        methodHandle.invokeExact("hello");
    }

}

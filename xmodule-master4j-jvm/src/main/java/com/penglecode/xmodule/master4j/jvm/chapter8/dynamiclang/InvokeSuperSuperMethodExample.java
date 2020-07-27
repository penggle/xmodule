package com.penglecode.xmodule.master4j.jvm.chapter8.dynamiclang;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

/**
 * 实现对super.super.xxx()方法的调用示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/22 14:18
 */
public class InvokeSuperSuperMethodExample {

    static class GrandFather {

        void thinking() {
            System.out.println("i am grandfather");
        }

    }

    static class Father extends GrandFather {

        @Override
        void thinking() {
            System.out.println("i am father");
        }

    }

    static class Son extends Father {

        @Override
        void thinking() {
            if(System.currentTimeMillis() % 2 == 0) {
                super.thinking();
            } else {
                try {
                    MethodType mt = MethodType.methodType(void.class);
                    Field lookupImpl = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                    lookupImpl.setAccessible(true);
                    MethodHandle mh = ((MethodHandles.Lookup)lookupImpl.get(null)).findSpecial(GrandFather.class, "thinking", mt, GrandFather.class);
                    mh.invoke(this);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Son().thinking();
    }

}

package com.penglecode.xmodule.master4j.jvm.chapter7.classloader;

/**
 * ClassLoader追踪示例
 *
 * 一般情况下程序中默认使用的类加载器就是 #sun.misc.Launcher$AppClassLoader，即Application Class Loader，委派调用轨迹是：
 *
 * Application Class Loader -> Extension Class Loader -> Bootstrap Class Loader
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/20 14:18
 */
public class ClassLoaderTraceExample {

    public static void traceClassLoader(ClassLoader classLoader) {
        if(classLoader != null) {
            System.out.println(classLoader);
            traceClassLoader(classLoader.getParent());
        } else {
            System.out.println("Bootstrap Class Loader represent by null");
        }
    }

    public static void main(String[] args) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader classClassLoader = ClassLoaderTraceExample.class.getClassLoader();

        System.out.println("contextClassLoader = " + contextClassLoader);
        traceClassLoader(contextClassLoader);

        System.out.println("classClassLoader = " + classClassLoader);
        traceClassLoader(classClassLoader);

        System.out.println("contextClassLoader == classClassLoader ? " + (contextClassLoader == classClassLoader));
    }

}

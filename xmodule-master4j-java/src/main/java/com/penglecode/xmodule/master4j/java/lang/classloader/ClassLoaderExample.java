package com.penglecode.xmodule.master4j.java.lang.classloader;

import com.penglecode.xmodule.common.util.FileUtils;
import com.sun.crypto.provider.AESKeyGenerator;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarFile;

/**
 * 类加载器示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/28 13:56
 */
public class ClassLoaderExample {

    /**
     * 启动类加载器（Bootstrap Class Loader）作为Java最顶层的ClassLoader，主要用来加载一些Java运行环境所必须的核心类，
     * 主要负责加载存放在<JAVA_HOME>/lib目录或者被-Xbootclasspath参数所指定的路径中存放的jar包和类
     *
     * 在JDK9之前，JDK API中并不存在Bootstrap ClassLoader
     * 而是以null来代替Bootstrap ClassLoader，具体见
     * @see ClassLoader#getParent()
     * @see Class#getClassLoader()
     */
    public static void aboutBootClassLoader() {
        System.out.println("启动类加载器（Bootstrap Class Loader）作为Java最顶层的ClassLoader，主要用来加载一些Java运行环境所必须的核心类，主要负责加载存放在<JAVA_HOME>/lib目录或者被-Xbootclasspath参数所指定的路径中存放的jar包和类：");
        String bootClassPath = System.getProperty("sun.boot.class.path");
        String[] bootJarPaths = bootClassPath.split(";");
        for(String bootJarPath : bootJarPaths) {
            System.out.println(bootJarPath);
        }

        System.out.println("---------------------随便测试一个Java核心类:java.lang.Thread类的类加载器：-------------------");

        ClassLoader coreClassLoader = Thread.class.getClassLoader();
        System.out.println(Thread.class.getName() + ".getClassLoader() = " + coreClassLoader);
        if(coreClassLoader == null) {
            System.out.println(coreClassLoader + " 则代表着最顶层的启动类加载器（Bootstrap Class Loader），只不过在在Java9之前，这个类是不存在的。");
        }

    }

    /**
     * 扩展类加载器（Extension Class Loader）：这个类加载器是在类sun.misc.Launcher$ExtClassLoader中以Java代码的形式实现的。
     * 它负责加载<JAVA_HOME>/lib/ext目录中，或者被java.ext.dirs系统变量所指定的路径中所有的类库。
     */
    public static void aboutExtClassLoader() {
        System.out.println("扩展类加载器（Extension Class Loader）：这个类加载器是在类sun.misc.Launcher$ExtClassLoader中以Java代码的形式实现的。它负责加载<JAVA_HOME>/lib/ext目录中，或者被java.ext.dirs系统变量所指定的路径中所有的类库。");
        String bootClassPath = System.getProperty("java.ext.dirs");
        String[] bootJarPaths = bootClassPath.split(";");
        for(String bootJarPath : bootJarPaths) {
            System.out.println(bootJarPath);
        }

        System.out.println("---------------------随便测试一个Java扩展类:com.sun.crypto.provider.AESKeyGenerator类的类加载器：-------------------");

        //com.sun.crypto.provider.AESKeyGenerator在<JAVA_HOME>/jre/lib/ext/sunjce_provider.jar中
        ClassLoader extClassLoader = AESKeyGenerator.class.getClassLoader();
        System.out.println(AESKeyGenerator.class.getName() + ".getClassLoader() = " + extClassLoader);
        if(extClassLoader == null) {
            System.out.println(extClassLoader + " 则代表着最顶层的启动类加载器（Bootstrap Class Loader），只不过在在Java9之前，这个类是不存在的。");
        }

        System.out.println("------------------------sun.misc.Launcher$ExtClassLoader的双亲委派序列：-----------------------");

        ClassLoader parent = extClassLoader;
        do {
            if(parent != null) {
                System.out.println(parent);
                parent = parent.getParent();
            } else {
                System.out.println("<BootClassLoader>");
                break;
            }
        } while (true);
    }

    /**
     * 应用程序类加载器（Application Class Loader）：这个类加载器由sun.misc.Launcher$AppClassLoader来实现。
     * 由于应用程序类加载器是ClassLoader类中的getSystemClassLoader()方法的返回值，所以有些场合中也称它为“系统类加载器”。
     * 它负责加载用户类路径（ClassPath）上所有的类库或者被java.class.path系统变量所指定的路径中所有的类库，开发者同样可以直接在代码中使用这个类加载器。如果应用程序中没有自定义过自己的类加载器，
     * 一般情况下这个就是程序中默认的类加载器。
     * @see ClassLoader#getSystemClassLoader()
     */
    public static void aboutAppClassLoader() {
        System.out.println("应用程序类加载器（Application Class Loader）：这个类加载器由sun.misc.Launcher$AppClassLoader来实现。它负责加载用户类路径（ClassPath）上所有的类库或者被java.class.path系统变量所指定的路径中所有的类库。");
        String javaClassPath = System.getProperty("java.class.path");
        String[] appJarPaths = javaClassPath.split(";");
        for(String appJarPath : appJarPaths) {
            System.out.println(appJarPath);
        }

        System.out.println("---------------------随便测试一个Java应用程序类:org.springframework.context.ApplicationContext类的类加载器：-------------------");

        ClassLoader appClassLoader = ApplicationContext.class.getClassLoader();
        System.out.println(ApplicationContext.class.getName() + ".getClassLoader() = " + appClassLoader);
        if(appClassLoader == null) {
            System.out.println(appClassLoader + " 则代表着最顶层的启动类加载器（Bootstrap Class Loader），只不过在在Java9之前，这个类是不存在的。");
        }

        System.out.println("------------------------sun.misc.Launcher$ExtClassLoader的双亲委派序列：-----------------------");

        ClassLoader parent = appClassLoader;
        do {
            if(parent != null) {
                System.out.println(parent);
                parent = parent.getParent();
            } else {
                System.out.println("<BootClassLoader>");
                break;
            }
        } while (true);
    }

    /**
     * 自定义类加载器步骤
     * （1）继承ClassLoader
     * （2）重写findClass()方法
     * （3）调用defineClass()方法
     *
     * 以指定的maven的本地jar包资源作为管辖范围(将相当于ExtClassLoader以<JAVA_HOME>/jre/lib/ext作为管辖范围一样)
     */
    static class CustomClassLoader extends ClassLoader {

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            return null;
        }
    }

    /**
     * 自定义的ClassLoader
     */
    public static void customClassLoader() {

    }

    public static void main(String[] args) {
        //aboutBootClassLoader();
        //aboutExtClassLoader();
        //aboutAppClassLoader();
        System.out.println();
    }

}

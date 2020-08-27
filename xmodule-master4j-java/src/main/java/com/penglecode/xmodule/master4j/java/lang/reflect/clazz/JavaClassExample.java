package com.penglecode.xmodule.master4j.java.lang.reflect.clazz;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * get系列：本类的public + 父类或接口的public（含静态方法）
 * getDeclared系列：本类所有的访问权限的元素（含静态方法）
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 14:25
 */
public class JavaClassExample {

    /**
     * 返回指定类定义的公共的内部类,以及从父类、父接口那里继承来的公共内部类
     */
    public static void getClasses() {
        System.out.println("===============getClasses()：返回指定类定义的公共的内部类,以及从父类、父接口那里继承来的公共内部类===============");
        Class<?> clazz1 = TomcatServer.class;
        Class<?>[] classes = clazz1.getClasses();
        for(Class<?> clazz : classes) {
            System.out.println(clazz);
        }
    }

    /**
     * 返回指定类中定义的公共、私有、保护的内部类(不包括父类、父接口中的)
     */
    public static void getDeclaredClasses() {
        System.out.println("===============getDeclaredClasses()：返回指定类中定义的公共、私有、保护的内部类(不包括父类、父接口中的)===============");
        Class<?> clazz1 = TomcatServer.class;
        Class<?>[] classes = clazz1.getDeclaredClasses();
        for(Class<?> clazz : classes) {
            System.out.println(clazz);
        }
    }

    /**
     * 返回一个成员内部类所在的类的Class
     * 言下之意，这个类不是个成员内部类，那么必定返回null
     */
    public static void getDeclaringClass() {
        System.out.println("===============getDeclaredClasses()：返回一个成员内部类所在的类的Class===============");
        Class<?> clazz1 = TomcatServer.class;
        System.out.println(clazz1.getDeclaringClass());

        Class<?> clazz2 = TomcatServer.DefaultTomcatInitializer.class;
        System.out.println(clazz2.getDeclaringClass());
    }

    /**
     * boolean isLocalClass()     ;//判断是不是局部类，也就是方法里面的类
     * boolean isMemberClass()    ;//判断是不是成员内部类，也就是一个类里面定义的类
     * boolean isAnonymousClass() ;//判断当前类是不是匿名类，匿名类一般用于实例化接口
     */
    public static void classBooleanApi() {

    }

    /**
     * 返回指定实体(类、接口、基本类型、void、注解)的父类
     * 1、如果该实体代表的是个类，那么getSuperclass()至少返回的是Object.class
     * 2、如果该实体代表的是接口、基本类型、void、注解，那么getSuperclass()返回的是null
     */
    public static void getSuperclass() {
        System.out.println("===============getSuperclass()：返回指定实体(类、接口、基本类型、void、注解)的父类===============");
        Class<?> clazz = TomcatServer.class;
        Class<?> superclazz;
        while((superclazz = clazz.getSuperclass()) != null) {
            System.out.println(clazz + " extends " + superclazz);
            clazz = superclazz;
        }

        System.out.println("--------------------------------------------");

        clazz = int.class;
        superclazz = clazz.getSuperclass();
        System.out.println(clazz + " extends " + superclazz);

        System.out.println("--------------------------------------------");

        clazz = void.class;
        superclazz = clazz.getSuperclass();
        System.out.println(clazz + " extends " + superclazz);

        System.out.println("--------------------------------------------");

        clazz = Serializable.class;
        superclazz = clazz.getSuperclass();
        System.out.println(clazz + " extends " + superclazz);

        System.out.println("--------------------------------------------");

        clazz = Thread.State.class;
        superclazz = clazz.getSuperclass();
        System.out.println(clazz + " extends " + superclazz);

        System.out.println("--------------------------------------------");

        clazz = Controller.class;
        superclazz = clazz.getSuperclass();
        System.out.println(clazz + " extends " + superclazz);
    }

    /**
     * 返回指定类或实现的接口列表
     * 1、如果指定类型是个类那么返回它implements接口列表
     * 2、如果指定类是个接口那么返回它extends接口列表
     */
    public static void getInterfaces() {
        System.out.println("===============getInterfaces()：返回指定类或实现的接口列表===============");
        Class<?> clazz1 = TomcatServer.class;
        Class<?>[] interfaces1 = clazz1.getInterfaces();
        System.out.println(clazz1 + " implements " + Arrays.toString(interfaces1));

        System.out.println("--------------------------------");

        Class<?> clazz2 = ServletServer.class;
        Class<?>[] interfaces2 = clazz2.getInterfaces();
        System.out.println(clazz2 + " implements " + Arrays.toString(interfaces2));

        System.out.println("--------------------------------");

        Class<?> clazz3 = SmartLifecycle.class;
        Class<?>[] interfaces3 = clazz3.getInterfaces();
        System.out.println(clazz3 + " implements " + Arrays.toString(interfaces3));
    }

    /**
     * 返回指定类的公共、私有、保护的成员字段或静态字段，但不包括从父类继承过来的
     */
    public static void getDeclaredFields() {
        System.out.println("===============getDeclaredFields()：返回指定类的公共、私有、保护的声明字段，但不包括从父类继承过来的===============");
        Class<?> clazz1 = TomcatServer.class;
        Field[] fields1 = clazz1.getDeclaredFields();
        System.out.println(Arrays.toString(fields1));
    }

    /**
     * 返回指定类的公共成员字段或静态字段，包括从父类或接口继承过来的
     */
    public static void getFields() {
        System.out.println("===============getFields()：返回指定类的公共成员字段或静态字段，包括从父类或接口继承过来的===============");
        Class<?> clazz1 = TomcatServer.class;
        Field[] fields1 = clazz1.getFields();
        System.out.println(Arrays.toString(fields1));
    }

    /**
     * 返回指定类型上的注解列表，包括父类上的注解(但是这些注解必须存在@Inherited标注)
     */
    public static void getAnnotations() {
        System.out.println("===============getAnnotations()：返回指定类型上的注解列表，包括父类上的注解(但是这些注解必须存在@Inherited标注)===============");
        Class<?> clazz1 = TomcatServer.class;
        Annotation[] annotations1 = clazz1.getAnnotations();
        System.out.println(Arrays.toString(annotations1));

        AutoRestartable autoRestartable = clazz1.getAnnotation(AutoRestartable.class);
        System.out.println(autoRestartable);
    }

    /**
     * 返回指定类型上的注解列表，不包括父类或父接口上的注解
     */
    public static void getDeclaredAnnotations() {
        System.out.println("===============getDeclaredAnnotations()：返回指定类型上的注解列表，不包括父类或父接口上的注解===============");
        Class<?> clazz1 = TomcatServer.class;
        Annotation[] annotations1 = clazz1.getDeclaredAnnotations();
        System.out.println(Arrays.toString(annotations1));

        Validated validated = clazz1.getDeclaredAnnotation(Validated.class);
        System.out.println(validated);
    }

    /**
     * 返回指定类的公共方法，包括从父类或接口继承过来的
     */
    public static void getMethods() {
        System.out.println("===============getMethods()：返回指定类的公共方法，包括从父类或接口继承过来的===============");
        Class<?> clazz1 = TomcatServer.class;
        Method[] methods1 = clazz1.getMethods();
        for(Method method : methods1) {
            System.out.println(method);
        }
    }

    /**
     * 返回指定类中的所有方法，不包括从父类或接口继承过来的
     */
    public static void getDeclaredMethods() {
        System.out.println("===============getDeclaredMethods()：返回指定类中的所有方法，不包括从父类或接口继承过来的===============");
        Class<?> clazz1 = TomcatServer.class;
        Method[] methods1 = clazz1.getDeclaredMethods();
        for(Method method : methods1) {
            System.out.println(method);
        }
    }

    public static void main(String[] args) {
        //getClasses();
        //getDeclaredClasses();
        getSuperclass();
        //getDeclaringClass();
        //getInterfaces();
        //getDeclaredFields();
        //getFields();
        //getAnnotations();
        //getDeclaredAnnotations();
        //getMethods();
        //getDeclaredMethods();
    }

}

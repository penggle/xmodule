package com.penglecode.xmodule.master4j.java.lang.thread.classscanning;

import com.penglecode.xmodule.master4j.spring.core.classscanning.Refoundable;
import org.reflections.Reflections;

import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/7 11:38
 */
public class ReflectionsExample {

    public static void scan1() {
        Reflections reflections = new Reflections("java.util");
        Set<Class<? extends Executor>> classes = reflections.getSubTypesOf(Executor.class);
        classes.stream().forEach(System.out::println);
    }

    public static void scan2() {
        Reflections reflections = new Reflections("com.penglecode.xmodule.master4j.spring.core.classscanning");
        Set<Class<? extends Refoundable>> classes = reflections.getSubTypesOf(Refoundable.class);
        classes.stream().forEach(System.out::println);
    }

    public static void main(String[] args) {
        scan1();
    }

}

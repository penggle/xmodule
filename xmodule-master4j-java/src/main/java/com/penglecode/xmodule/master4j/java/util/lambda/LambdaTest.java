package com.penglecode.xmodule.master4j.java.util.lambda;

import java.util.function.Consumer;

/**
 * lambda表达式的实现原理
 *
 * https://blog.csdn.net/MeituanTech/article/details/98566848
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/30 17:41
 */
public class LambdaTest {

    public static void main(String[] args) {
        Consumer<String> consumer = s -> System.out.println(s);
        consumer.accept("lambda");
    }

}

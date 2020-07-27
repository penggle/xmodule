package com.penglecode.xmodule.master4j.jvm.chapter10.syntacticsugar;

import java.util.*;

/**
 * Java语法糖示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/7/23 15:33
 */
public class JavaSyntacticSugarExample1 {

    public static void test1() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        int sum = 0;
        for(int i : list) {
            sum += i;
        }
        System.out.println(sum);
    }

    /**
     * 顺便说一句：合法的x == y比较，具有以下约束，否则报操作符==不能应用在x与y上
     * 1、x、y均为基本类型
     * 2、x、y有一个为Object类型
     * 3、x、y中满足x为y的子类或者子接口，或者y为x的子类或子接口，此种情况也就包含了情况2
     */
    public static void test2() {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        System.out.println(c == d);    //true
        System.out.println(e == f);    //false
        System.out.println(c == (a + b));    //true
        System.out.println(c.equals(a + b));    //true
        System.out.println(g == (a + b));    //true，这里通过反编译代码可知实际等价于：System.out.println(g == (long)(a + b))
        System.out.println(g.equals(a + b));  //false
    }

    public static void main(String[] args) {
        test2();
    }

}

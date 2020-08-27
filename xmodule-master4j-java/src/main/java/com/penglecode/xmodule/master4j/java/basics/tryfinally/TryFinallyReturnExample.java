package com.penglecode.xmodule.master4j.java.basics.tryfinally;

import java.util.Arrays;

/**
 * try finally块与return语句的示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/25 20:30
 */
public class TryFinallyReturnExample {

    /**
     * return语句返回的是原始类型
     */
    public static int returnTryFinally1() {
        int ret = 0;
        try {
            return ret; // 返回 0，finally内的修改效果不起作用
        } finally {
            ret++;
            //finally块包裹着return语句，那么必定在return之前执行
            System.out.println("finally : " + ret); //输出：finally : 1
        }
    }

    public static void returnTryFinally1Test() {
        int ret = returnTryFinally1();
        System.out.println("ret = " + ret); //输出：ret = 0
    }

    /**
     * return语句返回的是引用类型
     */
    public static int[] returnTryFinally2() {
        int[] ret = new int[] {0};
        try {
            return ret; // 返回 0，finally内的修改效果不起作用
        } finally {
            ret[0]++;
            //finally块包裹着return语句，那么必定在return之前执行
            System.out.println("finally : " + Arrays.toString(ret)); //输出：finally : [1]
        }
    }

    public static void returnTryFinally2Test() {
        int[] ret = returnTryFinally2();
        System.out.println("ret = " + Arrays.toString(ret)); //输出：ret = [1]
    }

    public static void main(String[] args) {
        //returnTryFinally1Test();
        returnTryFinally2Test();
    }

}

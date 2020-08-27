package com.penglecode.xmodule.master4j.java.util;

import java.util.Arrays;
import java.util.List;

/**
 * java.util.Arrays示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/14 18:20
 */
public class ArraysExample {

    public static void asList() {
        int[] a1 = new int[] {6, 7, 8, 9, 10};
        //Arrays.asList(T ... array)只能应用于非原始类型数组!!!
        List<Object> list1 = Arrays.asList(a1);
        System.out.println(list1);

        Integer[] a2 = {1, 2, 3, 4, 5};
        List<Integer> list2 = Arrays.asList(a2);
        System.out.println(list2);

        //Arrays.asList(T ... array)出来的List可以update
        list2.set(1, 0);
        System.out.println(list2);

        //Arrays.asList(T ... array)出来的List不能remove
        list2.remove(1); //java.lang.UnsupportedOperationException
    }

    public static void main(String[] args) {
        asList();
    }

}

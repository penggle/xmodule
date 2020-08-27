package com.penglecode.xmodule.master4j.java.basics.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于java.util.List讲解泛型的一些示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/25 16:18
 */
public class ListGenericExample {

    /**
     * Object类型的泛型List和非泛型List的区别
     */
    public static void genericListAndNoGenericList() {
        List<Object> list1 = new ArrayList<>(); //Object类型的泛型List，即任意类型的List
        List list2 = new ArrayList(); //非泛型类型的List

        list2 = list1; //合法的
        list1 = list2; //合法的

        List<String> list3 = new ArrayList<>();

        list2 = list3; //合法的
        list3 = list2; //合法的

        //list1 = list3; //不合法，两个具有不同类型的泛型List之间不能相互赋值
    }

    public static void wildcardListAndObjectList() {
        List<?> list1 = new ArrayList<>(); //未知类型的List
        List<Object> list2 = new ArrayList<>(); //任意类型的List

        /**
         * List<Object>中的Object也是已知类型，而List<?>中的?通配符代表未知类型，
         * 很明显从范围上来说List<?>所囊括的范围要比List<Object>要大，
         * 因此已知类型List可以赋值给未知类型List
         */
        list1 = list2;

        /**
         * 反过来是不可以的
         * 你可以把List<String>, List<Integer>赋值给List<?>，
         * 却不能把List<?>赋值给 List<Object>。
         */
        //list2 = list1; //不合法
    }

}

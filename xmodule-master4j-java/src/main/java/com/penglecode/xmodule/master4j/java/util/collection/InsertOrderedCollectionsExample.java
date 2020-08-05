package com.penglecode.xmodule.master4j.java.util.collection;

import java.util.*;

/**
 * 插入有序的集合示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/1 21:02
 */
public class InsertOrderedCollectionsExample {

    public static void main(String[] args) {
        String[] langs = {"Java", "Python", "C++", "C", "Go", "Rust"};

        System.out.println("==================List都是插入有序的=================");
        System.out.println("预期插入顺序：" + Arrays.toString(langs));
        List<String> list = new ArrayList<>();
        for (String lan : langs) {
            list.add(lan);
        }
        System.out.println("实际插入顺序：" + list);

        System.out.println("==================HashSet是插入无序的=================");
        System.out.println("预期插入顺序：" + Arrays.toString(langs));
        Set<String> set1 = new HashSet<>();
        for (String lan : langs) {
            set1.add(lan);
        }
        System.out.println("实际插入顺序：" + set1);

        System.out.println("==================TreeSet是插入无序的=================");
        System.out.println("预期插入顺序：" + Arrays.toString(langs));
        Set<String> set2 = new TreeSet<>();
        for (String lan : langs) {
            set2.add(lan);
        }
        System.out.println("实际插入顺序：" + set2);

        System.out.println("==================LinkedHashSet是插入有序的=================");
        System.out.println("预期插入顺序：" + Arrays.toString(langs));
        /**
         * HashSet有个非public的构造器：
         *     HashSet(int initialCapacity, float loadFactor, boolean dummy) {
         *         map = new LinkedHashMap<>(initialCapacity, loadFactor);
         *     }
         * LinkedHashSet虽然继承自HashSet，但是LinkedHashSet的构造器都继承自HashSet的上述构造器
         * 因此实际的底层map是LinkedHashMap
         */
        Set<String> set3 = new LinkedHashSet<>();
        for (String lan : langs) {
            set3.add(lan);
        }
        System.out.println("实际插入顺序：" + set3);
    }

}

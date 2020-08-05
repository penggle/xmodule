package com.penglecode.xmodule.master4j.java.util.collection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * LinkedList示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/2 22:15
 */
public class LinkedListExample {

    /**
     * 测试ArrayList和LinkedList的add(int index, Object element)性能
     *                          get(index)性能
     *
     * 调用500000次java.util.ArrayList.add(int index, Object element)方法，耗时：20169 ms
     * 调用500000次java.util.ArrayList.get(int index)方法，耗时：13 ms
     *
     * 调用500000次java.util.LinkedList.add(int index, Object element)方法，耗时：73 ms
     * 调用500000次java.util.LinkedList.get(int index)方法，耗时：672503 ms
     */
    public static void testPerformance(List<String> list, int count) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            list.add(0, String.valueOf(i));
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("调用%s次%s.add(int index, Object element)方法，耗时：%s ms", count, list.getClass().getName(), (end - start)));

        Random random = new Random();
        start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            int index = random.nextInt(count);
            list.get(index);
        }
        end = System.currentTimeMillis();
        System.out.println(String.format("调用%s次%s.get(int index)方法，耗时：%s ms", count, list.getClass().getName(), (end - start)));
    }

    public static void main(String[] args) {
        int count = 500000;
        List<String> aList = new ArrayList<>(count);
        List<String> lList = new LinkedList<>();

        //testPerformance(aList, count);
        //testPerformance(lList, count);
    }

}

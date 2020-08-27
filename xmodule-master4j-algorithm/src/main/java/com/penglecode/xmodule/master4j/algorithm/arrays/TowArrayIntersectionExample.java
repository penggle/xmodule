package com.penglecode.xmodule.master4j.algorithm.arrays;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * 算法题：两个数组的交集(350)
 * https://www.geekxh.com/1.0.%E6%95%B0%E7%BB%84%E7%B3%BB%E5%88%97/001.html
 *
 * 输入: nums1 = [1,2,2,1], nums2 = [2,2]
 * 输出: [2,2]
 *
 * 输入: nums1 = [1, 9, 2, 2, 1, 9], nums2 = [2, 2, 2, 9]
 * 输出: [2, 2, 9]
 *
 * 输入: nums1 = [1, 2, 2, 1, 2], nums2 = [2, 2, 2]
 * 输出: [2, 2, 2]
 *
 * 题解分析：
 * 首先拿到这道题，我们基本马上可以想到，此题可以看成是一道传统的映射题（map映射），
 * 为什么可以这样看呢，因为我们需找出两个数组的交集元素，同时应与两个数组中出现的次数一致。
 * 这样就导致了我们需要知道每个值出现的次数，所以映射关系就成了<元素,出现次数>。剩下的就是顺利成章的解题。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/10 13:24
 */
public class TowArrayIntersectionExample {

    public static <T> T[] intersect1(T[] array1, T[] array2) {
        int aLength1 = array1.length, aLength2 = array2.length;

        Map<T,Integer> mergedFoundCounts = new HashMap<>();

        //统计array1中各不同元素在array2中的出现次数
        Map<T,Integer> foundCounts12 = new HashMap<>();
        for(int i = 0; i < aLength1; i++) {
            int foundCount = 0;
            for(int j = 0; j < aLength2; j++) {
                if(array1[i].equals(array2[j])) {
                    foundCount++;
                }
            }
            foundCounts12.put(array1[i], foundCount); //覆盖,如果array1[0]与array1[3]相同，那么对应的foundCount也必定是相同的
        }

        //合并
        for(Map.Entry<T,Integer> entry12 : foundCounts12.entrySet()) {
            mergedFoundCounts.merge(entry12.getKey(), entry12.getValue(), Integer::min);
        }

        //统计array2中各元素在array1中的出现次数
        Map<T,Integer> foundCounts21 = new HashMap<>();
        for(int i = 0; i < aLength2; i++) {
            int foundCount = 0;
            for(int j = 0; j < aLength1; j++) {
                if(array2[i].equals(array1[j])) {
                    foundCount++;
                }
            }
            foundCounts21.put(array2[i], foundCount); //覆盖,如果array2[1]与array2[4]相同，那么对应的foundCount也必定是相同的
        }

        //合并
        for(Map.Entry<T,Integer> entry21 : foundCounts21.entrySet()) {
            mergedFoundCounts.merge(entry21.getKey(), entry21.getValue(), Integer::min);
        }

        //计算结果
        List<T> results = new ArrayList<>();
        for(Map.Entry<T,Integer> entry : mergedFoundCounts.entrySet()) {
            int count = entry.getValue();
            if(count > 0) {
                for(int i = 0; i < count; i++) {
                    results.add(entry.getKey());
                }
            }
        }

        return (T[]) results.toArray();
    }

    public static <T> T[] intersect2(T[] array1, T[] array2) {
        //统计array1中不同元素出现的次数，形成一个Map
        Map<T,Long> array1FoundCounts = Arrays.stream(array1).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        //统计array2中不同元素出现的次数，形成一个Map
        Map<T,Long> array2FoundCounts = Arrays.stream(array2).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<T> results = new ArrayList<>();
        for(Map.Entry<T,Long> entry1 : array1FoundCounts.entrySet()) {
            for(Map.Entry<T,Long> entry2 : array2FoundCounts.entrySet()) {
                if(entry1.getKey().equals(entry2.getKey())) {
                    int minFoundCounts = (int) Long.min(entry1.getValue(), entry2.getValue());
                    for(int i = 0; i < minFoundCounts; i++) {
                        results.add(entry1.getKey());
                    }
                    break;
                }
            }
        }
        return (T[]) results.toArray();
    }

    public static void intersect1Test() {
        Integer[] array1, array2;

        array1 = new Integer[]{1, 2, 2, 1};
        array2 = new Integer[]{2, 2};
        System.out.println(Arrays.toString(intersect1(array1, array2)));

        System.out.println("----------------------------");

        array1 = new Integer[]{1, 9, 2, 2, 1, 9};
        array2 = new Integer[]{2, 2, 2, 9};
        System.out.println(Arrays.toString(intersect1(array1, array2)));

        System.out.println("----------------------------");

        array1 = new Integer[]{1, 2, 2, 1, 2};
        array2 = new Integer[]{2, 2, 2};
        System.out.println(Arrays.toString(intersect1(array1, array2)));
    }

    public static void intersect2Test() {
        Integer[] array1, array2;

        array1 = new Integer[]{1, 2, 2, 1};
        array2 = new Integer[]{2, 2};
        System.out.println(Arrays.toString(intersect2(array1, array2)));

        System.out.println("----------------------------");

        array1 = new Integer[]{1, 9, 2, 2, 1, 9};
        array2 = new Integer[]{2, 2, 2, 9};
        System.out.println(Arrays.toString(intersect2(array1, array2)));

        System.out.println("----------------------------");

        array1 = new Integer[]{1, 2, 2, 1, 2};
        array2 = new Integer[]{2, 2, 2};
        System.out.println(Arrays.toString(intersect2(array1, array2)));
    }


    public static void main(String[] args) {
        System.out.println("===================================intersect1Test()====================================");
        intersect1Test();
        System.out.println("===================================intersect2Test()====================================");
        intersect2Test();
    }

}

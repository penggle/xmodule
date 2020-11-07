package com.penglecode.xmodule.master4j.algorithm.sort;

import java.util.Arrays;

/**
 *
 * 排序接口
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/12 11:33
 */
public interface Sort {

    public int[] sort(int[] array);

    static void swap(int i, int j, int[] array) {
        int temp;
        temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    static void printNumbers(int[] input) {
        System.out.println(Arrays.toString(input));
    }

}

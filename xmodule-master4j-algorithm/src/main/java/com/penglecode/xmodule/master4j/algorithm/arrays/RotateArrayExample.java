package com.penglecode.xmodule.master4j.algorithm.arrays;

import java.util.Arrays;

/**
 * 旋转数组
 *
 * 给定一个数组，将数组中的元素向右移动 k 个位置，其中 k 是非负数。
 *
 * 输入: [1,2,3,4,5,6,7] 和 k = 3
 * 输出: [5,6,7,1,2,3,4]
 *
 * 输入: [-1,-100,3,99] 和 k = 2
 * 输出: [3,99,-1,-100]
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/11 20:19
 */
public class RotateArrayExample {


    public static int[] rotate(int[] array, int k) {
        int length = array.length;
        int[] result = new int[length];
        for(int i = 0; i < length; i++) {
            int index = i + k;
            result[index >= length ? (index % length) : index] = array[i];
        }
        return result;
    }

    public static void main(String[] args) {
        int[] array1;
        array1 = new int[] {1,2,3,4,5,6,7};
        System.out.println("输入：" + Arrays.toString(array1));
        array1 = rotate(array1, 8);
        System.out.println("输出：" + Arrays.toString(array1));

        System.out.println("------------------------------------");

        array1 = new int[] {-1,-100,3,99};
        System.out.println("输入：" + Arrays.toString(array1));
        array1 = rotate(array1, 2);
        System.out.println("输出：" + Arrays.toString(array1));
    }

}

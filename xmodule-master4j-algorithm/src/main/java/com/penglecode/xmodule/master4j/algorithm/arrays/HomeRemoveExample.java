package com.penglecode.xmodule.master4j.algorithm.arrays;

import java.util.Arrays;

/**
 * 原地删除
 *
 * 给定一个数组 nums 和一个值 val，你需要原地移除所有数值等于 val 的元素，返回移除后数组的新长度。
 * 不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。
 * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
 *
 * 例如 nums = [1, 3, 8, 5, 1, 8, 8, 7, 2, 8, 0], val = 3,
 * 返回 4, nums 可能为 [1, 3, 0, 5, 1, 2, 7, null, null, null, null]
 * 你不需要考虑数组中超出新长度后面的元素。
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/13 23:12
 */
public class HomeRemoveExample {


    /**
     * 总体思想是，顺序遍历比较第i位是否是要找的value，如果是的话，
     * 那么将数组的最后一个元素覆盖过来，并且将被移过来的末尾元素处置为null
     * @param array
     * @param value
     * @return
     */
    public static int homeRemove(Integer[] array, int value) {
        int length = array.length;
        int i =0, j = length - 1; //j记录数组的逻辑末尾,这个末尾是非null的
        while (i < j) {
            if(array[i] == value) {
                array[i] = array[j]; //用逻辑末尾的元素填充出现value的i位置
                array[j--] = null; //用null填充被移走的元素位置
            } else {
                i++; //如果第i位元素值与value不匹配，直接跳过到下一个
            }
        }
        //特殊情况：如果最后一个元素(i==j时)正好也是value值，那么就直接删除
        if(array[j] == value) {
            array[j--] = null;
        }
        return j + 1; //新数组的元素个数,j是从0开始的，所以算个数时得+1
    }

    public static void main(String[] args) {
        Integer[] a1 = new Integer[] {1, 3, 8, 5, 1, 8, 8, 7, 2, 8, 0};
        System.out.println(homeRemove(a1, 8));
        System.out.println(Arrays.toString(a1));
        System.out.println("--------------------------------------------------");

        Integer[] a2 = new Integer[] {1, 3, 8, 5, 1, 8, 8, 7};
        System.out.println(homeRemove(a2, 8));
        System.out.println(Arrays.toString(a2));
    }

}

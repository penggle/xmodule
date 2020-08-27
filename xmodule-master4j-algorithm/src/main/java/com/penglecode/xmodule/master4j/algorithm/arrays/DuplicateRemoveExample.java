package com.penglecode.xmodule.master4j.algorithm.arrays;

import java.util.Arrays;

/**
 * 删除排序数组中的重复项
 *
 * 给定一个排序数组，你需要在原地删除重复出现的元素，使得每个元素只出现一次。
 * 返回移除后数组的新长度。不要使用额外的数组空间，你必须在原地修改输入数组并在使用 O(1) 额外空间的条件下完成。
 *
 * 给定数组 nums = [1,1,2],
 * 函数应该返回新的长度 2, 并且原数组 nums 的前两个元素被修改为 1, 2。
 * 你不需要考虑数组中超出新长度后面的元素。
 *
 * 给定 nums = [0,0,1,1,1,2,2,3,3,4],
 * 函数应该返回新的长度 5, 并且原数组 nums 的前五个元素被修改为 0, 1, 2, 3, 4。
 * 你不需要考虑数组中超出新长度后面的元素。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/14 13:41
 */
public class DuplicateRemoveExample {

    public static int duplicateRemove(int[] array) {
        int back = 0;
        for (int front = 1; front < array.length; front++) {
            if (array[back] != array[front]) {
                array[++back] = array[front];
            }
            System.out.println("back = " + back + ", front = " + front + ", array = " + Arrays.toString(array));
        }
        return back + 1;

    }

    public static void main(String[] args) {
        int[] a1 = new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        System.out.println(duplicateRemove(a1));
        System.out.println(Arrays.toString(a1));
    }

}

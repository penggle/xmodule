package com.penglecode.xmodule.master4j.algorithm.sort;

/**
 * 冒泡算法：通过重复地遍历要排序的列表，比较每对相邻的项目，并在顺序不正确的情况下交换它们。
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/12 11:31
 */
public class BubbleSortExample implements Sort {

    @Override
    public int[] sort(int[] array) {
        int complexity = 0;
        int n = array.length;
        for (int i = 0; i < n - 1; i++) { //外层循环控制遍历次数
            //内存循环处理单次遍历过程中比对并交换
            for (int j = 0; j < n - i - 1; j++) {
                complexity++;
                if (array[j] > array[j + 1]) {
                    Sort.swap(j, j + 1, array);
                }
            }
            Sort.printNumbers(array);
        }
        System.out.println(">>> complexity = " + complexity);
        return array;
    }

    public static void main(String[] args) {
        int[] input = {4, 2, 9, 6, 23, 12, 34, 0, 1};
        new BubbleSortExample().sort(input);
    }

}

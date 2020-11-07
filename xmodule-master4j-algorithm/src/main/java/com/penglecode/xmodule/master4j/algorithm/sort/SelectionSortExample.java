package com.penglecode.xmodule.master4j.algorithm.sort;

/**
 * 选择排序：通过给第 n 位选择一个第 n 小的数填充上去形成的算法，对于第 n 位来说，则从第 n 位开始搜索一个最小的数并记住该数的索引minIndex，
 * 在本轮搜索结束后交换第 n 位与第 minIndex 位的数，以此类推，在array.length轮选择过程中最终完成排序。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/12 12:18
 */
public class SelectionSortExample implements Sort {

    @Override
    public int[] sort(int[] array) {
        int complexity = 0;
        int n = array.length;
        for(int i = 0; i < n; i++) { //外层循环用于选择第i位的选择值
            int minIndex = i;
            for(int j = i; j < n; j++) { //内层循环从第i位开始寻找一个最小值及其minIndex
                complexity++;
                if(array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            Sort.swap(minIndex, i, array); //第i轮查找过后交换第i位和第minIndex位
            Sort.printNumbers(array);
        }
        System.out.println(">>> complexity = " + complexity);
        return array;
    }

    public static void main(String[] args) {
        int[] input = {4, 2, 9, 6, 23, 12, 34, 0, 1};
        new SelectionSortExample().sort(input);
    }

}

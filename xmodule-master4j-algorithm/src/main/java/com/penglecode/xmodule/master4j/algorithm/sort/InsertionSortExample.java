package com.penglecode.xmodule.master4j.algorithm.sort;

/**
 * 插入排序：思路是类似打扑克牌开局摸牌插牌的情形，每一步将一个待排序的记录，插入到前面已经排好序的有序序列中去，直到插完所有元素为止。
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/12 12:46
 */
public class InsertionSortExample implements Sort {

    @Override
    public int[] sort(int[] array) {
        int complexity = 0;
        int n = array.length;
        for(int i = 1; i < n; i++) { //外层循环控制排序轮数，其中i代表从第0位~第(i - 1)位为已经排好序的序列
            for(int j = i; j > 0; j--) { //内存循环将未知序的第i位插入到[0, i]开区间内的某个适当位置上
                complexity++;
                if(array[j] < array[j - 1]){
                    Sort.swap(j, j - 1, array);
                }
            }
            Sort.printNumbers(array);
        }
        System.out.println(">>> complexity = " + complexity);
        return array;
    }

    public static void main(String[] args) {
        int[] input = {4, 2, 9, 6, 23, 12, 34, 0, 1};
        new InsertionSortExample().sort(input);
    }

}

package com.penglecode.xmodule.master4j.algorithm.sort;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/12 15:53
 */
public class QuickSortExample implements Sort {

    @Override
    public int[] sort(int[] array) {
        doSort(array, 0, array.length - 1);
        return array;
    }

    protected void doSort(int[] array, int left, int right) {
        if(left >= right){ //如果left索引超过了right索引则结束拆分
            return;
        }
        //因为left/right后面都在动态变化，所以得先记录一份，后面递归再排序分段的时候需要用到
        int begin = left, end = right;
        //以最左边的数(left)为基准，即最左边的坑位腾出来了，可以充当temp交换位置
        int pivot = array[left];
        while(left < right) {
            // 先从右向左遍历找到一个比pivot小的，将其填充到temp交换位置
            while(left < right && array[right] >= pivot)
                right--;
            //赋值过后right索引处充当temp交换位置
            array[left] = array[right];

            // 再从左向右遍历找到一个比pivot大的，将其填充到temp交换位置
            while (left < right && array[left] <= pivot)
                left++;
            // 赋值过后left索引处充当temp交换位置
            array[right] = array[left];

            /**
             * 在right > left的情况下，继续执行上面的逻辑，
             * 形成以pivot为中心的左右两边整体大小分明的两个字列表：
             * array[0, left]和array[left + 1, length - 1]
             */
        }
        // 最后将pivot放到left位置。此时，left位置的左侧数值应该都比left小；
        // 而left位置的右侧数值应该都比left大。
        array[left] = pivot;

        Sort.printNumbers(array);

        //对pivot左侧的一组数值进行递归的切割，以至于将这些数值完整的排序
        doSort(array, begin, left);
        //对pivot右侧的一组数值进行递归的切割，以至于将这些数值完整的排序
        doSort(array, left + 1, end);
    }

    public static void main(String[] args) {
        int[] input = {4, 2, 9, 6, 23, 12, 34, 0, 1};
        new QuickSortExample().sort(input);
    }

}

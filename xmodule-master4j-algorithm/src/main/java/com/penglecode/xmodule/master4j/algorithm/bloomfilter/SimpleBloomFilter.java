package com.penglecode.xmodule.master4j.algorithm.bloomfilter;

import java.util.BitSet;

/**
 * 简单布隆过滤器实现
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/13 20:53
 */
public class SimpleBloomFilter {

    // 位数组的大小
    private static final int DEFAULT_SIZE = 2 << 24;

    // hash函数的种子
    private static final int[] SEEDS = new int[]{ 3, 13, 46 };

    // 位数组，数组中的元素只能是 0 或者 1
    private BitSet bits = new BitSet(DEFAULT_SIZE);

    public static void main(String[] args) {
        System.out.println(15 >> 6);
    }

}

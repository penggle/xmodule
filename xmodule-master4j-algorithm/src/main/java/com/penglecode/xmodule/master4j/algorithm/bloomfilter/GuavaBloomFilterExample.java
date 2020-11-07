package com.penglecode.xmodule.master4j.algorithm.bloomfilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * Guava版的布隆过滤器示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/13 22:22
 */
public class GuavaBloomFilterExample {

    /**
     * 测试误判率
     */
    public static void testFalseRate() {
        int size = 1000000; //预计插入数量
        double fpp = 0.01; //期望的误判率
        BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size, fpp);
        //插入数据[0, 1000000)
        for (int i = 0; i < 1000000; i++) {
            bloomFilter.put(i);
        }
        int count = 0;
        //测试[1000000, 2000000)之间的误判率
        for (int i = 1000000; i < 2000000; i++) {
            if (bloomFilter.mightContain(i)) {
                count++;
            }
        }
        System.out.println(String.format(">>> 总共的误判数: %s, 误判率: %s", count, count * 1.0 / 1000000));
    }

    public static void main(String[] args) {
        testFalseRate();
    }

}

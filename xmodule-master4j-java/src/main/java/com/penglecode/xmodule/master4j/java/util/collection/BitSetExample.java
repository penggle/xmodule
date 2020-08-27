package com.penglecode.xmodule.master4j.java.util.collection;

import java.util.BitSet;
import java.util.List;
import java.util.Random;

/**
 * BitSet类实现了一个按需增长的位向量。位Set的每一个组件都有一个boolean值。用非负的整数将BitSet的位编入索引。
 * 可以对每个编入索引的位进行测试、设置或者清除。通过逻辑与、逻辑或和逻辑异或操作，可以使用一个 BitSet修改另一个 BitSet的内容。
 * 默认情况下，set 中所有位的初始值都是false。
 * 每个位 set 都有一个当前大小，也就是该位 set 当前所用空间的位数。注意，这个大小与位 set 的实现有关，所以它可能随实现的不同而更改。
 * 位 set 的长度与位 set 的逻辑长度有关，并且是与实现无关而定义的。
 *
 * 常见的应用场景是对海量数据进行一些统计工作，比如日志分析、用户数统计等。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/16 20:17
 */
public class BitSetExample {

    /**
     * 面试题1：现在有1千万个随机数，随机数的范围在1到1亿之间。现在要求写出一种算法，将1到1亿之间没有在随机数中的数求出来
     */
    public static void interview1() {
        Random random = new Random();
        int maxValue = 100000000; //1亿
        int randomSize = 10000000; //一千万
        BitSet bitSet = new BitSet(maxValue);
        int[] randomValues = new int[randomSize];
        for(int i = 0; i < randomSize; i++) {
            int n = random.nextInt(maxValue);
            randomValues[i] = n; //随机数
            bitSet.set(n); //设置BitSet中代表随机数n的那个bit位的值为true
        }
        System.out.println("共产生了0~1亿之前的随机数 " + bitSet.cardinality() + " 个");
        for (int i = 0; i < maxValue; i++) {
            if(!bitSet.get(i)) { //输出不在上述随机数中的数
                System.out.println(i);
            }
        }
    }

    public static void main(String[] args) {
        interview1();
    }

}

package com.penglecode.xmodule.master4j.java.lang.thread;

/**
 * https://www.toutiao.com/i6861482289671438860/
 *
 * 缓存行（Cache line）
 *
 * 对计算机组成原理相对熟悉的小伙伴都知道，CPU 的速度比内存的速度高了几个数量级，为了 CPU 更快从内存中读取数据，设置了多级缓存机制，如下图所示：
 *
 * 当 CPU 运算时，首先会从 L1 缓存查找所需要的数据，如果没有找到，再去 L2 缓存中去找，以此类推，直到从内存中获取数据，这也就意味着，越长的调用链，
 * 所耗费的执行时间也越长。那是不是可以从主内存拿数据的时候，顺便多拿一些呢？这样就可以避免频繁从主内存中获取数据了。聪明的计算机科学家已经想到了这个法子，
 * 这就是缓存行的由来。缓存是由多个缓存行组成的，而每个缓存行大小通常来说，大小为 64 字节，并且每个缓存行有效地引用主内存中的一块儿地址，
 * CPU 每次从主内存中获取数据时，会将相邻的数据也一同拉取到缓存行中，这样当 CPU 执行运算时，就大大减少了与主内存的交互。
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/29 17:03
 */
public class CpuCacheLineEffectExample {

    //一维数组长度
    private static final int ARRAY1_SIZE = 1024 * 1024;

    //二维数组长度
    private static final int ARRAY2_SIZE = 8;

    private static long[][] array;

    /**
     * 考虑一般缓存行大小是64字节，一个 long 类型占8字节
     * windows 平台通过CMD命令查看缓存行大小：wmic cpu > d:/cpu.txt
     * 在cpu.txt中查找DataWidth字段的值，一般是64字节
     */
    protected static void initArray() {
        array = new long[ARRAY1_SIZE][];
        for(int i = 0; i < ARRAY1_SIZE; i++) {
            array[i] = new long[ARRAY2_SIZE];
            for(int j = 0; j < ARRAY2_SIZE; j++) {
                array[i][j] = 1L;
            }
        }
    }

    /**
     * 横向行式遍历求和
     */
    public static void sumArrayByRowTraversal() {
        long marked = System.currentTimeMillis();
        long sum = 0;
        for(int i = 0; i < ARRAY1_SIZE; i++) {
            for(int j = 0; j < ARRAY2_SIZE; j++) {
                sum += array[i][j];
            }
        }
        System.out.println("【cache line】sum = " + sum + ", loop times:" + (System.currentTimeMillis() - marked) + "ms");
    }

    /**
     * 纵向列式遍历求和
     */
    public static void sumArrayByColumnTraversal() {
        long marked = System.currentTimeMillis();
        long sum = 0;
        for(int i = 0; i < ARRAY2_SIZE; i++) {
            for(int j = 0; j < ARRAY1_SIZE; j++) {
                sum += array[j][i];
            }
        }
        System.out.println("【no cache line】sum = " + sum + ", loop times:" + (System.currentTimeMillis() - marked) + "ms");
    }

    /**
     * 可以看到，使用缓存行比没有使用缓存行的性能提升了将近 5 倍。
     */
    public static void main(String[] args) {
        System.out.println("-----------------------------------数组array初始化开始-----------------------------------");
        initArray();
        System.out.println("-----------------------------------横向行式遍历求和开始-----------------------------------");
        sumArrayByRowTraversal(); //【cache line】sum = 8388608, loop times:18ms
        System.out.println("-----------------------------------纵向列式遍历求和开始-----------------------------------");
        sumArrayByColumnTraversal(); //【no cache line】sum = 8388608, loop times:101ms
    }

}

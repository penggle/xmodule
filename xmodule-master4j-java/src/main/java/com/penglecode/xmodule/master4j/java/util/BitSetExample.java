package com.penglecode.xmodule.master4j.java.util;

import java.util.BitSet;

/**
 * BitSet示例
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/13 22:01
 */
public class BitSetExample {

    public static void main(String[] args) {
        BitSet bits1 = new BitSet(16);
        BitSet bits2 = new BitSet(16);

        for(int i=0; i<16; i++) {
            if((i%2) == 0) bits1.set(i);
            if((i%5) != 0) bits2.set(i);
        }
        System.out.println(bits1);
        System.out.println(bits2);
    }

}

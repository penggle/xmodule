package com.penglecode.xmodule.master4j.algorithm.numbers;

/**
 * 交换一个数的任意两位，使得交换后的这个数是最大的那个，(这里只考虑正数)
 *
 * 例如：1234，至多交换1次，那么最大的数应该是 4231
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/13 22:36
 */
public class BiggestNumberAfterSwapExample {

    public static int biggestNumber(int num) {
        StringBuilder sb = new StringBuilder(String.valueOf(num));
        int length = sb.length();
        int maxValue = 0, value;
        /**
         * 要想求出最大的必须从数字串的最后向前遍历每个字符，
         * 将其与第1位进行交换然后求出最大的那个
         */
        for(int i = length - 1; i >= 0; i--) {
            swap(sb, 0, i); //交换第1位与最后一位
            if((value = Integer.parseInt(sb.toString())) > maxValue) {
                maxValue = value; //再还原回去
            }
            swap(sb, i, 0);
        }
        return maxValue;
    }

    private static void swap(StringBuilder sb, int i, int j) {
        char temp = sb.charAt(i);
        sb.setCharAt(i, sb.charAt(j));
        sb.setCharAt(j, temp);
    }

    public static void main(String[] args) {
        System.out.println(biggestNumber(1234));
        System.out.println(biggestNumber(1986));
    }

}

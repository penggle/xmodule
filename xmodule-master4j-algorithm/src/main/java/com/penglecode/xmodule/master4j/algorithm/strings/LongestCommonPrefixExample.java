package com.penglecode.xmodule.master4j.algorithm.strings;

/**
 * 最长的公共前缀
 *
 * 编写一个函数来查找给定字符串数组中的最长公共前缀。如果不存在公共前缀，则返回""
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/11 17:00
 */
public class LongestCommonPrefixExample {

    public static String longestCommonPrefix(String[] strs) {
        if(strs != null && strs.length > 0) {
            int length = strs.length;
            int minOneLength = Integer.MAX_VALUE;
            int minOneIndex = -1;
            //首先循环找出最短长度的那个字符串
            for(int i = 0; i < length; i++) {
                if(strs[i].length() < minOneLength) {
                    minOneLength = strs[i].length();
                    minOneIndex = i;
                }
            }
            String minOneStr = strs[minOneIndex];

            int commonPrefixIndex = 0;
            for(; commonPrefixIndex < minOneLength; commonPrefixIndex++) {
                char charAtIndex = minOneStr.charAt(commonPrefixIndex);
                int count = 0;
                for(String str : strs) {
                    if(str.charAt(commonPrefixIndex) == charAtIndex) {
                        count++;
                    }
                }
                if(count != length) { //判断每个字符串中的第commonPrefixIndex位字符是否一样
                    break;
                }
            }
            return minOneStr.substring(0, commonPrefixIndex);
        }
        return "";
    }

    public static void main(String[] args) {
        String[] strs1 = { "flower", "flow", "flight" };
        System.out.println(longestCommonPrefix(strs1));

        String[] strs2 = { "java.lang.String", "java.lang.StringBuilder", "java.lang.StringBuffer" };
        System.out.println(longestCommonPrefix(strs2));

        String[] strs3 = { "www.taobao.com", "details.taobao.com", "www.baidu.com" };
        System.out.println(longestCommonPrefix(strs3));
    }

}

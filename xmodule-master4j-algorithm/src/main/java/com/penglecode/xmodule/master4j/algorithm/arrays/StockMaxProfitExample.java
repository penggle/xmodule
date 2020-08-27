package com.penglecode.xmodule.master4j.algorithm.arrays;

import java.util.Arrays;

/**
 * 买卖股票的最佳时机 II
 *
 * 给定一个数组，它的第 i 个元素是一支给定股票第 i 天的价格。
 * 如果你最多只允许完成一笔交易（即买入和卖出一支股票），设计一个算法来计算你所能获取的最大利润。注意你不能在买入股票前卖出股票。
 *
 * 输入: [7,1,5,3,6,4]
 * 输出: 5  (6 - 1 = 5)
 *
 * 输入: [1,2,3,4,5]
 * 输出: 4  (5 - 1 = 4)
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/11 17:59
 */
public class StockMaxProfitExample {

    public static void computeMaxProfit(int[] stockPrices) {
        int length = stockPrices.length;
        int maxProfit = 0;
        int stockBuyIndex = -1, stockSellIndex = -1;
        for(int i = 0; i < length; i++) {
            for(int j = i + 1; j < length; j++) {
                if((stockPrices[j] - stockPrices[i]) > maxProfit) {
                    maxProfit = stockPrices[j] - stockPrices[i];
                    stockBuyIndex = i;
                    stockSellIndex = j;
                }
            }
        }
        System.out.println("股票日价：" + Arrays.toString(stockPrices));
        if(stockBuyIndex == -1 && stockSellIndex == -1) {
            System.out.println("最佳收益：0");
        } else {
            System.out.println(String.format("最佳收益：第%d天买入，第%d天卖出，得最佳收益%d元", stockBuyIndex + 1, stockSellIndex + 1, stockPrices[stockSellIndex] - stockPrices[stockBuyIndex]));
        }
    }

    public static void main(String[] args) {
        computeMaxProfit(new int[] { 7,1,5,3,6,4 });
        System.out.println("-------------------------------------------------");
        computeMaxProfit(new int[] { 1,2,3,4,5 });
    }

}

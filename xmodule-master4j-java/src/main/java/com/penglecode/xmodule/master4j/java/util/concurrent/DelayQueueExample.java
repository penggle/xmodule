package com.penglecode.xmodule.master4j.java.util.concurrent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时队列DelayQueue示例
 *
 * 默认30分钟内未支付的订单将会被取消
 */
public class DelayQueueExample {

    private static final long ORDER_EXPIRING_TIME_SECONDS = TimeUnit.MINUTES.toSeconds(30);

    static class AutoClosingOrder implements Delayed {

        private final String orderId;

        private final LocalDateTime orderTime;

        public AutoClosingOrder(String orderId, LocalDateTime orderTime) {
            this.orderId = orderId;
            this.orderTime = orderTime;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long deltaInSeconds = Duration.between(orderTime, LocalDateTime.now()).getSeconds();
            return unit.convert(ORDER_EXPIRING_TIME_SECONDS - deltaInSeconds, TimeUnit.SECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            AutoClosingOrder other = (AutoClosingOrder) o;
            return orderTime.compareTo(other.orderTime);
        }

        @Override
        public String toString() {
            return "AutoClosingOrder{" +
                    "orderId='" + orderId + '\'' +
                    ", orderTime=" + orderTime +
                    '}';
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<AutoClosingOrder> orderList = new ArrayList<>();
        LocalDateTime nowTime = LocalDateTime.now();
        for(int i = 0; i < 10; i++) {
            LocalDateTime orderTime = nowTime.minusSeconds(ORDER_EXPIRING_TIME_SECONDS - (i + 1) * 60);
            AutoClosingOrder order = new AutoClosingOrder(orderTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), orderTime);
            orderList.add(order);
        }
        Collections.sort(orderList);
        orderList.forEach(order -> {
            System.out.println(">>> 生成新的订单：" + order);
        });

        System.out.println("---------------------------------------------------------------------------------------------");

        DelayQueue<AutoClosingOrder> delayQueue = new DelayQueue<>(orderList);
        AutoClosingOrder expiredOrder;
        while((expiredOrder = delayQueue.take()) != null) {
            System.out.println("<<< 取消超时订单：" + expiredOrder);
        }
    }

}

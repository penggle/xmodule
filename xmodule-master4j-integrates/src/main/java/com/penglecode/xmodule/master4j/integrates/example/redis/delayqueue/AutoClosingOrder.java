package com.penglecode.xmodule.master4j.integrates.example.redis.delayqueue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 自动关闭的订单
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/11 0:12
 */
public class AutoClosingOrder implements Delayed {

    public static final long ORDER_EXPIRING_TIME_SECONDS = TimeUnit.MINUTES.toSeconds(30);

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

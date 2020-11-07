package com.penglecode.xmodule.master4j.integrates.example.redis.delayqueue;

import com.penglecode.xmodule.common.support.NamedThreadFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * 基于Redis的延时队列
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/10 23:39
 */
public class RedisDelayQueue<T extends Delayed> implements DelayQueue<T>, InitializingBean {

    private static final TimeUnit DEFAULT_TIMEUNIT = TimeUnit.SECONDS;

    private final String topic;

    private final RedisTemplate<String,Object> redisTemplate;

    public RedisDelayQueue(String topic, RedisTemplate<String,Object> redisTemplate) {
        Assert.hasText(topic, "Parameter 'topic' must be required!");
        Assert.notNull(redisTemplate, "Parameter 'redisTemplate' must be required!");
        this.topic = topic;
        this.redisTemplate = redisTemplate;
    }

    protected void init() {
        redisTemplate.opsForValue().increment(topic);
    }

    public void put(T element) throws Exception {
        boolean success = redisTemplate.opsForZSet().add(keyOfZset(), element, element.getDelay(DEFAULT_TIMEUNIT));
        Assert.isTrue(success, String.format("Put an element(%s) to delay queue failed!", element));
    }

    public T take() throws Exception {
        return (T) redisTemplate.opsForList().leftPop(keyOfZset(), Duration.ZERO);
    }

    @Scheduled(initialDelay=5000, fixedDelay=10000)
    protected void schedule() {
        BoundZSetOperations<String,Object> boundZSetOperations = redisTemplate.boundZSetOps(keyOfZset());
    }

    protected String keyOfZset() {
        return topic + "_" + "zset";
    }

    protected String keyOfHash() {
        return topic + "_" + "hash";
    }

    protected String keyOfList() {
        return topic + "_" + "list";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.init();
    }
}

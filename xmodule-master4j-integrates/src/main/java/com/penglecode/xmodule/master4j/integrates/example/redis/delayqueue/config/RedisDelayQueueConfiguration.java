package com.penglecode.xmodule.master4j.integrates.example.redis.delayqueue.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.master4j.integrates.example.redis.delayqueue.RedisDelayQueue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.net.UnknownHostException;
import java.util.concurrent.Delayed;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/11 0:16
 */
@Configuration
@ConditionalOnProperty(name="spring.example.redis-delayqueue.enabled", havingValue="true", matchIfMissing=false)
public class RedisDelayQueueConfiguration extends AbstractSpringConfiguration {

    @Bean
    public RedisSerializer<String> defaultRedisKeySerializer() {
        return RedisSerializer.string();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public RedisSerializer<Object> defaultRedisValueSerializer() {
        ObjectMapper objectMapper = JsonUtils.createDefaultObjectMapper();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                      RedisSerializer<String> defaultRedisKeySerializer,
                                                      RedisSerializer<Object> defaultRedisValueSerializer)
            throws UnknownHostException {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //true-启用事务需要与@Transactional注解联合使用,不需要手动调mutli()和exec()
        redisTemplate.setEnableTransactionSupport(false);
        //key的序列化
        redisTemplate.setKeySerializer(defaultRedisKeySerializer);
        redisTemplate.setHashKeySerializer(defaultRedisKeySerializer);
        //value的序列化
        redisTemplate.setValueSerializer(defaultRedisValueSerializer);
        redisTemplate.setHashValueSerializer(defaultRedisValueSerializer);
        return redisTemplate;
    }

    @Bean
    public RedisDelayQueue<? extends Delayed> orderDelayQueue(RedisTemplate<String,Object> redisTemplate) {
        return new RedisDelayQueue<>("auto_closing_order", redisTemplate);
    }

}

package com.penglecode.xmodule.master4j.integrates.example.redis.delayqueue.test;

import com.penglecode.xmodule.master4j.integrates.example.boot.Master4jIntegratesApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/27 12:14
 */
@SpringBootTest(
        properties="spring.example.redis-delayqueue.enabled=true",
        classes={ Master4jIntegratesApplication.class },
        webEnvironment=WebEnvironment.NONE
)
public class RedisDelayQueueExampleTest {

    @Resource(name="redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    public void printRedisTemplate() {
        System.out.println(redisTemplate);
    }

    @Test
    public void testRedisTemplate1() {
        redisTemplate.opsForValue().set("nowTime", LocalDateTime.now().toString());
        System.out.println(LocalDateTime.now());
        Object first = redisTemplate.opsForList().leftPop("myorder-list", Duration.ZERO);
        System.out.println(first);
        System.out.println(LocalDateTime.now());
    }

}

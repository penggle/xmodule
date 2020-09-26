package com.penglecode.xmodule.master4j.springboot.example.webmvc;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/26 16:48
 */
@Service
public class AsyncExampleService {

    @Async
    public void performAsync() {
        System.out.println("【performAsync】>>> start");
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(5));
        System.out.println("【performAsync】>>> end");
    }

}

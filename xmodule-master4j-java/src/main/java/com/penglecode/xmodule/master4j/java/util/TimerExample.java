package com.penglecode.xmodule.master4j.java.util;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/9 13:03
 */
public class TimerExample {


    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println(String.format("现在时间是：%s", LocalDateTime.now()));
            }
        }, 1000, 1000);
        Thread.sleep(15000);
        timer.cancel();
    }

}

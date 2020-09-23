package com.penglecode.xmodule.master4j.spring.context.imports.common;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 20:16
 */
public class SmsMessageService implements MessageService {

    @Override
    public void sendMessage(String message, String target) {
        System.out.println(String.format("【SmsMessageService】>>> sendMessage(%s, %s)", message, target));
    }

}

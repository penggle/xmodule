package com.penglecode.xmodule.master4j.java.lang.reflect.proxy;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/24 18:22
 */
public class SmsMessageSendServiceImpl implements MessageSendService {

    @Override
    public boolean sendMessage(String target, String message) {
        System.out.println(String.format("Sending message：[%s] to [%s]", message, target));
        return true;
    }

}

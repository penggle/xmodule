package com.penglecode.xmodule.master4j.spring.context.imports.common;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 20:14
 */
public class MailMessageService implements MessageService {

    @Override
    public void sendMessage(String message, String target) {
        System.out.println(String.format("【MailMessageService】>>> sendMessage(%s, %s)", message, target));
    }

}

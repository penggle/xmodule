package com.penglecode.xmodule.master4j.spring.context.imports.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 20:20
 */
@Configuration
public class MessageServiceConfiguration {

    @Bean
    public MessageService smsMessageService() {
        return new SmsMessageService();
    }

    @Bean
    public MessageService mailMessageService() {
        return new MailMessageService();
    }

}

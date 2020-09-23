package com.penglecode.xmodule.master4j.spring.context.imports.registrar;

import com.penglecode.xmodule.common.util.CommonValidateUtils;
import com.penglecode.xmodule.master4j.spring.context.imports.common.MailMessageService;
import com.penglecode.xmodule.master4j.spring.context.imports.common.MessageService;
import com.penglecode.xmodule.master4j.spring.context.imports.common.SmsMessageService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/16 20:25
 */
public class CompositeMessageService implements MessageService, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void sendMessage(String message, String target) {
        if(CommonValidateUtils.isEmail(target)) {
            applicationContext.getBean(MailMessageService.class).sendMessage(message, target);
        } else if (CommonValidateUtils.isMobilePhone(target)) {
            applicationContext.getBean(SmsMessageService.class).sendMessage(message, target);
        } else {
            throw new UnsupportedOperationException("Unsupported target to send message!");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}

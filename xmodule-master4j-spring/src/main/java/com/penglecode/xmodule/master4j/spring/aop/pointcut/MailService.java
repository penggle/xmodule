package com.penglecode.xmodule.master4j.spring.aop.pointcut;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/19 21:26
 */
public interface MailService {

    @Proxyable
    public boolean sendTextMail(String mailTo, String title, String content);

    public boolean sendHtmlMail(String mailTo, String title, String content);

}

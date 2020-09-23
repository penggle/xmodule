package com.penglecode.xmodule.master4j.spring.aop.pointcut;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/19 21:50
 */
public class GMailServiceImpl implements MailService {

    @Override
    public boolean sendTextMail(String mailTo, String title, String content) {
        System.out.println(String.format("【%s】>>> sendTextMail(%s, %s, %s)", getClass().getSimpleName(), mailTo, title, content));
        return true;
    }

    @Override
    public boolean sendHtmlMail(String mailTo, String title, String content) {
        System.out.println(String.format("【%s】>>> sendTextMail(%s, %s, %s)", getClass().getSimpleName(), mailTo, title, content));
        return true;
    }

}

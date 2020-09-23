package com.penglecode.xmodule.master4j.spring.beans.instantiation;

import com.penglecode.xmodule.common.util.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/11 20:31
 */
public class ServerConfigPropertyEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        ServerConfig value = (ServerConfig) getValue();
        return value.getHost() + ":" + value.getPort();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if(!StringUtils.isEmpty(text)) {
            String[] values = text.split(":");
            if(values.length == 2) {
                ServerConfig value = new ServerConfig();
                value.setHost(values[0]);
                value.setPort(Integer.parseInt(values[1]));
                System.out.println(String.format("【%s】>>> Using ServerConfigPropertyEditor: %s => %s", getClass().getSimpleName(), text, value));
                setValue(value);
            }
        }
    }
}

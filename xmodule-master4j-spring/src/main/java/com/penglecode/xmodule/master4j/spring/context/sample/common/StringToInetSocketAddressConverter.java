package com.penglecode.xmodule.master4j.spring.context.sample.common;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/15 16:53
 */
@Component
public class StringToInetSocketAddressConverter implements Converter<String, InetSocketAddress> {
    @Override
    public InetSocketAddress convert(String source) {
        InetSocketAddress value = null;
        String[] array;
        if(source != null && (array = source.split(":")).length == 2) {
            value = new InetSocketAddress(array[0], Integer.parseInt(array[1]));
        }
        System.out.println(String.format("【%s】>>> Using StringToInetSocketAddressConverter: %s => %s", getClass().getSimpleName(), source, value));
        return value;
    }
}

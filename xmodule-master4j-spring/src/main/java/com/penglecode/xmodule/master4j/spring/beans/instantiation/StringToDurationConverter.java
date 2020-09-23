package com.penglecode.xmodule.master4j.spring.beans.instantiation;

import org.springframework.core.convert.converter.Converter;

import java.time.Duration;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/11 22:33
 */
public class StringToDurationConverter implements Converter<String, Duration> {

    @Override
    public Duration convert(String source) {
        Duration value = Duration.parse(source);
        System.out.println(String.format("【%s】>>> Using StringToDurationConverter: %s => %s", getClass().getSimpleName(), source, value));
        return value;
    }

}

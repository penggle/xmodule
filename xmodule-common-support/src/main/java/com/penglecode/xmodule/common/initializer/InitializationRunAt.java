package com.penglecode.xmodule.common.initializer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.ApplicationEvent;

/**
 * 初始化动作的运行时期
 * 
 * @author 	pengpeng
 * @date	2019年6月20日 下午12:32:26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface InitializationRunAt {

	Class<? extends ApplicationEvent> value();
	
}

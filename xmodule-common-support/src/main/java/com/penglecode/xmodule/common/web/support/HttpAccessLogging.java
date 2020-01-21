package com.penglecode.xmodule.common.web.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 对于打在Spring MVC控制器方法或Jersey Rest方法上该注解将进行日志记录
 * 
 * @author	  	pengpeng
 * @date	  	2014年10月16日 下午9:17:42
 * @version  	1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface HttpAccessLogging {

	/**
	 * 日志标题
	 * @return
	 */
	String title() default "";
	
	/**
	 * 日志所属模块
	 * @return
	 */
	String module() default "Unkown";
	
	/**
	 * 日志记录方式
	 * @return
	 */
	LoggingMode loggingMode() default LoggingMode.DB;
	
	/**
	 * 排除铭感字段
	 * @return
	 */
	String[] excludeParams() default {};
	
	public static enum LoggingMode {
		
		DB, FILE;
		
	}
	
}

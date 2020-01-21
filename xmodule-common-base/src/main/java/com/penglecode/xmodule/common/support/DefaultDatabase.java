package com.penglecode.xmodule.common.support;

import java.lang.annotation.*;

/**
 * 默认主库
 * 
 * @author 	pengpeng
 * @date   		2017年6月1日 下午6:34:14
 * @version 	1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DefaultDatabase {

	String value() default "";
	
}

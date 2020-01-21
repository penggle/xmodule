package com.penglecode.xmodule.common.support;

import java.lang.annotation.*;

/**
 * 分库分表
 * 
 * @author 	pengpeng
 * @date   		2017年6月1日 下午6:34:14
 * @version 	1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ShardingDatabase {

	String value() default "";
	
}

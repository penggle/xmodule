package com.penglecode.xmodule.common.codegen;

import java.lang.annotation.*;

/**
 * 数据模型注解
 * 
 * @author 	pengpeng
 * @date	2019年3月2日 上午9:28:57
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Model {

	/**
	 * 模型名称(用于自动生成代码的相关注释)
	 * @return
	 */
	String name() default "";
	
	/**
	 * 模型别名(用于Service方法名的生成)
	 * @return
	 */
	String alias() default "";
	
}

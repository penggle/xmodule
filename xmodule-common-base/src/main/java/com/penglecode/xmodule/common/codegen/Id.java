package com.penglecode.xmodule.common.codegen;

import java.lang.annotation.*;

/**
 * 数据模型ID
 * 
 * @author 	pengpeng
 * @date	2019年3月2日 上午9:28:57
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Id {

}

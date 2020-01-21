package com.penglecode.xmodule.spring.examples.core.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
@Inherited
public @interface ResponseMapping {

	HttpStatus status() default HttpStatus.INTERNAL_SERVER_ERROR;
	
	String message() default "Internal Server Error";
	
}

package com.penglecode.xmodule.common.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

/**
 * 有关Class的工具类
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月19日 下午4:00:20
 * @version  	1.0
 */
@SuppressWarnings("unused")
public class ClassUtils extends org.springframework.util.ClassUtils {

	/**
	 * 根据默认的ClassLoader来获取class
	 * @param name
	 * @return
	 */
	public static Class<?> forName(String name) {
		return resolveClassName(name, getDefaultClassLoader());
	}
	
	public static Class<?> loadClass(String name) {
		try {
			return forName(name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}

package com.penglecode.xmodule.common.util;

/**
 * 有关Class的工具类
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月19日 下午4:00:20
 * @version  	1.0
 */
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

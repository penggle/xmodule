package com.penglecode.xmodule.common.mybatis;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * mybatis mapper key工具类
 * 
 * @author	  	pengpeng
 * @date	  	2014年8月1日 下午3:54:03
 * @version  	1.0
 */
public class MybatisUtils {

    public static String getMapperKey(Class<?> mapperClass, String key) {
        return mapperClass.getName() + "Mapper." + key;
    }

    public static boolean isEmpty(Object paramObj) {
        return !isNotEmpty(paramObj);
    }

    public static boolean isNotEmpty(Object paramObj) {
        if (paramObj == null) {
            return false;
        }
        if (paramObj instanceof String) {
            String str = (String) paramObj;
            return !str.trim().equals("");
        }
        if (paramObj.getClass().isArray()) {
            return Array.getLength(paramObj) > 0;
        }
        if (paramObj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) paramObj;
            return !map.isEmpty();
        }
        if (paramObj instanceof Collection) {
            Collection<?> collection = (Collection<?>) paramObj;
            return !collection.isEmpty();
        }
        return true;
    }

    public static boolean isArrayOrCollection(Object paramObj) {
        if (paramObj == null) {
            return false;
        }
        if (paramObj instanceof Collection || paramObj.getClass().isArray()) {
            return true;
        }
        return false;
    }
    
    public static boolean isContainsParameter(Map<String,Object> paramMap, String paramName) {
    	if(paramMap != null) {
    		return paramMap.containsKey(paramName);
    	}
    	return false;
    }

}

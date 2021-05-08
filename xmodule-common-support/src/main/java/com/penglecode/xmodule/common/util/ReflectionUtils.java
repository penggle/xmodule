package com.penglecode.xmodule.common.util;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 反射工具类
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月19日 下午7:12:07
 * @version  	1.0
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

	private ReflectionUtils() {}

	/** 
     * 获得超类的参数类型，取第一个参数类型 
     * @param clazz 超类类型
     */  
    public static Class<?> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }
    
    /** 
     * 根据索引获得超类的参数类型 
     * @param clazz 超类类型 
     * @param index 索引 
     */  
    public static Class getClassGenricType(final Class clazz, final int index) {
    	Assert.notNull(clazz, "Parameter 'clazz' must be not null!");
    	Assert.state(index > -1, "Parameter 'index' must be > -1!");
        Type genType = clazz.getGenericSuperclass();  
        if (!(genType instanceof ParameterizedType)) {  
            return Object.class;  
        }  
        Type[] params = ((ParameterizedType)genType).getActualTypeArguments();  
        if (index >= params.length) {
            return Object.class;  
        }  
        if (!(params[index] instanceof Class)) {  
            return Object.class;  
        }  
        return (Class) params[index];  
    }
    
    public static Class<?> getFieldGenricType(final Field field) {
    	return getFieldGenricType(field, 0);
    }
    
    @SuppressWarnings("rawtypes")
	public static Class getFieldGenricType(final Field field, final int index) {
    	Assert.notNull(field, "Parameter 'field' must be not null!");
    	Assert.state(index > -1, "Parameter 'index' must be > -1!");
    	Type type = field.getGenericType();
    	if(type instanceof ParameterizedType){
			ParameterizedType ptype = (ParameterizedType)type;
			type = ptype.getActualTypeArguments()[index];
			if(type instanceof ParameterizedType){
				return (Class)((ParameterizedType) type).getRawType();
			}else{
				return (Class) type;
			}
		}else{
			return (Class) type;
		}
    }

	/**
	 * 返回指定类的所有字段(包括public|protected|private|default)，包括父类的
	 * @param targetClass
	 * @return
	 */
	public static Set<Field> getAllFields(Class<?> targetClass) {
		Assert.notNull(targetClass, "Parameter 'targetClass' must be not null!");
		Set<Field> allFields = new LinkedHashSet<>();
		Class<?> searchType = targetClass;
		while (searchType != null && !Object.class.equals(searchType)) {
			Field[] fields = searchType.getDeclaredFields();
			allFields.addAll(Arrays.asList(fields));
			searchType = searchType.getSuperclass();
		}
		return allFields;
	}

	/**
	 * <p>根据属性字段名称获取@{code java.lang.reflect.Field}</p>
	 * 
	 * @param targetClass
	 * @param fieldName
	 * @return
	 */
	public static Field findField(Class<?> targetClass, String fieldName) {
		Assert.notNull(targetClass, "Parameter 'targetClass' must be not null!");
		Assert.hasText(fieldName, "Parameter 'fieldName' must be not empty!");
		Class<?> searchType = targetClass;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (Field field : fields) {
				if (fieldName.equals(field.getName())) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}
	
	/**
	 * <p>在目标对象上设置属性字段的值(包括修改final字段的值)</p>
	 * 
	 * @param field
	 * @param target
	 * @param value
	 */
	public static void setFieldValue(Field field, Object target, Object value) {
		try {
			field.setAccessible(true);
			field.set(target, value);
		} catch (Exception e) {
			throw new ReflectionException(String.format("setting field's value failed by reflection! error message is: %s", e.getMessage()), e);
		}
	}
	
	/**
	 * 设置常量值
	 * @param constsClass
	 * @param fieldName
	 * @param value
	 */
	public static void setFinalConstsValue(Class<?> constsClass, String fieldName, Object value) {
		Field field = findField(constsClass, fieldName);
		setFinalFieldValue(null, field, value);
	}

	/**
	 * 修改final字段的值
	 * @param target
	 * @param fieldName
	 * @param value
	 */
	public static void setFinalFieldValue(Object target, String fieldName, Object value) {
		setFinalFieldValue(target, findField(target.getClass(), fieldName), value);
	}

	/**
	 * 修改final字段的值
	 * @param target
	 * @param field
	 * @param value
	 */
	public static void setFinalFieldValue(Object target, Field field, Object value) {
		Assert.notNull(field, "Parameter 'field' can not be null!");
    	try {
			field.setAccessible(true);
			final Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(target, value);
			//设置完后再将修饰符改回去
			modifiersField.setInt(field, field.getModifiers() | Modifier.FINAL);
			modifiersField.setAccessible(false);
			field.setAccessible(false);
		} catch (Exception e) {
			throw new ReflectionException(String.format("modify the value of final field failed by reflection! error message is: %s", e.getMessage()), e);
		}  
    }
	
	/**
	 * <p>在目标对象上获取属性字段的值</p>
	 * 
	 * @param field
	 * @param target
	 * @return
	 */
	public static <T> T getFieldValue(Field field, Object target) {
		Assert.notNull(field, "Parameter 'field' can not be null!");
		try {
			field.setAccessible(true);
			return (T) field.get(target);
		} catch (Exception e) {
			throw new ReflectionException(String.format("getting field's value failed by reflection! error message is: %s", e.getMessage()), e);
		}
	}
	
	/**
	 * <p>在目标对象上获取属性字段的值</p>
	 * 
	 * @param target
	 * @param fieldName
	 * @return
	 */
	public static <T> T getFieldValue(Object target, String fieldName) {
		Field field = findField(target.getClass(), fieldName);
		return getFieldValue(field, target);
	}
	
	/**
	 * <p>根据方法名字及入参在类中查找方法</p>
	 * 
	 * @param targetClass
	 * @param methodName
	 * @param paramTypes
	 * @return
	 */
	public static Method findMethod(Class<?> targetClass, String methodName, Class<?>... paramTypes) {
		Assert.notNull(targetClass, "Parameter 'targetClass' can not be null!");
		Assert.hasText(methodName, "Parameter 'methodName' can not be empty!");
		Class<?> searchType = targetClass;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
			for (Method method : methods) {
				if (methodName.equals(method.getName())
						&& (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}
	
	/**
	 * <p>根据方法名字在类中查找其无参方法</p>
	 * 
	 * @param targetClass
	 * @param methodName
	 * @return
	 */
	public static Method findMethod(Class<?> targetClass, String methodName) {
		return findMethod(targetClass, methodName, new Class<?>[0]);
	}
	
	/**
	 * Determine whether the given method is a "getClass" method.
	 * @see Object#getClass()
	 * @param method
	 * @return 
	 */
	public static boolean isGetClassMethod(@Nullable Method method) {
		return (method != null && "getClass".equals(method.getName()) && method.getParameterCount() == 0);
	}
	
	/**
	 * Determine whether the given method is a "clone" method.
	 * @see Object#clone()
	 * @param method
	 * @return 
	 */
	public static boolean isCloneMethod(@Nullable Method method) {
		return (method != null && "clone".equals(method.getName()) && method.getParameterCount() == 0);
	}
	
	/**
	 * Determine whether the given method is a "notify" method.
	 * @see Object#notify()
	 * @param method
	 * @return 
	 */
	public static boolean isNotifyMethod(@Nullable Method method) {
		return (method != null && "notify".equals(method.getName()) && method.getParameterCount() == 0);
	}
	
	/**
	 * Determine whether the given method is a "notifyAll" method.
	 * @see Object#notify()
	 * @param method
	 * @return 
	 */
	public static boolean isNotifyAllMethod(@Nullable Method method) {
		return (method != null && "notifyAll".equals(method.getName()) && method.getParameterCount() == 0);
	}
	
	/**
	 * Determine whether the given method is a "wait" method.
	 * @see Object#wait()
	 * @param method
	 * @return 
	 */
	public static boolean isWaitMethod(@Nullable Method method) {
		return (method != null && "wait".equals(method.getName()) && (method.getParameterCount() == 0 || method.getParameterCount() == 1 || method.getParameterCount() == 2));
	}
	
	/**
	 * Determine whether the given method is a "finalize" method.
	 * @see Object#finalize()
	 * @param method
	 * @return 
	 */
	public static boolean isFinalizeMethod(@Nullable Method method) {
		return (method != null && "finalize".equals(method.getName()) && method.getParameterCount() == 0);
	}
	
	public static class ReflectionException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public ReflectionException(String message, Throwable cause) {
			super(message, cause);
		}

		public ReflectionException(String message) {
			super(message);
		}

		public ReflectionException(Throwable cause) {
			super(cause);
		}
		
	}
	
}
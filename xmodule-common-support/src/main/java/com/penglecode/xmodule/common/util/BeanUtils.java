package com.penglecode.xmodule.common.util;

import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * bean与bean之间、bean与map之间的转换
 * 
 * @author 	pengpeng
 * @date	2019年1月30日 上午10:19:27
 */
@SuppressWarnings({"unchecked", "deprecation"})
public class BeanUtils extends org.springframework.beans.BeanUtils {

	/**
	 * bean转为map
	 * @param bean
	 * @return
	 */
	public static Map<String,Object> beanToMap(Object bean) {
		return BeanMap.create(bean);
	}
	
	/**
	 * map转为bean
	 * @param beanType
	 * @param properties
	 * @return
	 */
	public static <T> T mapToBean(Class<T> beanType, Map<String,Object> properties) {
		T target = instantiate(beanType);
		populate(target, properties);
		return target;
	}
	
	/**
	 * 将Map形式的属性值(properties)填充到bean中去
	 * @param bean
	 * @param properties
	 */
	public static void populate(Object bean, Map<String,Object> properties) {
		if(!CollectionUtils.isEmpty(properties)) {
			BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
			for(Map.Entry<String,Object> property : properties.entrySet()) {
				if(beanWrapper.isWritableProperty(property.getKey())) {
					try {
						beanWrapper.setPropertyValue(property.getKey(), property.getValue());
					} catch (NotWritablePropertyException e) {
					}
				}
			}
		}
	}
	
	/**
	 * Bean深度克隆
	 * @param bean
	 * @return
	 */
	public static <T> T deepClone(T bean) {
		Assert.notNull(bean, "Parameter 'bean' can not be null!");
		Class<T> clazz = (Class<T>) bean.getClass();
		String json = JsonUtils.object2Json(bean);
		return JsonUtils.json2Object(json, clazz);
	}
	
}

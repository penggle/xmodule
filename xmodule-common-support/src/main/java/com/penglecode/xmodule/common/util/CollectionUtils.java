package com.penglecode.xmodule.common.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 集合类型的工具类
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月19日 下午3:38:21
 * @version  	1.0
 */
public class CollectionUtils {

	/**
	 * <p>判断Collection<?>集合是否为空</p>
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection<?> collection){
		return collection == null || collection.isEmpty();
	}
	
	/**
	 * <p>判断Map<?,?>集合是否为空</p>
	 * 
	 * @param map
	 * @return
	 */
	public static boolean isEmpty(Map<?,?> map){
		return map == null || map.isEmpty();
	}
	
	/**
	 * <p>判断Collection<?>集合中是否含有null元素</p>
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean containsNull(Collection<?> collection){
		if(!isEmpty(collection)){
			for(Object obj : collection){
				if(obj == null){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * <p>过滤Collection<?>集合中的null元素</p>
	 * 
	 * @param collection
	 */
	public static void filterNull(Collection<?> collection){
		if(!isEmpty(collection)){
			for(Iterator<?> it = collection.iterator(); it.hasNext();){
				if(it.next() == null){
					it.remove();
				}
			}
		}
	}
	
	/**
	 * <p>如果collection为null/empty则返回defaultValue否则原值返回</p>
	 * @param collection
	 * @param defaultValue
	 * @return
	 */
	public static <T> Collection<T> defaultIfEmpty(Collection<T> collection, Collection<T> defaultValue) {
		return isEmpty(collection) ? defaultValue : collection;
	}
	
	/**
	 * <p>如果collection为null/empty则返回defaultValue否则原值返回</p>
	 * @param collection
	 * @param defaultValue
	 * @return
	 */
	public static <T> List<T> defaultIfEmpty(List<T> collection, List<T> defaultValue) {
		return isEmpty(collection) ? defaultValue : collection;
	}
	
	/**
	 * <p>如果collection为null/empty则返回defaultValue否则原值返回</p>
	 * @param collection
	 * @param defaultValue
	 * @return
	 */
	public static <T> Set<T> defaultIfEmpty(Set<T> collection, Set<T> defaultValue) {
		return isEmpty(collection) ? defaultValue : collection;
	}
	
	/**
	 * <p>如果map为null/empty则返回defaultValue否则原值返回</p>
	 * @param map
	 * @param defaultValue
	 * @return
	 */
	public static <K,V> Map<K,V> defaultIfEmpty(Map<K,V> map, Map<K,V> defaultValue) {
		return isEmpty(map) ? defaultValue : map;
	}
	
}

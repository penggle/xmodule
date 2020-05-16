package com.penglecode.xmodule.common.util;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * 数据模型转换工具类
 * 
 * @author 	pengpeng
 * @date 	2020年3月24日 下午2:09:05
 */
public class ModelConvertUtils {

	/**
	 * 将数据模型转换为Map
	 * @param <T>
	 * @param model
	 * @param converter
	 * @param defaultValue
	 * @return
	 */
	public static <T extends BaseModel<T>> Map<String,Object> convert(T model, Function<T,Map<String,Object>> converter, Map<String,Object> defaultValue) {
		Assert.notNull(converter, "Convert function 'converter' can not be null!");
		if(model != null) {
			return converter.apply(model);
		}
		return null;
	}
	
	/**
	 * 将数据模型转换为Map
	 * @param <T>
	 * @param modelList
	 * @param converter
	 * @param defaultValue
	 * @return
	 */
	public static <T extends BaseModel<T>> List<Map<String,Object>> convert(List<T> modelList, Function<T,Map<String,Object>> converter, List<Map<String,Object>> defaultValue) {
		if(!CollectionUtils.isEmpty(modelList)) {
			return modelList.stream().map(converter).collect(Collectors.toList());
		}
		return defaultValue;
	}
	
}

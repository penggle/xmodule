package com.penglecode.xmodule.common.util;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * 数据模型中字段值解码
 * 例如将字段中的 typeCode=1进行解码转义得到typeName="字面意义"
 * 
 * @author 	pengpeng
 * @date   		2017年6月6日 下午4:45:57
 * @version 	1.0
 */
public class ModelDecodeUtils {

	public static <T extends BaseModel<T>> T decodeModel(T model) {
		return model != null ? model.decode() : null;
	}
	
	public static <T extends BaseModel<T>> List<T> decodeModel(List<T> modelList) {
		Optional.ofNullable(modelList).ifPresent(models -> {
			models.stream().forEach(model -> model.decode());
		});
		return modelList;
	}
	
	public static <T> T decodeModel(T model, Consumer<T> consumer) {
		Optional.ofNullable(model).ifPresent(consumer);
		return model;
	}

	public static <T> List<T> decodeModel(List<T> modelList, Consumer<T> consumer) {
		Optional.ofNullable(modelList).ifPresent(models -> {
			models.stream().forEach(consumer);
		});
		return modelList;
	}

}

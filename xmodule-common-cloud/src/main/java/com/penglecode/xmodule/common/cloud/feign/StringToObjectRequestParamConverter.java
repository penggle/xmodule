package com.penglecode.xmodule.common.cloud.feign;

import java.util.Collections;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.web.bind.annotation.RequestParam;

import com.penglecode.xmodule.common.exception.ApplicationRuntimeException;
import com.penglecode.xmodule.common.util.JsonUtils;

/**
 * feign-client在解析@RequestParam注解的复杂对象时，在spring-mvc收到请求时将String反序列化为对象的转换器
 * 
 * 示例：
 * spring-mvc收到请求时的url: http://ip:port/search?condition={...}
 * 
 * @GetMapping(value="/search")
 * public Object searchList(@RequestParam Condition condition) { //该Converter将JSON字符串形式的Condition转换为对象Condition
 * 		...
 * }
 * 
 * @author 	pengpeng
 * @date	2019年5月22日 下午3:52:17
 */
public class StringToObjectRequestParamConverter implements ConditionalGenericConverter {

	private static final TypeDescriptor STRING_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);
	
	public StringToObjectRequestParamConverter() {
		super();
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, Object.class));
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		try {
			if(source != null && JsonUtils.isJsonObject(source.toString())) {
				return JsonUtils.json2Object(source.toString(), targetType.getObjectType());
			}
			return null;
		} catch (Exception e) {
			throw new ApplicationRuntimeException(e);
		}
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		if(STRING_TYPE_DESCRIPTOR.equals(sourceType)) {
			Class<?> clazz = targetType.getObjectType();
			if(!BeanUtils.isSimpleProperty(clazz)) {
				return targetType.hasAnnotation(RequestParam.class);
			}
		}
		return false;
	}

}

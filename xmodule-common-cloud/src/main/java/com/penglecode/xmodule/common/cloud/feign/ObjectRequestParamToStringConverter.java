package com.penglecode.xmodule.common.cloud.feign;

import java.util.Collections;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.penglecode.xmodule.common.exception.ApplicationRuntimeException;
import com.penglecode.xmodule.common.util.JsonUtils;

/**
 * feign-client在解析@RequestParam注解的复杂对象时，feign-client发起请求时将对象序列化为String的转换器
 * 
 * 示例：
 * @GetMapping(value="/search")
 * public Object searchList(@RequestParam Condition condition);
 * 
 * feign-client在发起请求时的url: http://ip:port/search?condition={...}
 * 
 * @author 	pengpeng
 * @date	2019年5月22日 下午3:52:17
 */
public class ObjectRequestParamToStringConverter implements ConditionalGenericConverter {

	private static final TypeDescriptor STRING_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);
	
	private final ObjectMapper objectMapper;
	
	public ObjectRequestParamToStringConverter() {
		super();
		this.objectMapper = JsonUtils.createDefaultObjectMapper();
		this.objectMapper.setSerializationInclusion(Include.NON_EMPTY);
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(Object.class, String.class));
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		try {
			return objectMapper.writeValueAsString(source);
		} catch (Exception e) {
			throw new ApplicationRuntimeException(e);
		}
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		if(STRING_TYPE_DESCRIPTOR.equals(targetType)) {
			Class<?> clazz = sourceType.getObjectType();
			if(!BeanUtils.isSimpleProperty(clazz)) {
				return sourceType.hasAnnotation(RequestParam.class);
			}
		}
		return false;
	}

}

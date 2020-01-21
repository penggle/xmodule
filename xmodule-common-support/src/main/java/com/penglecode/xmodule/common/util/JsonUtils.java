package com.penglecode.xmodule.common.util;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.penglecode.xmodule.common.support.CustomObjectMapper;

public class JsonUtils {

	public static final String DEFAULT_EMPTY_JSON_OBJECT = "{}";
	
	public static final String DEFAULT_EMPTY_JSON_ARRAY = "[]";
	
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private static final ObjectMapper defaultObjectMapper = createDefaultObjectMapper();
	
	/**
	 * 对象转json字符串
	 * @param object
	 * @return
	 */
	public static String object2Json(Object object) {
		return object2Json(defaultObjectMapper, object);
	}
	
	/**
	 * 对象转json字符串
	 * @param objectMapper
	 * @param object
	 * @return
	 */
	public static String object2Json(ObjectMapper objectMapper, Object object) {
		try {
			if(object != null) {
				return objectMapper.writeValueAsString(object);
			}
			return null;
		} catch (JsonProcessingException e) {
			throw new JacksonJsonException(e);
		}
	}
	
	/**
	 * json字符串转普通javabean
	 * @param <T>
	 * @param json
	 * @param clazz		- 注：clazz所指对象存在泛型,例如 Result<User> 则转换后User的实际类型是个Map,此类情况应该使用TypeReference进行转换
	 * @return
	 */
	public static <T> T json2Object(String json, Class<T> clazz) {
		return json2Object(defaultObjectMapper, json, clazz);
	}
	
	/**
	 * json字符串转普通javabean
	 * @param <T>
	 * @param objectMapper
	 * @param json
	 * @param clazz		- 注：clazz所指对象存在泛型,例如 Result<User> 则转换后User的实际类型是个Map,此类情况应该使用TypeReference进行转换
	 * @return
	 */
	public static <T> T json2Object(ObjectMapper objectMapper, String json, Class<T> clazz) {
		try {
			if(!StringUtils.isEmpty(json)) {
				return objectMapper.readValue(json, clazz);
			}
			return null;
		} catch (Exception e) {
			throw new JacksonJsonException(e);
		}
	}
	
	/**
	 * json字符串转泛型类对象
	 * 示例： List<User> userList = json2Object("[{"username":"jack","accounts":[{"accountId":"","amount":1200.00},...]},...]", new TypeReference<List<User>>(){});
	 * 		  Result<User> result = json2Object("{"success": true, "message": "OK", data: {"userId": 12345, "userName": "jack"}}", new TypeReference<Result<User>>(){});
	 * @param <T>
	 * @param json
	 * @param typeReference
	 * @return
	 */
	public static <T> T json2Object(String json, TypeReference<T> typeReference) {
		return json2Object(defaultObjectMapper, json, typeReference);
	}
	
	/**
	 * json字符串转泛型类对象
	 * 示例： List<User> userList = json2Object("[{"username":"jack","accounts":[{"accountId":"","amount":1200.00},...]},...]", new TypeReference<List<User>>(){});
	 * 		  Result<User> result = json2Object("{"success": true, "message": "OK", data: {"userId": 12345, "userName": "jack"}}", new TypeReference<Result<User>>(){});
	 * @param <T>
	 * @param objectMapper
	 * @param json
	 * @param typeReference
	 * @return
	 */
	public static <T> T json2Object(ObjectMapper objectMapper, String json, TypeReference<T> typeReference) {
		try {
			if(!StringUtils.isEmpty(json)) {
				return objectMapper.readValue(json, typeReference);
			}
			return null;
		} catch (Exception e) {
			throw new JacksonJsonException(e);
		}
	}
	
	/**
	 * 创建根JsonNode
	 * @param json
	 * @return
	 */
	public static JsonNode createRootJsonNode(String json) {
		return createRootJsonNode(defaultObjectMapper, json);
	}
	
	/**
	 * 创建根JsonNode
	 * @param objectMapper
	 * @param json
	 * @return
	 */
	public static JsonNode createRootJsonNode(ObjectMapper objectMapper, String json) {
		try {
			return objectMapper.readTree(json);
		} catch (IOException e) {
			throw new JacksonJsonException(e.getMessage(), e);
		}
	}
	
	/**
	 * 判断json字符串是否是JSON对象
	 * @param json
	 * @return
	 */
	public static boolean isJsonObject(String json) {
		if(json != null) {
			return json.startsWith("{") && json.endsWith("}");
		}
		return false;
	}
	
	/**
	 * 判断json字符串是否是JSON数组
	 * @param json
	 * @return
	 */
	public static boolean isJsonArray(String json) {
		if(json != null) {
			return json.startsWith("[") && json.endsWith("]");
		}
		return false;
	}
	
	/**
	 * 判断json字符串是否是JSON Object数组
	 * @param json
	 * @return
	 */
	public static boolean isJsonObjectArray(String json) {
		boolean b = isJsonArray(json);
		if(b) {
			json = StringUtils.trim(StringUtils.strip(json, "[]"));
			return json.startsWith("{") && json.endsWith("}");
		}
		return false;
	}
	
	/**
	 * 创建默认配置的ObjectMapper
	 * @return
	 */
	public static ObjectMapper createDefaultObjectMapper() {
		ObjectMapper objectMapper = new CustomObjectMapper();
		//通过以下三项配置来开启仅以属性字段来序列化和反序列化对象(忽略get方法)
		//objectMapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
		//objectMapper.disable(MapperFeature.AUTO_DETECT_IS_GETTERS);
		//objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		//将被序列化对象的类名作为一个字段(字段名@class)输出到序列化后的JSON字符串中
		//objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
		// 建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用
		//defaultObjectMapper.setSerializationInclusion(Include.NON_DEFAULT);
		//去掉默认的时间戳格式
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		//设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//objectMapper.getDeserializationConfig().getDateFormat().setTimeZone(zone);
		objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		//单引号处理,允许单引号
		objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		//允许wrap/unwrap
		objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
		
		objectMapper.registerModule(new Jdk8Module());
		objectMapper.registerModule(new JavaTimeModule());
		return objectMapper;
	}
	
	public static ObjectMapper getDefaultObjectMapper() {
		return defaultObjectMapper;
	}

	public static class JacksonJsonException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public JacksonJsonException(String message, Throwable cause) {
			super(message, cause);
		}

		public JacksonJsonException(String message) {
			super(message);
		}

		public JacksonJsonException(Throwable cause) {
			super(cause);
		}
		
	}
	
}
package com.penglecode.xmodule.common.util;

import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.MapFunction;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingException;

/**
 * JsonPath框架来操作Json字符串
 * 
 * @author 	pengpeng
 * @date	2019年1月18日 上午11:16:33
 */
public class JsonPathUtils {

	private static final ObjectMapper defaultObjectMapper = JsonUtils.createDefaultObjectMapper();
	
	static {
		defaultObjectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		defaultObjectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
	}
	
	private static final Configuration defaultConfiguration = Configuration.builder()
			.jsonProvider(new JacksonJsonProvider(defaultObjectMapper))
			.mappingProvider(new CustomJacksonMappingProvider(defaultObjectMapper)).build();
	
	public static DocumentContext parseJson(String json) {
		Assert.isTrue(JsonUtils.isJsonObject(json) || JsonUtils.isJsonArray(json), "Parameter 'json' must be a JSON string!");
		return parseJson(json, defaultConfiguration);
	}
	
	public static DocumentContext parseJson(String json, Configuration configuration) {
		Assert.isTrue(JsonUtils.isJsonObject(json) || JsonUtils.isJsonArray(json), "Parameter 'json' must be a JSON string!");
		Assert.notNull(configuration, "Parameter 'configuration' must be required!");
		return JsonPath.parse(json, configuration);
	}
	
	public static <T> T read(String json, String path, Class<T> valueType, Predicate... filters) {
		return read(parseJson(json), path, valueType, filters);
	}
	
	public static <T> T read(DocumentContext context, String path, Class<T> valueType, Predicate... filters) {
		Assert.notNull(context, "Parameter 'context' must be required!");
		Assert.hasText(path, "Parameter 'path' must be required!");
		Assert.notNull(valueType, "Parameter 'valueType' must be required!");
		return context.read(path, valueType, filters);
	}
	
	public static <T> T read(String json, String path, TypeRef<T> typeRef) {
		return read(parseJson(json), path, typeRef);
	}
	
	public static <T> T read(DocumentContext context, String path, TypeRef<T> typeRef) {
		Assert.notNull(context, "Parameter 'context' must be required!");
		Assert.hasText(path, "Parameter 'path' must be required!");
		Assert.notNull(typeRef, "Parameter 'typeRef' must be required!");
		return context.read(path, typeRef);
	}
	
	public static void add(DocumentContext context, String path, Object value, Predicate... filters) {
		Assert.notNull(context, "Parameter 'context' must be required!");
		Assert.hasText(path, "Parameter 'path' must be required!");
		context.add(path, value, filters);
	}
	
	public static void add(String json, String path, Object value, Predicate... filters) {
		add(parseJson(json), path, value, filters);
	}
	
	public static void set(DocumentContext context, String path, Object value, Predicate... filters) {
		Assert.notNull(context, "Parameter 'context' must be required!");
		Assert.hasText(path, "Parameter 'path' must be required!");
		context.set(path, value, filters);
	}
	
	public static void set(String json, String path, Object value, Predicate... filters) {
		set(parseJson(json), path, value, filters);
	}
	
	public static void put(DocumentContext context, String path, String key, Object value, Predicate... filters) {
		Assert.notNull(context, "Parameter 'context' must be required!");
		Assert.hasText(path, "Parameter 'path' must be required!");
		Assert.notNull(key, "Parameter 'key' must be required!");
		context.put(path, key, value, filters);
	}
	
	public static void put(String json, String path, String key, Object value, Predicate... filters) {
		put(parseJson(json), path, key, value, filters);
	}
	
	public static void delete(DocumentContext context, String path, Predicate... filters) {
		Assert.notNull(context, "Parameter 'context' must be required!");
		Assert.hasText(path, "Parameter 'path' must be required!");
		context.delete(path, filters);
	}
	
	public static void delete(String json, String path, Predicate... filters) {
		delete(parseJson(json), path, filters);
	}
	
	public static void map(DocumentContext context, String path, MapFunction mapFunction, Predicate... filters) {
		Assert.notNull(context, "Parameter 'context' must be required!");
		Assert.hasText(path, "Parameter 'path' must be required!");
		Assert.notNull(mapFunction, "Parameter 'mapFunction' must be required!");
		context.map(path, mapFunction, filters);
	}
	
	public static void map(String json, String path, MapFunction mapFunction, Predicate... filters) {
		map(parseJson(json), path, mapFunction, filters);
	}
	
	@SuppressWarnings("unchecked")
	public static class CustomJacksonMappingProvider extends JacksonMappingProvider {

		private final ObjectMapper objectMapper;
		
		public CustomJacksonMappingProvider() {
			super();
			this.objectMapper = new ObjectMapper();
		}

		public CustomJacksonMappingProvider(ObjectMapper objectMapper) {
			super(objectMapper);
			this.objectMapper = objectMapper;
		}

		@Override
		public <T> T map(Object source, Class<T> targetType, Configuration configuration) {
			try {
				return super.map(source, targetType, configuration);
			} catch (MappingException e) {
				if(String.class.equals(targetType)) {
					try {
						return (T) objectMapper.writeValueAsString(source);
					} catch (JsonProcessingException ex) {
						throw new MappingException(ex);
					}
				}
				throw e;
			}
		}

		public ObjectMapper getObjectMapper() {
			return objectMapper;
		}
		
	}
	
}

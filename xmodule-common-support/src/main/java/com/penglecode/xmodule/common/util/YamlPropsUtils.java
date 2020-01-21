package com.penglecode.xmodule.common.util;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsSchema;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.penglecode.xmodule.common.exception.ApplicationRuntimeException;
/**
 * YAML文件 - javaMap - Properties文件三者之间的转换
 * 
 * @author 	pengpeng
 * @date	2019年6月6日 下午6:18:59
 */
@SuppressWarnings({ "unchecked", "deprecation" })
public class YamlPropsUtils {

	private static final ObjectMapper yamlObjectMapper;
	
	private static final ObjectMapper propsObjectMapper;
	
	private static final JavaPropsSchema propsFormatSchema;
	
	static {
		YAMLFactory yamlFactory = new YAMLFactory()
		        .configure(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID, false)
		        .configure(YAMLGenerator.Feature.MINIMIZE_QUOTES, false)
		        .configure(YAMLGenerator.Feature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS, true)
		        .configure(YAMLGenerator.Feature.WRITE_DOC_START_MARKER, false);
		yamlObjectMapper = new ObjectMapper(yamlFactory)
		        .registerModule(new Jdk8Module())
		        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
		        .enable(SerializationFeature.INDENT_OUTPUT)
		        .disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
		
		
		JavaPropsFactory propsFactory = new JavaPropsFactory();
		propsObjectMapper = new ObjectMapper(propsFactory)
		        .registerModule(new Jdk8Module())
		        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
		        .enable(SerializationFeature.INDENT_OUTPUT)
		        .disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
		
		propsFormatSchema = JavaPropsSchema.emptySchema().withFirstArrayOffset(0).withWriteIndexUsingMarkers(true);
	}
	
	public static Map<String,Object> yaml2Map(String yaml) {
		try {
			return yamlObjectMapper.readValue(yaml, Map.class);
		} catch (Exception e) {
			throw new ApplicationRuntimeException(e);
		}
	}
	
	public static String map2Yaml(Map<String,Object> yamlMap) {
		try {
			return yamlObjectMapper.writeValueAsString(yamlMap);
		} catch (Exception e) {
			throw new ApplicationRuntimeException(e);
		}
	}
	
	public static Map<String,Object> props2Map(String props) {
		try {
			return propsObjectMapper.readValue(props, Map.class);
		} catch (Exception e) {
			throw new ApplicationRuntimeException(e);
		}
	}
	
	public static String map2Props(Map<String,Object> propsMap) {
		try {
			return propsObjectMapper.writer(propsFormatSchema).writeValueAsString(propsMap);
		} catch (Exception e) {
			throw new ApplicationRuntimeException(e);
		}
	}
	
}

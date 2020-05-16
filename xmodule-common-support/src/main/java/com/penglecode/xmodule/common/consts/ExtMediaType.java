package com.penglecode.xmodule.common.consts;

import java.nio.charset.Charset;
import java.util.Map;

import org.springframework.http.MediaType;

/**
 * 扩展的MediaType
 * 
 * @author 	pengpeng
 * @date 	2020年2月17日 下午8:14:27
 */
public class ExtMediaType extends MediaType {

	private static final long serialVersionUID = 1L;

	/**
	 * Public constant media type for {@code application/cbor}.
	 * @since 5.2
	 */
	public static final MediaType APPLICATION_YAML;

	/**
	 * A String equivalent of {@link MediaType#APPLICATION_CBOR}.
	 * @since 5.2
	 */
	public static final String APPLICATION_YAML_VALUE = "application/yaml";
	
	static {
		APPLICATION_YAML = new MediaType("application", "yaml");
	}
	
	public ExtMediaType(org.springframework.http.MediaType other, Charset charset) {
		super(other, charset);
	}

	public ExtMediaType(org.springframework.http.MediaType other, Map<String, String> parameters) {
		super(other, parameters);
	}

	public ExtMediaType(String type, String subtype, Charset charset) {
		super(type, subtype, charset);
	}

	public ExtMediaType(String type, String subtype, double qualityValue) {
		super(type, subtype, qualityValue);
	}

	public ExtMediaType(String type, String subtype, Map<String, String> parameters) {
		super(type, subtype, parameters);
	}

	public ExtMediaType(String type, String subtype) {
		super(type, subtype);
	}

	public ExtMediaType(String type) {
		super(type);
	}

}

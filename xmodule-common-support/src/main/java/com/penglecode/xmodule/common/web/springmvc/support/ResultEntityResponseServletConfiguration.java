package com.penglecode.xmodule.common.web.springmvc.support;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.penglecode.xmodule.common.boot.config.AbstractSpringConfiguration;
import com.penglecode.xmodule.common.support.Result;

/**
 * 处理输出HTTP消息对象为#Result 的时候尽最大努力将HttpServletResponse的状态码保持与Result.code一致的配置
 * 
 * @author 	pengpeng
 * @date 	2020年2月7日 下午9:25:58
 */
public class ResultEntityResponseServletConfiguration extends AbstractSpringConfiguration implements WebMvcConfigurer {

	@Override
	@SuppressWarnings("unchecked")
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		for(int i = 0, len = converters.size(); i < len; i++) {
			HttpMessageConverter<?> converter = converters.get(i);
			if(!(converter instanceof DelegateHttpMessageConverter)) {
				converter = new DelegateHttpMessageConverter((HttpMessageConverter<Object>) converter);
				converters.set(i, converter); //replace
			}
		}
	}
	
	/**
	 * 代理的HttpMessageConverter，实现在输出HTTP消息对象为#Result 的时候尽最大努力将HttpServletResponse的状态码保持与Result.code一致
	 * 
	 * @author 	pengpeng
	 * @date 	2020年2月7日 下午6:46:22
	 */
	public static class DelegateHttpMessageConverter implements HttpMessageConverter<Object> {

		private final HttpMessageConverter<Object> delegate;
		
		public DelegateHttpMessageConverter(HttpMessageConverter<Object> delegate) {
			super();
			Assert.notNull(delegate, "Parameter 'delegate' can not be null!");
			this.delegate = delegate;
		}

		@Override
		public boolean canRead(Class<?> clazz, MediaType mediaType) {
			return delegate.canRead(clazz, mediaType);
		}

		@Override
		public boolean canWrite(Class<?> clazz, MediaType mediaType) {
			return delegate.canWrite(clazz, mediaType);
		}

		@Override
		public List<MediaType> getSupportedMediaTypes() {
			return delegate.getSupportedMediaTypes();
		}

		@Override
		public Object read(Class<?> clazz, HttpInputMessage inputMessage)
				throws IOException, HttpMessageNotReadableException {
			return delegate.read(clazz, inputMessage);
		}

		@Override
		public void write(Object t, MediaType contentType, HttpOutputMessage outputMessage)
				throws IOException, HttpMessageNotWritableException {
			prepareOutputMessage(t, outputMessage);
			delegate.write(t, contentType, outputMessage);
		}

		@SuppressWarnings("unchecked")
		protected void prepareOutputMessage(Object t, HttpOutputMessage outputMessage) {
			if(t instanceof Result && outputMessage instanceof ServerHttpResponse) {
				Result<Object> result = (Result<Object>) t;
				ServerHttpResponse serverHttpResponse = (ServerHttpResponse) outputMessage;
				HttpStatus status = HttpStatus.resolve(result.getCode());
				if(status != null) {
					serverHttpResponse.setStatusCode(status);
				}
			}
		}
		
	}
	
}

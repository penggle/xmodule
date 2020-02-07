package com.penglecode.xmodule.common.web.webflux.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.reactivestreams.Publisher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.support.Result;

import reactor.core.publisher.Mono;

/**
 * 处理输出HTTP消息对象为#Result 的时候尽最大努力将HttpServletResponse的状态码保持与Result.code一致的配置
 * 
 * @author 	pengpeng
 * @date 	2020年2月7日 下午9:25:58
 */
public class ResultEntityResponseReativeConfiguration implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if(bean instanceof DefaultServerCodecConfigurer) {
			return new DelegateServerCodecConfigurer((ServerCodecConfigurer) bean);
		}
		return bean;
	}

	/**
	 * 代理的ServerCodecConfigurer
	 * 
	 * 注册该bean的唯一途径是在BeanPostProcessor中替换该bean
	 * 
	 * @author 	pengpeng
	 * @date 	2020年2月7日 下午8:59:38
	 */
	public static class DelegateServerCodecConfigurer implements ServerCodecConfigurer {

		private final ServerCodecConfigurer delegate;
		
		private final List<HttpMessageReader<?>> readers;
		
		private final List<HttpMessageWriter<?>> writers;
		
		public DelegateServerCodecConfigurer(ServerCodecConfigurer delegate) {
			super();
			Assert.notNull(delegate, "Parameter 'delegate' can not be null!");
			this.delegate = delegate;
			this.readers = createReaders(delegate);
			this.writers = createWriters(delegate);
		}

		@Override
		public CustomCodecs customCodecs() {
			return delegate.customCodecs();
		}

		@Override
		public void registerDefaults(boolean registerDefaults) {
			delegate.registerDefaults(registerDefaults);
		}

		@Override
		public List<HttpMessageReader<?>> getReaders() {
			return readers;
		}

		@Override
		public List<HttpMessageWriter<?>> getWriters() {
			return writers;
		}

		@Override
		public ServerDefaultCodecs defaultCodecs() {
			return delegate.defaultCodecs();
		}

		@Override
		public ServerCodecConfigurer clone() {
			return delegate.clone();
		}

		protected ServerCodecConfigurer getDelegate() {
			return delegate;
		}
		
		@SuppressWarnings("unchecked")
		protected List<HttpMessageWriter<?>> createWriters(ServerCodecConfigurer delegate) {
			List<HttpMessageWriter<?>> orginalWriters = delegate.getWriters();
			List<HttpMessageWriter<?>> newWriters = new ArrayList<>();
			for(HttpMessageWriter<?> writer : orginalWriters) {
				if(!(writer instanceof DelegateHttpMessageWriter)) {
					writer = new DelegateHttpMessageWriter((HttpMessageWriter<Object>) writer);
				}
				newWriters.add(writer);
			}
			return newWriters;
		}
		
		protected List<HttpMessageReader<?>> createReaders(ServerCodecConfigurer delegate) {
			return new ArrayList<>(delegate.getReaders());
		}

	}
	
	public static class DelegateHttpMessageWriter implements HttpMessageWriter<Object> {

		private final HttpMessageWriter<Object> delegate;
		
		public DelegateHttpMessageWriter(HttpMessageWriter<Object> delegate) {
			super();
			Assert.notNull(delegate, "Parameter 'delegate' can not be null!");
			this.delegate = delegate;
		}

		@Override
		public List<MediaType> getWritableMediaTypes() {
			return delegate.getWritableMediaTypes();
		}

		@Override
		public boolean canWrite(ResolvableType elementType, MediaType mediaType) {
			return delegate.canWrite(elementType, mediaType);
		}

		@Override
		public Mono<Void> write(Publisher<? extends Object> inputStream, ResolvableType elementType,
				MediaType mediaType, ReactiveHttpOutputMessage message, Map<String, Object> hints) {
			return delegate.write(prepareOutputMessage(inputStream, message), elementType, mediaType, message, hints);
		}

		@Override
		public Mono<Void> write(Publisher<? extends Object> inputStream, ResolvableType actualType,
				ResolvableType elementType, MediaType mediaType, ServerHttpRequest request, ServerHttpResponse response,
				Map<String, Object> hints) {
			return delegate.write(prepareOutputMessage(inputStream, response), actualType, elementType, mediaType, request, response, hints);
		}

		protected HttpMessageWriter<Object> getDelegate() {
			return delegate;
		}
		
		@SuppressWarnings("unchecked")
		protected Publisher<? extends Object> prepareOutputMessage(Publisher<? extends Object> inputStream, ReactiveHttpOutputMessage outputMessage) {
			return Mono.from(inputStream).flatMap(t -> {
				if(t != null && t instanceof Result && outputMessage instanceof ServerHttpResponse) {
					Result<Object> result = (Result<Object>) t;
					ServerHttpResponse serverHttpResponse = (ServerHttpResponse) outputMessage;
					HttpStatus status = HttpStatus.resolve(result.getCode());
					if(status != null) {
						serverHttpResponse.setStatusCode(status);
					}
				}
				return Mono.just(t);
			});
		}
		
	}
	
}

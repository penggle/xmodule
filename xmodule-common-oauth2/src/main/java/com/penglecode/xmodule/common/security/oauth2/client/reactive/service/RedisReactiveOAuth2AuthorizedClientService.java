package com.penglecode.xmodule.common.security.oauth2.client.reactive.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.penglecode.xmodule.common.redis.GlobalRedisKeys;
import com.penglecode.xmodule.common.security.oauth2.client.support.OAuth2ClientJsonSerializers;
import com.penglecode.xmodule.common.security.oauth2.consts.OAuth2ApplicationConstants;
import com.penglecode.xmodule.common.util.JsonUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

/**
 * 基于Redis存储的ReactiveOAuth2AuthorizedClientService
 * 
 * @author 	pengpeng
 * @date 	2020年2月4日 下午5:07:00
 */
@SuppressWarnings("unchecked")
public class RedisReactiveOAuth2AuthorizedClientService implements ReactiveOAuth2AuthorizedClientService {

	private static final String DEFAULT_OAUTH2_AUTHORIZED_CLIENT_KEY_PREFIX = "OAUTH2_AUTHORIZED_CLIENT_";
	
	private final ReactiveClientRegistrationRepository clientRegistrationRepository;
	
	private final RedisTemplate<String,Object> redisTemplate;
	
	private String oauth2AuthorizedClientKeyPrefix = DEFAULT_OAUTH2_AUTHORIZED_CLIENT_KEY_PREFIX;
	
	public RedisReactiveOAuth2AuthorizedClientService(ReactiveClientRegistrationRepository clientRegistrationRepository, RedisConnectionFactory redisConnectionFactory) {
		super();
		Assert.notNull(clientRegistrationRepository, "Parameter 'clientRegistrationRepository' must be required!");
		Assert.notNull(redisConnectionFactory, "Parameter 'redisConnectionFactory' must be required!");
		this.clientRegistrationRepository = clientRegistrationRepository;
		this.redisTemplate = createRedisTemplate(redisConnectionFactory);
		this.redisTemplate.afterPropertiesSet();
	}
	
	@Override
	public <T extends OAuth2AuthorizedClient> Mono<T> loadAuthorizedClient(String clientRegistrationId,
			String principalName) {
		Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
		Assert.hasText(principalName, "principalName cannot be empty");
		return this.clientRegistrationRepository.findByRegistrationId(clientRegistrationId)
				.map(clientRegistration -> oauth2AuthorizedClientKey(clientRegistrationId, principalName))
				.flatMap(key -> {
					OAuth2AuthorizedClient authorizedClient = (OAuth2AuthorizedClient) redisTemplate.opsForValue().get(key);
					return Mono.justOrEmpty((T)authorizedClient);
				});
	}

	@Override
	public Mono<Void> saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
		Assert.notNull(authorizedClient, "authorizedClient cannot be null");
		Assert.notNull(principal, "principal cannot be null");
		return Mono.fromRunnable(() -> {
			String key = oauth2AuthorizedClientKey(authorizedClient.getClientRegistration().getRegistrationId(), principal.getName());
			Instant issuedAt = OAuth2ApplicationConstants.DEFAULT_OAUTH2_CLIENT_CONFIG.getClock().instant(); //use authorizedClient.getAccessToken().getIssuedAt() ?
			Duration timeout = Duration.between(issuedAt, authorizedClient.getAccessToken().getExpiresAt());
			redisTemplate.opsForValue().set(key, authorizedClient, timeout);
		});
	}

	@Override
	public Mono<Void> removeAuthorizedClient(String clientRegistrationId, String principalName) {
		Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
		Assert.hasText(principalName, "principalName cannot be empty");
		return this.clientRegistrationRepository.findByRegistrationId(clientRegistrationId)
				.map(clientRegistration -> oauth2AuthorizedClientKey(clientRegistrationId, principalName))
				.doOnNext(redisTemplate::delete)
				.then(Mono.empty());
	}
	
	@SuppressWarnings("deprecation")
	protected RedisTemplate<String,Object> createRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		StringRedisSerializer redisKeySerializer = new StringRedisSerializer();
		//key的序列化
		redisTemplate.setKeySerializer(redisKeySerializer);
		redisTemplate.setHashKeySerializer(redisKeySerializer);
		//value的序列化
		
		ObjectMapper objectMapper = JsonUtils.createDefaultObjectMapper();
		objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
		objectMapper.registerModule(OAuth2ClientJsonSerializers.DEFAULT_MODULE);
		RedisSerializer<Object> redisValueSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
		redisTemplate.setValueSerializer(redisValueSerializer);
		redisTemplate.setHashValueSerializer(redisValueSerializer);
		return redisTemplate;
	}
	
	protected String oauth2AuthorizedClientKey(String clientRegistrationId, String principalName) {
		String bizKey = clientRegistrationId + "_" + principalName;
		return GlobalRedisKeys.ofKey(oauth2AuthorizedClientKeyPrefix, bizKey);
	}

	protected String getOauth2AuthorizedClientKeyPrefix() {
		return oauth2AuthorizedClientKeyPrefix;
	}

	public void setOauth2AuthorizedClientKeyPrefix(String oauth2AuthorizedClientKeyPrefix) {
		this.oauth2AuthorizedClientKeyPrefix = oauth2AuthorizedClientKeyPrefix;
	}

	protected ReactiveClientRegistrationRepository getClientRegistrationRepository() {
		return clientRegistrationRepository;
	}

	protected RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

}

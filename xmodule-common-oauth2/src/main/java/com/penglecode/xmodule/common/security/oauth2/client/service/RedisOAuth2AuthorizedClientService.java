package com.penglecode.xmodule.common.security.oauth2.client.service;

import java.time.Duration;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.penglecode.xmodule.common.redis.GlobalRedisKeys;
import com.penglecode.xmodule.common.util.JsonUtils;

/**
 * 基于Redis存储的OAuth2AuthorizedClientService
 * 
 * @author 	pengpeng
 * @date 	2020年2月2日 下午5:21:03
 */
@SuppressWarnings("unchecked")
public class RedisOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

	private static final String DEFAULT_OAUTH2_AUTHORIZED_CLIENT_KEY_PREFIX = "OAUTH2_AUTHORIZED_CLIENT_";
	
	private final ClientRegistrationRepository clientRegistrationRepository;
	
	private final RedisTemplate<String,Object> redisTemplate;
	
	private String oauth2AuthorizedClientKeyPrefix = DEFAULT_OAUTH2_AUTHORIZED_CLIENT_KEY_PREFIX;
	
	public RedisOAuth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository, RedisConnectionFactory redisConnectionFactory) {
		super();
		Assert.notNull(clientRegistrationRepository, "Parameter 'clientRegistrationRepository' must be required!");
		Assert.notNull(redisConnectionFactory, "Parameter 'redisConnectionFactory' must be required!");
		this.clientRegistrationRepository = clientRegistrationRepository;
		this.redisTemplate = createRedisTemplate(redisConnectionFactory);
		this.redisTemplate.afterPropertiesSet();
	}
	
	@Override
	public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
		Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
		Assert.hasText(principalName, "principalName cannot be empty");
		ClientRegistration registration = this.clientRegistrationRepository.findByRegistrationId(clientRegistrationId);
		if (registration == null) {
			return null;
		}
		String key = oauth2AuthorizedClientKey(clientRegistrationId, principalName);
		return (T) redisTemplate.opsForValue().get(key);
	}

	@Override
	public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
		Assert.notNull(authorizedClient, "authorizedClient cannot be null");
		Assert.notNull(principal, "principal cannot be null");
		String key = oauth2AuthorizedClientKey(authorizedClient.getClientRegistration().getRegistrationId(), principal.getName());
		Duration timeout = Duration.between(authorizedClient.getAccessToken().getIssuedAt(), authorizedClient.getAccessToken().getExpiresAt());
		redisTemplate.opsForValue().set(key, authorizedClient, timeout);
	}

	@Override
	public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
		Assert.hasText(clientRegistrationId, "clientRegistrationId cannot be empty");
		Assert.hasText(principalName, "principalName cannot be empty");
		String key = oauth2AuthorizedClientKey(clientRegistrationId, principalName);
		redisTemplate.delete(key);
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

	protected ClientRegistrationRepository getClientRegistrationRepository() {
		return clientRegistrationRepository;
	}

	protected RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

}

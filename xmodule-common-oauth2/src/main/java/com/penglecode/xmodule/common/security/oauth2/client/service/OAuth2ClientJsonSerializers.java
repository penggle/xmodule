package com.penglecode.xmodule.common.security.oauth2.client.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.penglecode.xmodule.common.util.SpringUtils;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * 有关OAuth2客户端数据模型JSON序列化的集合
 * 
 * @author 	pengpeng
 * @date 	2020年2月2日 下午6:22:31
 */
@SuppressWarnings("unchecked")
public class OAuth2ClientJsonSerializers {

	public static final SimpleModule DEFAULT_MODULE = new SimpleModule();
	
	static {
		DEFAULT_MODULE.addSerializer(OAuth2AuthorizedClient.class, new OAuth2AuthorizedClientJsonSerializer());
		DEFAULT_MODULE.addSerializer(OAuth2AccessToken.class, new OAuth2AccessTokenJsonSerializer());
		DEFAULT_MODULE.addSerializer(OAuth2RefreshToken.class, new OAuth2RefreshTokenJsonSerializer());
		
		DEFAULT_MODULE.addDeserializer(OAuth2AuthorizedClient.class, new OAuth2AuthorizedClientJsonDeserializer());
		DEFAULT_MODULE.addDeserializer(OAuth2AccessToken.class, new OAuth2AccessTokenJsonDeserializer());
		DEFAULT_MODULE.addDeserializer(OAuth2RefreshToken.class, new OAuth2RefreshTokenJsonDeserializer());
	}
	
	public static class OAuth2AuthorizedClientJsonSerializer extends JsonSerializer<OAuth2AuthorizedClient> {

		@Override
		public void serialize(OAuth2AuthorizedClient value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			serializeWithType(value, gen, serializers, null);
		}

		@Override
		public void serializeWithType(OAuth2AuthorizedClient value, JsonGenerator gen, SerializerProvider serializers,
				TypeSerializer typeSer) throws IOException {
			gen.writeStartObject();
			
			if(typeSer != null) {
				gen.writeStringField(typeSer.getPropertyName(), handledType().getName());
			}
			
			gen.writeStringField("clientRegistrationId", value.getClientRegistration().getRegistrationId());
			gen.writeStringField("principalName", value.getPrincipalName());
			gen.writeObjectField("accessToken", value.getAccessToken());
			gen.writeObjectField("refreshToken", value.getRefreshToken());
			gen.writeEndObject();
		}

		@Override
		public Class<OAuth2AuthorizedClient> handledType() {
			return OAuth2AuthorizedClient.class;
		}

	}
	
	public static class OAuth2AccessTokenJsonSerializer extends JsonSerializer<OAuth2AccessToken> {

		@Override
		public void serialize(OAuth2AccessToken value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			serializeWithType(value, gen, serializers, null);
		}

		@Override
		public void serializeWithType(OAuth2AccessToken value, JsonGenerator gen, SerializerProvider serializers,
				TypeSerializer typeSer) throws IOException {
			gen.writeStartObject();
			
			if(typeSer != null) {
				gen.writeStringField(typeSer.getPropertyName(), handledType().getName());
			}
			
			gen.writeStringField("tokenType", value.getTokenType().getValue());
			gen.writeObjectField("scopes", new HashSet<String>(value.getScopes() == null ? Collections.EMPTY_SET : value.getScopes()));
			gen.writeObjectField("tokenValue", value.getTokenValue());
			gen.writeObjectField("issuedAt", value.getIssuedAt());
			gen.writeObjectField("expiresAt", value.getExpiresAt());
			gen.writeEndObject();
		}

		@Override
		public Class<OAuth2AccessToken> handledType() {
			return OAuth2AccessToken.class;
		}

	}
	
	public static class OAuth2RefreshTokenJsonSerializer extends JsonSerializer<OAuth2RefreshToken> {

		@Override
		public void serialize(OAuth2RefreshToken value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			serializeWithType(value, gen, serializers, null);
		}

		@Override
		public void serializeWithType(OAuth2RefreshToken value, JsonGenerator gen, SerializerProvider serializers,
				TypeSerializer typeSer) throws IOException {
			gen.writeStartObject();
			
			if(typeSer != null) {
				gen.writeStringField(typeSer.getPropertyName(), handledType().getName());
			}
			
			gen.writeObjectField("tokenValue", value.getTokenValue());
			gen.writeObjectField("issuedAt", value.getIssuedAt());
			gen.writeObjectField("expiresAt", value.getExpiresAt());
			gen.writeEndObject();
		}

		@Override
		public Class<OAuth2RefreshToken> handledType() {
			return OAuth2RefreshToken.class;
		}

	}
	
	public static class OAuth2AuthorizedClientJsonDeserializer extends JsonDeserializer<OAuth2AuthorizedClient> {

		@Override
		public OAuth2AuthorizedClient deserialize(JsonParser jsonParser, DeserializationContext context)
				throws IOException, JsonProcessingException {
			ObjectCodec codec = jsonParser.getCodec();
			JsonNode jsonNode = codec.readTree(jsonParser);
			String clientRegistrationId = jsonNode.get("clientRegistrationId").asText();
			ClientRegistration clientRegistration = getClientRegistrationRepository().findByRegistrationId(clientRegistrationId);
			if(clientRegistration != null) {
				String principalName = jsonNode.get("principalName").asText();
				OAuth2AccessToken accessToken = jsonNode.get("accessToken").traverse(codec).readValueAs(OAuth2AccessToken.class);
				OAuth2RefreshToken refreshToken = null;
				if(jsonNode.hasNonNull("refreshToken")) {
					refreshToken = jsonNode.get("refreshToken").traverse(codec).readValueAs(OAuth2RefreshToken.class);
				}
				return new OAuth2AuthorizedClient(clientRegistration, principalName, accessToken, refreshToken);
			}
			return null;
		}
		
		protected ClientRegistrationRepository getClientRegistrationRepository() {
			return SpringUtils.getBean(ClientRegistrationRepository.class);
		}
		
	}
	
	public static class OAuth2AccessTokenJsonDeserializer extends JsonDeserializer<OAuth2AccessToken> {

		@Override
		public OAuth2AccessToken deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
			ObjectCodec codec = jsonParser.getCodec();
			JsonNode jsonNode = codec.readTree(jsonParser);
			TokenType tokenType = TokenType.BEARER;
			Set<String> scopes = jsonNode.get("scopes").traverse(codec).readValueAs(new TypeReference<Set<String>>() {});
			String tokenValue = jsonNode.get("tokenValue").asText();
			String instant = null;
			instant = jsonNode.get("issuedAt").asText();
			Instant issuedAt = StringUtils.isEmpty(instant) ? null : Instant.parse(instant);
			instant = jsonNode.get("expiresAt").asText();
			Instant expiresAt = StringUtils.isEmpty(instant) ? null : Instant.parse(instant);
			return new OAuth2AccessToken(tokenType, tokenValue, issuedAt, expiresAt, scopes);
		}
		
	}
	
	public static class OAuth2RefreshTokenJsonDeserializer extends JsonDeserializer<OAuth2RefreshToken> {

		@Override
		public OAuth2RefreshToken deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
			ObjectCodec codec = jsonParser.getCodec();
			JsonNode jsonNode = codec.readTree(jsonParser);
			String tokenValue = jsonNode.get("tokenValue").asText();
			String instant = null;
			instant = jsonNode.get("issuedAt").asText();
			Instant issuedAt = StringUtils.isEmpty(instant) ? null : Instant.parse(instant);
			/*
			instant = jsonNode.get("expiresAt").asText();
			Instant expiresAt = StringUtils.isEmpty(instant) ? null : Instant.parse(instant);
			*/
			return new OAuth2RefreshToken(tokenValue, issuedAt);
		}
		
	}
	
}

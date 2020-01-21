package com.penglecode.xmodule.common.util;

import java.util.Map;

import org.springframework.util.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.PayloadTransformer;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/**
 * 基于nimbus-jose-jwt实现的JWT令牌工具类
 * 
 * @author 	pengpeng
 * @date	2019年12月25日 上午9:13:13
 */
public class JwtUtils {

	public static final String ISSUER_CLAIM = "iss";
	public static final String SUBJECT_CLAIM = "sub";
	public static final String AUDIENCE_CLAIM = "aud";
	public static final String EXPIRATION_TIME_CLAIM = "exp";
	public static final String NOT_BEFORE_CLAIM = "nbf";
	public static final String ISSUED_AT_CLAIM = "iat";
	public static final String JWT_ID_CLAIM = "jti";
	
	/**
	 * 创建JWT令牌
	 * @param signer
	 * @param header
	 * @param claims
	 * @return
	 */
	public static String createJwtToken(JWSSigner signer, JWSHeader header, JWTClaimsSet claims) {
		Assert.notNull(signer, "Parameter 'signer' must be required!");
		Assert.notNull(header, "Parameter 'header' must be required!");
		Assert.notNull(claims, "Parameter 'claims' must be required!");
		try {
			SignedJWT signedJwt = new SignedJWT(header, claims);
			signedJwt.sign(signer);
	        return signedJwt.serialize();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * 创建JWT令牌 (基于HMAC SHA-256签名算法)
	 * @param secret	- HMAC对称加密算法的秘钥
	 * @param payload	- 令牌的载荷
	 * @return
	 */
	public static String createHS256JwtToken(String secret, Map<String,Object> payload) {
		Assert.hasText(secret, "Parameter 'secret' must be required!");
		Assert.notEmpty(payload, "Parameter 'secret' can not be empty!");
		try {
			JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).keyID(UUIDUtils.uuid()).type(JOSEObjectType.JWT).build();
			JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
			for(Map.Entry<String,Object> entry : payload.entrySet()) {
				builder.claim(entry.getKey(), entry.getValue());
			}
			JWTClaimsSet claims = builder.build();
			return createJwtToken(new MACSigner(secret), header, claims);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * 检验JWT令牌的签名 (基于HMAC SHA-256签名算法)
	 * @param secret	- HMAC对称加密算法的秘钥
	 * @param token		- JWT令牌
	 * @return
	 */
	public static boolean validateHS256JwtToken(String secret, String token) {
		try {
			JWSVerifier verifier = new MACVerifier(secret);
			final JWSObject jwtObject = JWSObject.parse(token);
	        return jwtObject.verify(verifier);
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 获取JWT令牌的payload (基于HMAC SHA-256签名算法)
	 * @param secret	- HMAC对称加密算法的秘钥
	 * @param token		- JWT令牌
	 * @return
	 */
	public static Map<String,Object> getHS256JwtTokenPayload(String secret, String token) {
		try {
			final JWSObject jwtObject = JWSObject.parse(token);
	        String payload = jwtObject.getPayload().toString();
	        return JsonUtils.json2Object(payload, new TypeReference<Map<String,Object>>(){});
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * 获取JWT令牌的payload (基于HMAC SHA-256签名算法)
	 * @param secret	- HMAC对称加密算法的秘钥
	 * @param token		- JWT令牌
	 * @return
	 */
	public static <T> T getHS256JwtTokenPayload(String secret, String token, PayloadTransformer<T> transformer) {
		try {
			final JWSObject jwtObject = JWSObject.parse(token);
			return jwtObject.getPayload().toType(transformer);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
}

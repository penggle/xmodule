package com.penglecode.xmodule.common.security.jwt;

import com.nimbusds.jose.Payload;
import com.nimbusds.jose.PayloadTransformer;
import com.penglecode.xmodule.common.util.JsonUtils;

public class JwtTokenTransformer implements PayloadTransformer<JwtToken> {

	@Override
	public JwtToken transform(Payload payload) {
		return JsonUtils.json2Object(payload.toString(), JwtToken.class);
	}

}

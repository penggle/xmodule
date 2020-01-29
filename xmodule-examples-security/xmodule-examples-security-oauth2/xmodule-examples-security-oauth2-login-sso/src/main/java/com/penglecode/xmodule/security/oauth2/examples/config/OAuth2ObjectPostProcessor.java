package com.penglecode.xmodule.security.oauth2.examples.config;

import org.springframework.security.config.annotation.ObjectPostProcessor;

public class OAuth2ObjectPostProcessor implements ObjectPostProcessor<Object> {

	@Override
	public <O> O postProcess(O object) {
		System.out.println(">>>> postProcess = " + object);
		return object;
	}

}

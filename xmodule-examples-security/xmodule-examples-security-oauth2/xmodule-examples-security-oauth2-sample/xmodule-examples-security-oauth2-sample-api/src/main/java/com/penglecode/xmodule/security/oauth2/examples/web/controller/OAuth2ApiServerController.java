package com.penglecode.xmodule.security.oauth2.examples.web.controller;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.servlet.support.HttpApiResourceSupport;

@RestController
@RequestMapping("/api/server")
public class OAuth2ApiServerController extends HttpApiResourceSupport {

	@GetMapping(value="/info", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<String> getServerInfo() {
		Map<String,Object> serverInfo = new LinkedHashMap<String,Object>();
		serverInfo.put("name", getEnvironment().getProperty("spring.application.name"));
		serverInfo.put("port", getEnvironment().getProperty("server.port"));
		serverInfo.put("nowTime", Instant.now());
		return Result.success().message("OK").data(serverInfo).build();
	}
	
}
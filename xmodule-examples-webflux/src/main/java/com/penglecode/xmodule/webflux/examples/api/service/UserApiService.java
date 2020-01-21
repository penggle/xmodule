package com.penglecode.xmodule.webflux.examples.api.service;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/user")
public interface UserApiService {

	@GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getUserById(@PathVariable("id") Long id);
	
}

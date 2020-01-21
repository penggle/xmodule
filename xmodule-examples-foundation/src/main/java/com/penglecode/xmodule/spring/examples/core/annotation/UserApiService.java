package com.penglecode.xmodule.spring.examples.core.annotation;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.penglecode.xmodule.common.support.Result;

@RequestMapping("/api/user")
public interface UserApiService extends FallbackableApiService {

	@PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> createUser(@RequestBody Map<String,Object> user);
	
	@PutMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> updateUser(@PathVariable("id") String id, @RequestBody Map<String,Object> user);
	
	@DeleteMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> deleteUserById(@PathVariable("id") String id);
	
	@GetMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> getUserById(@PathVariable("id") String id);
	
	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> getAllUsers();
	
}

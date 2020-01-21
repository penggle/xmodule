package com.penglecode.xmodule.examples.springcloud.api.service;

import java.util.List;

import org.springframework.cloud.openfeign.DefaultHystrixFallbackFactory;
import org.springframework.cloud.openfeign.FallbackableFeignClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.examples.springcloud.api.model.User;

/**
 * service-producer提供的API
 * 
 * @author 	pengpeng
 * @date 	2020年1月18日 下午5:31:49
 */
@FeignClient(name="springcloud-consul-service-producer", qualifier="userApiService", contextId="userApiService", fallbackFactory=DefaultHystrixFallbackFactory.class)
public interface UserApiService extends FallbackableFeignClient {

	/**
	 * 注册账户
	 * @param user
	 */
	@PostMapping(value="/api/user/register", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Result<Long> registerUser(@RequestBody User user);
	
	/**
	 * 修改密码
	 * @param product
	 */
	@PutMapping(value="/api/user/modifypwd", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Result<Boolean> modifyPassword(@RequestBody User user);
	
	/**
	 * 根据userId获取用户信息
	 * @param userId
	 * @return
	 */
	@GetMapping(value="/api/user/{userId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<User> getUserById(@PathVariable("userId") Long userId);
	
	/**
	 * 根据条件查询用户信息列表
	 * @param condition
	 * @return
	 */
	@GetMapping(value="/api/user/list", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<List<User>> getUserList(@RequestParam User condition);
	
}

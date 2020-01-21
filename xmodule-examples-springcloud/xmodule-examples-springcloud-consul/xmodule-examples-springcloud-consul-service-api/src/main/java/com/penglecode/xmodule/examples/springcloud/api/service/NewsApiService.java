package com.penglecode.xmodule.examples.springcloud.api.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.penglecode.xmodule.examples.springcloud.api.model.News;
import com.penglecode.xmodule.examples.springcloud.api.model.OpenApiResult;

/**
 * 基于https://api.apiopen.top/api.html上的开放API接口
 * 
 * 此处通过@FeignClient(name="springcloud-consul-openapi-sidecar")使得NewsApiService通过sidecar微服务(xmodule-examples-springcloud-consul-openapi-sidecar)进行代理转发
 * 
 * @author 	pengpeng
 * @date 	2020年1月18日 下午5:18:17
 */
@FeignClient(name="springcloud-consul-openapi-sidecar", qualifier="newsApiService", contextId="newsApiService")
public interface NewsApiService {

	@GetMapping(value="/getWangYiNews")
	public OpenApiResult<List<News>> getNewsList(
			@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "count", defaultValue = "10") Integer count);
	
}

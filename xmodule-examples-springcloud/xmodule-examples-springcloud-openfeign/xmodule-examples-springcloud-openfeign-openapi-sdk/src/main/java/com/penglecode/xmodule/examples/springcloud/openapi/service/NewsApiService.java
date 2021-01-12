package com.penglecode.xmodule.examples.springcloud.openapi.service;

import com.penglecode.xmodule.examples.springcloud.openapi.model.News;
import com.penglecode.xmodule.examples.springcloud.openapi.model.OpenApiResult;
import com.penglecode.xmodule.examples.springcloud.openapi.service.fallback.NewsApiServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 基于https://api.apiopen.top/api.html上的开放API接口
 * 
 * @author 	pengpeng
 * @date 	2020年1月18日 下午5:18:17
 */
@FeignClient(name="openapi", qualifier="newsApiService", contextId="newsApiService", url="${feign.client.openapi.url}", fallbackFactory=NewsApiServiceFallbackFactory.class)
public interface NewsApiService {

	/**
	 * 根据条件查询新闻列表
	 * @param page
	 * @param count
	 * @return
	 */
	@GetMapping(value="/getWangYiNews", produces=MediaType.APPLICATION_JSON_VALUE)
	public OpenApiResult<List<News>> getNewsList(
			@RequestParam(name = "page", defaultValue = "1") Integer page,
			@RequestParam(name = "count", defaultValue = "10") Integer count);
	
}

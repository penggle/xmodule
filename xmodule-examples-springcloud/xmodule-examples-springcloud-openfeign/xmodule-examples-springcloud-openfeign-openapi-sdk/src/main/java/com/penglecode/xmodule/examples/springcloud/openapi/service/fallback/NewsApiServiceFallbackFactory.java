package com.penglecode.xmodule.examples.springcloud.openapi.service.fallback;

import com.penglecode.xmodule.examples.springcloud.openapi.model.News;
import com.penglecode.xmodule.examples.springcloud.openapi.model.OpenApiResult;
import com.penglecode.xmodule.examples.springcloud.openapi.service.NewsApiService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@link NewsApiService}的FallbackFactory
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/12/31 11:31
 */
@Component
public class NewsApiServiceFallbackFactory implements FallbackFactory<NewsApiService> {

    @Override
    public NewsApiService create(Throwable cause) {
        return new NewsApiServiceFallbackImpl(cause);
    }

    public static class NewsApiServiceFallbackImpl extends AsbtractFallbackApiService implements NewsApiService {

        public NewsApiServiceFallbackImpl(Throwable cause) {
            super(cause);
        }

        @Override
        public OpenApiResult<List<News>> getNewsList(Integer page, Integer count) {
            return commonFallbackResult();
        }

    }

}

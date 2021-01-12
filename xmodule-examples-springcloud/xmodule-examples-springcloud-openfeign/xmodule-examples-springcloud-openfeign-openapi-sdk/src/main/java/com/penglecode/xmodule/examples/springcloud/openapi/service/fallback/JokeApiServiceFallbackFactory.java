package com.penglecode.xmodule.examples.springcloud.openapi.service.fallback;

import com.penglecode.xmodule.examples.springcloud.openapi.model.Joke;
import com.penglecode.xmodule.examples.springcloud.openapi.model.OpenApiResult;
import com.penglecode.xmodule.examples.springcloud.openapi.service.JokeApiService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * {@link JokeApiService}的FallbackFactory
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/12/31 11:31
 */
@Component
public class JokeApiServiceFallbackFactory implements FallbackFactory<JokeApiService> {

    @Override
    public JokeApiService create(Throwable cause) {
        return new JokeApiServiceFallbackImpl(cause);
    }

    public static class JokeApiServiceFallbackImpl extends AsbtractFallbackApiService implements JokeApiService {

        public JokeApiServiceFallbackImpl(Throwable cause) {
            super(cause);
        }

        @Override
        public OpenApiResult<Joke> getJokeById(String sid) {
            return commonFallbackResult();
        }

        @Override
        public OpenApiResult<List<Joke>> getJokeList(String type, Integer page, Integer count) {
            return commonFallbackResult();
        }

    }

}

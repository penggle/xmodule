package com.penglecode.xmodule.examples.springcloud.openapi.web.controller;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.examples.springcloud.openapi.model.OpenApiResult;
import com.penglecode.xmodule.examples.springcloud.openapi.model.Joke;
import com.penglecode.xmodule.examples.springcloud.openapi.model.News;
import com.penglecode.xmodule.examples.springcloud.openapi.service.JokeApiService;
import com.penglecode.xmodule.examples.springcloud.openapi.service.NewsApiService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/12/31 14:39
 */
@RestController
@RequestMapping("/open-api/example")
public class OpenApiExampleController {

    @Resource(name="jokeApiService")
    private JokeApiService jokeApiService;

    @Resource(name="newsApiService")
    private NewsApiService newsApiService;

    @GetMapping(value="/joke/list", produces=MediaType.APPLICATION_JSON_VALUE)
    public Result<List<Joke>> getJokeList(@RequestParam(name = "type", required = false) String type,
                                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                                          @RequestParam(name = "count", defaultValue = "10") Integer count) {
        OpenApiResult<List<Joke>> openApiResult = jokeApiService.getJokeList(type, page, count);
        return openApiResult.toResult();
    }

    @GetMapping(value="/joke/{sid}", produces=MediaType.APPLICATION_JSON_VALUE)
    public Result<Joke> getJokeById(@PathVariable("sid") String sid) {
        OpenApiResult<Joke> openApiResult = jokeApiService.getJokeById(sid);
        return openApiResult.toResult();
    }

    @GetMapping(value="/news/list", produces=MediaType.APPLICATION_JSON_VALUE)
    public Result<List<News>> getNewsList(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                          @RequestParam(name = "count", defaultValue = "10") Integer count) {
        OpenApiResult<List<News>> openApiResult = newsApiService.getNewsList(page, count);
        return openApiResult.toResult();
    }

}

package com.penglecode.xmodule.master4j.springboot.example.nginx;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.master4j.springboot.example.common.model.Joke;
import com.penglecode.xmodule.master4j.springboot.example.common.service.JokeApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Joke接口
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/10/3 14:07
 */
@RestController
@RequestMapping("/api/joke")
public class JokeApiController {

    @Autowired
    private JokeApiService jokeApiService;

    @GetMapping(value="/list", produces=MediaType.APPLICATION_JSON_VALUE)
    public Result<List<Joke>> getJokeList(@RequestHeader HttpHeaders httpHeaders,
                                          @RequestParam(name="type", required=false) String type,
                                          @RequestParam(name="page", defaultValue="1") Integer page,
                                          @RequestParam(name="count", defaultValue="10") Integer count) {
        printRequestHeaders(httpHeaders);
        List<Joke> jokeList = jokeApiService.getJokeList(type, page, count);
        return Result.success().data(jokeList).build();
    }

    @GetMapping(value="/{sid}", produces=MediaType.APPLICATION_JSON_VALUE)
    public Result<List<Joke>> getJokeBySid(@RequestHeader HttpHeaders httpHeaders,
                                           @PathVariable(name="sid") String sid) {
        printRequestHeaders(httpHeaders);
        Joke joke = jokeApiService.getJokeBySid(sid);
        return Result.success().data(joke).build();
    }

    protected void printRequestHeaders(HttpHeaders httpHeaders) {
        httpHeaders.forEach((headerName, headerValues) -> {
            System.out.println(String.format(">>> %s = %s", headerName, (headerValues != null && headerValues.size() == 1) ? headerValues.get(0) : headerValues));
        });
    }

}

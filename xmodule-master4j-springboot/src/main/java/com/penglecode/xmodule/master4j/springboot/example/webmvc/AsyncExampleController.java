package com.penglecode.xmodule.master4j.springboot.example.webmvc;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Servlet3异步请求示例
 *
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/26 16:27
 */
@RestController
public class AsyncExampleController {

    @Autowired
    private ConfigurableEnvironment environment;

    @Autowired
    private AsyncExampleService asyncExampleService;

    @GetMapping(value="/async/serverinfo", produces=MediaType.APPLICATION_JSON_VALUE)
    public Callable<Object> getServerInfo() {
        return () -> {
            Thread.sleep(5000);
            Map<String,Object> serverInfo = new LinkedHashMap<>();
            serverInfo.put("nowTime", DateTimeUtils.formatNow());
            serverInfo.put("appName", environment.getProperty("spring.application.name"));
            serverInfo.put("serverPort", environment.getProperty("server.port"));
            return serverInfo;
        };
    }

    @GetMapping(value="/async/invoke", produces=MediaType.APPLICATION_JSON_VALUE)
    public Object invokeAsyncMethod() {
        asyncExampleService.performAsync();
        return Result.success().build();
    }

}

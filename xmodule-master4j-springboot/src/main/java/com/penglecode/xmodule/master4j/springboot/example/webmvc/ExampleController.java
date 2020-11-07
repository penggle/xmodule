package com.penglecode.xmodule.master4j.springboot.example.webmvc;

import com.penglecode.xmodule.common.util.DateTimeUtils;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

/**
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/26 11:26
 */
@RestController
public class ExampleController {

    @GetMapping(value="/now", produces=MediaType.APPLICATION_JSON_VALUE)
    public Object now(HttpServletRequest request, HttpServletResponse response, Model model) {
        System.out.println(String.format("【%s】>>> now(%s, %s, %s)", getClass().getSimpleName(), request, response, model));
        return Collections.singletonMap("nowTime", DateTimeUtils.formatNow());
    }

    @PostMapping(value="/login", produces=MediaType.APPLICATION_JSON_VALUE)
    public Object login(HttpServletRequest request, HttpServletResponse response, String username, String password, Map<String,Object> model) {
        model.put("username", username);
        model.put("password", password);
        System.out.println(String.format("【%s】>>> login(%s, %s, %s)", getClass().getSimpleName(), request, response, model));
        return Collections.singletonMap("success", true);
    }

}

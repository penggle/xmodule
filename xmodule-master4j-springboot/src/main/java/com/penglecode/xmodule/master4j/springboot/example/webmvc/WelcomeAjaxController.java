package com.penglecode.xmodule.master4j.springboot.example.webmvc;

import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/**
 * 异步Ajax请求的示例Controller
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/26 11:26
 */
@RestController
public class WelcomeAjaxController {

    @GetMapping(value="/async/welcome", produces=MediaType.APPLICATION_JSON_VALUE)
    public Object welcome(HttpServletRequest request, HttpServletResponse response, Model model) {
        System.out.println(String.format("【%s】>>> welcome(%s, %s, %s)", getClass().getSimpleName(), request, response, model));
        return Collections.singletonMap("message", "you are welcome!");
    }

}

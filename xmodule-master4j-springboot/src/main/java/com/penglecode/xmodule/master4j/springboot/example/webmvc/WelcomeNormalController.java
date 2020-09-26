package com.penglecode.xmodule.master4j.springboot.example.webmvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 普通同步请求的示例Controller
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/26 11:26
 */
@Controller
public class WelcomeNormalController {

    @GetMapping(value="/sync/welcome")
    public String welcome(HttpServletRequest request, HttpServletResponse response, Model model) {
        System.out.println(String.format("【%s】>>> welcome(%s, %s, %s)", getClass().getSimpleName(), request, response, model));
        return "welcome";
    }

}

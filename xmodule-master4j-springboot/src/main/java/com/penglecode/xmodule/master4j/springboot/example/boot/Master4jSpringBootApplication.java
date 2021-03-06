package com.penglecode.xmodule.master4j.springboot.example.boot;

import com.penglecode.xmodule.master4j.springboot.example.BasePackage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * xmodule-master4j-springboot的启动入口程序
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/23 15:33
 */
@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class Master4jSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(Master4jSpringBootApplication.class);
    }

}

package com.penglecode.xmodule.master4j.netty.boot;

import com.penglecode.xmodule.BasePackage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class NettyExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(NettyExampleApplication.class, args);
	}
	
}
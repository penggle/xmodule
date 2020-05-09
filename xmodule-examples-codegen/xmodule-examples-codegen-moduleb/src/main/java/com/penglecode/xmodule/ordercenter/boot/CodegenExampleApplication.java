package com.penglecode.xmodule.ordercenter.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class CodegenExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodegenExampleApplication.class, args);
	}
	
}
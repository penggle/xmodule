package com.penglecode.xmodule.webflux.examples.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses = BasePackage.class)
public class WebFluxExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebFluxExampleApplication.class, args);
	}

}
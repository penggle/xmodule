package com.penglecode.xmodule.hydra.oauth2.examples.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class HydraLoginExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(HydraLoginExampleApplication.class, args);
	}
	
}
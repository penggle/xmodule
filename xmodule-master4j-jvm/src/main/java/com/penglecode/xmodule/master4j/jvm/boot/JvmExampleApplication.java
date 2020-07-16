package com.penglecode.xmodule.master4j.jvm.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class JvmExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(JvmExampleApplication.class, args);
	}

}

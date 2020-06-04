package com.penglecode.xmodule.rabbitmq.examples.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.penglecode.xmodule.BasePackage;

@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class RabbitMqExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitMqExampleApplication.class, args);
	}
	
}
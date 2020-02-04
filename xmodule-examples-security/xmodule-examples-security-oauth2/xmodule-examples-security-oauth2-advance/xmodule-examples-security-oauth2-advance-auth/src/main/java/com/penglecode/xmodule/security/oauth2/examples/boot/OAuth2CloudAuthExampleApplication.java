package com.penglecode.xmodule.security.oauth2.examples.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.penglecode.xmodule.BasePackage;

@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses=BasePackage.class)
@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class OAuth2CloudAuthExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(OAuth2CloudAuthExampleApplication.class, args);
	}
	
}

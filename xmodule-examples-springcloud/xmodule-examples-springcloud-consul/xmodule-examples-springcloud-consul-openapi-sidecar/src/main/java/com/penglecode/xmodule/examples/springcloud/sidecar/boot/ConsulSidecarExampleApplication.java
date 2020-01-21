package com.penglecode.xmodule.examples.springcloud.sidecar.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.penglecode.xmodule.BasePackage;

@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses=BasePackage.class)
@SpringBootApplication(scanBasePackageClasses=BasePackage.class)
public class ConsulSidecarExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsulSidecarExampleApplication.class, args);
	}
	
}

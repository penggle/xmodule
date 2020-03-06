package com.penglecode.xmodule.springboot.examples.dynamicbean.test;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.penglecode.xmodule.common.util.SpringUtils;
import com.penglecode.xmodule.springboot.examples.boot.SpringBootExampleApplication;
import com.penglecode.xmodule.springboot.examples.dynamicbean.AbstractOpennessApiService;
import com.penglecode.xmodule.springboot.examples.dynamicbean.OpennessApiServiceFactory;
import com.penglecode.xmodule.springboot.examples.dynamicbean.OpennessDnsApiService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE, classes = SpringBootExampleApplication.class)
public class DynamicBeanTest {

	@Autowired
	private OpennessApiServiceFactory opennessApiServiceFactory;
	
	@Test
	public void test1() {
		try {
			Class<OpennessDnsApiService> serviceBeanClass = OpennessDnsApiService.class;
			String nodeName = "node01";
			OpennessDnsApiService serviceBean1 = opennessApiServiceFactory.getOpennessApiService(serviceBeanClass, nodeName);
			System.out.println(">>> created: " + serviceBean1);
			opennessApiServiceFactory.destroyOpennessApiService(serviceBeanClass, nodeName);
		
			serviceBean1 = SpringUtils.getBeanQuietly(opennessApiServiceFactory.getServiceBeanName(serviceBeanClass, nodeName));
			System.out.println(">>> destroyed: " + serviceBean1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test2() {
		Class<OpennessDnsApiService> serviceBeanClass = OpennessDnsApiService.class;
		String nodeName1 = "node01";
		String serviceBeanName1 = nodeName1 + "OpennessDnsApiService";
		
		SpringUtils.registerBean(serviceBeanName1, serviceBeanClass, true, bdb -> {
			bdb.addConstructorArgValue(nodeName1);
		});
		
		OpennessDnsApiService serviceBean1 = SpringUtils.getBean(serviceBeanName1, serviceBeanClass);
		System.out.println(">>> serviceBean1 = " + serviceBean1);
		
		String nodeName2 = "node02";
		String serviceBeanName2 = nodeName2 + "OpennessDnsApiService";
		
		SpringUtils.registerBean(serviceBeanName2, serviceBeanClass, true, bdb -> {
			bdb.addConstructorArgValue(nodeName2);
		});
		
		OpennessDnsApiService serviceBean2 = SpringUtils.getBean(serviceBeanName2, serviceBeanClass);
		System.out.println(">>> serviceBean2 = " + serviceBean2);
		
		Map<String,AbstractOpennessApiService> serviceBeans = SpringUtils.getBeansOfType(AbstractOpennessApiService.class);
		serviceBeans.forEach((key, value) -> {
			System.out.println(">>> " + key + " = " + value);
		});
		
		SpringUtils.destroyBean(serviceBeanName1, null);
		SpringUtils.destroyBean(serviceBeanName2, null);
	}
	
	
	@Test
	public void test3() {
		Class<OpennessDnsApiService> serviceBeanClass = OpennessDnsApiService.class;
		String nodeName1 = "node01";
		String serviceBeanName1 = nodeName1 + "OpennessDnsApiService";
		
		SpringUtils.registerBean(serviceBeanName1, serviceBeanClass, false, bdb -> {
			bdb.addConstructorArgValue(nodeName1);
		});
		
		OpennessDnsApiService serviceBean1 = SpringUtils.getBean(serviceBeanName1, serviceBeanClass);
		System.out.println(">>> serviceBean1 = " + serviceBean1);
		
		String nodeName2 = "node02";
		String serviceBeanName2 = nodeName2 + "OpennessDnsApiService";
		
		SpringUtils.registerBean(serviceBeanName2, serviceBeanClass, false, bdb -> {
			bdb.addConstructorArgValue(nodeName2);
		});
		
		OpennessDnsApiService serviceBean2 = SpringUtils.getBean(serviceBeanName2, serviceBeanClass);
		System.out.println(">>> serviceBean2 = " + serviceBean2);
		
		Map<String,AbstractOpennessApiService> serviceBeans = SpringUtils.getBeansOfType(AbstractOpennessApiService.class);
		serviceBeans.forEach((key, value) -> {
			System.out.println(">>> " + key + " = " + value);
		});
		
		SpringUtils.destroyBean(serviceBeanName1, serviceBean1);
		SpringUtils.destroyBean(serviceBeanName2, serviceBean2);
	}
	
}

package com.penglecode.xmodule.codegen.examples.boot;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;

import com.penglecode.xmodule.codegen.examples.boot.generator.order.OrderControllerCodeGenerator;
import com.penglecode.xmodule.codegen.examples.boot.generator.order.OrderMybatisCodeGenerator;
import com.penglecode.xmodule.codegen.examples.boot.generator.order.OrderServiceCodeGenerator;
import com.penglecode.xmodule.common.codegen.config.ControllerCodegenConfiguration;
import com.penglecode.xmodule.common.codegen.config.MybatisCodegenConfiguration;
import com.penglecode.xmodule.common.codegen.config.ServiceCodegenConfiguration;

@SpringBootApplication
@Import({MybatisCodegenConfiguration.class, ServiceCodegenConfiguration.class, ControllerCodegenConfiguration.class})
public class CodegenExampleApplication implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//generateOrderMybatisCode();
		//generateOrderServiceCode();
		generateOrderControllerCode();
	}
	
	protected void generateOrderMybatisCode() {
		new OrderMybatisCodeGenerator().generate();
	}
	
	protected void generateOrderServiceCode() {
		new OrderServiceCodeGenerator().generate();
	}
	
	protected void generateOrderControllerCode() {
		new OrderControllerCodeGenerator().generate();
	}
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(CodegenExampleApplication.class).web(WebApplicationType.NONE).run(args);
	}

}

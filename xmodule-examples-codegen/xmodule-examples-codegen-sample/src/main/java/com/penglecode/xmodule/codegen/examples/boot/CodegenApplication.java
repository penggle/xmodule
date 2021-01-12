package com.penglecode.xmodule.codegen.examples.boot;

import com.penglecode.xmodule.common.codegen.config.ControllerCodegenConfiguration;
import com.penglecode.xmodule.common.codegen.config.MybatisCodegenConfiguration;
import com.penglecode.xmodule.common.codegen.config.ServiceCodegenConfiguration;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({MybatisCodegenConfiguration.class, ServiceCodegenConfiguration.class, ControllerCodegenConfiguration.class})
public class CodegenApplication implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("========================================代码自动生成========================================");
	}
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(CodegenApplication.class).web(WebApplicationType.NONE).run(args);
	}

}

package com.penglecode.xmodule.codegen.examples.boot.generator.order;

import com.penglecode.xmodule.common.codegen.config.MybatisCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.mybatis.AbstractMybatisCodeGenerator;

public class OrderMybatisCodeGenerator extends AbstractMybatisCodeGenerator {

	public OrderMybatisCodeGenerator() {
		super("classpath:mybatis-codegen-config-order.xml", "order");
		MybatisCodegenConfigProperties config = getCodegenConfigProperties();
		//运行时指定Model.java代码输出位置
		config.getJavaModel().setTargetProject("../xmodule-examples-codegen-modulea/src/main/java");
		//运行时指定Mapper.xml代码输出位置(比如输出到隔壁project)
		config.getXmlMapper().setTargetProject("../xmodule-examples-codegen-moduleb/src/main/java");
		//运行时指定Mapper.java代码输出位置(比如输出到隔壁project)
		config.getDaoMapper().setTargetProject("../xmodule-examples-codegen-moduleb/src/main/java");
	}

}

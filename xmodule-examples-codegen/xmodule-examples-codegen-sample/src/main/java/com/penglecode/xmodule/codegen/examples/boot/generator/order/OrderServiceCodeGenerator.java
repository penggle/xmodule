package com.penglecode.xmodule.codegen.examples.boot.generator.order;

import com.penglecode.xmodule.BasePackage;
import com.penglecode.xmodule.common.codegen.config.ServiceCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.service.AbstractServiceCodeGenerator;
import com.penglecode.xmodule.common.util.ClassUtils;

public class OrderServiceCodeGenerator extends AbstractServiceCodeGenerator {

	public OrderServiceCodeGenerator() {
		super("order", new Class<?>[] {
			ClassUtils.forName("com.penglecode.xmodule.ordercenter.model.Order"),
			ClassUtils.forName("com.penglecode.xmodule.ordercenter.model.OrderDetail")
		});
		ServiceCodegenConfigProperties configProperties = getCodegenConfigProperties();
		//运行时指定Model.class的扫描基准包
		configProperties.setBasePackage(BasePackage.class.getPackage().getName());
		//运行时指定XxxService.java代码输出位置(比如输出到隔壁project)
		configProperties.getJinterface().setTargetProject("../xmodule-examples-codegen-moduleb/src/main/java");
		//运行时指定XxxServiceImpl.java代码输出位置(比如输出到隔壁project)
		configProperties.getJimplement().setTargetProject("../xmodule-examples-codegen-moduleb/src/main/java");
	}

}

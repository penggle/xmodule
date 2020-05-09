package com.penglecode.xmodule.codegen.examples.boot.generator.order;

import com.penglecode.xmodule.BasePackage;
import com.penglecode.xmodule.common.codegen.config.ControllerCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.controller.AbstractControllerCodeGenerator;
import com.penglecode.xmodule.common.util.ClassUtils;

public class OrderControllerCodeGenerator extends AbstractControllerCodeGenerator {

	public OrderControllerCodeGenerator() {
		super("order", new Class<?>[] {
			ClassUtils.forName("com.penglecode.xmodule.ordercenter.model.Order"),
			ClassUtils.forName("com.penglecode.xmodule.ordercenter.model.OrderDetail")
		});
		ControllerCodegenConfigProperties configProperties = getCodegenConfigProperties();
		//运行时指定Model.class的扫描基准包
		configProperties.setBasePackage(BasePackage.class.getPackage().getName());
		//运行时指定XxxController.java代码输出位置(比如输出到隔壁project)
		configProperties.setTargetProject("../xmodule-examples-codegen-moduleb/src/main/java");
		configProperties.setPrefixOfApiUrl("/api/ordercenter"); //接口URL前缀
		configProperties.getDomainOfApiUrlMapping().put("com.penglecode.xmodule.ordercenter.model.Order", "order"); //接口URL的domain部分
		configProperties.getDomainOfApiUrlMapping().put("com.penglecode.xmodule.ordercenter.model.OrderDetail", "order/detail"); //接口URL的domain部分
	}

}

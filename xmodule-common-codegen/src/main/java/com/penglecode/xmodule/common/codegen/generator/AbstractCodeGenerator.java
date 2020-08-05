package com.penglecode.xmodule.common.codegen.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.codeden.consts.CodegenModule;
import com.penglecode.xmodule.common.codegen.config.AbstractCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.AbstractCodegenConfiguration;
import com.penglecode.xmodule.common.util.SpringUtils;

/**
 * 代码生成器基类
 * @param <C>
 * @author 	pengpeng
 * @date 	2020年4月21日 下午4:09:46
 */
@SuppressWarnings({ "unchecked", "rawtypes"})
public abstract class AbstractCodeGenerator<C extends AbstractCodegenConfiguration> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCodeGenerator.class);
	
	/**
	 * 业务模块名或者开发者名字,以此解决分工协作问题，
	 * 需要在application.yaml中配置spring.codegen.xxx.config.{businessModule}.*
	 */
	private final String businessModule;
	
	/**
	 * 代码生成配置
	 */
	private final C codegenConfiguration;

	public AbstractCodeGenerator(String businessModule) {
		super();
		Assert.hasText(businessModule, "Parameter 'businessModule' must be required!");
		this.businessModule = businessModule;
		ResolvableType rt = ResolvableType.forClass(getClass());
		while(!rt.equals(ResolvableType.NONE) && !AbstractCodeGenerator.class.equals(rt.getRawClass())) {
			rt = rt.getSuperType();
		}
		Class<?> codegenConfigurationType = (Class<?>) rt.getGeneric(0).getType();
		this.codegenConfiguration = (C) SpringUtils.getBean(codegenConfigurationType);
	}
	
	/**
	 * 生成代码
	 */
	public final void execute() {
		String codeModule = getCodeModule().getName();
		LOGGER.info(">>> 生成数据模型的{}代码开始...", codeModule);
		try {
			init();
			doExecute();
		} catch (Exception e) {
			LOGGER.error(String.format(">>> 生成数据模型的%s代码出错：%s", codeModule, e.getMessage()), e);
		}
		LOGGER.info("<<< 生成数据模型的{}代码结束...", codeModule);
	}

	/**
	 * 初始化
	 * @throws Exception
	 */
	protected void init() throws Exception {
	}
	
	/**
	 * 执行代码生成
	 * @throws Exception
	 */
	protected abstract void doExecute() throws Exception;
	
	/**
	 * 返回代码层次模块
	 * @return
	 */
	protected abstract CodegenModule getCodeModule();
	
	/**
	 * 生成代码生成配置
	 * @param <T>
	 * @return
	 */
	protected <T extends AbstractCodegenConfigProperties> T getCodegenConfigProperties() {
		return (T) codegenConfiguration.getConfig().get(businessModule);
	}
	
	protected String getBusinessModule() {
		return businessModule;
	}

	protected C getCodegenConfiguration() {
		return codegenConfiguration;
	}
	
}

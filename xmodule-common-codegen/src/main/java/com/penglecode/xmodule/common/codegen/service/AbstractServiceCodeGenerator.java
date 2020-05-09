package com.penglecode.xmodule.common.codegen.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.codeden.consts.CodegenModule;
import com.penglecode.xmodule.common.codeden.support.CodegenUtils;
import com.penglecode.xmodule.common.codegen.config.CommonCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.ServiceCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.ServiceCodegenConfiguration;
import com.penglecode.xmodule.common.codegen.generator.AbstractCodeGenerator;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.ClassUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.FileUtils;
import com.penglecode.xmodule.common.util.ModelDecodeUtils;
import com.penglecode.xmodule.common.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Service代码生成基类
 * 
 * @author 	pengpeng
 * @date 	2020年4月22日 上午9:16:30
 */
@SuppressWarnings("unchecked")
public abstract class AbstractServiceCodeGenerator extends AbstractCodeGenerator<ServiceCodegenConfiguration> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServiceCodeGenerator.class);
	
	/**
	 * 要生成Service代码的数据模型类，例如对User.java数据模型生成UserService.java和UserServiceImpl.java
	 */
	private final List<Class<?>> modelClasses;
	
	public AbstractServiceCodeGenerator(String businessModule, Class<?>... modelClasses) {
		super(businessModule);
		Assert.notEmpty(modelClasses, "Parameter 'modelClasses' must be required!");
		this.modelClasses = Arrays.asList(modelClasses);
	}

	@Override
	protected void init() throws Exception {
		//根据需要创建Service接口所在目录
		FileUtils.mkDirIfNecessary(getServiceInterfacePackageDir());
		//根据需要创建Service实现类所在目录
		FileUtils.mkDirIfNecessary(getServiceImplementPackageDir());
	}

	@Override
	protected void doGenerate() throws Exception {
		List<String> generatedCodeFilePaths = new ArrayList<String>();
		try {
			for(Class<?> modelClass : modelClasses) {
				generatedCodeFilePaths.add(generateServiceInterfaceCode(modelClass));
				generatedCodeFilePaths.add(generateServiceImplementCode(modelClass));
			}
		} catch (Exception e) {
			generatedCodeFilePaths.stream().forEach(FileUtils::deleteFileQuietly); //rollback for exception
			throw e;
		}
	}

	/**
	 * 生成服务接口代码
	 * @param modelClass
	 * @return 返回生成的代码文件路径
	 * @throws Exception
	 */
	protected String generateServiceInterfaceCode(Class<?> modelClass) throws Exception {
		LOGGER.info(String.format(">>> 生成数据模型[%s]的%s接口代码", modelClass.getName(), getCodeModule().getName()));
		Map<String,Object> serviceCodeParameter = createServiceInterfaceCodeParameter(modelClass);
		serviceCodeParameter.put("serviceClassFileName", CodegenUtils.createClassFileName((String) serviceCodeParameter.get("serviceClassName"), new File(getServiceInterfacePackageDir())));
		return doServiceInterfaceCodeGeneration(serviceCodeParameter);
	}
	
	/**
	 * 生成服务实现代码
	 * @param modelClass
	 * @return 返回生成的代码文件路径
	 * @throws Exception
	 */
	protected String generateServiceImplementCode(Class<?> modelClass) throws Exception {
		LOGGER.info(String.format(">>> 生成数据模型[%s]的%s实现代码", modelClass.getName(), getCodeModule().getName()));
		Map<String,Object> serviceCodeParameter = createServiceImplementCodeParameter(modelClass);
		serviceCodeParameter.put("serviceImplClassFileName", CodegenUtils.createClassFileName((String) serviceCodeParameter.get("serviceImplClassName"), new File(getServiceImplementPackageDir())));
		return doServiceImplementCodeGeneration(serviceCodeParameter);
	}
	
	/**
	 * 创建服务接口代码生成所需参数
	 * @param modelClass
	 * @return
	 * @throws Exception
	 */
	protected Map<String,Object> createServiceInterfaceCodeParameter(Class<?> modelClass) throws Exception {
		Map<String,Object> serviceCodeParameter = new HashMap<String,Object>();
		
		ServiceCodegenConfigProperties configProperties = getCodegenConfigProperties();
		CommonCodegenConfigProperties interfaceConfig = configProperties.getJinterface();
		
		serviceCodeParameter.put("packageName", interfaceConfig.getTargetPackage());
		
		serviceCodeParameter.put("serviceClassName", CodegenUtils.getModelServiceInterfaceClassName(modelClass.getSimpleName()));
		
		serviceCodeParameter.put("date", DateTimeUtils.formatNow("yyyy'年'MM'月'dd'日' a HH:mm:ss"));
		serviceCodeParameter.put("author", interfaceConfig.getAuthor());
		serviceCodeParameter.put("modelClass", modelClass.getName());
		serviceCodeParameter.put("modelClassName", modelClass.getSimpleName());
		serviceCodeParameter.put("modelAliasName", CodegenUtils.getModelAliasName(modelClass));
		serviceCodeParameter.put("modelName", CodegenUtils.getModelName(modelClass));
		
		Class<?> modelIdClass = CodegenUtils.getModelIdClass(modelClass);
		serviceCodeParameter.put("modelIdClass", modelIdClass.getName());
		String modelIdClassName = modelIdClass.getSimpleName();
		if(modelIdClass.getName().contains(modelClass.getName() + "$")) { //基于<ModelClass>内部类的复合主键?
			modelIdClassName = modelIdClass.getName().replace(modelClass.getPackage().getName() + ".", "").replace('$', '.');
		}
		serviceCodeParameter.put("modelIdClassName", modelIdClassName);
		
		List<Field> modelIdFields = CodegenUtils.getModelIdFields(modelClass);
		if(modelIdFields.size() > 1) { //复合主键?
			serviceCodeParameter.put("modelIdName", "id");
		} else {
			Field modelIdField = modelIdFields.get(0);
			serviceCodeParameter.put("modelIdName", modelIdField.getName());
		}
		
		//JDK导入类库
		Set<String> jdkImports = new TreeSet<String>(Comparator.comparing(Function.identity()));
		jdkImports.add(List.class.getName());
		if(modelIdClass.getName().startsWith("java.") && !modelIdClass.getName().startsWith("java.lang.")) {
			jdkImports.add(modelIdClass.getName());
		}
		serviceCodeParameter.put("jdkImports", jdkImports);
		
		//第三方导入类库
		Set<String> thirdImports = new TreeSet<String>(Comparator.comparing(Function.identity()));
		serviceCodeParameter.put("thirdImports", thirdImports);
		
		//项目内导入类库
		Set<String> projectImports = new TreeSet<String>(Comparator.comparing(Function.identity()));
		projectImports.add(Page.class.getName());
		projectImports.add(Sort.class.getName());
		projectImports.add(modelClass.getName());
		if(modelIdFields.size() > 1) { //复合主键?
			if(!modelIdClass.getName().contains(modelClass.getName() + "$")) { //非基于<ModelClass>内部类的复合主键?
				projectImports.add(modelIdClass.getName().replace('$', '.'));
			}
		}
		serviceCodeParameter.put("projectImports", projectImports);
		return serviceCodeParameter;
	}
	
	/**
	 * 创建服务实现代码生成所需参数
	 * @param modelClass
	 * @return
	 * @throws Exception
	 */
	protected Map<String,Object> createServiceImplementCodeParameter(Class<?> modelClass) throws Exception {
		Map<String,Object> serviceCodeParameter = new HashMap<String,Object>();
		
		ServiceCodegenConfigProperties configProperties = getCodegenConfigProperties();
		CommonCodegenConfigProperties interfaceConfig = configProperties.getJinterface();
		CommonCodegenConfigProperties implementConfig = configProperties.getJimplement();
		
		serviceCodeParameter.put("packageName", implementConfig.getTargetPackage());
		
		String serviceClassName = CodegenUtils.getModelServiceInterfaceClassName(modelClass.getSimpleName());
		serviceCodeParameter.put("serviceClassName", serviceClassName);
		serviceCodeParameter.put("modelClassNameLower", StringUtils.firstLetterLowerCase(modelClass.getSimpleName()));
		serviceCodeParameter.put("serviceImplClassName", CodegenUtils.getModelServiceImplementClassName(modelClass.getSimpleName()));
		serviceCodeParameter.put("serviceBeanName", StringUtils.firstLetterLowerCase(serviceClassName));
		
		serviceCodeParameter.put("modelClass", modelClass.getName());
		serviceCodeParameter.put("modelClassName", modelClass.getSimpleName());
		serviceCodeParameter.put("modelAliasName", CodegenUtils.getModelAliasName(modelClass));
		serviceCodeParameter.put("modelName", CodegenUtils.getModelName(modelClass));
		
		Class<?> modelIdClass = CodegenUtils.getModelIdClass(modelClass);
		serviceCodeParameter.put("modelIdClass", modelIdClass.getName());
		String modelIdClassName = modelIdClass.getSimpleName();
		if(modelIdClass.getName().contains(modelClass.getName() + "$")) { //基于<ModelClass>内部类的复合主键?
			modelIdClassName = modelIdClass.getName().replace(modelClass.getPackage().getName() + ".", "").replace('$', '.');
		}
		serviceCodeParameter.put("modelIdClassName", modelIdClassName);
		
		Class<?> modelMapBuilderClass = ClassUtils.forName(modelClass.getName() + ".MapBuilder");
		Method[] modelMethods = modelMapBuilderClass.getDeclaredMethods();
		List<String> modelMapWithMethods = Stream.of(modelMethods).filter(m -> {
			return Modifier.isPublic(m.getModifiers()) && m.getName().startsWith("with");
		}).map(m -> m.getName()).collect(Collectors.toList());
		serviceCodeParameter.put("modelMapWithMethods", modelMapWithMethods);
		
		List<Field> modelIdFields = CodegenUtils.getModelIdFields(modelClass);
		if(modelIdFields.size() > 1) { //存在复合主键?
			serviceCodeParameter.put("getModelIdMethodName", "ofPrimaryKey");
			serviceCodeParameter.put("modelIdName", "id");
		} else {
			Field modelIdField = modelIdFields.get(0);
			serviceCodeParameter.put("modelIdName", modelIdField.getName());
			serviceCodeParameter.put("getModelIdMethodName", CodegenUtils.getGetterMethodName(modelIdField.getName(), modelIdField.getType()));
		}
		
		//JDK导入类库
		Set<String> jdkImports = new TreeSet<String>(Comparator.comparing(Function.identity()));
		jdkImports.add(List.class.getName());
		jdkImports.add(Map.class.getName());
		if(modelIdClass.getName().startsWith("java.") && !modelIdClass.getName().startsWith("java.lang.")) {
			jdkImports.add(modelIdClass.getName());
		}
		serviceCodeParameter.put("jdkImports", jdkImports);
		
		//第三方导入类库
		Set<String> thirdImports = new TreeSet<String>(Comparator.comparing(Function.identity()));
		thirdImports.add(RowBounds.class.getName());
		thirdImports.add(Autowired.class.getName());
		thirdImports.add(Service.class.getName());
		thirdImports.add(Propagation.class.getName());
		thirdImports.add(Transactional.class.getName());
		serviceCodeParameter.put("thirdImports", thirdImports);
		
		//项目内导入类库
		Set<String> projectImports = new TreeSet<String>(Comparator.comparing(Function.identity()));
		projectImports.add(Page.class.getName());
		projectImports.add(Sort.class.getName());
		projectImports.add(ValidationAssert.class.getName());
		projectImports.add(modelClass.getName());
		projectImports.add(ModelDecodeUtils.class.getName());
		projectImports.add(CodegenUtils.getModelMapperClass(modelClass, getCodegenConfigProperties().getBasePackage()).getName());
		projectImports.add(Sort.class.getName());
		if(modelIdFields.size() > 1) { //复合主键?
			if(!modelIdClass.getName().contains(modelClass.getName() + "$")) { //非基于<ModelClass>内部类的复合主键?
				projectImports.add(modelIdClass.getName().replace('$', '.'));
			}
		}
		
		if(!interfaceConfig.getTargetPackage().equals(implementConfig.getTargetPackage())) { //如果服务接口与服务实现不在同一个包下,则需要导包
			projectImports.add(interfaceConfig.getTargetPackage() + "." + serviceClassName);
		}
		serviceCodeParameter.put("projectImports", projectImports);
		return serviceCodeParameter;
	}
	
	/**
	 * 执行服务接口代码生成
	 * @param serviceCodeParameter
	 * @return 返回生成的代码文件路径
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	protected String doServiceInterfaceCodeGeneration(Map<String,Object> serviceCodeParameter) throws Exception {
		Configuration configuration = new Configuration();
		configuration.setDirectoryForTemplateLoading(getServiceCodeTemplateDir());
		Template serviceCodeTemplate = configuration.getTemplate("ModelService.ftl");
		String serviceCodeFile = getServiceInterfacePackageDir() + "/" + serviceCodeParameter.get("serviceClassFileName");
		try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(serviceCodeFile))))) {
			serviceCodeTemplate.process(serviceCodeParameter, out);
		}
		return serviceCodeFile;
	}
	
	/**
	 * 执行服务实现接口代码生成
	 * @param serviceCodeParameter
	 * @return 返回生成的代码文件路径
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	protected String doServiceImplementCodeGeneration(Map<String,Object> serviceCodeParameter) throws Exception {
		Configuration configuration = new Configuration();
		configuration.setDirectoryForTemplateLoading(getServiceCodeTemplateDir());
		Template serviceCodeTemplate = configuration.getTemplate("ModelServiceImpl.ftl");
		String serviceCodeFile = getServiceImplementPackageDir() + "/" + serviceCodeParameter.get("serviceImplClassFileName");
		try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(serviceCodeFile))))) {
			serviceCodeTemplate.process(serviceCodeParameter, out);
		}
		return serviceCodeFile;
	}
	
	/**
	 * 获取服务代码freemarker模板所在目录
	 * @return
	 */
	protected File getServiceCodeTemplateDir() {
		return new File(AbstractServiceCodeGenerator.class.getResource("").getFile());
	}
	
	/**
	 * 获取Service接口所在包的文件路径
	 * @return
	 */
	protected String getServiceInterfacePackageDir() {
		CommonCodegenConfigProperties interfaceConfig = getCodegenConfigProperties().getJinterface();
		return FileUtils.normalizePath(interfaceConfig.getTargetProject() + "/" + interfaceConfig.getTargetPackage().replace(".", "/"));
	}
	
	/**
	 * 获取Service实现类所在包的文件路径
	 * @return
	 */
	protected String getServiceImplementPackageDir() {
		CommonCodegenConfigProperties implementConfig = getCodegenConfigProperties().getJimplement();
		return FileUtils.normalizePath(implementConfig.getTargetProject() + "/" + implementConfig.getTargetPackage().replace(".", "/"));
	}

	@Override
	protected ServiceCodegenConfigProperties getCodegenConfigProperties() {
		return super.getCodegenConfigProperties();
	}

	@Override
	protected CodegenModule getCodeModule() {
		return CodegenModule.SERVICE;
	}
	
	protected List<Class<?>> getModelClasses() {
		return modelClasses;
	}
	
}

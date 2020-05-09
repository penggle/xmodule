package com.penglecode.xmodule.common.codegen.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.codeden.consts.CodegenModule;
import com.penglecode.xmodule.common.codeden.support.CodegenUtils;
import com.penglecode.xmodule.common.codegen.config.ControllerCodegenConfigProperties;
import com.penglecode.xmodule.common.codegen.config.ControllerCodegenConfiguration;
import com.penglecode.xmodule.common.codegen.generator.AbstractCodeGenerator;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.util.ClassUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.FileUtils;
import com.penglecode.xmodule.common.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Controller代码生成基类
 * 
 * @author 	pengpeng
 * @date 	2020年4月22日 下午3:30:40
 */
public class AbstractControllerCodeGenerator extends AbstractCodeGenerator<ControllerCodegenConfiguration> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractControllerCodeGenerator.class);
	
	/**
	 * 要生成Controller代码的数据模型类，例如对User.java数据模型生成UserController.java和UserControllerImpl.java
	 */
	private final List<Class<?>> modelClasses;
	
	public AbstractControllerCodeGenerator(String businessModule, Class<?>... modelClasses) {
		super(businessModule);
		Assert.notEmpty(modelClasses, "Parameter 'modelClasses' must be required!");
		this.modelClasses = Arrays.asList(modelClasses);
	}

	@Override
	protected void init() throws Exception {
		//根据需要创建Controller所在目录
		FileUtils.mkDirIfNecessary(getControllerPackageDir());
	}

	@Override
	protected void doGenerate() throws Exception {
		List<String> generatedCodeFilePaths = new ArrayList<String>();
		try {
			for(Class<?> modelClass : modelClasses) {
				generatedCodeFilePaths.add(generateControllerCode(modelClass));
			}
		} catch (Exception e) {
			generatedCodeFilePaths.stream().forEach(FileUtils::deleteFileQuietly); //rollback for exception
			throw e;
		}
	}
	
	/**
	 * 生成Controller代码
	 * @param modelClass
	 * @return 返回生成的代码文件路径
	 * @throws Exception
	 */
	protected String generateControllerCode(Class<?> modelClass) throws Exception {
		LOGGER.info(String.format(">>> 生成数据模型[%s]的%s代码", modelClass.getName(), getCodeModule().getName()));
		Map<String,Object> controllerCodeParameter = createControllerCodeParameter(modelClass);
		controllerCodeParameter.put("controllerClassFileName", CodegenUtils.createClassFileName((String) controllerCodeParameter.get("controllerClassName"), new File(getControllerPackageDir())));
		return doControllerCodeGeneration(controllerCodeParameter);
	}
	
	/**
	 * 创建Controller代码生成所需参数
	 * @param modelClass
	 * @return
	 * @throws Exception
	 */
	protected Map<String,Object> createControllerCodeParameter(Class<?> modelClass) throws Exception {
		Map<String,Object> controllerCodeParameter = new HashMap<String,Object>();
		
		ControllerCodegenConfigProperties configProperties = getCodegenConfigProperties();
		
		controllerCodeParameter.put("packageName", configProperties.getTargetPackage());
		
		controllerCodeParameter.put("controllerClassName", createControllerClassName(modelClass.getSimpleName()));
		
		controllerCodeParameter.put("date", DateTimeUtils.formatNow("yyyy'年'MM'月'dd'日' a HH:mm:ss"));
		controllerCodeParameter.put("author", configProperties.getAuthor());
		
		String serviceClassName = CodegenUtils.getModelServiceInterfaceClassName(modelClass.getSimpleName());
		controllerCodeParameter.put("serviceClassName", serviceClassName);
		controllerCodeParameter.put("modelReferenceName", StringUtils.firstLetterLowerCase(modelClass.getSimpleName()));
		controllerCodeParameter.put("serviceBeanName", StringUtils.firstLetterLowerCase(serviceClassName));
		
		String modelAliasName = CodegenUtils.getModelAliasName(modelClass);
		controllerCodeParameter.put("modelClass", modelClass.getName());
		controllerCodeParameter.put("modelClassName", modelClass.getSimpleName());
		controllerCodeParameter.put("modelAliasName", modelAliasName);
		controllerCodeParameter.put("modelName", CodegenUtils.getModelName(modelClass));
		
		controllerCodeParameter.put("prefixOfApiUrl", StringUtils.defaultIfEmpty(configProperties.getPrefixOfApiUrl(), ""));
		controllerCodeParameter.put("domainOfApiUrl", StringUtils.defaultIfEmpty(configProperties.getDomainOfApiUrlMapping().get(modelClass.getName()), modelAliasName.toLowerCase()));
		
		Class<?> modelIdClass = CodegenUtils.getModelIdClass(modelClass);
		controllerCodeParameter.put("modelIdClass", modelIdClass.getName());
		String modelIdClassName = modelIdClass.getSimpleName();
		if(modelIdClass.getName().contains(modelClass.getName() + "$")) { //基于<ModelClass>内部类的复合主键?
			modelIdClassName = modelIdClass.getName().replace(modelClass.getPackage().getName() + ".", "").replace('$', '.');
		}
		controllerCodeParameter.put("modelIdClassName", modelIdClassName);
		
		List<Field> modelIdFields = CodegenUtils.getModelIdFields(modelClass);
		
		boolean isCompositePrimaryKey = modelIdFields.size() > 1;
		
		if(isCompositePrimaryKey) { //复合主键?
			controllerCodeParameter.put("modelIdName", "id");
			
			StringBuilder idPathVariables = new StringBuilder();
			for(int i = 0, len = modelIdFields.size(); i < len; i++) {
				Field modelIdField = modelIdFields.get(i);
				idPathVariables.append("{" + modelIdField.getName() + "}");
				if(i != len - 1) {
					idPathVariables.append("/");
				}
			}
			controllerCodeParameter.put("modelIdPath", idPathVariables.toString());
		} else {
			Field modelIdField = modelIdFields.get(0);
			controllerCodeParameter.put("modelIdName", modelIdField.getName());
			controllerCodeParameter.put("modelIdPath", "{" + modelIdField.getName() + "}");
		}
		controllerCodeParameter.put("isCompositePrimaryKey", isCompositePrimaryKey);
		
		Class<?> extendsClass = null;
		if(!StringUtils.isEmpty(configProperties.getExtendsClass())) {
			extendsClass = ClassUtils.loadClass(configProperties.getExtendsClass());
		}
		controllerCodeParameter.put("extendsClassName", extendsClass == null ? null : extendsClass.getSimpleName());
		
		//JDK导入类库
		Set<String> jdkImports = new TreeSet<String>(Comparator.comparing(Function.identity()));
		jdkImports.add(List.class.getName());
		if(modelIdClass.getName().startsWith("java.") && !modelIdClass.getName().startsWith("java.lang.")) {
			jdkImports.add(modelIdClass.getName());
		}
		controllerCodeParameter.put("jdkImports", jdkImports);
		
		//第三方导入类库
		Set<String> thirdImports = new TreeSet<String>(Comparator.comparing(Function.identity()));
		thirdImports.add(Resource.class.getName());
		thirdImports.add(MediaType.class.getName());
		thirdImports.add(DeleteMapping.class.getName());
		thirdImports.add(GetMapping.class.getName());
		if(!isCompositePrimaryKey) {
			thirdImports.add(PathVariable.class.getName());
		}
		thirdImports.add(PostMapping.class.getName());
		thirdImports.add(PutMapping.class.getName());
		thirdImports.add(RequestBody.class.getName());
		thirdImports.add(RestController.class.getName());
		controllerCodeParameter.put("thirdImports", thirdImports);
		
		//项目内导入类库
		Set<String> projectImports = new TreeSet<String>(Comparator.comparing(Function.identity()));
		
		projectImports.add(Result.class.getName());
		projectImports.add(PageResult.class.getName());
		projectImports.add(Page.class.getName());
		projectImports.add(Sort.class.getName());
		projectImports.add(modelClass.getName());
		projectImports.add(CodegenUtils.getModelServiceClass(modelClass, modelAliasName, configProperties.getBasePackage()).getName());
		
		if(modelIdFields.size() > 1) { //复合主键?
			if(!modelIdClass.getName().contains(modelClass.getName() + "$")) { //非基于<ModelClass>内部类的复合主键?
				projectImports.add(modelIdClass.getName().replace('$', '.'));
			}
		}
		
		if(extendsClass != null) {
			projectImports.add(configProperties.getExtendsClass());
		}
		
		controllerCodeParameter.put("projectImports", projectImports);
		return controllerCodeParameter;
	}
	
	/**
	 * 执行Controller代码生成
	 * @param controllerCodeParameter
	 * @return 返回生成的代码文件路径
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	protected String doControllerCodeGeneration(Map<String,Object> controllerCodeParameter) throws Exception {
		Configuration configuration = new Configuration();
		configuration.setDirectoryForTemplateLoading(getControllerCodeTemplateDir());
		Template controllerCodeTemplate = configuration.getTemplate("ModelController.ftl");
		String controllerCodeFile = getControllerPackageDir() + "/" + controllerCodeParameter.get("controllerClassFileName");
		try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(controllerCodeFile))))) {
			controllerCodeTemplate.process(controllerCodeParameter, out);
		}
		return controllerCodeFile;
	}
	
	/**
	 * 获取Controller代码freemarker模板所在目录
	 * @return
	 */
	protected File getControllerCodeTemplateDir() {
		return new File(AbstractControllerCodeGenerator.class.getResource("").getFile());
	}
	
	/**
	 * 生成Controller类名
	 * @param modelClassName
	 * @return
	 */
	protected String createControllerClassName(String modelClassName) {
		return modelClassName + "Controller";
	}

	@Override
	protected CodegenModule getCodeModule() {
		return CodegenModule.CONTROLLER;
	}

	/**
	 * 获取Controller所在包的文件路径
	 * @return
	 */
	protected String getControllerPackageDir() {
		ControllerCodegenConfigProperties config = getCodegenConfigProperties();
		return FileUtils.normalizePath(config.getTargetProject() + "/" + config.getTargetPackage().replace(".", "/"));
	}
	
}

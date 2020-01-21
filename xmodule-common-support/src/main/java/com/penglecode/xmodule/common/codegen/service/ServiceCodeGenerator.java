package com.penglecode.xmodule.common.codegen.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.support.ValueHolder;
import com.penglecode.xmodule.common.util.ArrayUtils;
import com.penglecode.xmodule.common.util.ClassScanningUtils;
import com.penglecode.xmodule.common.util.ClassUtils;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.FileUtils;
import com.penglecode.xmodule.common.util.ModelDecodeUtils;
import com.penglecode.xmodule.common.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 服务代码生成
 * 
 * @author 	pengpeng
 * @date	2019年3月1日 下午3:48:51
 */
public abstract class ServiceCodeGenerator {

	/**
	 * 生成服务Service代码
	 * @param config
	 * @param modelClasses
	 */
	public void generateServiceCode(ServiceCodeConfig config, Class<?>... modelClasses) throws Exception {
		Assert.notNull(config, "Parameter 'config' can not be null!");
		Assert.notEmpty(modelClasses, "Parameter 'modelClasses' can not be empty!");
		System.out.println(">>> 生成数据模型的服务代码开始!");
		prepareServiceCode(config);
		
		List<String> generatedCodeFilePaths = new ArrayList<String>();
		try {
			for(Class<?> modelClass : modelClasses) {
				generatedCodeFilePaths.add(generateServiceInterfaceCode(config, modelClass));
				generatedCodeFilePaths.add(generateServiceImplementationCode(config, modelClass));
			}
		} catch (Exception e) {
			generatedCodeFilePaths.stream().forEach(FileUtils::deleteFileQuietly); //rollback for exception
			throw e;
		}
		System.out.println("<<< 生成数据模型的服务代码完毕!");
	}
	
	/**
	 * 准备服务代码生成的一些事宜(比如生成代码所在目录)
	 * @param config
	 */
	protected void prepareServiceCode(ServiceCodeConfig config) {
		String projectSourceDir = getProjectSourceDir(config);
		String serviceInterfaceDir = projectSourceDir + "/" + config.getServiceInterfacePackage().replace(".", "/");
		FileUtils.mkDirIfNecessary(serviceInterfaceDir);
		config.setServiceInterfaceDir(serviceInterfaceDir);
		String serviceImplementationDir = projectSourceDir + "/" + config.getServiceImplementationPackage().replace(".", "/");
		FileUtils.mkDirIfNecessary(serviceImplementationDir);
		config.setServiceImplementationDir(serviceImplementationDir);
	}
	
	/**
	 * 生成服务接口代码
	 * @param config
	 * @param modelClass
	 * @return 返回生成的代码文件路径
	 * @throws Exception
	 */
	protected String generateServiceInterfaceCode(ServiceCodeConfig config, Class<?> modelClass) throws Exception {
		System.out.println(String.format(">>> 生成数据模型[%s]的服务接口代码", modelClass.getName()));
		Map<String,Object> serviceCodeParameter = createServiceInterfaceCodeParameter(config, modelClass);
		serviceCodeParameter.put("serviceClassFileName", generateServiceCodeFileName((String) serviceCodeParameter.get("serviceClassName"), new File(config.getServiceInterfaceDir())));
		return doServiceInterfaceCodeGeneration(config, serviceCodeParameter);
	}
	
	/**
	 * 生成服务实现代码
	 * @param config
	 * @param modelClass
	 * @return 返回生成的代码文件路径
	 * @throws Exception
	 */
	protected String generateServiceImplementationCode(ServiceCodeConfig config, Class<?> modelClass) throws Exception {
		System.out.println(String.format(">>> 生成数据模型[%s]的服务实现代码", modelClass.getName()));
		Map<String,Object> serviceCodeParameter = createServiceImplementationCodeParameter(config, modelClass);
		serviceCodeParameter.put("serviceImplClassFileName", generateServiceCodeFileName((String) serviceCodeParameter.get("serviceImplClassName"), new File(config.getServiceImplementationDir())));
		return doServiceImplementationCodeGeneration(config, serviceCodeParameter);
	}
	
	/**
	 * 生成服务类文件名,如 UserService.java，UserService.java.1，UserServiceImpl.java，UserServiceImpl.java.1
	 * @param serviceClassName
	 * @param serviceClassFileDir
	 * @return
	 */
	protected String generateServiceCodeFileName(String serviceClassName, File serviceClassFileDir) {
		String serviceClassFileName = serviceClassName + ".java";
		String fileSuffix = "";
		File[] childFiles = serviceClassFileDir.listFiles(f -> !f.isDirectory());
		if(!ArrayUtils.isEmpty(childFiles)) {
			Integer version = Stream.of(childFiles).filter(f -> f.getName().startsWith(serviceClassFileName)).map(f -> {
				String suffix = f.getName().replace(serviceClassFileName, "");
				if(StringUtils.isEmpty(suffix)) {
					return 0;
				} else {
					return Integer.parseInt(StringUtils.stripStart(suffix, "."));
				}
			}).max(Comparator.comparing(Function.identity())).orElse(null);
			if(version != null) {
				fileSuffix = "." + (version + 1);
			}
		}
		return serviceClassFileName + fileSuffix;
	}
	
	/**
	 * 创建服务接口代码生成所需参数
	 * @param config
	 * @param modelClass
	 * @return
	 * @throws Exception
	 */
	protected Map<String,Object> createServiceInterfaceCodeParameter(ServiceCodeConfig config, Class<?> modelClass) throws Exception {
		Map<String,Object> serviceCodeParameter = new HashMap<String,Object>();
		
		serviceCodeParameter.put("packageName", config.getServiceInterfacePackage());
		
		serviceCodeParameter.put("serviceClassName", createServiceInterfaceClassName(modelClass.getSimpleName()));
		
		serviceCodeParameter.put("date", DateTimeUtils.formatNow("yyyy'年'MM'月'dd'日' a HH:mm:ss"));
		serviceCodeParameter.put("author", config.getAuthor());
		serviceCodeParameter.put("modelClass", modelClass.getName());
		serviceCodeParameter.put("modelClassName", modelClass.getSimpleName());
		serviceCodeParameter.put("modelAliasName", getModelAliasName(modelClass));
		serviceCodeParameter.put("modelName", getModelName(modelClass));
		
		Class<?> modelIdClass = getModelIdClass(modelClass);
		serviceCodeParameter.put("modelIdClass", modelIdClass.getName());
		serviceCodeParameter.put("modelIdClassName", modelIdClass.getSimpleName());
		
		//JDK导入类库
		Set<String> jdkImports = new TreeSet<String>(Comparator.comparing(Function.identity()));
		jdkImports.add(List.class.getName());
		if(!modelIdClass.getName().startsWith("java.lang.")) {
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
		serviceCodeParameter.put("projectImports", projectImports);
		return serviceCodeParameter;
	}
	
	/**
	 * 创建服务实现代码生成所需参数
	 * @param config
	 * @param modelClass
	 * @return
	 * @throws Exception
	 */
	protected Map<String,Object> createServiceImplementationCodeParameter(ServiceCodeConfig config, Class<?> modelClass) throws Exception {
		Map<String,Object> serviceCodeParameter = new HashMap<String,Object>();
		
		serviceCodeParameter.put("packageName", config.getServiceImplementationPackage());
		
		String serviceClassName = createServiceInterfaceClassName(modelClass.getSimpleName());
		serviceCodeParameter.put("serviceClassName", serviceClassName);
		serviceCodeParameter.put("modelClassNameLower", StringUtils.firstLetterLowerCase(modelClass.getSimpleName()));
		serviceCodeParameter.put("serviceImplClassName", createServiceImplementationClassName(modelClass.getSimpleName()));
		serviceCodeParameter.put("serviceBeanName", StringUtils.firstLetterLowerCase(serviceClassName));
		
		serviceCodeParameter.put("modelClass", modelClass.getName());
		serviceCodeParameter.put("modelClassName", modelClass.getSimpleName());
		serviceCodeParameter.put("modelAliasName", getModelAliasName(modelClass));
		serviceCodeParameter.put("modelName", getModelName(modelClass));
		
		Class<?> modelIdClass = getModelIdClass(modelClass);
		serviceCodeParameter.put("modelIdClass", modelIdClass.getName());
		serviceCodeParameter.put("modelIdClassName", modelIdClass.getSimpleName());
		
		Class<?> modelMapBuilderClass = ClassUtils.forName(modelClass.getName() + ".MapBuilder");
		Method[] modelMethods = modelMapBuilderClass.getDeclaredMethods();
		List<String> modelMapWithMethods = Stream.of(modelMethods).filter(m -> {
			return Modifier.isPublic(m.getModifiers()) && m.getName().startsWith("with");
		}).map(m -> m.getName()).collect(Collectors.toList());
		serviceCodeParameter.put("modelMapWithMethods", modelMapWithMethods);
		
		Field modelIdField = getModelIdField(modelClass);
		serviceCodeParameter.put("getModelIdMethodName", "get" + StringUtils.firstLetterUpperCase(modelIdField.getName()));
		
		
		//JDK导入类库
		Set<String> jdkImports = new TreeSet<String>(Comparator.comparing(Function.identity()));
		jdkImports.add(List.class.getName());
		jdkImports.add(Map.class.getName());
		if(!modelIdClass.getName().startsWith("java.lang.")) {
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
		projectImports.add(getModelMapperClass(config, modelClass).getName());
		projectImports.add(Sort.class.getName());
		if(!config.getServiceInterfacePackage().equals(config.getServiceImplementationPackage())) { //如果服务接口与服务实现不在同一个包下
			projectImports.add(config.getServiceInterfacePackage() + "." + serviceClassName);
		}
		serviceCodeParameter.put("projectImports", projectImports);
		return serviceCodeParameter;
	}
	
	
	/**
	 * 获取项目源码的路径
	 * @param config
	 * @return
	 */
	protected String getProjectSourceDir(ServiceCodeConfig config) {
		String filePath = getClass().getResource("/").getFile();
		filePath = StringUtils.stripStart(filePath, "/");
		filePath = filePath.replace("/target/classes/", "");
		filePath = filePath.replace("/target/test-classes/", "");
		filePath = filePath + "/" + config.getProjectSourceLocation();
		return FileUtils.normalizePath(filePath);
	}
	
	/**
	 * 获取数据模型的名称
	 * @param modelClass
	 * @return
	 */
	protected String getModelName(Class<?> modelClass) {
		Model model = AnnotationUtils.findAnnotation(modelClass, Model.class);
		return StringUtils.defaultIfEmpty(model.name(), modelClass.getSimpleName());
	}
	
	/**
	 * 获取数据模型的别名
	 * @param modelClass
	 * @return
	 */
	protected String getModelAliasName(Class<?> modelClass) {
		Model model = AnnotationUtils.findAnnotation(modelClass, Model.class);
		return StringUtils.defaultIfEmpty(model.alias(), modelClass.getSimpleName());
	}
	
	/**
	 * 获取数据模型的ID
	 * @param modelClass
	 * @return
	 */
	protected Class<?> getModelIdClass(Class<?> modelClass) {
		final ValueHolder<Class<?>> value = new ValueHolder<Class<?>>();
		ReflectionUtils.doWithFields(modelClass, new ReflectionUtils.FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				Id id = AnnotationUtils.getAnnotation(field, Id.class);
				if(id != null) {
					value.setValue(field.getType());
				}
			}
		});
		Assert.notNull(value.getValue(), "No @Id found in model class : " + modelClass.getName());
		return value.getValue();
	}
	
	/**
	 * 获取数据模型的ID
	 * @param modelClass
	 * @return
	 */
	protected Field getModelIdField(Class<?> modelClass) {
		final ValueHolder<Field> value = new ValueHolder<Field>();
		ReflectionUtils.doWithFields(modelClass, new ReflectionUtils.FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				Id id = AnnotationUtils.getAnnotation(field, Id.class);
				if(id != null) {
					value.setValue(field);
				}
			}
		});
		Assert.notNull(value.getValue(), "No @Id found in model class : " + modelClass.getName());
		return value.getValue();
	}
	
	/**
	 * 获取服务代码freemarker模板所在目录
	 * @return
	 */
	protected File getServiceCodeTemplateDir() {
		return new File(ServiceCodeGenerator.class.getResource("").getFile());
	}
	
	/**
	 * 执行服务接口代码生成
	 * @param config
	 * @param serviceCodeParameter
	 * @return 返回生成的代码文件路径
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	protected String doServiceInterfaceCodeGeneration(ServiceCodeConfig config, Map<String,Object> serviceCodeParameter) throws Exception {
		Configuration configuration = new Configuration();
		configuration.setDirectoryForTemplateLoading(getServiceCodeTemplateDir());
		Template serviceCodeTemplate = configuration.getTemplate("ModelService.ftl");
		String serviceCodeFile = config.getServiceInterfaceDir() + "/" + serviceCodeParameter.get("serviceClassFileName");
		try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(serviceCodeFile))))) {
			serviceCodeTemplate.process(serviceCodeParameter, out);
		}
		return serviceCodeFile;
	}
	
	/**
	 * 执行服务实现接口代码生成
	 * @param config
	 * @param serviceCodeParameter
	 * @return 返回生成的代码文件路径
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	protected String doServiceImplementationCodeGeneration(ServiceCodeConfig config, Map<String,Object> serviceCodeParameter) throws Exception {
		Configuration configuration = new Configuration();
		configuration.setDirectoryForTemplateLoading(getServiceCodeTemplateDir());
		Template serviceCodeTemplate = configuration.getTemplate("ModelServiceImpl.ftl");
		String serviceCodeFile = config.getServiceImplementationDir() + "/" + serviceCodeParameter.get("serviceImplClassFileName");
		try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(serviceCodeFile))))) {
			serviceCodeTemplate.process(serviceCodeParameter, out);
		}
		return serviceCodeFile;
	}
	
	/**
	 * 生成服务接口类名
	 * @param modelClassName
	 * @return
	 */
	protected String createServiceInterfaceClassName(String modelClassName) {
		return modelClassName + "Service";
	}
	
	/**
	 * 生成服务实现类名
	 * @param modelClassName
	 * @return
	 */
	protected String createServiceImplementationClassName(String modelClassName) {
		return modelClassName + "ServiceImpl";
	}
	
	/**
	 * 获取数据模型的Mybatis Mapper接口类的名字
	 * @param modelClassName
	 * @return
	 */
	protected String getModelMapperClassName(String modelClassName) {
		return modelClassName + "Mapper";
	}
	
	/**
	 * 获取项目下的该数据模型所对应的MybatisMapper接口类
	 * @param config
	 * @param modelClass
	 * @return
	 */
	protected Class<?> getModelMapperClass(ServiceCodeConfig config, Class<?> modelClass) {
		String mapperClassName = getModelMapperClassName(modelClass.getSimpleName());
		Set<String> classNames = ClassScanningUtils.scanPackageClassNames(config.getProjectBasePackage());
		ValueHolder<Class<?>> value = new ValueHolder<Class<?>>();
		classNames.stream().filter(n -> n.substring(n.lastIndexOf(".") + 1).equals(mapperClassName)).forEach(className ->{
			Class<?> mapperClass = null;
			try {
				mapperClass = ClassUtils.forName(className);
				if(BaseMybatisMapper.class.isAssignableFrom(mapperClass)) {
					Class<?> modelClazz = getModelClassFromMapper(mapperClass);
					if(modelClass.equals(modelClazz)) {
						value.setValue(mapperClass);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		Assert.notNull(value.getValue(), "No " + mapperClassName + " found for model class : " + modelClass.getName());
		return value.getValue();
	}
	
	/**
	 * 从数据模型的MybatisMapper接口上获取数据模型的类型
	 * @param mapperClass
	 * @return
	 */
	private Class<?> getModelClassFromMapper(Class<?> mapperClass) {
		Type[] types = mapperClass.getGenericInterfaces();
		ParameterizedType type = (ParameterizedType) types[0];
		return (Class<?>) type.getActualTypeArguments()[0];
	}
	
}

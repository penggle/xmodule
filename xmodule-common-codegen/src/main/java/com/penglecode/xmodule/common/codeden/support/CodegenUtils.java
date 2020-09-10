package com.penglecode.xmodule.common.codeden.support;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.IdClass;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.ValueHolder;
import com.penglecode.xmodule.common.util.ArrayUtils;
import com.penglecode.xmodule.common.util.ClassScanningUtils;
import com.penglecode.xmodule.common.util.ClassUtils;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * 代码生成工具类
 * 
 * @author 	pengpeng
 * @date 	2020年4月21日 上午9:05:35
 */
public class CodegenUtils {

	private static final String LINE_SEPARATOR;

    static {
        String ls = System.getProperty("line.separator"); //$NON-NLS-1$
        if (ls == null) {
            ls = "\n"; //$NON-NLS-1$
        }
        LINE_SEPARATOR = ls;
    }
	
    /**
     * 换行
     * @param sb
     */
    public static void newLine(StringBuilder sb) {
        sb.append(LINE_SEPARATOR);
    }
    
    /**
     * 判断指定的modelClass是否具有复合主键
     * @param modelClass
     * @return
     */
    public static boolean hasCompositePrimaryKey(Class<?> modelClass) {
    	IdClass idClass = AnnotationUtils.findAnnotation(modelClass, IdClass.class);
    	return idClass != null;
    }
    
    /**
	 * 生成指定类文件名, 如果已经存在则使用自增版本进行重命名
	 * @param classSimpleName			- 类短名，例如UserService, UserServiceImpl, UserController
	 * @param classFileDir				- 类文件所在目录
	 * @return 返回文件名, 例如 UserService.java，UserService.java.1，UserServiceImpl.java，UserServiceImpl.java.1
	 */
    public static String createClassFileName(String classSimpleName, File classFileDir) {
		String classFileName = classSimpleName + ".java";
		String fileSuffix = "";
		File[] childFiles = classFileDir.listFiles(f -> !f.isDirectory());
		if(!ArrayUtils.isEmpty(childFiles)) {
			Integer version = Stream.of(childFiles).filter(f -> f.getName().startsWith(classFileName)).map(f -> {
				String suffix = f.getName().replace(classFileName, "");
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
		return classFileName + fileSuffix;
	}
    
    /**
	 * 获取数据模型的名称
	 * @param modelClass
	 * @return
	 */
	public static String getModelName(Class<?> modelClass) {
		Model model = AnnotationUtils.findAnnotation(modelClass, Model.class);
		return StringUtils.defaultIfEmpty(model.name(), modelClass.getSimpleName());
	}
	
	/**
	 * 获取数据模型的别名
	 * @param modelClass
	 * @return
	 */
	public static String getModelAliasName(Class<?> modelClass) {
		Model model = AnnotationUtils.findAnnotation(modelClass, Model.class);
		return StringUtils.defaultIfEmpty(model.alias(), modelClass.getSimpleName());
	}
	
	
	/**
	 * 获取数据模型的ID
	 * @param modelClass
	 * @return
	 */
	public static Class<?> getModelIdClass(Class<?> modelClass) {
		IdClass idClass = AnnotationUtils.findAnnotation(modelClass, IdClass.class);
		if(idClass != null) { //复合主键?
			return idClass.value();
		} else { //单一主键
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
	}
	
	/**
	 * 获取数据模型的ID
	 * @param modelClass
	 * @return
	 */
	public static List<Field> getModelIdFields(Class<?> modelClass) {
		List<Field> fieldList = new ArrayList<Field>();
		ReflectionUtils.doWithFields(modelClass, new ReflectionUtils.FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				Id id = AnnotationUtils.getAnnotation(field, Id.class);
				if(id != null) {
					fieldList.add(field);
				}
			}
		});
		Assert.notEmpty(fieldList, "No @Id found in model class : " + modelClass.getName());
		return fieldList;
	}
	
	/**
	 * 获取项目下的该数据模型所对应的Mybatis Mapper接口类
	 * @param modelClass
	 * @param basePackage
	 * @return
	 */
	public static Class<?> getModelMapperClass(Class<?> modelClass, String basePackage) {
		String mapperClassName = getModelMapperClassName(modelClass.getSimpleName());
		Set<String> classNames = ClassScanningUtils.scanClassNames(basePackage);
		ValueHolder<Class<?>> value = new ValueHolder<Class<?>>();
		classNames.stream().filter(n -> n.substring(n.lastIndexOf(".") + 1).equals(mapperClassName)).forEach(className ->{
			Class<?> mapperClass;
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
	 * 获取项目下的该数据模型所对应的Service接口类
	 * @param modelClass
	 * @param modelAliasName
	 * @param basePackage
	 * @return
	 */
	public static Class<?> getModelServiceClass(Class<?> modelClass, String modelAliasName, String basePackage) {
		String serviceClassName = getModelServiceInterfaceClassName(modelClass.getSimpleName());
		Set<String> classNames = ClassScanningUtils.scanClassNames(basePackage);
		ValueHolder<Class<?>> value = new ValueHolder<Class<?>>();
		classNames.stream().filter(n -> n.substring(n.lastIndexOf(".") + 1).equals(serviceClassName)).forEach(className ->{
			Class<?> serviceClass;
			try {
				serviceClass = ClassUtils.forName(className);
				String createModelMethodName = "create" + modelAliasName;
				Method createModelMethod = ReflectionUtils.findMethod(serviceClass, createModelMethodName, modelClass);
				if(createModelMethod != null) {
					value.setValue(serviceClass);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		Assert.notNull(value.getValue(), "No " + serviceClassName + " found for model class : " + modelClass.getName());
		return value.getValue();
	}
	
	/**
	 * 获取数据模型的Mybatis Mapper接口类的名字
	 * @param modelClassName
	 * @return
	 */
	public static String getModelMapperClassName(String modelClassName) {
		return modelClassName + "Mapper";
	}
	
	/**
	 * 生成服务接口类名
	 * @param modelClassName
	 * @return
	 */
	public static String getModelServiceInterfaceClassName(String modelClassName) {
		return modelClassName + "Service";
	}
	
	/**
	 * 生成服务实现类名
	 * @param modelClassName
	 * @return
	 */
	public static String getModelServiceImplementClassName(String modelClassName) {
		return modelClassName + "ServiceImpl";
	}
	
	/**
	 * 从数据模型的MybatisMapper接口上获取数据模型的类型
	 * @param mapperClass
	 * @return
	 */
	public static Class<?> getModelClassFromMapper(Class<?> mapperClass) {
		Type[] types = mapperClass.getGenericInterfaces();
		ParameterizedType type = (ParameterizedType) types[0];
		return (Class<?>) type.getActualTypeArguments()[0];
	}
    
	/**
	 * 通过指定字段名及字段类型获取其Getter方法
	 * @param propertyName
	 * @param propertyType
	 * @return
	 */
	public static String getGetterMethodName(String propertyName, Class<?> propertyType) {
		return JavaBeansUtil.getGetterMethodName(propertyName, new FullyQualifiedJavaType(propertyType.getName()));
	}
	
	/**
	 * 通过指定字段名获取其Setter方法
	 * @param propertyName
	 * @return
	 */
	public static String getSetterMethodName(String propertyName) {
		return JavaBeansUtil.getSetterMethodName(propertyName);
	}
	
}

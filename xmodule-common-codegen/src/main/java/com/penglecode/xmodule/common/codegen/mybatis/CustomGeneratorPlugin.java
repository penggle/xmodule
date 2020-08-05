package com.penglecode.xmodule.common.codegen.mybatis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.penglecode.xmodule.common.codeden.consts.CodegenConstants;
import com.penglecode.xmodule.common.codeden.support.CodegenUtils;
import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.IdClass;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.mybatis.Dialect.Type;
import com.penglecode.xmodule.common.mybatis.MybatisUtils;
import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.BaseModel;
import com.penglecode.xmodule.common.util.*;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomGeneratorPlugin extends PluginAdapter {

	/** 表名别名 */
	private String tableAliasName = "a";
	
	/** 数据库方言 */
	private Type dialect = Type.MYSQL;
	
	/** 默认的Serializable类 */
	private String defaultSerializableClass = Serializable.class.getName();
	
	/** 实体模型自定义的Serializable类 */
	private String modelSerializableClass = BaseModel.class.getName();
	
	/** baseMapper的全路径类名 */
	private Set<String> baseMapperClasses = new HashSet<String>();
	
	/** 复合主键的内部类名 */
	private String compositePrimaryKeyClass = "PrimaryKey";
	
	/** MybatisUtils类的全路径类名 */
	private String mybatisUtilsClass = MybatisUtils.class.getName();
	
	/** 解决MybatisGenerator设置overwrite=false时仅对Java文件有效,XML文件无效的问题 */
	private Boolean mergeableXmlMapper;
	
	public CustomGeneratorPlugin() {
		super();
		baseMapperClasses.add(BaseMybatisMapper.class.getName());
	}

	@Override
	public boolean validate(List<String> warnings) {
		return true;
	}

	@Override
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		String dialectName = properties.getProperty("dialect", Type.MYSQL.name());
		this.dialect = ObjectUtils.defaultIfNull(Type.getType(dialectName), Type.MYSQL);
		this.mergeableXmlMapper = Boolean.valueOf(properties.getProperty("mergeableXmlMapper", "false"));
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		addModelAnnotations(topLevelClass, introspectedTable); //加入@Model, @Id注解
		addModelSuperTypes(topLevelClass, introspectedTable); //加入 XxxModel implements BaseModel {} 声明
		addModelAdditionalProperties(topLevelClass, introspectedTable); //生成实体的辅助字段
		addModelMapBuilder(topLevelClass, introspectedTable); //加入MapBuilder内部类
		addModelCompositePrimaryKey(topLevelClass, introspectedTable); //生成实体的复合主键类(如果存在复合主键的话)
		return true;
	}
	
	/**
	 * 添加实体上的注解，例如加入@Model, @Id注解
	 * @param topLevelClass
	 * @param introspectedTable
	 */
	protected void addModelAnnotations(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> pkColumns = introspectedTable.getPrimaryKeyColumns();
		
		//添加@IdClass主键
		if(pkColumns.size() > 1) { //复合主键?
			StringBuilder sb = new StringBuilder("@" + IdClass.class.getSimpleName() + "(" + compositePrimaryKeyClass + ".class" + ")");
			topLevelClass.addAnnotation(sb.toString());
			topLevelClass.addImportedType(new FullyQualifiedJavaType(IdClass.class.getName()));
			topLevelClass.addImportedType(new FullyQualifiedJavaType(topLevelClass.getType().getFullyQualifiedName() + "." + compositePrimaryKeyClass));
		}
		
		//添加@Model注解
		String modelName = introspectedTable.getTableConfiguration().getProperty("modelName");
		String modelAlias = introspectedTable.getTableConfiguration().getProperty("modelAlias");
		if(!StringUtils.isEmpty(modelName)) {
			StringBuilder sb = new StringBuilder("@" + Model.class.getSimpleName() + "(name=\"" + modelName + "\"");
			if(!StringUtils.isEmpty(modelAlias)) {
				sb.append(", alias=\"" + modelAlias + "\"");
			}
			sb.append(")");
			topLevelClass.addAnnotation(sb.toString());
			topLevelClass.addImportedType(new FullyQualifiedJavaType(Model.class.getName()));
		}
		
		//添加@Id注解
		for(IntrospectedColumn pkColumn : pkColumns) {
			topLevelClass.getFields().stream().filter(f -> f.getName().equals(pkColumn.getJavaProperty())).findFirst().ifPresent(f -> {
				f.addAnnotation("@" + Id.class.getSimpleName());
				topLevelClass.addImportedType(new FullyQualifiedJavaType(Id.class.getName()));
			});
		}
	}
	
	/**
	 * 添加实体的超类或接口，加入 XxxModel implements BaseModel {} 声明
	 * @param topLevelClass
	 * @param introspectedTable
	 */
	protected void addModelSuperTypes(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		String importedType = modelSerializableClass;
		String superInterface = modelSerializableClass;
		Class<?> clazz = ClassUtils.forName(superInterface);
		if(clazz.equals(BaseModel.class)) {
			superInterface = clazz.getSimpleName() + "<" + topLevelClass.getType().getShortName() + ">";
		}
		topLevelClass.addImportedType(new FullyQualifiedJavaType(importedType));
		topLevelClass.addSuperInterface(new FullyQualifiedJavaType(superInterface));
		
		Field field = new Field("serialVersionUID", new FullyQualifiedJavaType("long"));
		field.setFinal(true);
		field.setInitializationString("1L");
		field.setStatic(true);
		field.setVisibility(JavaVisibility.PRIVATE);
		field.addJavaDocLine(" ");
		context.getCommentGenerator().addFieldComment(field, introspectedTable);
		topLevelClass.getFields().add(0, field);
	}
	
	/**
	 * 添加实体的MapBuilder内部类
	 * @param topLevelClass
	 * @param introspectedTable
	 */
	protected void addModelMapBuilder(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		FullyQualifiedJavaType mapBuilderJavaType = new FullyQualifiedJavaType("MapBuilder");
		FullyQualifiedJavaType modelMapJavaType = new FullyQualifiedJavaType("java.util.Map<String,Object>");
		topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.Map"));
		topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.LinkedHashMap"));
		
		Method mapBuilderMethod = new Method("mapBuilder");
		mapBuilderMethod.setVisibility(JavaVisibility.PUBLIC);
		mapBuilderMethod.setReturnType(mapBuilderJavaType);
		mapBuilderMethod.addBodyLine("return new MapBuilder();");
		topLevelClass.addMethod(mapBuilderMethod);
		
		InnerClass mapBuilderClass = new InnerClass(mapBuilderJavaType);
		mapBuilderClass.setVisibility(JavaVisibility.PUBLIC);
		
		mapBuilderClass.addJavaDocLine("/**");
		mapBuilderClass.addJavaDocLine(" * Auto generated by Mybatis Generator");
		mapBuilderClass.addJavaDocLine(" */");
		
		Field modelPropertiesField = new Field("modelProperties", modelMapJavaType);
		modelPropertiesField.addJavaDocLine(" ");
		modelPropertiesField.setVisibility(JavaVisibility.PRIVATE);
		modelPropertiesField.setFinal(true);
		modelPropertiesField.setInitializationString("new LinkedHashMap<String,Object>()");
		mapBuilderClass.addField(modelPropertiesField);
		
		Method mapBuilderConstructor = new Method("MapBuilder");
		mapBuilderConstructor.setConstructor(true);
		mapBuilderConstructor.addBodyLine("super();");
		mapBuilderClass.addMethod(mapBuilderConstructor);
		
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		for(IntrospectedColumn column : allColumns) {
			addMapBuilderMethod(topLevelClass, column, mapBuilderClass, mapBuilderJavaType);
		}
		
		Method buildMethod = new Method("build");
		buildMethod.setVisibility(JavaVisibility.PUBLIC);
		buildMethod.setReturnType(modelMapJavaType);
		buildMethod.addBodyLine("return modelProperties;");
		mapBuilderClass.addMethod(buildMethod);
		
		topLevelClass.addInnerClass(mapBuilderClass);
	}
	
	protected void addMapBuilderMethod(TopLevelClass topLevelClass, IntrospectedColumn column, InnerClass mapBuilderClass, FullyQualifiedJavaType mapBuilderJavaType) {
		String propertyName = column.getJavaProperty();
		Method mapBuilderMethod = new Method("with" + StringUtils.firstLetterUpperCase(propertyName));
		mapBuilderMethod.setVisibility(JavaVisibility.PUBLIC);
		mapBuilderMethod.setReturnType(mapBuilderJavaType);
		mapBuilderMethod.addParameter(new Parameter(column.getFullyQualifiedJavaType(), propertyName, true));
		mapBuilderMethod.addBodyLine("modelProperties.put(\"" + propertyName + "\", BaseModel.first(" + propertyName + ", " + getFieldGetMethodName(topLevelClass, column) + "()));");
		mapBuilderMethod.addBodyLine("return this;");
		mapBuilderClass.addMethod(mapBuilderMethod);
	}
	
	protected String getFieldGetMethodName(TopLevelClass topLevelClass, IntrospectedColumn column) {
		return "get" + StringUtils.firstLetterUpperCase(column.getJavaProperty());
	}

	/**
	 * 生成实体的复合主键类(如果存在复合主键的话)
	 * @param topLevelClass
	 * @param introspectedTable
	 */
	protected void addModelCompositePrimaryKey(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> pkColumns = introspectedTable.getPrimaryKeyColumns();
		if(pkColumns.size() > 1) { //存在复合主键?
			FullyQualifiedJavaType primaryKeyJavaType = new FullyQualifiedJavaType(compositePrimaryKeyClass);
			InnerClass primaryKeyClass = new InnerClass(primaryKeyJavaType);
			primaryKeyClass.setVisibility(JavaVisibility.PUBLIC);
			primaryKeyClass.setStatic(true);
			primaryKeyClass.addJavaDocLine("/**");
			primaryKeyClass.addJavaDocLine(" * Auto generated by Mybatis Generator");
			primaryKeyClass.addJavaDocLine(" */");
			
			topLevelClass.addImportedType(new FullyQualifiedJavaType(defaultSerializableClass));
			primaryKeyClass.addSuperInterface(new FullyQualifiedJavaType(defaultSerializableClass));
			
			
			Field field = new Field("serialVersionUID", new FullyQualifiedJavaType("long"));
			field.setFinal(true);
			field.setInitializationString("1L");
			field.setStatic(true);
			field.setVisibility(JavaVisibility.PRIVATE);
			field.addJavaDocLine(" ");
			primaryKeyClass.addField(field);
			
			//无参构造器
			Method pkClassConstructor0 = new Method(compositePrimaryKeyClass);
			pkClassConstructor0.setConstructor(true);
			pkClassConstructor0.setVisibility(JavaVisibility.PUBLIC);
			pkClassConstructor0.addBodyLine("super();");
			primaryKeyClass.addMethod(pkClassConstructor0);
			
			List<String> pkClassConstructor1Args = new ArrayList<>();
			
			//全参构造器
			Method pkClassConstructor1 = new Method(compositePrimaryKeyClass);
			pkClassConstructor1.setConstructor(true);
			pkClassConstructor1.setVisibility(JavaVisibility.PUBLIC);
			pkClassConstructor1.addBodyLine("super();");
			for(IntrospectedColumn pkColumn : pkColumns) {
				String pkJavaProperty = pkColumn.getJavaProperty();
				FullyQualifiedJavaType pkJavaType = pkColumn.getFullyQualifiedJavaType();
				
				pkClassConstructor1Args.add(pkJavaProperty);
				
				pkClassConstructor1.addParameter(new Parameter(pkJavaType, pkJavaProperty));
				pkClassConstructor1.addBodyLine("this." + pkJavaProperty + " = " + pkJavaProperty + ";");
			}
			primaryKeyClass.addMethod(pkClassConstructor1);
			
			//生成getter/setter方法
			for(IntrospectedColumn pkColumn : pkColumns) {
				String pkJavaProperty = pkColumn.getJavaProperty();
				FullyQualifiedJavaType pkJavaType = pkColumn.getFullyQualifiedJavaType();
				
				Field modelPropertiesField = new Field(pkJavaProperty, pkColumn.getFullyQualifiedJavaType());
				modelPropertiesField.addJavaDocLine("/** " + StringUtils.defaultIfEmpty(pkColumn.getRemarks(), pkColumn.getActualColumnName().toLowerCase()) + " */");
				modelPropertiesField.setVisibility(JavaVisibility.PRIVATE);
				primaryKeyClass.addField(modelPropertiesField);
				
				StringBuilder sb = new StringBuilder();
				
				//生成get方法
				Method getterMethod = new Method(JavaBeansUtil.getGetterMethodName(pkJavaProperty, pkJavaType));
				getterMethod.setReturnType(pkJavaType);
				getterMethod.setVisibility(JavaVisibility.PUBLIC);
		        sb.append("return "); //$NON-NLS-1$
		        sb.append(pkJavaProperty);
		        sb.append(';');
		        getterMethod.addBodyLine(sb.toString());
		        primaryKeyClass.addMethod(getterMethod);
		        
		        sb.setLength(0);
		        
		        //生成set方法
				Method setterMethod = new Method(JavaBeansUtil.getSetterMethodName(pkJavaProperty));
				setterMethod.setVisibility(JavaVisibility.PUBLIC);
				setterMethod.addParameter(new Parameter(pkJavaType, pkJavaProperty));
				sb.append("this."); //$NON-NLS-1$
	            sb.append(pkJavaProperty);
	            sb.append(" = "); //$NON-NLS-1$
	            sb.append(pkJavaProperty);
	            sb.append(';');
	            setterMethod.addBodyLine(sb.toString());
		        primaryKeyClass.addMethod(setterMethod);
			}
			
			//生成Model-to-PrimaryKey对象的方法
			Method ofPrimaryKey = new Method("ofPrimaryKey");
			ofPrimaryKey.setReturnType(new FullyQualifiedJavaType(compositePrimaryKeyClass));
			ofPrimaryKey.setVisibility(JavaVisibility.PUBLIC);
			ofPrimaryKey.addBodyLine("return new " + compositePrimaryKeyClass + "(" + String.join(", ", pkClassConstructor1Args) + ");");
			
			topLevelClass.addMethod(ofPrimaryKey);
			topLevelClass.addInnerClass(primaryKeyClass);
		}
	}
	
	/**
	 * 生成实体的辅助字段(如果需要的话)
	 * @param topLevelClass
	 * @param introspectedTable
	 */
	protected void addModelAdditionalProperties(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		//添加范围查询辅助字段
		int modelPropertySize = topLevelClass.getFields().size();
		Map<String,Set<QueryConditionOperator>> exampleQueryWhereColumnMap =  getExampleQueryWhereColumnMap(introspectedTable);
		if(!CollectionUtils.isEmpty(exampleQueryWhereColumnMap)) {
			for(Map.Entry<String,Set<QueryConditionOperator>> entry : exampleQueryWhereColumnMap.entrySet()) {
				String columnName = entry.getKey();
				Set<QueryConditionOperator> columnOps = entry.getValue();
				if(columnOps.stream().anyMatch(this::isRangeWhereCondition)) {
					allColumns.stream().filter(c -> c.getActualColumnName().toLowerCase().equals(columnName)).findFirst().ifPresent(column -> {
						String rangeMinAdditionalPropertyName = getAdditionalPropertyName(CodegenConstants.RANGE_MIN_JAVA_PROPERTY_PREFIX, column.getJavaProperty());
						addModelAdditionalProperty(topLevelClass, introspectedTable, column, rangeMinAdditionalPropertyName, modelPropertySize == topLevelClass.getFields().size());
						String rangeMaxAdditionalPropertyName = getAdditionalPropertyName(CodegenConstants.RANGE_MAX_JAVA_PROPERTY_PREFIX, column.getJavaProperty());
						addModelAdditionalProperty(topLevelClass, introspectedTable, column, rangeMaxAdditionalPropertyName, modelPropertySize == topLevelClass.getFields().size());
					});
				}
			}
		}
	}
	
	/**
	 * 添加辅助字段(包括getter/setter方法)
	 * @param topLevelClass
	 * @param introspectedTable
	 * @param dbColumn
	 * @param additionalPropertyName
	 * @param firstAdditionalProperty
	 */
	protected void addModelAdditionalProperty(TopLevelClass topLevelClass, IntrospectedTable introspectedTable, IntrospectedColumn dbColumn, String additionalPropertyName, boolean firstAdditionalProperty) {
		FullyQualifiedJavaType additionalPropertyJavaType = dbColumn.getFullyQualifiedJavaType();
		Field field = new Field(additionalPropertyName, additionalPropertyJavaType);
		
		if(firstAdditionalProperty) {
			field.addJavaDocLine("//以下属于辅助字段");
			field.addJavaDocLine("");
		}
		
		//field.addJavaDocLine("/** {@link #" + dbColumn.getJavaProperty() + "} 的范围查询辅助字段 */");
		field.setVisibility(JavaVisibility.PRIVATE);
		topLevelClass.addField(field);
		
		StringBuilder sb = new StringBuilder();
		
		//生成get方法
		Method getterMethod = new Method(JavaBeansUtil.getGetterMethodName(additionalPropertyName, additionalPropertyJavaType));
		getterMethod.setReturnType(additionalPropertyJavaType);
		getterMethod.setVisibility(JavaVisibility.PUBLIC);
        sb.append("return "); //$NON-NLS-1$
        sb.append(additionalPropertyName);
        sb.append(';');
        getterMethod.addBodyLine(sb.toString());
        topLevelClass.addMethod(getterMethod);
        
        sb.setLength(0);
        
        //生成set方法
		Method setterMethod = new Method(JavaBeansUtil.getSetterMethodName(additionalPropertyName));
		setterMethod.setVisibility(JavaVisibility.PUBLIC);
		setterMethod.addParameter(new Parameter(additionalPropertyJavaType, additionalPropertyName));
		sb.append("this."); //$NON-NLS-1$
        sb.append(additionalPropertyName);
        sb.append(" = "); //$NON-NLS-1$
        sb.append(additionalPropertyName);
        sb.append(';');
        setterMethod.addBodyLine(sb.toString());
        topLevelClass.addMethod(setterMethod);
	}
	
	@Override
	public boolean sqlMapGenerated(GeneratedXmlFile sqlMap, IntrospectedTable introspectedTable) {
		try {
			java.lang.reflect.Field field = sqlMap.getClass().getDeclaredField("isMergeable");
			field.setAccessible(true);
			field.setBoolean(sqlMap, mergeableXmlMapper);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 自定义Mapper.java生成
	 */
	@Override
	public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
		String mapperAnnotationString = introspectedTable.getTableConfiguration().getProperty("mapperAnnotations");
		String[] mapperAnnotations = null;
		if(!StringUtils.isEmpty(mapperAnnotationString)) {
			mapperAnnotations = mapperAnnotationString.split(",");
		}
		// 获取实体类
		FullyQualifiedJavaType modelType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
		// import接口
		for (String baseMapper : baseMapperClasses) {
			baseMapper = baseMapper.trim();
			if(!StringUtils.isEmpty(baseMapper)) {
				interfaze.addImportedType(new FullyQualifiedJavaType(baseMapper));
				interfaze.addSuperInterface(new FullyQualifiedJavaType(baseMapper + "<" + modelType.getShortName() + ">"));
			}
		}
		// import实体类
		interfaze.addImportedType(modelType);
		
		if(mapperAnnotations != null && mapperAnnotations.length > 0) {
			for(String mapperAnnotation : mapperAnnotations) {
				mapperAnnotation = mapperAnnotation.trim();
				if(!StringUtils.isEmpty(mapperAnnotation)) {
					FullyQualifiedJavaType annotation = new FullyQualifiedJavaType(mapperAnnotation);
					interfaze.addImportedType(annotation);
					interfaze.addAnnotation("@" + annotation.getShortName());
				}
			}
		}
		return true;
	}
	
	/**
	 * 自定义Mapper.xml生成
	 */
	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		XmlElement rootElement = document.getRootElement();
		
		//rootElement.addElement(createNewLineElement());
		//rootElement.addElement(createBaseColumnListElement(introspectedTable)); //创建<sql id="xxxBaseColumnList"/>
		
		rootElement.addElement(createNewLineElement());
		rootElement.addElement(createAutoGenCodeCommentStartElement());
		
		rootElement.addElement(createInsertModelElement(introspectedTable)); //创建<insert id="insertModel"/>
		rootElement.addElement(createNewLineElement());
		
		rootElement.addElement(createUpdateModelByIdElement(introspectedTable)); //创建<update id="updateModelById"/>
		rootElement.addElement(createNewLineElement());
		
		rootElement.addElement(createDeleteModelByIdElement(introspectedTable)); //创建<delete id="deleteModelById"/>
		rootElement.addElement(createNewLineElement());
		
		rootElement.addElement(createDeleteModelByIdsElement(introspectedTable)); //创建<delete id="deleteModelByIds"/>
		rootElement.addElement(createNewLineElement());
		
		rootElement.addElement(createSelectModelByIdElement(introspectedTable)); //创建<select id="selectModelById"/>
		rootElement.addElement(createNewLineElement());
		
		rootElement.addElement(createSelectModelByExampleElement(introspectedTable)); //创建<select id="selectModelByExample"/>
		rootElement.addElement(createNewLineElement());
		
		rootElement.addElement(createSelectModelListByIdsElement(introspectedTable)); //创建<select id="selectModelListByIds"/>
		rootElement.addElement(createNewLineElement());
		
		rootElement.addElement(createSelectAllModelListElement(introspectedTable)); //创建<select id="selectAllModelList"/>
		rootElement.addElement(createNewLineElement());
		
		rootElement.addElement(createSelectAllModelCountElement(introspectedTable)); //创建<select id="selectAllModelCount"/>
		rootElement.addElement(createNewLineElement());
		
		rootElement.addElement(createSelectModelListByExampleElement(introspectedTable)); //创建<select id="selectModelPageListByExample"/>
		rootElement.addElement(createNewLineElement());
		
		rootElement.addElement(createSelectModelPageListByExampleElement(introspectedTable)); //创建<select id="selectModelPageListByExample"/>
		rootElement.addElement(createNewLineElement());
		
		rootElement.addElement(createSelectModelPageCountByExampleElement(introspectedTable)); //创建<select id="selectModelPageCountByExample"/>
		rootElement.addElement(createNewLineElement());
		
		rootElement.addElement(createAutoGenCodeCommentEndElement());
		rootElement.addElement(createCustomizedCodeCommentStartElement());
		rootElement.addElement(createNewLineElement());
		rootElement.addElement(createNewLineElement());
		rootElement.addElement(createCustomizedCodeCommentEndElement());
		return true;
	}
	
	/**
	 * 创建空行元素
	 * @return
	 */
	protected TextElement createNewLineElement() {
		return new TextElement("");
	}
	
	/**
	 * 创建自动生成代码开始注释
	 * @return
	 */
	protected TextElement createAutoGenCodeCommentStartElement() {
		StringBuilder sb = new StringBuilder();
		//OutputUtilities.xmlIndent(sb, 1);
		sb.append("<!-- Auto-Generation Code Start -->");
		CodegenUtils.newLine(sb);
		return new TextElement(sb.toString());
	}
	
	/**
	 * 创建<insert id="insertModel"/>
	 * @param introspectedTable
	 * @return
	 */
	protected XmlElement createInsertModelElement(IntrospectedTable introspectedTable) {
		XmlElement element = new XmlElement("insert");
		element.addAttribute(new Attribute("id", "insertModel"));
		element.addAttribute(new Attribute("parameterType", introspectedTable.getTableConfiguration().getDomainObjectName()));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		
		List<IntrospectedColumn> pkColumns = introspectedTable.getPrimaryKeyColumns();
		if(!CollectionUtils.isEmpty(pkColumns) && pkColumns.size() == 1) {
			IntrospectedColumn pkColumn = pkColumns.get(0);
			if(pkColumn.isAutoIncrement()) {
				element.addAttribute(new Attribute("keyProperty", pkColumn.getJavaProperty()));
				element.addAttribute(new Attribute("useGeneratedKeys", "true"));
			}
		}
		
		String insertModelColumnsJson = introspectedTable.getTableConfiguration().getProperty("insertModelColumns");
		Set<String> insertModelColumns = new LinkedHashSet<String>();
		if(!StringUtils.isEmpty(insertModelColumnsJson)) {
			List<String> columnNameList = JsonUtils.json2Object(insertModelColumnsJson, new TypeReference<List<String>>() {});
			if(!CollectionUtils.isEmpty(columnNameList)) {
				if(!CollectionUtils.isEmpty(pkColumns)) {
					for(int i = 0, len = pkColumns.size(); i < len; i++) {
						columnNameList.add(i, pkColumns.get(i).getActualColumnName().toLowerCase()); //加入主键列
					}
				}

				//去重
				insertModelColumns.addAll(columnNameList);
			}
		}
		
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		List<IntrospectedColumn> insertColumns = new ArrayList<IntrospectedColumn>();
		
		if(!CollectionUtils.isEmpty(insertModelColumns)) { //指定了插入字段?
			for(IntrospectedColumn column : allColumns) {
				for(String columnName : insertModelColumns) {
					if(column.getActualColumnName().toLowerCase().equals(columnName.toLowerCase())) {
						insertColumns.add(column);
						break;
					}
				}
			}
		} else { //全字段插入
			insertColumns.addAll(allColumns);
		}
		
		IntrospectedColumn column;
		
		StringBuilder sb = new StringBuilder();
		//OutputUtilities.javaIndent(sb, 1);
		sb.append("INSERT INTO " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + "(");
		CodegenUtils.newLine(sb);
		for(int i = 0, len = insertColumns.size(); i < len; i++) {
			column = insertColumns.get(i);
			OutputUtilities.javaIndent(sb, 3);
			sb.append(column.getActualColumnName().toLowerCase());
			if(i != len - 1) {
				sb.append(",");
			}
			CodegenUtils.newLine(sb);
		}
		OutputUtilities.javaIndent(sb, 2);
		sb.append(") VALUES (");
		CodegenUtils.newLine(sb);
		for(int i = 0, len = insertColumns.size(); i < len; i++) {
			column = insertColumns.get(i);
			OutputUtilities.javaIndent(sb, 3);
			sb.append("#{" + column.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(column) + "}");
			if(i != len - 1) {
				sb.append(",");
			}
			CodegenUtils.newLine(sb);
		}
		OutputUtilities.javaIndent(sb, 2);
		sb.append(")");
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	/**
	 * 创建创建<update id="updateModelById"/>
	 * @param introspectedTable
	 * @return
	 */
	protected XmlElement createUpdateModelByIdElement(IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> pkColumns = introspectedTable.getPrimaryKeyColumns();
		Assert.notEmpty(pkColumns, String.format("No primary key found in table %s for mapper method: %s.updateModelById", introspectedTable.getTableConfiguration().getTableName(), introspectedTable.getTableConfiguration().getDomainObjectName() + "Mapper"));
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		List<IntrospectedColumn> updateColumns = new ArrayList<IntrospectedColumn>();
		
		for(IntrospectedColumn column : allColumns) {
			if(!pkColumns.contains(column)) { //排除pkColumn
				updateColumns.add(column);
			}
		}
		
		XmlElement element = new XmlElement("update");
		element.addAttribute(new Attribute("id", "updateModelById"));
		element.addAttribute(new Attribute("parameterType", "java.util.Map"));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		
		IntrospectedColumn updateColumn;
		
		StringBuilder sb = new StringBuilder();
		//OutputUtilities.javaIndent(sb, 1);
		sb.append("UPDATE " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + tableAliasName);
		CodegenUtils.newLine(sb);
		OutputUtilities.javaIndent(sb, 1);
		OutputUtilities.xmlIndent(sb, 1);
		
		sb.append("     SET ");
		for(int i = 0, len = pkColumns.size(); i < len; i++) {
			String pkColumnName = pkColumns.get(i).getActualColumnName().toLowerCase();
			sb.append(tableAliasName + "." + pkColumnName + " = " + tableAliasName + "." + pkColumnName);
			if(i != len - 1) {
				sb.append(", ");
			}
		}
		CodegenUtils.newLine(sb);
		for(int i = 0, len = updateColumns.size(); i < len; i++) {
			updateColumn = updateColumns.get(i);
			OutputUtilities.javaIndent(sb, 3);
			sb.append("<if test=\"@" + mybatisUtilsClass + "@isContainsParameter(paramMap, '" + updateColumn.getJavaProperty() + "')\">");
			CodegenUtils.newLine(sb);
			
			OutputUtilities.javaIndent(sb, 1);
			sb.append("           ");
			sb.append(",");
			sb.append(tableAliasName + "." + updateColumn.getActualColumnName().toLowerCase() + " = " + "#{paramMap." + updateColumn.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(updateColumn) + "}");
			
			CodegenUtils.newLine(sb);
			
			OutputUtilities.javaIndent(sb, 3);
			sb.append("</if>");
			CodegenUtils.newLine(sb);
		}
		if(pkColumns.size() == 1) { //单一主键?
			IntrospectedColumn pkColumn = pkColumns.get(0);
			sb.append("         WHERE " + tableAliasName + "." + pkColumn.getActualColumnName().toLowerCase() + " = " + "#{id, jdbcType=" + getJdbcTypeName(pkColumn) + "}");
		} else { //复合主键?
			sb.append("         WHERE ");
			for(int i = 0, len = pkColumns.size(); i < len; i++) {
				IntrospectedColumn pkColumn = pkColumns.get(i);
				sb.append(tableAliasName + "." + pkColumn.getActualColumnName().toLowerCase() + " = " + "#{id." + pkColumn.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(pkColumn) + "}");
				if(i != len - 1) {
					sb.append(" AND ");
				}
			}
		}
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	/**
	 * 创建<delete id="deleteModelById"/>
	 * @param introspectedTable
	 * @return
	 */
	protected XmlElement createDeleteModelByIdElement(IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> pkColumns = introspectedTable.getPrimaryKeyColumns();
		Assert.notEmpty(pkColumns, String.format("No primary key found in table %s for mapper method: %s.deleteModelById", introspectedTable.getTableConfiguration().getTableName(), introspectedTable.getTableConfiguration().getDomainObjectName() + "Mapper"));
		
		String parameterType;
		if(pkColumns.size() == 1) { //单一主键?
			parameterType = pkColumns.get(0).getFullyQualifiedJavaType().getFullyQualifiedNameWithoutTypeParameters();
		} else { //复合主键?
			//parameterType = introspectedTable.getTableConfiguration().getDomainObjectName() + CodegenConstants.MODEL_COMPOSITE_PRIMARY_KEY_SUFFIX;
			parameterType = defaultSerializableClass;
		}
		
		XmlElement element = new XmlElement("delete");
		element.addAttribute(new Attribute("id", "deleteModelById"));
		element.addAttribute(new Attribute("parameterType", parameterType));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		
		StringBuilder sb = new StringBuilder();
		//OutputUtilities.javaIndent(sb, 1);
		sb.append("DELETE FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase());
		if(pkColumns.size() == 1) { //单一主键?
			IntrospectedColumn pkColumn = pkColumns.get(0);
			sb.append(" WHERE " + pkColumn.getActualColumnName().toLowerCase() + " = " + "#{" + pkColumn.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(pkColumn) + "}");
		} else { //复合主键?
			sb.append(" WHERE ");
			for(int i = 0, len = pkColumns.size(); i < len; i++) {
				IntrospectedColumn pkColumn = pkColumns.get(i);
				sb.append(pkColumn.getActualColumnName().toLowerCase() + " = " + "#{" + pkColumn.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(pkColumn) + "}");
				if(i != len - 1) {
					sb.append(" AND ");
				}
			}
		}
		
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	/**
	 * 创建<delete id="deleteModelByIds"/>
	 * @param introspectedTable
	 * @return
	 */
	protected XmlElement createDeleteModelByIdsElement(IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> pkColumns = introspectedTable.getPrimaryKeyColumns();
		Assert.notEmpty(pkColumns, String.format("No primary key found in table %s for mapper method: %s.deleteModelByIds", introspectedTable.getTableConfiguration().getTableName(), introspectedTable.getTableConfiguration().getDomainObjectName() + "Mapper"));
		
		XmlElement element = new XmlElement("delete");
		element.addAttribute(new Attribute("id", "deleteModelByIds"));
		element.addAttribute(new Attribute("parameterType", "java.util.List"));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		
		StringBuilder sb = new StringBuilder();
		//OutputUtilities.javaIndent(sb, 1);
		sb.append("DELETE FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase());
		if(pkColumns.size() == 1) { //单一主键? 使用in查询
			IntrospectedColumn pkColumn = pkColumns.get(0);
			sb.append(" WHERE " + pkColumn.getActualColumnName().toLowerCase() + " in ");
			CodegenUtils.newLine(sb);
			OutputUtilities.javaIndent(sb, 2);
			sb.append("<foreach collection=\"list\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">");
			CodegenUtils.newLine(sb);
			OutputUtilities.javaIndent(sb, 3);
			sb.append("#{item, jdbcType=" + getJdbcTypeName(pkColumn) + "}");
			CodegenUtils.newLine(sb);
			OutputUtilities.javaIndent(sb, 2);
			sb.append("</foreach>");
		} else { //复合主键? 使用多个OR查询
			sb.append(" WHERE ");
			CodegenUtils.newLine(sb);
			OutputUtilities.javaIndent(sb, 2);
			sb.append("<foreach collection=\"list\" index=\"index\" item=\"item\" open=\"\" separator=\" OR \" close=\"\">");
			CodegenUtils.newLine(sb);
			OutputUtilities.javaIndent(sb, 3);
			sb.append("(");
			for(int i = 0, len = pkColumns.size(); i < len; i++) {
				IntrospectedColumn pkColumn = pkColumns.get(i);
				sb.append(pkColumn.getActualColumnName().toLowerCase() + " = " + "#{item." + pkColumn.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(pkColumn) + "}");
				if(i != len - 1) {
					sb.append(" AND ");
				}
			}
			sb.append(")");
			CodegenUtils.newLine(sb);
			OutputUtilities.javaIndent(sb, 2);
			sb.append("</foreach>");
		}
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	/**
	 * 创建<select id="selectModelById"/>
	 * @param introspectedTable
	 * @return
	 */
	protected XmlElement createSelectModelByIdElement(IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> pkColumns = introspectedTable.getPrimaryKeyColumns();
		Assert.notEmpty(pkColumns, String.format("No primary key found in table %s for mapper method: %s.selectModelById", introspectedTable.getTableConfiguration().getTableName(), introspectedTable.getTableConfiguration().getDomainObjectName() + "Mapper"));
		
		String parameterType;
		if(pkColumns.size() == 1) { //单一主键?
			parameterType = pkColumns.get(0).getFullyQualifiedJavaType().getFullyQualifiedNameWithoutTypeParameters();
		} else { //复合主键?
			//parameterType = introspectedTable.getTableConfiguration().getDomainObjectName() + CodegenConstants.MODEL_COMPOSITE_PRIMARY_KEY_SUFFIX;
			parameterType = defaultSerializableClass;
		}
		
		XmlElement element = new XmlElement("select");
		element.addAttribute(new Attribute("id", "selectModelById"));
		element.addAttribute(new Attribute("parameterType", parameterType));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		element.addAttribute(new Attribute("resultType", introspectedTable.getTableConfiguration().getDomainObjectName()));
		
		StringBuilder sb = new StringBuilder();
		//OutputUtilities.javaIndent(sb, 1);
		sb.append("SELECT ");
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		IntrospectedColumn column;
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			if(i == 0) {
				//nothing to do
			} else {
				sb.append("       ");
				OutputUtilities.javaIndent(sb, 2);
			}
			sb.append(getColumnSelectStatement(column) + "\t" + column.getJavaProperty());
			if(i != len - 1) {
				sb.append(",");
			}
			CodegenUtils.newLine(sb);
		}
		OutputUtilities.javaIndent(sb, 2);
		sb.append("  FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + tableAliasName);
		CodegenUtils.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		if(pkColumns.size() == 1) { //单一主键?
			IntrospectedColumn pkColumn = pkColumns.get(0);
			sb.append(" WHERE " + tableAliasName + "." + pkColumn.getActualColumnName().toLowerCase() + " = " + "#{" + pkColumn.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(pkColumn) + "}");
		} else { //复合主键?
			sb.append(" WHERE ");
			for(int i = 0, len = pkColumns.size(); i < len; i++) {
				IntrospectedColumn pkColumn = pkColumns.get(i);
				sb.append(tableAliasName + "." + pkColumn.getActualColumnName().toLowerCase() + " = " + "#{" + pkColumn.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(pkColumn) + "}");
				if(i != len - 1) {
					sb.append(" AND ");
				}
			}
		}
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	/**
	 * 创建<select id="selectModelByExample"/>
	 * @param introspectedTable
	 * @return
	 */
	protected XmlElement createSelectModelByExampleElement(IntrospectedTable introspectedTable) {
		Map<String,Set<QueryConditionOperator>> exampleQueryWhereColumnMap =  getExampleQueryWhereColumnMap(introspectedTable);
		
		XmlElement element = new XmlElement("select");
		element.addAttribute(new Attribute("id", "selectModelByExample"));
		element.addAttribute(new Attribute("parameterType", introspectedTable.getTableConfiguration().getDomainObjectName()));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		element.addAttribute(new Attribute("resultType", introspectedTable.getTableConfiguration().getDomainObjectName()));
		
		StringBuilder sb = new StringBuilder();
		//OutputUtilities.javaIndent(sb, 1);
		sb.append("SELECT ");
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		IntrospectedColumn column;
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			if(i == 0) {
				//nothing to do
			} else {
				sb.append("       ");
				OutputUtilities.javaIndent(sb, 2);
			}
			sb.append(getColumnSelectStatement(column) + "\t" + column.getJavaProperty());
			if(i != len - 1) {
				sb.append(",");
			}
			CodegenUtils.newLine(sb);
		}
		OutputUtilities.javaIndent(sb, 2);
		sb.append("  FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + tableAliasName);
		CodegenUtils.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append(" WHERE 1=1");
		//IF where条件
		boolean allocatedWhereCondition = !exampleQueryWhereColumnMap.isEmpty();
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			Set<QueryConditionOperator> columnOps = exampleQueryWhereColumnMap.get(column.getActualColumnName().toLowerCase());
			if(allocatedWhereCondition && CollectionUtils.isEmpty(columnOps)) { //如果指定了where条件列
				continue;
			}
			for(QueryConditionOperator columnOp : columnOps) {
				String javaPropertyName = getJavaPropertyNameOfOp(column, columnOp);
				CodegenUtils.newLine(sb);
				OutputUtilities.javaIndent(sb, 2);
				sb.append("<if test=\"@" + mybatisUtilsClass + "@isNotEmpty(" + javaPropertyName + ")\">");
				CodegenUtils.newLine(sb);
				if(isRangeWhereCondition(columnOp)) {
					OutputUtilities.javaIndent(sb, 2);
					sb.append("<![CDATA[");
					CodegenUtils.newLine(sb);
				}
				OutputUtilities.javaIndent(sb, 1);
				sb.append("       AND ");
				
				applyExampleWhereCondition(sb, columnOp, column, "", javaPropertyName);
				CodegenUtils.newLine(sb);
				if(isRangeWhereCondition(columnOp)) {
					OutputUtilities.javaIndent(sb, 2);
					sb.append("]]>");
					CodegenUtils.newLine(sb);
				}
				OutputUtilities.javaIndent(sb, 2);
				sb.append("</if>");
			}
		}
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	/**
	 * 创建<select id="selectModelListByIds"/>
	 * @param introspectedTable
	 * @return
	 */
	protected XmlElement createSelectModelListByIdsElement(IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> pkColumns = introspectedTable.getPrimaryKeyColumns();
		Assert.notEmpty(pkColumns, String.format("No primary key found in table %s for mapper method: %s.selectModelListByIds", introspectedTable.getTableConfiguration().getTableName(), introspectedTable.getTableConfiguration().getDomainObjectName() + "Mapper"));
		
		XmlElement element = new XmlElement("select");
		element.addAttribute(new Attribute("id", "selectModelListByIds"));
		element.addAttribute(new Attribute("parameterType", "java.util.List"));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		element.addAttribute(new Attribute("resultType", introspectedTable.getTableConfiguration().getDomainObjectName()));
		
		StringBuilder sb = new StringBuilder();
		//OutputUtilities.javaIndent(sb, 1);
		sb.append("SELECT ");
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		IntrospectedColumn column;
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			if(i == 0) {
				//nothing to do
			} else {
				sb.append("       ");
				OutputUtilities.javaIndent(sb, 2);
			}
			sb.append(getColumnSelectStatement(column) + "\t" + column.getJavaProperty());
			if(i != len - 1) {
				sb.append(",");
			}
			CodegenUtils.newLine(sb);
		}
		OutputUtilities.javaIndent(sb, 2);
		sb.append("  FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + tableAliasName);
		CodegenUtils.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);

		boolean singlePrimaryKey = pkColumns.size() == 1;

		if(singlePrimaryKey) { //单一主键? 使用in查询
			IntrospectedColumn pkColumn = pkColumns.get(0);
			sb.append(" WHERE " + tableAliasName + "." + pkColumn.getActualColumnName().toLowerCase() + " in ");
			CodegenUtils.newLine(sb);
			OutputUtilities.javaIndent(sb, 2);
			sb.append("<foreach collection=\"list\" index=\"index\" item=\"item\" open=\"(\" separator=\",\" close=\")\">");
			CodegenUtils.newLine(sb);
			OutputUtilities.javaIndent(sb, 3);
			sb.append("#{item, jdbcType=" + getJdbcTypeName(pkColumn) + "}");
			CodegenUtils.newLine(sb);
			OutputUtilities.javaIndent(sb, 2);
			sb.append("</foreach>");
		} else { //复合主键? 使用多个OR查询
			sb.append(" WHERE ");
			CodegenUtils.newLine(sb);
			OutputUtilities.javaIndent(sb, 2);
			sb.append("<foreach collection=\"list\" index=\"index\" item=\"item\" open=\"\" separator=\" OR \" close=\"\">");
			CodegenUtils.newLine(sb);
			OutputUtilities.javaIndent(sb, 3);
			sb.append("(");
			for(int i = 0, len = pkColumns.size(); i < len; i++) {
				IntrospectedColumn pkColumn = pkColumns.get(i);
				sb.append(tableAliasName + "." + pkColumn.getActualColumnName().toLowerCase() + " = " + "#{item." + pkColumn.getJavaProperty() + ", jdbcType=" + getJdbcTypeName(pkColumn) + "}");
				if(i != len - 1) {
					sb.append(" AND ");
				}
			}
			sb.append(")");
			CodegenUtils.newLine(sb);
			OutputUtilities.javaIndent(sb, 2);
			sb.append("</foreach>");
		}

		CodegenUtils.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		
		if(singlePrimaryKey) { //单一主键?
			sb.append(" ORDER BY " + tableAliasName + "." + pkColumns.get(0).getActualColumnName().toLowerCase() + " ASC");
		} else { //复合主键?
			sb.append(" ORDER BY ");
			for(int i = 0, len = pkColumns.size(); i < len; i++) {
				IntrospectedColumn pkColumn = pkColumns.get(i);
				sb.append(tableAliasName + "." + pkColumn.getActualColumnName().toLowerCase() + " ASC");
				if(i != len - 1) {
					sb.append(", ");
				}
			}
		}
		
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	/**
	 * 创建<select id="selectAllModelList"/>
	 * @param introspectedTable
	 * @return
	 */
	protected XmlElement createSelectAllModelListElement(IntrospectedTable introspectedTable) {
		List<IntrospectedColumn> pkColumns = introspectedTable.getPrimaryKeyColumns();
		
		XmlElement element = new XmlElement("select");
		element.addAttribute(new Attribute("id", "selectAllModelList"));
		element.addAttribute(new Attribute("parameterType", "java.util.Map"));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		element.addAttribute(new Attribute("resultType", introspectedTable.getTableConfiguration().getDomainObjectName()));
		
		StringBuilder sb = new StringBuilder();
		//OutputUtilities.javaIndent(sb, 1);
		sb.append("SELECT ");
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		IntrospectedColumn column;
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			if(i == 0) {
				//noting to do
			} else {
				sb.append("       ");
				OutputUtilities.javaIndent(sb, 2);
			}
			sb.append(getColumnSelectStatement(column) + "\t" + column.getJavaProperty());
			if(i != len - 1) {
				sb.append(",");
			}
			CodegenUtils.newLine(sb);
		}
		OutputUtilities.javaIndent(sb, 2);
		sb.append("  FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + tableAliasName);
		CodegenUtils.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append(" WHERE 1=1");
		CodegenUtils.newLine(sb);
		if(!CollectionUtils.isEmpty(pkColumns)) {
			OutputUtilities.javaIndent(sb, 2);
			if(pkColumns.size() == 1) { //单一主键?
				sb.append(" ORDER BY " + tableAliasName + "." + pkColumns.get(0).getActualColumnName().toLowerCase() + " ASC");
			} else { //复合主键?
				sb.append(" ORDER BY ");
				for(int i = 0, len = pkColumns.size(); i < len; i++) {
					IntrospectedColumn pkColumn = pkColumns.get(i);
					sb.append(tableAliasName + "." + pkColumn.getActualColumnName().toLowerCase() + " ASC");
					if(i != len - 1) {
						sb.append(", ");
					}
				}
			}
		}
		
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	/**
	 * 创建<select id="selectAllModelCount"/>
	 * @param introspectedTable
	 * @return
	 */
	protected XmlElement createSelectAllModelCountElement(IntrospectedTable introspectedTable) {
		XmlElement element = new XmlElement("select");
		element.addAttribute(new Attribute("id", "selectAllModelCount"));
		element.addAttribute(new Attribute("parameterType", "java.util.Map"));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		element.addAttribute(new Attribute("resultType", "java.lang.Integer"));
		
		StringBuilder sb = new StringBuilder();
		//OutputUtilities.javaIndent(sb, 1);
		sb.append("SELECT count(*)");
		CodegenUtils.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append("  FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + tableAliasName);
		CodegenUtils.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append(" WHERE 1=1");
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	/**
	 * 创建<select id="selectModelListByExample"/>
	 * @param introspectedTable
	 * @return
	 */
	protected XmlElement createSelectModelListByExampleElement(IntrospectedTable introspectedTable) {
		return createSelectModelListByExampleElement(introspectedTable, "selectModelListByExample");
	}
	
	/**
	 * <select id="selectModelPageListByExample"/>
	 * @param introspectedTable
	 * @return
	 */
	protected XmlElement createSelectModelPageListByExampleElement(IntrospectedTable introspectedTable) {
		return createSelectModelListByExampleElement(introspectedTable, "selectModelPageListByExample");
	}
	
	protected XmlElement createSelectModelListByExampleElement(IntrospectedTable introspectedTable, String statementId) {
		Map<String,Set<QueryConditionOperator>> exampleQueryWhereColumnMap =  getExampleQueryWhereColumnMap(introspectedTable);
		
		XmlElement element = new XmlElement("select");
		element.addAttribute(new Attribute("id", statementId));
		element.addAttribute(new Attribute("parameterType", "java.util.Map"));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		element.addAttribute(new Attribute("resultType", introspectedTable.getTableConfiguration().getDomainObjectName()));
		
		StringBuilder sb = new StringBuilder();
		//OutputUtilities.javaIndent(sb, 1);
		sb.append("SELECT ");
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		IntrospectedColumn column;
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			if(i == 0) {
				//noting to do
			} else {
				sb.append("       ");
				OutputUtilities.javaIndent(sb, 2);
			}
			sb.append(getColumnSelectStatement(column) + "\t" + column.getJavaProperty());
			if(i != len - 1) {
				sb.append(",");
			}
			CodegenUtils.newLine(sb);
		}
		OutputUtilities.javaIndent(sb, 2);
		sb.append("  FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + tableAliasName);
		CodegenUtils.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append(" WHERE 1=1");
		CodegenUtils.newLine(sb);
		//IF where条件
		boolean allocatedWhereCondition = !exampleQueryWhereColumnMap.isEmpty();
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			Set<QueryConditionOperator> columnOps = exampleQueryWhereColumnMap.get(column.getActualColumnName().toLowerCase());
			if(allocatedWhereCondition && CollectionUtils.isEmpty(columnOps)) { //如果指定了where条件列
				continue;
			}
			for(QueryConditionOperator columnOp : columnOps) {
				String javaPropertyName = getJavaPropertyNameOfOp(column, columnOp);
				
				OutputUtilities.javaIndent(sb, 2);
				sb.append("<if test=\"example != null and @" + mybatisUtilsClass + "@isNotEmpty(example." + javaPropertyName + ")\">");
				CodegenUtils.newLine(sb);
				if(isRangeWhereCondition(columnOp)) {
					OutputUtilities.javaIndent(sb, 2);
					sb.append("<![CDATA[");
					CodegenUtils.newLine(sb);
				}
				OutputUtilities.javaIndent(sb, 1);
				sb.append("       AND ");
				
				applyExampleWhereCondition(sb, columnOp, column, "example.", javaPropertyName);
				CodegenUtils.newLine(sb);
				if(isRangeWhereCondition(columnOp)) {
					OutputUtilities.javaIndent(sb, 2);
					sb.append("]]>");
					CodegenUtils.newLine(sb);
				}
				OutputUtilities.javaIndent(sb, 2);
				sb.append("</if>");
				CodegenUtils.newLine(sb);
			}
		}
		//IF order by条件
		OutputUtilities.javaIndent(sb, 2);
		sb.append("<if test=\"sort != null and @" + mybatisUtilsClass + "@isNotEmpty(sort.orders)\">");
		CodegenUtils.newLine(sb);
		
		OutputUtilities.javaIndent(sb, 1);
		sb.append("     ORDER BY ");
		
		sb.append("<foreach collection=\"sort.orders\" index=\"index\" item=\"item\" open=\"\" separator=\",\" close=\"\">");
		sb.append("${item.property} ${item.direction}");
		sb.append("</foreach>");
		
		CodegenUtils.newLine(sb);
		
		OutputUtilities.javaIndent(sb, 2);
		sb.append("</if>");
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	/**
	 * 创建<select id="selectModelPageCountByExample"/>
	 * @param introspectedTable
	 * @param statementId
	 * @return
	 */
	protected XmlElement createSelectModelPageCountByExampleElement(IntrospectedTable introspectedTable) {
		Map<String,Set<QueryConditionOperator>> exampleQueryWhereColumnMap =  getExampleQueryWhereColumnMap(introspectedTable);
		
		XmlElement element = new XmlElement("select");
		element.addAttribute(new Attribute("id", "selectModelPageCountByExample"));
		element.addAttribute(new Attribute("parameterType", "java.util.Map"));
		element.addAttribute(new Attribute("statementType", "PREPARED"));
		element.addAttribute(new Attribute("resultType", "java.lang.Integer"));
		
		StringBuilder sb = new StringBuilder();
		//OutputUtilities.javaIndent(sb, 1);
		sb.append("SELECT count(*)");
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		IntrospectedColumn column;
		CodegenUtils.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append("  FROM " + introspectedTable.getTableConfiguration().getTableName().toLowerCase() + " " + tableAliasName);
		CodegenUtils.newLine(sb);
		OutputUtilities.javaIndent(sb, 2);
		sb.append(" WHERE 1=1");
		//IF where条件
		boolean allocatedWhereCondition = !exampleQueryWhereColumnMap.isEmpty();
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			Set<QueryConditionOperator> columnOps = exampleQueryWhereColumnMap.get(column.getActualColumnName().toLowerCase());
			if(allocatedWhereCondition && CollectionUtils.isEmpty(columnOps)) { //如果指定了where条件列
				continue;
			}
			for(QueryConditionOperator columnOp : columnOps) {
				String javaPropertyName = getJavaPropertyNameOfOp(column, columnOp);
				CodegenUtils.newLine(sb);
				OutputUtilities.javaIndent(sb, 2);
				sb.append("<if test=\"example != null and @" + mybatisUtilsClass + "@isNotEmpty(example." + javaPropertyName + ")\">");
				CodegenUtils.newLine(sb);
				if(isRangeWhereCondition(columnOp)) {
					OutputUtilities.javaIndent(sb, 2);
					sb.append("<![CDATA[");
					CodegenUtils.newLine(sb);
				}
				OutputUtilities.javaIndent(sb, 1);
				sb.append("       AND ");
				
				applyExampleWhereCondition(sb, columnOp, column, "example.", javaPropertyName);
				CodegenUtils.newLine(sb);
				if(isRangeWhereCondition(columnOp)) {
					OutputUtilities.javaIndent(sb, 2);
					sb.append("]]>");
					CodegenUtils.newLine(sb);
				}
				OutputUtilities.javaIndent(sb, 2);
				sb.append("</if>");
			}
		}
		element.addElement(new TextElement(sb.toString()));
		return element;
	}
	
	/**
	 * 应用where条件到SQL语句
	 * @param sql					- sql语句
	 * @param op					- where条件类型
	 * @param column				- where条件所应用的数据库列
	 * @param paramDomain 			- 参数域例如：example.
	 * @param javaPropertyName		- 实体java字段名
	 */
	protected void applyExampleWhereCondition(StringBuilder sql, QueryConditionOperator op, IntrospectedColumn column, String paramDomain, String javaPropertyName) {
		if(QueryConditionOperator.LIKE.equals(op)) {
			if(Type.ORACLE.equals(dialect)) { //Oracle数据库 like写法
				sql.append(tableAliasName + "." + column.getActualColumnName().toLowerCase() + " " + op.getOpExpression() + " '%' || " + "#{" + paramDomain + javaPropertyName + ", jdbcType=" + getJdbcTypeName(column) + "} || '%'");
			} else { //默认为MySQL写法
				sql.append(tableAliasName + "." + column.getActualColumnName().toLowerCase() + " " + op.getOpExpression() + " CONCAT('%', " + "#{" + paramDomain + javaPropertyName + ", jdbcType=" + getJdbcTypeName(column) + "}, '%')");
			}
		} else if (isRangeWhereCondition(op)) {
			if(Types.TIMESTAMP == column.getJdbcType() || Types.DATE == column.getJdbcType()) {
				if(Types.TIMESTAMP == column.getJdbcType()) { //处理日期时间字段
					if(Type.ORACLE.equals(dialect)) { //Oracle 日期时间字段查询写法
						sql.append(tableAliasName + "." + column.getActualColumnName().toLowerCase() + " " + op.getOpExpression() + " " + "to_date(#{" + paramDomain + javaPropertyName + ", jdbcType=" + getJdbcTypeName(column) + "}, 'yyyy-mm-dd hh24:mi:ss')");
					} else { //默认为MySQL日期时间字段查询写法
						sql.append(tableAliasName + "." + column.getActualColumnName().toLowerCase() + " " + op.getOpExpression() + " " + "#{" + paramDomain + javaPropertyName + ", jdbcType=" + getJdbcTypeName(column) + "}");
					}
				} else if (Types.DATE == column.getJdbcType()) { //处理日期字段
					if(Type.ORACLE.equals(dialect)) { //Oracle 日期字段查询写法
						sql.append(tableAliasName + "." + column.getActualColumnName().toLowerCase() + " " + op.getOpExpression() + " " + "to_date(#{" + paramDomain + javaPropertyName + ", jdbcType=" + getJdbcTypeName(column) + "}, 'yyyy-mm-dd')");
					} else { //默认为MySQL日期字段查询写法
						sql.append(tableAliasName + "." + column.getActualColumnName().toLowerCase() + " " + op.getOpExpression() + " " + "#{" + paramDomain + javaPropertyName + ", jdbcType=" + getJdbcTypeName(column) + "}");
					}
				}
			} else { //其余数据类型处理
				sql.append(tableAliasName + "." + column.getActualColumnName().toLowerCase() + " " + op.getOpExpression() + " " + "#{" + paramDomain + javaPropertyName + ", jdbcType=" + getJdbcTypeName(column) + "}");
			}
		} else { //默认为equals查询
			sql.append(tableAliasName + "." + column.getActualColumnName().toLowerCase() + " " + op.getOpExpression() + " " + "#{" + paramDomain + javaPropertyName + ", jdbcType=" + getJdbcTypeName(column) + "}");
		}
	}
	
	/**
	 * 创建自动生成代码结束注释
	 * @return
	 */
	protected TextElement createAutoGenCodeCommentEndElement() {
		StringBuilder sb = new StringBuilder();
		//OutputUtilities.xmlIndent(sb, 1);
		sb.append("<!-- Auto-Generation Code End -->");
		CodegenUtils.newLine(sb);
		return new TextElement(sb.toString());
	}
	
	/**
	 * 创建自定义代码开始注释
	 * @return
	 */
	protected TextElement createCustomizedCodeCommentStartElement() {
		StringBuilder sb = new StringBuilder();
		//OutputUtilities.xmlIndent(sb, 1);
		sb.append("<!-- Customized Code Start -->");
		CodegenUtils.newLine(sb);
		return new TextElement(sb.toString());
	}
	
	/**
	 * 创建自定义代码结束注释
	 * @return
	 */
	protected TextElement createCustomizedCodeCommentEndElement() {
		StringBuilder sb = new StringBuilder();
		//OutputUtilities.xmlIndent(sb, 1);
		sb.append("<!-- Customized Code End -->");
		CodegenUtils.newLine(sb);
		return new TextElement(sb.toString());
	}
	
	/**
	 * 获取按Example查询的where条件
	 * @param introspectedTable
	 * @return
	 */
	protected Map<String,Set<QueryConditionOperator>> getExampleQueryWhereColumnMap(IntrospectedTable introspectedTable) {
		Map<String,Set<QueryConditionOperator>> exampleQueryWhereColumnMap = new LinkedHashMap<>();
		String exampleQueryConditions = introspectedTable.getTableConfiguration().getProperty("exampleQueryConditions");
		if(!StringUtils.isEmpty(exampleQueryConditions)) {
			Map<String,String> exampleQueryWhereColumnMap1 = JsonUtils.json2Object(exampleQueryConditions, new TypeReference<Map<String,String>>(){});
			if(exampleQueryWhereColumnMap1 != null) {
				String value;
				for(Map.Entry<String,String> entry : exampleQueryWhereColumnMap1.entrySet()) {
					value = entry.getValue();
					if(!StringUtils.isEmpty(value)) {
						value = value.toLowerCase();
						String[] opa = StringUtils.strip(value.trim(), ",").split(",");
						Set<QueryConditionOperator> ops = Stream.of(opa).map(QueryConditionOperator::getOperator).filter(Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new));
						if(!CollectionUtils.isEmpty(ops)) {
							exampleQueryWhereColumnMap.put(entry.getKey().toLowerCase(), ops);
						}
					}
				}
			}
		}
		return exampleQueryWhereColumnMap;
	}
	
	/**
	 * 创建基础列SQL，例如 <sql id="xxxBaseColumnList"/>
	 * @param introspectedTable
	 * @return
	 */
	@SuppressWarnings("unused")
	protected XmlElement createBaseColumnListElement(IntrospectedTable introspectedTable) {
		XmlElement sqlElement = new XmlElement("sql");
		String domainName = introspectedTable.getTableConfiguration().getDomainObjectName();
		domainName = Character.toLowerCase(domainName.charAt(0)) + domainName.substring(1);
		String id = domainName + "BaseColumnList";
		sqlElement.addAttribute(new Attribute("id", id));
		
		List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
		StringBuilder sb = new StringBuilder();
		IntrospectedColumn column;
		for(int i = 0, len = allColumns.size(); i < len; i++) {
			column = allColumns.get(i);
			if(i == 0) {
				OutputUtilities.javaIndent(sb, 1);
			} else {
				OutputUtilities.javaIndent(sb, 2);
			}
			sb.append(getColumnSelectStatement(column) + "\t" + column.getJavaProperty());
			if(i != len - 1) {
				sb.append(",");
				CodegenUtils.newLine(sb);
			}
		}
		
		sqlElement.addElement(new TextElement(sb.toString()));
		return sqlElement;
	}
	
	/**
	 * 获取某数据库列的select查询语句表示形式，例如a.user_name、DATE_FORMAT(a.create_time, '%Y-%m-%d %T')等
	 * @param column
	 * @return
	 */
	protected String getColumnSelectStatement(IntrospectedColumn column) {
		String columnSelects = tableAliasName + "." + column.getActualColumnName().toLowerCase();
		if(Type.ORACLE.equals(dialect)) { //Oracle 日期时间格式化写法
			if(Types.TIMESTAMP == column.getJdbcType()) {
				return "TO_CHAR(" + columnSelects + ", 'yyyy-mm-dd hh24:mi:ss')";
			} else if (Types.DATE == column.getJdbcType()) {
				return "TO_CHAR(" + columnSelects + ", 'yyyy-mm-dd')";
			}
		} else { //默认为MySQL写法
			if(Types.TIMESTAMP == column.getJdbcType()) {
				return "DATE_FORMAT(" + columnSelects + ", '%Y-%m-%d %T')";
			} else if (Types.DATE == column.getJdbcType()) {
				return "DATE_FORMAT(" + columnSelects + ", '%Y-%m-%d')";
			}
		}
		return columnSelects;
	}
	
	/**
	 * 获取数据库列的JdbcType名称，例如VARCHAR, BIGINT等
	 * @param column
	 * @return
	 */
	protected String getJdbcTypeName(IntrospectedColumn column) {
		String jdbcTypeName = column.getJdbcTypeName();
		if(String.class.getName().equals(column.getFullyQualifiedJavaType().getFullyQualifiedNameWithoutTypeParameters()) 
				&& (Types.DATE == column.getJdbcType() || Types.TIMESTAMP == column.getJdbcType())) {
			jdbcTypeName = "VARCHAR";
		}
		return jdbcTypeName;
	}
	
	/**
	 * 是否是区间where条件?
	 * @param op
	 * @return
	 */
	protected boolean isRangeWhereCondition(QueryConditionOperator op) {
		return QueryConditionOperator.GT.equals(op) || QueryConditionOperator.LT.equals(op) || QueryConditionOperator.GTE.equals(op) || QueryConditionOperator.LTE.equals(op);
	}
	
	/**
	 * 是否是>或>=区间where条件?
	 * @param op
	 * @return
	 */
	protected boolean isGTRangeWhereCondition(QueryConditionOperator op) {
		return QueryConditionOperator.GT.equals(op) || QueryConditionOperator.GTE.equals(op);
	}
	
	/**
	 * 是否是<或<=区间where条件?
	 * @param op
	 * @return
	 */
	protected boolean isLTRangeWhereCondition(QueryConditionOperator op) {
		return QueryConditionOperator.LT.equals(op) || QueryConditionOperator.LTE.equals(op);
	}
	
	/**
	 * 获取where条件下的Java属性名称
	 * @param column
	 * @param op
	 * @return
	 */
	protected String getJavaPropertyNameOfOp(IntrospectedColumn column, QueryConditionOperator op) {
		String javaPropertyName = column.getJavaProperty();
		if(isRangeWhereCondition(op)) {
			if(isGTRangeWhereCondition(op)) {
				javaPropertyName = getAdditionalPropertyName(CodegenConstants.RANGE_MIN_JAVA_PROPERTY_PREFIX, javaPropertyName);
			} else if (isLTRangeWhereCondition(op)) {
				javaPropertyName = getAdditionalPropertyName(CodegenConstants.RANGE_MAX_JAVA_PROPERTY_PREFIX, javaPropertyName);
			}
		}
		return javaPropertyName;
	}
	
	protected String getAdditionalPropertyName(String prefix, String javaPropertyName) {
		return prefix + Character.toUpperCase(javaPropertyName.charAt(0)) + javaPropertyName.substring(1);
	}
	
	public String getTableAliasName() {
		return tableAliasName;
	}

	public void setTableAliasName(String tableAliasName) {
		this.tableAliasName = tableAliasName;
	}

	public Type getDialect() {
		return dialect;
	}

	public void setDialect(Type dialect) {
		this.dialect = dialect;
	}

	public String getDefaultSerializableClass() {
		return defaultSerializableClass;
	}

	public void setDefaultSerializableClass(String defaultSerializableClass) {
		this.defaultSerializableClass = defaultSerializableClass;
	}

	public String getModelSerializableClass() {
		return modelSerializableClass;
	}

	public void setModelSerializableClass(String modelSerializableClass) {
		this.modelSerializableClass = modelSerializableClass;
	}

	public Set<String> getBaseMapperClasses() {
		return baseMapperClasses;
	}

	public void setBaseMapperClasses(Set<String> baseMapperClasses) {
		this.baseMapperClasses = baseMapperClasses;
	}

	public String getCompositePrimaryKeyClass() {
		return compositePrimaryKeyClass;
	}

	public void setCompositePrimaryKeyClass(String compositePrimaryKeyClass) {
		this.compositePrimaryKeyClass = compositePrimaryKeyClass;
	}

	public String getMybatisUtilsClass() {
		return mybatisUtilsClass;
	}

	public void setMybatisUtilsClass(String mybatisUtilsClass) {
		this.mybatisUtilsClass = mybatisUtilsClass;
	}

	public Boolean getMergeableXmlMapper() {
		return mergeableXmlMapper;
	}

	public void setMergeableXmlMapper(Boolean mergeableXmlMapper) {
		this.mergeableXmlMapper = mergeableXmlMapper;
	}

	@Override
	public boolean clientDeleteByPrimaryKeyMethodGenerated(org.mybatis.generator.api.dom.java.Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientInsertMethodGenerated(org.mybatis.generator.api.dom.java.Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientSelectByPrimaryKeyMethodGenerated(org.mybatis.generator.api.dom.java.Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(org.mybatis.generator.api.dom.java.Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(org.mybatis.generator.api.dom.java.Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(org.mybatis.generator.api.dom.java.Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientInsertSelectiveMethodGenerated(org.mybatis.generator.api.dom.java.Method method,
			Interface interfaze, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean clientSelectAllMethodGenerated(org.mybatis.generator.api.dom.java.Method method, Interface interfaze,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapResultMapWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element,
			IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapBaseColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapBlobColumnListElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}

	@Override
	public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		return false;
	}
	
}

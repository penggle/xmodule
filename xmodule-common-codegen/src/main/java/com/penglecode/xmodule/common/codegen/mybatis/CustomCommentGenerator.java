package com.penglecode.xmodule.common.codegen.mybatis;

import java.util.Properties;
import java.util.Set;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;

import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.StringUtils;


/**
 * 自定义的注释生成类
 * 
 * @author 	pengpeng
 * @date	2018年2月9日 下午2:10:20
 */
public class CustomCommentGenerator implements CommentGenerator {

	@Override
	public void addConfigurationProperties(Properties properties) {
		
	}

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		field.addJavaDocLine("/** " + StringUtils.defaultIfEmpty(introspectedColumn.getRemarks(), introspectedColumn.getActualColumnName().toLowerCase()) + " */");
	}
	
	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
		
	}

	@Override
	public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		topLevelClass.addJavaDocLine("/**");
		
		String modelComments = introspectedTable.getRemarks();
		if(StringUtils.isEmpty(modelComments)) {
			String domainObjectName = introspectedTable.getTableConfiguration().getDomainObjectName();
			modelComments = StringUtils.defaultIfEmpty(introspectedTable.getTableConfiguration().getProperty("modelName"), domainObjectName) 
							+ " (" + introspectedTable.getTableConfiguration().getTableName() + ")" + " 实体类";
		}
		
		topLevelClass.addJavaDocLine(" * " + modelComments);
		topLevelClass.addJavaDocLine(" * ");
		topLevelClass.addJavaDocLine(" * @author " + StringUtils.defaultIfEmpty(introspectedTable.getTableConfiguration().getProperty("modelCommentAuthor"), "Administrator"));
		topLevelClass.addJavaDocLine(" * @date	" + DateTimeUtils.formatNow("yyyy'年'MM'月'dd'日' a HH:mm:ss") + "");
		topLevelClass.addJavaDocLine(" */");
	}

	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
		
	}

	@Override
	public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
		
	}

	@Override
	public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
		
	}

	@Override
	public void addGetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		
	}

	@Override
	public void addSetterComment(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn) {
		
	}

	@Override
	public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
		
	}

	@Override
	public void addJavaFileComment(CompilationUnit compilationUnit) {
		
	}

	@Override
	public void addComment(XmlElement xmlElement) {
		
	}

	@Override
	public void addRootComment(XmlElement rootElement) {
		
	}

	@Override
	public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable,
			Set<FullyQualifiedJavaType> imports) {
		
	}

	@Override
	public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
		
	}

	@Override
	public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable,
			Set<FullyQualifiedJavaType> imports) {
		
	}

	@Override
	public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable,
			IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
		
	}

	@Override
	public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable,
			Set<FullyQualifiedJavaType> imports) {
		
	}

}

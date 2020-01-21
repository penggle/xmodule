package com.penglecode.xmodule.common.codegen.mybatis;

import java.sql.Types;
import java.util.Properties;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

/**
 * 自定义的JavaTypeResolver
 * 
 * @author 	pengpeng
 * @date	2018年2月9日 下午2:22:57
 */
public class CustomJavaTypeResolver extends JavaTypeResolverDefaultImpl {

	protected boolean forceDateTimeStampAsString = false;
	
	protected boolean forceTinyintAsInteger = false;
	
	protected boolean forceNumber1AsBoolean = false;
	
	protected boolean forceDecimalNumericAsDouble = false;
	
	public CustomJavaTypeResolver() {
		super();
	}
	
	@Override
	public void addConfigurationProperties(Properties properties) {
		super.addConfigurationProperties(properties);
		this.forceDateTimeStampAsString = Boolean.valueOf(properties.getProperty("forceDateTimeStampAsString", "false"));
		this.forceTinyintAsInteger = Boolean.valueOf(properties.getProperty("forceTinyintAsInteger", "false"));
		this.forceNumber1AsBoolean = Boolean.valueOf(properties.getProperty("forceNumber1AsBoolean", "false"));
		this.forceDecimalNumericAsDouble = Boolean.valueOf(properties.getProperty("forceDecimalNumericAsDouble", "false"));
		overrideTypeMap();
	}

	@Override
	public String calculateJdbcTypeName(IntrospectedColumn introspectedColumn) {
		overrideJdbcType(introspectedColumn);
		return super.calculateJdbcTypeName(introspectedColumn);
	}
	
	protected void overrideJdbcType(IntrospectedColumn column) {
		FullyQualifiedJavaType javaType = column.getFullyQualifiedJavaType();
		if (Boolean.class.getName().equals(javaType.getFullyQualifiedNameWithoutTypeParameters())) {
			column.setJdbcType(Types.BOOLEAN);
		}
	}
	
	protected void overrideTypeMap() {
		if(forceDateTimeStampAsString) {
			typeMap.put(Types.DATE, new JdbcTypeInformation("DATE", //$NON-NLS-1$
	                new FullyQualifiedJavaType(String.class.getName())));
			typeMap.put(Types.TIME, new JdbcTypeInformation("TIME", //$NON-NLS-1$
	                new FullyQualifiedJavaType(String.class.getName())));
			typeMap.put(Types.TIMESTAMP, new JdbcTypeInformation("TIMESTAMP", //$NON-NLS-1$
	                new FullyQualifiedJavaType(String.class.getName())));
		}
		if(forceTinyintAsInteger) {
			typeMap.put(Types.TINYINT, new JdbcTypeInformation("TINYINT", //$NON-NLS-1$
	                new FullyQualifiedJavaType(Integer.class.getName())));
		}
		if(forceDecimalNumericAsDouble) {
			typeMap.put(Types.NUMERIC, new JdbcTypeInformation("NUMERIC", //$NON-NLS-1$
	                new FullyQualifiedJavaType(Double.class.getName())));
			typeMap.put(Types.DECIMAL, new JdbcTypeInformation("DECIMAL", //$NON-NLS-1$
	                new FullyQualifiedJavaType(Double.class.getName())));
		}
	}

	protected FullyQualifiedJavaType overrideDefaultType(IntrospectedColumn column, FullyQualifiedJavaType defaultType) {
        FullyQualifiedJavaType answer = defaultType;

        switch (column.getJdbcType()) {
        case Types.BIT:
            answer = calculateBitReplacement(column, defaultType);
            break;
        case Types.TINYINT:
        case Types.DOUBLE:
        case Types.INTEGER:
            answer = calculateNumberReplacement(column, defaultType);
            break;
        case Types.DECIMAL:
        case Types.NUMERIC:
            answer = calculateBigDecimalReplacement(column, defaultType);
            break;
        default:
            break;
        }
        
        return answer;
    }
	
	protected FullyQualifiedJavaType calculateNumberReplacement(IntrospectedColumn column, FullyQualifiedJavaType defaultType) {
		FullyQualifiedJavaType answer;
        if (column.getLength() == 1 && forceNumber1AsBoolean) {
            answer = new FullyQualifiedJavaType(Boolean.class.getName());
        } else {
            answer = defaultType;
        }
        return answer;
	}
	
	protected FullyQualifiedJavaType calculateBigDecimalReplacement(IntrospectedColumn column, FullyQualifiedJavaType defaultType) {
        FullyQualifiedJavaType answer;
        if (forceDecimalNumericAsDouble) {
        	answer = defaultType;
        } else {
        	answer = super.calculateBigDecimalReplacement(column, defaultType);
        }
        return answer;
    }
	
}

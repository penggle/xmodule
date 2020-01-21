package com.penglecode.xmodule.common.mybatis;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
/**
 * Mybatis分页插件-在执行sql前将原始sql转换为分页sql
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月28日 下午3:49:03
 * @version  	1.0
 */
@Intercepts({@Signature(type=StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PaginationInterceptor implements Interceptor {

	private Dialect dialect;
	
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
		RowBounds rowBounds = (RowBounds)metaStatementHandler.getValue("delegate.rowBounds");
		if(rowBounds == null || rowBounds == RowBounds.DEFAULT){
			return invocation.proceed();
		}
		if(dialect == null){
			Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
			Dialect.Type databaseType = null;
			String d = configuration.getVariables().getProperty("dialect");
			if(d == null || d.trim().equals("")){
				throw new IllegalStateException("No property named with 'dialect' defined in mybatis configuration xml file.");
			}
			try {
				databaseType = Dialect.Type.valueOf(d);
			} catch (Exception e) {
				throw new IllegalStateException(String.format("No such dialect enum defined in class %s.", Dialect.Type.class));
			}
			
			switch (databaseType) {
			  	case MYSQL: // MySQL分页
			  		dialect = new MySQLDialect();
			  		break;
			  	case ORACLE: // Oracle分页
			  		dialect = new OracleDialect();
			  		break;
			}
			if(dialect == null){
				throw new IllegalStateException(String.format("No %s dialect found!", databaseType));
			}
		}
		String originalSql = metaStatementHandler.getValue("delegate.boundSql.sql").toString();
		metaStatementHandler.setValue("delegate.boundSql.sql", dialect.getLimitSql(originalSql, rowBounds.getOffset(), rowBounds.getLimit()));
		metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
        metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
        return invocation.proceed(); 
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {}

}

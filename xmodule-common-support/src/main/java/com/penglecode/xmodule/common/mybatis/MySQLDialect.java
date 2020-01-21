package com.penglecode.xmodule.common.mybatis;
/**
 * 数据库分页之mysql
 * 
 * @author	  	pengpeng
 * @date	  	2014年8月1日 下午3:53:41
 * @version  	1.0
 */
public class MySQLDialect implements Dialect {

	public String getLimitSql(String sql, int offset, int limit) {
		sql = sql + " limit " + offset + ", " + limit;
		return sql;
	}

}

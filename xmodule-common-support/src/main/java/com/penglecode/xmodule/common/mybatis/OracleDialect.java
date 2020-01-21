package com.penglecode.xmodule.common.mybatis;
/**
 * oracle数据库分页实现类
 * 
 * @author	  	pengpeng
 * @date	  	2014年8月1日 下午3:53:50
 * @version  	1.0
 */
public class OracleDialect implements Dialect {

	public static final String DEFAULT_ROWNUM_COLUMN_NAME = "rn_";
	
	private static final String DEFAULT_PAGING_SQL_FORMAT = "select *"
														   + " from (select rownum rn_, page_inner_tab.*"
														           + " from (%s) page_inner_tab"
														          + " where rownum <= %s) page_outer_tab"
														  + " where page_outer_tab.rn_ > %s";
	
	public String getLimitSql(String sql, int offset, int limit) {
		return String.format(DEFAULT_PAGING_SQL_FORMAT, sql, offset + limit, offset);
	}

}

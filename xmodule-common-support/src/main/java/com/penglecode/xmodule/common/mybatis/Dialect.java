package com.penglecode.xmodule.common.mybatis;
/**
 * 数据库方言
 * (其中声明了各种数据库的分页语句实现)
 * 
 * @author	  	pengpeng
 * @date	  	2014年8月1日 下午3:54:33
 * @version  	1.0
 */
public interface Dialect {

	/**
	 * 根据原始查询sql语句及分页参数获取分页sql,
	 * (注意：如果SQL语句中使用了left join、right join查询一对多的结果集时,请不要使用该分页处理机制,得另寻他法)
	 * @param sql
	 * @param offset	- 起始记录行数(从0开始)
	 * @param limit		- 从起始记录行数offset开始获取limit行记录
	 * @return
	 */
	public String getLimitSql(String sql, int offset, int limit);
	
	public enum Type {
		MYSQL, ORACLE;
		
		public static Type getType(String name) {
			for(Type em : values()) {
				if(em.name().equals(name)) {
					return em;
				}
			}
			return null;
		}
	}
	
}

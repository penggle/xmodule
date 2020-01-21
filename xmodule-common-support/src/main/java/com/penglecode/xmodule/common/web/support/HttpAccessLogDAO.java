package com.penglecode.xmodule.common.web.support;

/**
 * 日志DAO,如将日志写入数据库、日志文件等等
 * 
 * @param <T>
 * @author	  	pengpeng
 * @date	  	2014年10月17日 下午7:24:08
 * @version  	1.0
 */
public interface HttpAccessLogDAO {

	/**
	 * 获取日志的记录方式
	 * @return
	 */
	public HttpAccessLogging.LoggingMode getLoggingMode();
	
	/**
	 * 日志处理方法,如将日志写入数据库、日志文件等等
	 * @param httpAccessLog
	 * @throws Exception
	 */
	public void saveLog(HttpAccessLog<?> httpAccessLog) throws Exception;

}

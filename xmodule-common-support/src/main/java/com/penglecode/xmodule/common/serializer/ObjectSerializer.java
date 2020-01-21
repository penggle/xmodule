package com.penglecode.xmodule.common.serializer;
/**
 * 统一对象序列化接口
 * @author	  	pengpeng
 * @date	  	2014年10月14日 下午1:27:27
 * @version  	1.0
 */
public interface ObjectSerializer {

	public <T> byte[] serialize(T target);
	
	public <T> T deserialize(byte[] bytes);
	
}

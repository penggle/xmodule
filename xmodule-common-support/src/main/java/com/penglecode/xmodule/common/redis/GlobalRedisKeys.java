package com.penglecode.xmodule.common.redis;

import com.penglecode.xmodule.common.consts.Constant;
import com.penglecode.xmodule.common.consts.SpringEnvConstant;
import com.penglecode.xmodule.common.util.StringUtils;

/**
 * 全局Redis的key设置
 * 
 * @author 	pengpeng
 * @date	2019年12月27日 上午10:58:51
 */
public final class GlobalRedisKeys {

	private static final Constant<String> GLOBAL_KEY_PREFIX = new SpringEnvConstant<String>("spring.redis.common.global-key-prefix", "") {};
	
	/**
	 * 给业务key添加全局prefix
	 * @param bizKey		- 业务key
	 * @return 完整的Redis key
	 */
	public static String ofKey(String bizKey) {
		return GLOBAL_KEY_PREFIX.get() + bizKey;
	}
	
	/**
	 * 给业务key添加全局prefix
	 * @param bizPrefix		- 业务prefix
	 * @param bizKey		- 业务key
	 * @return 完整的Redis key
	 */
	public static String ofKey(String bizPrefix, String bizKey) {
		return GLOBAL_KEY_PREFIX.get() + bizPrefix + bizKey;
	}
	
	/**
	 * 通过完整的Redis key获取业务key
	 * @param fullKey		- 完整的Redis key
	 * @return 业务key
	 */
	public static String fromKey(String fullKey) {
		return StringUtils.stripStart(fullKey, GLOBAL_KEY_PREFIX.get());
	}
	
	/**
	 * 通过完整的Redis key获取业务key
	 * @param fullKey		- 完整的Redis key
	 * @param bizPrefix		- 业务prefix
	 * @return 业务key
	 */
	public static String fromKey(String fullKey, String bizPrefix) {
		return StringUtils.stripStart(fullKey, GLOBAL_KEY_PREFIX.get() + bizPrefix);
	}
	
}

package com.penglecode.xmodule.common.util;

import java.util.Random;

/**
 * 随机工具类
 * 
 * @author  pengpeng
 * @date 	 2015年8月4日 下午10:26:01
 * @version 1.0
 */
public class RandomCodeUtils {

	private static final char[] CODES = new char[]{
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
		'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
		'W', 'X', 'Y', 'Z'
	};
	
	/**
	 * 生成{length}位随机数字短信验证码
	 * @param length
	 * @return
	 */
	public static String randomNumberCode(int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++){
			sb.append(CODES[random.nextInt(10)]);
		}
		return sb.toString();
	}
	
	/**
	 * 生成{length}位随机数字、字母短信验证码
	 * @param length
	 * @return
	 */
	public static String randomCode(int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < length; i++){
			sb.append(CODES[random.nextInt(CODES.length)]);
		}
		return sb.toString();
	}
	
}

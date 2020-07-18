package com.penglecode.xmodule.common.util;

import java.util.regex.Pattern;

/**
 * 常用认证：如手机号码、邮箱等
 * 
 * @author  pengpeng
 * @date 	 2015年8月4日 下午1:19:31
 * @version 1.0
 */
public class CommonValidateUtils {

	public static final String REGEX_MOBILE_PHONE = "1[3-9][0-9]{1}[0-9]{8}";
	
	public static final String REGEX_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	
	public static final String REGEX_IDCARD = "(\\d{15})|(\\d{17}(\\d|X|x))";
	
	public static final Pattern PATTERN_MOBILE_PHONE = Pattern.compile(REGEX_MOBILE_PHONE);
	
	public static final Pattern PATTERN_EMAIL = Pattern.compile(REGEX_EMAIL);
	
	public static final Pattern PATTERN_IDCARD = Pattern.compile(REGEX_IDCARD);
	
	/**
	 * 判断手机号码是否合法
	 * @param mobilePhone
	 * @return
	 */
	public static boolean isMobilePhone(String mobilePhone) {
		if(StringUtils.isEmpty(mobilePhone)){
			return false;
		}
		return PATTERN_MOBILE_PHONE.matcher(mobilePhone).matches();
	}
	
	/**
     * 检测邮箱地址是否合法
     * @param email
     * @return true合法 false不合法
     */
	public static boolean isEmail(String email) {
		if(StringUtils.isEmpty(email)){
			return false;
		}
		// Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
		return PATTERN_EMAIL.matcher(email).matches();
	}
	
	/**
	 * 检测身份证号码是否有效
	 * @param idcard
	 * @return
	 */
	public static boolean isIdcard(String idcard) {
		if(StringUtils.isEmpty(idcard)) {
			return false;
		}
		return PATTERN_IDCARD.matcher(idcard).matches();
	}
	
	public static void main(String[] args) {
		String idcard = "342425198607284712";
		System.out.println(idcard.substring(idcard.length() - 6));
	}
	
}

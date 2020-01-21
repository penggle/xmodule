package com.penglecode.xmodule.common.util;

import java.io.UnsupportedEncodingException;

/**
 * 基本常用类型与字节数组的相互转换工具
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月19日 下午3:26:04
 * @version  	1.0
 */
public class ByteArrayUtils {
	
	/**
	 * byte型1个字节
	 */
	public static final int PRIMITIVE_TYPE_BYTE_LENGTH_BYTE = 1;
	
	/**
	 * short型2个字节
	 */
	public static final int PRIMITIVE_TYPE_BYTE_LENGTH_SHORT = 2;
	
	/**
	 * int型4个字节
	 */
	public static final int PRIMITIVE_TYPE_BYTE_LENGTH_INT = 4;
	
	/**
	 * long型8个字节
	 */
	public static final int PRIMITIVE_TYPE_BYTE_LENGTH_LONG = 8;
	
	/**
	 * float型4个字节
	 */
	public static final int PRIMITIVE_TYPE_BYTE_LENGTH_FLOAT = 4;
	
	/**
	 * byte型数据转byte[]
	 * @param number
	 * @return
	 */
	public static byte[] byteToByteArray(byte number){
		return new byte[]{number};
	}
	
	/**
	 * <p>byte[]型数据转byte</p>
	 * 
	 * @param bytes
	 * @return
	 */
	public static Byte byteArrayToByte(byte[] bytes){
		return bytes[0];
	}
	
	/**
	 * <p>short型数据转byte[]</p>
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] shortToByteArray(short number){
		int byteLength = PRIMITIVE_TYPE_BYTE_LENGTH_SHORT;
		byte[] bytes = new byte[byteLength];
		for(int i = 0; i < byteLength; i++){
			bytes[byteLength - i - 1] = (byte)((number >> (8 * i)) & 0xff);
		}
		return bytes;
	}
	
	/**
	 * <p>byte[]型数据转short</p>
	 * 
	 * @param bytes
	 * @return
	 */
	public static Short byteArrayToShort(byte[] bytes){
		short number = 0;
		int byteLength = bytes.length;
		for(int i = 0; i < byteLength; i++){
			number |= ((short)(bytes[byteLength - i - 1] & 0xff) << (8 * i));
		}
		return number;
	}
	
	/**
	 * <p>int型数据转byte[]</p>
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] intToByteArray(int number){
		int byteLength = PRIMITIVE_TYPE_BYTE_LENGTH_INT;
		byte[] bytes = new byte[byteLength];
		for(int i = 0; i < byteLength; i++){
			bytes[byteLength - i - 1] = (byte)((number >> (8 * i)) & 0xff);
		}
		return bytes;
	}
	
	/**
	 * <p>byte[]型数据转int</p>
	 * 
	 * @param bytes
	 * @return
	 */
	public static Integer byteArrayToInt(byte[] bytes){
		int number = 0;
		int byteLength = bytes.length;
		for(int i = 0; i < byteLength; i++){
			number |= ((int)(bytes[byteLength - i - 1] & 0xff) << (8 * i));
		}
		return number;
	}
	
	/**
	 * <p>long型数据转byte[]</p>
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] longToByteArray(long number){
		int byteLength = PRIMITIVE_TYPE_BYTE_LENGTH_LONG;
		byte[] bytes = new byte[byteLength];
		for(int i = 0; i < byteLength; i++){
			bytes[byteLength - i - 1] = (byte)((number >> (8 * i)) & 0xff);
		}
		return bytes;
	}
	
	/**
	 * <p>byte[]型数据转long</p>
	 * 
	 * @param bytes
	 * @return
	 */
	public static Long byteArrayToLong(byte[] bytes){
		long number = 0l;
		int byteLength = bytes.length;
		for(int i = 0; i < byteLength; i++){
			number |= ((long)(bytes[byteLength - i - 1] & 0xff) << (8 * i));
		}
		return number;
	}
	
	/**
	 * <p>float型数据转byte[]</p>
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] floatToByteArray(float number){
		int bits = Float.floatToIntBits(number);
		return intToByteArray(bits);
	}
	
	/**
	 * <p>byte[]型数据转float</p>
	 * 
	 * @param bytes
	 * @return
	 */
	public static Float byteArrayToFloat(byte[] bytes){
		int bits = byteArrayToInt(bytes);
		return Float.intBitsToFloat(bits);
	}
	
	/**
	 * <p>double型数据转byte[]</p>
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] doubleToByteArray(double number){
		long bits = Double.doubleToLongBits(number);
		return longToByteArray(bits);
	}
	
	/**
	 * <p>byte[]型数据转double</p>
	 * 
	 * @param bytes
	 * @return
	 */
	public static Double byteArrayToDouble(byte[] bytes){
		long bits = byteArrayToLong(bytes);
		return Double.longBitsToDouble(bits);
	}
	
	/**
	 * <p>char型数据转byte[]</p>
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] charToByteArray(char chr){
		return shortToByteArray((short)chr);
	}
	
	/**
	 * <p>byte[]型数据转char</p>
	 * 
	 * @param bytes
	 * @return
	 */
	public static Character byteArrayToChar(byte[] bytes){
		short s = byteArrayToShort(bytes);
		return (char)s;
	}
	
	/**
	 * <p>String型数据转byte[]</p>
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] stringToByteArray(String str){
		return str.getBytes();
	}
	
	/**
	 * <p>byte[]型数据转String</p>
	 * 
	 * @param bytes
	 * @return
	 */
	public static String byteArrayToString(byte[] bytes){
		return new String(bytes);
	}
	
	/**
	 * <p>String型数据转byte[]</p>
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] stringToByteArray(String str, String charset){
		try {
			return str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * <p>byte[]型数据转String</p>
	 * @param bytes
	 * @return
	 */
	public static String byteArrayToString(byte[] bytes, String charset){
		try {
			return new String(bytes, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * <p>byte[]型数据转十六进制字符串</p>
	 * @param src
	 * @return
	 */
	public static String byteArrayToHexString(byte[] bytes) {
		StringBuilder stringBuilder = new StringBuilder("");
		int len = bytes.length;
		if (bytes == null || len <= 0) {
			return null;
		}
		for (int i = 0; i < len; i++) {
			int v = bytes[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	
	public static byte[] hexStringToByteArray(String hexString) {   
	    if (hexString == null || hexString.equals("")) {   
	        return null;   
	    }   
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length() / 2;   
	    char[] hexChars = hexString.toCharArray();   
	    byte[] d = new byte[length];   
	    for (int i = 0; i < length; i++) {   
	        int pos = i * 2;   
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
	    }   
	    return d;   
	}
	
	public static byte charToByte(char c) {   
	    return (byte) "0123456789ABCDEF".indexOf(c);   
	}
	
}

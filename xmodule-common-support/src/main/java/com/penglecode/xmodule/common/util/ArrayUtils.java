package com.penglecode.xmodule.common.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 有关数组的工具类
 * 
 * @author	  	pengpeng
 * @date	  	2014年7月19日 下午12:08:30
 * @version  	1.0
 */
public class ArrayUtils {

	/**
	 * <p>判断数据是否为空</p>
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(byte[] array){
		return array == null || array.length == 0;
	}
	
	/**
	 * <p>判断数据是否为空</p>
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(short[] array){
		return array == null || array.length == 0;
	}
	
	/**
	 * <p>判断数据是否为空</p>
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(int[] array){
		return array == null || array.length == 0;
	}
	
	/**
	 * <p>判断数据是否为空</p>
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(long[] array){
		return array == null || array.length == 0;
	}
	
	/**
	 * <p>判断数据是否为空</p>
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(float[] array){
		return array == null || array.length == 0;
	}
	
	/**
	 * <p>判断数据是否为空</p>
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(double[] array){
		return array == null || array.length == 0;
	}
	
	/**
	 * <p>判断数据是否为空</p>
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(char[] array){
		return array == null || array.length == 0;
	}
	
	/**
	 * <p>判断数据是否为空</p>
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(boolean[] array){
		return array == null || array.length == 0;
	}
	
	/**
	 * <p>判断数据是否为空</p>
	 * 
	 * @param array
	 * @return
	 */
	public static boolean isEmpty(Object[] array){
		return array == null || array.length == 0;
	}
	
	/**
	 * <p>将原始类型数组转换成对象类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static Byte[] toObjectArray(byte[] array){
		if(array == null){
			return null;
		}else{
			Byte[] result = new Byte[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>将原始类型数组转换成对象类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static Short[] toObjectArray(short[] array){
		if(array == null){
			return null;
		}else{
			Short[] result = new Short[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>将原始类型数组转换成对象类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static Integer[] toObjectArray(int[] array){
		if(array == null){
			return null;
		}else{
			Integer[] result = new Integer[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>将原始类型数组转换成对象类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static Long[] toObjectArray(long[] array){
		if(array == null){
			return null;
		}else{
			Long[] result = new Long[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>将原始类型数组转换成对象类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static Float[] toObjectArray(float[] array){
		if(array == null){
			return null;
		}else{
			Float[] result = new Float[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>将原始类型数组转换成对象类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static Double[] toObjectArray(double[] array){
		if(array == null){
			return null;
		}else{
			Double[] result = new Double[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>将原始类型数组转换成对象类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static Character[] toObjectArray(char[] array){
		if(array == null){
			return null;
		}else{
			Character[] result = new Character[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>将原始类型数组转换成对象类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static Boolean[] toObjectArray(boolean[] array){
		if(array == null){
			return null;
		}else{
			Boolean[] result = new Boolean[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>将对象类型数组转换成原始类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static byte[] toPrimitiveArray(Byte[] array){
		if(array == null){
			return null;
		}else{
			byte[] result = new byte[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>将对象类型数组转换成原始类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static short[] toPrimitiveArray(Short[] array){
		if(array == null){
			return null;
		}else{
			short[] result = new short[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>将对象类型数组转换成原始类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static int[] toPrimitiveArray(Integer[] array){
		if(array == null){
			return null;
		}else{
			int[] result = new int[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>将对象类型数组转换成原始类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static long[] toPrimitiveArray(Long[] array){
		if(array == null){
			return null;
		}else{
			long[] result = new long[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>将对象类型数组转换成原始类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static float[] toPrimitiveArray(Float[] array){
		if(array == null){
			return null;
		}else{
			float[] result = new float[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>将对象类型数组转换成原始类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static double[] toPrimitiveArray(Double[] array){
		if(array == null){
			return null;
		}else{
			double[] result = new double[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>将对象类型数组转换成原始类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static char[] toPrimitiveArray(Character[] array){
		if(array == null){
			return null;
		}else{
			char[] result = new char[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>将对象类型数组转换成原始类型数组</p>
	 * 
	 * @param array
	 * @return
	 */
	public static boolean[] toPrimitiveArray(Boolean[] array){
		if(array == null){
			return null;
		}else{
			boolean[] result = new boolean[array.length];
			for(int i = 0; i < array.length; i++){
				result[i] = array[i];
			}
			return result;
		}
	}
	
	/**
	 * <p>Null-safe 获取数组长度</p>
	 * 
	 * @param array
	 * @return
	 */
	public static int getLength(Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }
	
	/**
	 * <p>判断对象数组中是否含有null元素</p>
	 * 
	 * @param array
	 * @return
	 */
	public static boolean containsNull(Object[] array){
		if(!isEmpty(array)){
			for(Object obj : array){
				if(obj == null){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * <p>过滤对象数组中null元素</p>
	 * 
	 * @param array
	 * @return	返回新的数据
	 */
	public static Object[] filterNull(Object[] array){
		if(!isEmpty(array)){
			List<Object> list = new ArrayList<Object>(array.length);
			for(Object obj : array){
				if(obj != null){
					list.add(obj);
				}
			}
			return list.toArray();
		}
		return array;
	}
	
}

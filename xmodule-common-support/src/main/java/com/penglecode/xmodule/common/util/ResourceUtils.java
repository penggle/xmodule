package com.penglecode.xmodule.common.util;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.penglecode.xmodule.common.consts.ApplicationConstants;

/**
 * 使用注意：
 * 使用如下两个方法[getResources(), getResource()]都不会有问题,除非文件不存在!
 * 获取到了org.springframework.core.io.Resource对象后再调用Resource.getFile()时需要注意：
 * 如果文件在jar包中,那么是会报错的,但是Resource.getInputStream()却不报错,why?
 * 原因是,java.io.File对文件的定义是相对于操作系统的,文件在jar包中根本就超出了操作系统所能识别的范畴了，所以getFile()就会报FileNotFoundException
 * 
 * @author  pengpeng
 * @date 	 2015年12月5日 下午3:09:45
 * @version 1.0
 */
public class ResourceUtils extends org.springframework.util.ResourceUtils {

	public static ResourcePatternResolver getResourcePatternResolver() {
		return ApplicationConstants.DEFAULT_RESOURCE_PATTERN_RESOLVER.get();
	}
	
	public static Resource[] getResources(String locationPattern) {
		try {
			return getResourcePatternResolver().getResources(locationPattern);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Resource getResource(String locationPattern) {
		Resource[] resources = getResources(locationPattern);
		if(resources != null){
			return resources[0];
		}
		return null;
	}
	
}

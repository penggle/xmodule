package com.penglecode.xmodule.master4j.jvm.chapter2.oom;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 在JDK7及其以下版本中设置VM Args：-XX:PermSize=10M -XX:MaxPermSize=10M，产生异常：java.lang.OutOfMemoryError: PermGen space
 * 
 * 在JDK8及其以上版本中:
 * 		1、如果未设MaxMetaspaceSize，则在内存较大的情况下运行大半天都未必能出现：java.lang.OutOfMemoryError: Metaspace
 * 		2、如果设置-XX:MaxMetaspaceSize=10m -XX:MetaspaceSize=10m，则会立马出现：java.lang.OutOfMemoryError: Metaspace
 * 
 * 建议：
 * 		1、MetaspaceSize和MaxMetaspaceSize设置一样大。
 * 		2、具体设置多大，建议稳定运行一段时间后通过jstat -gc pid确认且这个值大一些，对于大部分项目256m即可。
 * 
 * @author 	pengpeng
 * @date 	2020年6月18日 下午7:01:34
 */
public class MetaspaceOutOfMemoryExample {

	public static void main(String[] args) {
		while (true) {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(OOMObject.class);
			enhancer.setUseCache(false);
			enhancer.setCallback(new MethodInterceptor() {
				public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
					return proxy.invokeSuper(obj, args);
				}
			});
			enhancer.create();
		}
	}

	static class OOMObject {
	}

}

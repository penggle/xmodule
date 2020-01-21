package org.springframework.cloud.openfeign;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.springframework.cglib.proxy.InvocationHandler;

/**
 * 默认熔断|降级统一拦截处理器
 * 
 * 如需自定义请继承重写doInvoke方法
 * 
 * @author 	pengpeng
 * @date	2019年6月1日 下午3:17:20
 */
public abstract class HystrixFallbackHandler implements InvocationHandler {

	private static final List<String> JAVA_OBJECT_METHODS = Arrays.asList("equals", "hashCode", "toString", "clone", "finalize", "getClass", "notify", "notifyAll", "wait");
	
	private final Class<?> feignClientClass;
	
	private final Throwable cause;
	
	public HystrixFallbackHandler(Class<?> feignClientClass, Throwable cause) {
		super();
		this.feignClientClass = feignClientClass;
		this.cause = cause;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String feignClientClassName = feignClientClass.getName();
		String methodName = method.getName();
		String methodExpression = method.toGenericString();
		Class<?>[] methodParams = method.getParameterTypes();
		if(JAVA_OBJECT_METHODS.contains(methodName)){
	    	if(methodName.equals("equals") && methodParams.length == 1){
		    	return false;
		    }else if(methodName.equals("hashCode") && methodParams.length == 0){
		    	return new Integer((feignClientClassName + "." + methodExpression).hashCode());
		    }else if(methodName.equals("toString") && methodParams.length == 0){
		    	return "HystrixFallbackProxy[" + feignClientClassName + "." + methodExpression + "]";
		    }else if(methodName.equals("clone") && methodParams.length == 0){
		    	return null;
		    }else if(methodName.equals("finalize") && methodParams.length == 0){
		    	throw new UnsupportedOperationException("Operation Not Supported!");
		    }else if(methodName.equals("getClass") && methodParams.length == 0){
		    	return feignClientClass;
		    }else if(methodName.equals("notify") && methodParams.length == 0){
		    	throw new UnsupportedOperationException("Operation Not Supported!");
		    }else if(methodName.equals("notifyAll") && methodParams.length == 0){
		    	throw new UnsupportedOperationException("Operation Not Supported!");
		    }else if(methodName.equals("wait")){
		    	throw new UnsupportedOperationException("Operation Not Supported!");
		    }
	    }
		return handle(proxy, method, args);
	}
	
	protected abstract Object handle(Object proxy, Method method, Object[] args) throws Throwable;

	public Class<?> getFeignClientClass() {
		return feignClientClass;
	}

	public Throwable getCause() {
		return cause;
	}

}

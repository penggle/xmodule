package com.penglecode.xmodule.common.web.springmvc.support;

import com.penglecode.xmodule.common.util.ArrayUtils;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.SpringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 增强的RequestParamMethodArgumentResolver，解决@RequestParam注解显示地用于用户自定义POJO对象时的参数解析问题
 * 
 * 举个例子：
 * 
 * 请求1：http://172.16.18.174:18180/api/user/list1/?condition={"userName": "a", "status": 1}&page={"currentPage": 1, "pageSize": 20}&sort={"orders": [{"property": "createTime", "direction": "desc"},{"property": "updateTime", "direction": "asc"}]}
 * 
 * 请求2：http://172.16.18.174:18180/api/user/list/?userName=a&status=1&currentPage=1&pageSize=20&orders=createTime:desc,updateTime:desc
 * 
 * @GetMapping(value="/api/user/list", produces=APPLICATION_JSON)
 * public PageResult<List<User>> getUserListByPage( @RequestParam User condition, @RequestParam Page page, @RequestParam Sort sort );
 * 
 * 如上例所示，请求1的参数能够正确地被@RequestParam注解解析，但是请求2却不行，该实现即是解决此问题的
 * 
 * @author 	pengpeng
 * @date	2019年5月22日 上午9:22:04
 */
public class EnhancedRequestParamMethodArgumentResolver extends RequestParamMethodArgumentResolver {

	/**
	 * 明确指出的可解析的参数类型列表
	 */
	private List<Class<?>> resolvableParameterTypes;
	
	public EnhancedRequestParamMethodArgumentResolver(boolean useDefaultResolution) {
		super(useDefaultResolution);
	}

	public EnhancedRequestParamMethodArgumentResolver(ConfigurableBeanFactory beanFactory,
			boolean useDefaultResolution) {
		super(beanFactory, useDefaultResolution);
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		Object arg = super.resolveName(name, parameter, request);
		if(arg == null) {
			if(isResolvableParameter(parameter)) {
				HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
				Map<String,Object> parameterMap = getRequestParameters(servletRequest);
				arg = instantiateParameter(parameter);
				SpringUtils.setBeanProperty(arg, parameterMap);
			}
		}
		return arg;
	}
	
	/**
	 * 判断@RequestParam注解的参数是否是可解析的
	 * 1、不是一个SimpleProperty (由BeanUtils.isSimpleProperty()方法决定)
	 * 2、不是一个Map类型 (Map类型走RequestParamMapMethodArgumentResolver,此处不做考虑)
	 * 3、该参数类型具有默认的无参构造器
	 * @param parameter
	 * @return
	 */
	protected boolean isResolvableParameter(MethodParameter parameter) {
		Class<?> clazz = parameter.getNestedParameterType();
		if(!CollectionUtils.isEmpty(resolvableParameterTypes)) {
			for(Class<?> parameterType : resolvableParameterTypes) {
				if(parameterType.isAssignableFrom(clazz)) {
					return true;
				}
			}
		}
		if(!BeanUtils.isSimpleProperty(clazz) && !Map.class.isAssignableFrom(clazz)) {
			Constructor<?>[] constructors = clazz.getDeclaredConstructors();
			if(!ArrayUtils.isEmpty(constructors)) {
				for(Constructor<?> constructor : constructors) {
					if(constructor.getParameterTypes().length == 0) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 实例化一个@RequestParam注解参数的实例
	 * @param parameter
	 * @return
	 */
	protected Object instantiateParameter(MethodParameter parameter) {
		return BeanUtils.instantiateClass(parameter.getNestedParameterType());
	}
	
	protected Map<String,Object> getRequestParameters(HttpServletRequest request) {
		Map<String,Object> parameters = new HashMap<>();
		Map<String,String[]> paramMap = request.getParameterMap();
		if(!CollectionUtils.isEmpty(paramMap)) {
			paramMap.forEach((key, values) -> parameters.put(key, ArrayUtils.isEmpty(values) ? null : (values.length == 1 ? values[0] : values)));
		}
		return parameters;
	}

	public List<Class<?>> getResolvableParameterTypes() {
		return resolvableParameterTypes;
	}

	public void setResolvableParameterTypes(List<Class<?>> resolvableParameterTypes) {
		this.resolvableParameterTypes = resolvableParameterTypes;
	}
	
}

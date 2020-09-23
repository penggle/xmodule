package com.penglecode.xmodule.master4j.spring.aop.pointcut;

import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 排除java.lang.Object中的所有方法
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/18 16:27
 */
public class ObjectMethodExcludedPointcut extends StaticMethodMatcherPointcut {

    private final Map<Method,Class<?>> objectMethods;

    public ObjectMethodExcludedPointcut() {
        Method[] objectMethods = Object.class.getDeclaredMethods();
        this.objectMethods = Arrays.stream(objectMethods).collect(Collectors.toMap(Function.identity(), Method::getDeclaringClass));
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return !objectMethods.containsKey(method);
    }
}

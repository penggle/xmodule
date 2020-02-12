package com.penglecode.xmodule.common.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public final class UnsupportedOperationExceptionInvocationHandler implements InvocationHandler {
	public Object invoke(Object proxy, Method method, Object[] args) {
		throw new UnsupportedOperationException(method + " is not supported");
	}
}
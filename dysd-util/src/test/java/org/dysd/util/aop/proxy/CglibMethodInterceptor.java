package org.dysd.util.aop.proxy;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class CglibMethodInterceptor implements MethodInterceptor{

	@Override
	public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
		System.out.println("[cglib]before doService");
		Object rs = arg3.invokeSuper(arg0, arg2);
		System.out.println("[cglib]before doService");
		return rs;
	}


}

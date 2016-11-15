package org.dysd.util.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JdkInvocationHandler implements InvocationHandler{

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("[jdk]before doService");
		//Object rs = method.invoke(proxy, args);
		System.out.println("[jdk]after doService");
		return null;
	}

}

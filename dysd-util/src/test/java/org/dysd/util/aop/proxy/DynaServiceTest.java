package org.dysd.util.aop.proxy;

import java.lang.reflect.Proxy;

import org.junit.Test;
import org.springframework.cglib.proxy.Enhancer;

public class DynaServiceTest {

	@Test
	public void testJdk() {
		JdkInvocationHandler h = new JdkInvocationHandler();
		IDynaProxyService service = (IDynaProxyService) Proxy.newProxyInstance(IDynaProxyService.class.getClassLoader(), new Class<?>[]{IDynaProxyService.class}, h);
		
		service.doService();
	}

	@Test
	public void testCglib() {
		DynaProxyServiceImpl service = (DynaProxyServiceImpl)Enhancer.create(DynaProxyServiceImpl.class, new CglibMethodInterceptor());
		service.doService();
		
		service.doExecute();
	}
}

package org.dysd.util.aop.proxy;

public class DynaProxyServiceImpl implements IDynaProxyService{

	@Override
	public void doService() {
		System.out.println("doService");
	}

	public void doExecute() {
		System.out.println("doExecute");
	}
}

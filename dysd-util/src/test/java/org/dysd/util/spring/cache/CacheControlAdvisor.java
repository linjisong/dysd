package org.dysd.util.spring.cache;

import java.lang.reflect.Method;
import java.security.AccessControlException;
import java.util.Collection;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor;
import org.springframework.cache.interceptor.CacheOperation;
import org.springframework.cache.interceptor.CacheOperationSource;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Component;

@Component
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class CacheControlAdvisor extends BeanFactoryCacheOperationSourceAdvisor{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6233191503670217581L;
	
	/**
	 * 保护的缓存名称
	 */
	private String protectedCacheName = "beneform4j";

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		// 调用父类方法
		super.setBeanFactory(beanFactory);
		// 设置优先级为最高
		super.setOrder(HIGHEST_PRECEDENCE);
		// 获取配置的缓存操作提取源，用于定义切点
		final CacheOperationSource cacheOperationSource = beanFactory.getBean(CacheOperationSource.class);
		// 手工设置
		super.setCacheOperationSource(cacheOperationSource);
		// 手工设置切面逻辑
		super.setAdvice(new MethodInterceptor(){
			@Override
			public Object invoke(MethodInvocation invocation) throws Throwable {
				Method method = invocation.getMethod();
				Class<?> targetClass = getTargetClass(invocation.getThis());
				Collection<CacheOperation> operations = cacheOperationSource.getCacheOperations(method, targetClass);
				for(CacheOperation operation : operations){
					Set<String> cacheNames = operation.getCacheNames();
					if(null != cacheNames && cacheNames.contains(protectedCacheName)){
						throw new AccessControlException(operation.toString());
					}
				}
				return invocation.proceed();
			}
		});
	}
	
	private Class<?> getTargetClass(Object target) {
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
		if (targetClass == null && target != null) {
			targetClass = target.getClass();
		}
		return targetClass;
	}
}

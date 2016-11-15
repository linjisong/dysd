/**
 * Copyright (c) 2016-2017, the original author or authors (dysd_2016@163.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dysd.dao.mybatis.interceptor;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;

/**
 * Mybatis拦截器插件抽象实现类
 * @author linjisong
 * @version 0.0.1
 * @since 0.0.1
 * @date 2016-11-15
 */
public abstract class AbstractInterceptor implements Interceptor{
	
	private Properties properties;
	
	private static Field target;
	static{
		try{
			target = Plugin.class.getDeclaredField("target");
			target.setAccessible(true);
		}catch(Exception e){}
	}

	/**
	 * 获取拦截的目标类
	 * @param invocation
	 * @param cls 目标类型
	 * @return 目标类
	 */
	protected <T> T getTarget(Invocation invocation, Class<T> cls){
		Object obj = invocation.getTarget();
		while(Proxy.isProxyClass(obj.getClass())){//处理嵌套插件的目标类获取
			try{
				obj = target.get(Proxy.getInvocationHandler(obj));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return cls.cast(obj);
	}
	
	/**
	 * 获取拦截的目标方法的第index个参数
	 * @param invocation
	 * @param cls   参数类型
	 * @param index 参数索引
	 * @return 第index个参数对象
	 */
	protected <T> T getArgument(Invocation invocation, Class<T> cls, int index){
		Object obj = invocation.getArgs()[index];
		return cls.cast(obj);
	}
	
	/**
	 * 获取配置的属性
	 * @param key  属性名
	 * @return 属性值
	 */
	protected String getProperty(String key){
		return null == properties ? null : properties.getProperty(key);
	}

	/**
	 * 包装插件
	 */
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/**
	 * 设置插件属性
	 */
	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
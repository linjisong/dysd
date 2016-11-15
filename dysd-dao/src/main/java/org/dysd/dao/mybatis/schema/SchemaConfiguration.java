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
package org.dysd.dao.mybatis.schema;

import java.lang.reflect.Field;

import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.transaction.Transaction;
import org.dysd.dao.mybatis.interceptor.resultset.ResultSetHandlerInterceptor;
import org.dysd.dao.mybatis.interceptor.statement.StatementHandlerInterceptor;
import org.dysd.dao.mybatis.mapper.MapperRegistry;
import org.dysd.dao.mybatis.schema.executor.BatchExecutor;
import org.dysd.dao.mybatis.schema.executor.ReuseExecutor;
import org.dysd.dao.mybatis.schema.executor.SimpleExecutor;

/**
 * 可解析XSD的mybatis配置对象
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class SchemaConfiguration extends Configuration{
	
	/**
	 * 是否自动配置
	 */
	private boolean autoConfig = true;
	
	public SchemaConfiguration() {
		super();
		customConfiguration();
	}

	public SchemaConfiguration(Environment environment) {
		super(environment);
		customConfiguration();
	}
	
	/**
	 * 自定义配置
	 */
	protected void customConfiguration(){
		try{
			// 替换默认的MapperRegistry
			Field field = Configuration.class.getDeclaredField("mapperRegistry");
			field.setAccessible(true);
			field.set(this, new MapperRegistry(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(autoConfig){
			autoConfig();
		}
	}

	/**
	 * 自动配置
	 */
	protected void autoConfig() {
		this.setMapUnderscoreToCamelCase(true); //Java属性与数据库字段采用驼峰式对应
		this.setAutoMappingBehavior(AutoMappingBehavior.FULL);//字段映射采用复合规则，有配置的使用配置映射，未配置的采用默认映射 
		this.setSafeResultHandlerEnabled(false);
		this.setCallSettersOnNulls(true);
		this.setUseColumnLabel(true);
		this.setLogPrefix("mybatis.");
		this.setDefaultScriptingLanguage(SchemaXMLLanguageDriver.class);
		this.addInterceptor(new StatementHandlerInterceptor());
		this.addInterceptor(new ResultSetHandlerInterceptor());
	}
	
	/**
	 * 覆盖执行器的创建，从而覆盖缓存键值CacheKey的生成，以避免在生成缓存键值时，由于含有表达式而出现异常
	 */
	@Override
	public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
		executorType = executorType == null ? defaultExecutorType : executorType;
	    executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
	    Executor executor;
	    if (ExecutorType.BATCH == executorType) {
	      executor = new BatchExecutor(this, transaction);
	    } else if (ExecutorType.REUSE == executorType) {
	      executor = new ReuseExecutor(this, transaction);
	    } else {
	      executor = new SimpleExecutor(this, transaction);
	    }
	    if (cacheEnabled) {
	      executor = new CachingExecutor(executor);
	    }
	    executor = (Executor) interceptorChain.pluginAll(executor);
	    return executor;
	}

	/**
	 * 设置是否自动配置
	 * @param autoConfig
	 */
	public void setAutoConfig(boolean autoConfig) {
		this.autoConfig = autoConfig;
	}
}

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
package org.dysd.dao;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.dysd.dao.dialect.IDialect;
import org.dysd.dao.dialect.impl.Db2;
import org.dysd.dao.dialect.impl.H2;
import org.dysd.dao.dialect.impl.MySQL;
import org.dysd.dao.dialect.impl.Oracle;
import org.dysd.dao.dialect.impl.SybaseASE;
import org.dysd.dao.dialect.impl.SybaseIQ;
import org.dysd.dao.mybatis.component.MybatisComponentFactory;
import org.dysd.dao.mybatis.component.impl.DefaultMybatisComponentFactory;
import org.dysd.dao.mybatis.schema.SchemaHandlers;
import org.dysd.dao.mybatis.schema.expression.IExpressionHandler;
import org.dysd.dao.mybatis.schema.function.ISqlConfigFunction;
import org.dysd.dao.mybatis.schema.function.ISqlConfigFunctionFactory;
import org.dysd.dao.mybatis.schema.script.IScriptHandler;
import org.dysd.dao.mybatis.schema.statement.IStatementHandler;
import org.dysd.util.config.BaseConfig;

/**
 * Dao配置
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class DaoConfig extends BaseConfig{

	private static final LinkedHashMap<String, IDialect> defaultDatabaseProductNameDialectMapping = new LinkedHashMap<String, IDialect>();
	static{
		defaultDatabaseProductNameDialectMapping.put("oracle", Oracle.getInstance());
		defaultDatabaseProductNameDialectMapping.put("mysql", MySQL.getInstance());
		defaultDatabaseProductNameDialectMapping.put("db2", Db2.getInstance());
		defaultDatabaseProductNameDialectMapping.put("h2", H2.getInstance());
		defaultDatabaseProductNameDialectMapping.put("ADAPTIVE SERVER ENTERPRISE", SybaseASE.getInstance());
		defaultDatabaseProductNameDialectMapping.put("IQ", SybaseIQ.getInstance());
	}
	
	/**
	 * 表名前缀
	 */
	private static String tablePrefix = "";
	
	/**
	 * sqlId映射（通过注入该映射，可以替换执行平台中已打包好的SQL）
	 */
	private static Map<String, String> sqlIdMapping;
	
	/**
	 * 数据库产品名称和数据库方言的映射关系
	 */
	private static LinkedHashMap<String, IDialect> databaseProductNameDialectMapping = defaultDatabaseProductNameDialectMapping;
	
	/**
	 * 自定义语句级处理器
	 */
	private static Map<String, Map<String, IStatementHandler>> customStatementHandlers;
	/**
	 * 自定义脚本级处理器
	 */
	private static Map<String, Map<String, IScriptHandler>> customScriptHandlers;
	/**
	 * 自定义表达式处理器
	 */
	private static Set<IExpressionHandler> customExpressionHandlers;
	/**
	 * 自定义Sql配置函数
	 */
	private static Set<ISqlConfigFunction> customSqlConfigFunctions;
	/**
	 * 自定义Sql配置函数工厂
	 */
	private static Set<ISqlConfigFunctionFactory> customSqlConfigFunctionFactorys;
	/**
	 * mybatis组件工厂
	 */
	private static MybatisComponentFactory mybatisComponentFactory = new DefaultMybatisComponentFactory();

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		registerCustomStatementHandler();
		registerCustomScriptHandler();
		registerCustomExpressionHandler();
		registerCustomSqlConfigFunction();
		registerCustomSqlFunctionFactory();
	}
	
	/**
	 * 校验
	 */
	@Override
	public void validate(){
		super.validate();
		
		// 执行当前配置类中的校验
	}
	
	/**
	 * 获取表名前缀
	 * @return 配置中的表名前缀
	 */
	public static String getTablePrefix() {
		return getValue(tablePrefix, "table_prefix");
	}

	/**
	 * 注入表名前缀
	 * @param tablePrefix 表名前缀
	 */
	public void setTablePrefix(String tablePrefix) {
		DaoConfig.tablePrefix = tablePrefix;
	}
	
	/**
	 * 获取SQL-ID映射
	 * @return 原SQL-ID和新SQL-ID的映射
	 */
	public static Map<String, String> getSqlIdMapping() {
		return sqlIdMapping;
	}
	
	/**
	 * 注入sqlId映射
	 * @param sqlIdMapping 原SQL-ID和新SQL-ID的映射
	 */
	public void setSqlIdMapping(Map<String, String> sqlIdMapping) {
		DaoConfig.sqlIdMapping = sqlIdMapping;
	}

	/**
	 * 获取数据库产品名称中关键字和方言的映射配置
	 * @return 数据库产品名称中关键字和方言的映射
	 */
	public static Map<String, IDialect> getDatabaseProductNameDialectMapping() {
		return databaseProductNameDialectMapping;
	}

	/**
	 * 注入数据库产品名称和方言的映射配置
	 * @param databaseProductNameDialectMapping 数据库产品名称中关键字和方言的映射
	 */
	public void setDatabaseProductNameDialectMapping(LinkedHashMap<String, IDialect> databaseProductNameDialectMapping) {
		if(null != databaseProductNameDialectMapping && !databaseProductNameDialectMapping.isEmpty()){
			if(null != DaoConfig.databaseProductNameDialectMapping){//在前面插入应用配置
				databaseProductNameDialectMapping.putAll(DaoConfig.databaseProductNameDialectMapping);
			}
			DaoConfig.databaseProductNameDialectMapping = databaseProductNameDialectMapping;
		}
	}
	
	/**
	 * 注入自定义表达式处理器
	 * @param customExpressionHandlers
	 */
	public void setCustomExpressionHandlers(Set<IExpressionHandler> customExpressionHandlers) {
		DaoConfig.customExpressionHandlers = customExpressionHandlers;
	}

	/**
	 * 注入脚本级元素处理器，外层key值为命名空间，内层key值为元素名
	 * @param customScriptHandlers
	 */
	public void setCustomScriptHandlers(Map<String, Map<String, IScriptHandler>> customScriptHandlers) {
		DaoConfig.customScriptHandlers = customScriptHandlers;
	}

	/**
	 * 注入语句级元素处理器，外层key值为命名空间，内层key值为元素名
	 * @param customStatementHandlers
	 */
	public void setCustomStatementHandlers(Map<String, Map<String, IStatementHandler>> customStatementHandlers) {
		DaoConfig.customStatementHandlers = customStatementHandlers;
	}

	/**
	 * 注入自定义SQL配置函数
	 * @param customSqlConfigFunctions
	 */
	public void setCustomSqlConfigFunctions(Set<ISqlConfigFunction> customSqlConfigFunctions) {
		DaoConfig.customSqlConfigFunctions = customSqlConfigFunctions;
	}

	/**
	 * 注入自定义SQL配置函数工厂
	 * @param customSqlConfigFunctionFactorys
	 */
	public void setCustomSqlConfigFunctionFactorys(Set<ISqlConfigFunctionFactory> customSqlConfigFunctionFactorys) {
		DaoConfig.customSqlConfigFunctionFactorys = customSqlConfigFunctionFactorys;
	}
	
	/**
	 * 获取mybatis组件工厂
	 * @return
	 */
	public static MybatisComponentFactory getMybatisComponentFactory() {
		return getComponent(mybatisComponentFactory, MybatisComponentFactory.class);
	}

	/**
	 * 注入mybatis组件工厂
	 * @param mybatisComponentFactory mybatis组件工厂
	 */
	public void setMybatisComponentFactory(MybatisComponentFactory mybatisComponentFactory) {
		if(null != mybatisComponentFactory){
			DaoConfig.mybatisComponentFactory = mybatisComponentFactory;
		}
	}
	
	/**
	 * 注册自定义语句级元素处理器
	 */
	private void registerCustomStatementHandler() {
		if(null != customStatementHandlers ){
			for(String namespaceUri : customStatementHandlers.keySet()){
				Map<String, IStatementHandler> handlers = customStatementHandlers.get(namespaceUri);
				if(null != handlers){
					for(String name : handlers.keySet()){
						SchemaHandlers.register(namespaceUri, name, handlers.get(name));
					}
				}
			}
		}
	}

	/**
	 * 注册自定义脚本级元素处理器
	 */
	private void registerCustomScriptHandler() {
		if(null != customScriptHandlers ){
			for(String namespaceUri : customScriptHandlers.keySet()){
				Map<String, IScriptHandler> handlers = customScriptHandlers.get(namespaceUri);
				if(null != handlers){
					for(String name : handlers.keySet()){
						SchemaHandlers.register(namespaceUri, name, handlers.get(name));
					}
				}
			}
		}
	}

	/**
	 * 注册自定义表达式处理器
	 */
	private void registerCustomExpressionHandler() {
		if(null != customExpressionHandlers ){
			SchemaHandlers.register(customExpressionHandlers);
		}
	}

	/**
	 * 注册自定义SQL配置函数
	 */
	private void registerCustomSqlConfigFunction() {
		if(null != customSqlConfigFunctions ){
			for(ISqlConfigFunction sqlConfigFunction : customSqlConfigFunctions){
				SchemaHandlers.register(sqlConfigFunction);
			}
		}
	}

	/**
	 * 注册自定义SQL配置函数工厂
	 */
	private void registerCustomSqlFunctionFactory() {
		if(null != customSqlConfigFunctionFactorys ){
			for(ISqlConfigFunctionFactory sqlConfigFunctionFactory : customSqlConfigFunctionFactorys){
				SchemaHandlers.register(sqlConfigFunctionFactory);
			}
		}
	}
}

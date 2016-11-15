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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.dysd.dao.mybatis.schema.expression.IExpressionHandler;
import org.dysd.dao.mybatis.schema.expression.impl.SpelExpressionHandler;
import org.dysd.dao.mybatis.schema.function.ISqlConfigFunction;
import org.dysd.dao.mybatis.schema.function.ISqlConfigFunctionFactory;
import org.dysd.dao.mybatis.schema.function.factory.LikeSqlConfigFunctionFactory;
import org.dysd.dao.mybatis.schema.function.single.ConcatSqlConfigFunction;
import org.dysd.dao.mybatis.schema.function.single.DecodeSqlConfigFunction;
import org.dysd.dao.mybatis.schema.script.IScriptHandler;
import org.dysd.dao.mybatis.schema.script.extend.DbScriptHandler;
import org.dysd.dao.mybatis.schema.script.original.BindScriptHandler;
import org.dysd.dao.mybatis.schema.script.original.ChooseScriptHandler;
import org.dysd.dao.mybatis.schema.script.original.ForEachScriptHandler;
import org.dysd.dao.mybatis.schema.script.original.IfScriptHandler;
import org.dysd.dao.mybatis.schema.script.original.OtherwiseScriptHandler;
import org.dysd.dao.mybatis.schema.script.original.SetScriptHandler;
import org.dysd.dao.mybatis.schema.script.original.TrimScriptHandler;
import org.dysd.dao.mybatis.schema.script.original.WhereScriptHandler;
import org.dysd.dao.mybatis.schema.statement.IStatementHandler;
import org.dysd.dao.mybatis.schema.statement.extend.DbStatementHandler;
import org.dysd.dao.mybatis.schema.statement.original.CRUDStatementHandler;
import org.dysd.dao.mybatis.schema.statement.original.CacheRefStatementHandler;
import org.dysd.dao.mybatis.schema.statement.original.CacheStatementHandler;
import org.dysd.dao.mybatis.schema.statement.original.ParameterMapStatementHandler;
import org.dysd.dao.mybatis.schema.statement.original.ResultMapStatementHandler;
import org.dysd.dao.mybatis.schema.statement.original.SqlStatementHandler;
import org.dysd.util.Tool;
import org.w3c.dom.Node;

/**
 * XSD模式下的处理器管理类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class SchemaHandlers {
	
	/**
	 * 原生配置翻译过来的默认命名空间
	 */
	private static final String SQL_MAPPER_NAMESPACE = "http://dysd.org/schema/sqlmapper";
	/**
	 * 扩展元素所在的命名空间
	 */
	private static final String SQL_MAPPER_EXTEND_NAMESPACE = "http://dysd.org/schema/sqlmapper-extend";

	/**
	 * 脚本级元素解析器
	 */
	private static final Map<String, Map<String, IScriptHandler>> scripts = new HashMap<String, Map<String, IScriptHandler>>();
	/**
	 * 语句级元素解析器
	 */
	private static final Map<String, Map<String, IStatementHandler>> statements = new HashMap<String, Map<String, IStatementHandler>>();
	/**
	 * 表达式处理器
	 */
	private static final Set<IExpressionHandler> expressions = new LinkedHashSet<IExpressionHandler>();
	/**
	 * 默认表达式处理器
	 */
	private static final IExpressionHandler defaultExpressionHandler = new SpelExpressionHandler();
	/**
	 * SQL配置函数
	 */
	private static final Map<String, ISqlConfigFunction> sqlConfigFunctions = new HashMap<String, ISqlConfigFunction>();
	
	static {
		//注册默认命名空间的StatementHandler
		register("cache-ref", new CacheRefStatementHandler());
		register("cache", new CacheStatementHandler());
		register("parameterMap", new ParameterMapStatementHandler());
		register("resultMap", new ResultMapStatementHandler());
		register("sql", new SqlStatementHandler());
		register("select|insert|update|delete", new CRUDStatementHandler());
		
		//注册默认命名空间的ScriptHandler
		register("trim", new TrimScriptHandler());
		register("where", new WhereScriptHandler());
		register("set", new SetScriptHandler());
		register("foreach", new ForEachScriptHandler());
		register("if|when", new IfScriptHandler());
		register("choose", new ChooseScriptHandler());
		//register("when", new IfScriptHandler());
		register("otherwise", new OtherwiseScriptHandler());
		register("bind", new BindScriptHandler());
		
		// 注册自定义命名空间的处理器
		registerExtend("db", new DbStatementHandler(), new DbScriptHandler());
		
		// 注册SqlConfigFunction
		register(new DecodeSqlConfigFunction());
		register(new ConcatSqlConfigFunction());
		
		// 注册SqlConfigFunctionFactory
		register(new LikeSqlConfigFunctionFactory());
	}
	
	/**
	 * 同一个元素，同时处理脚本级和语句级解析器
	 * @param namespaceUri
	 * @param name
	 * @param statement
	 * @param script
	 */
	public static void register(String namespaceUri, String name, IStatementHandler statement, IScriptHandler script){
		register(namespaceUri, name, statement);
		register(namespaceUri, name, script);
	}
	
	/**
	 * 注册脚本级元素解析器，name支持竖线分隔多个元素名
	 * @param namespaceUri
	 * @param name
	 * @param handler
	 */
	public static void register(String namespaceUri, String name, IStatementHandler handler){
		Map<String, IStatementHandler> hs = statements.get(namespaceUri);
		if(null == hs){
			hs = new HashMap<String, IStatementHandler>();
			statements.put(namespaceUri, hs);
		}
		for(String n : name.split("\\s*\\|\\s*")){
			hs.put(n, handler);
		}
	}
	
	/**
	 * 注册语句级元素解析器，name支持竖线分隔多个元素名
	 * @param namespaceUri
	 * @param name
	 * @param handler
	 */
	public static void register(String namespaceUri, String name, IScriptHandler handler){
		Map<String, IScriptHandler> hs = scripts.get(namespaceUri);
		if(null == hs){
			hs = new HashMap<String, IScriptHandler>();
			scripts.put(namespaceUri, hs);
		}
		for(String n : name.split("\\s*\\|\\s*")){
			hs.put(n, handler);
		}
	}
	
	/**
	 * 注册表达式处理器
	 * @param handler
	 */
	public static void register(IExpressionHandler handler){
		expressions.add(handler);
	}
	
	/**
	 * 注册一组表达式处理器
	 * @param handlers
	 */
	public static void register(Collection<? extends IExpressionHandler> handlers){
		expressions.addAll(handlers);
	}
	
	/**
	 * 注册SQL配置函数
	 * @param sqlConfigFunction
	 */
	public static void register(ISqlConfigFunction sqlConfigFunction){
		String name = sqlConfigFunction.getName().toUpperCase();
		ISqlConfigFunction old = sqlConfigFunctions.get(name);
		if(null == old || sqlConfigFunction.getOrder() < old.getOrder()){
			sqlConfigFunctions.put(name, sqlConfigFunction);
		}
	}
	
	/**
	 * 注册SQL配置函数工厂
	 * @param sqlConfigFunctionFactory
	 */
	public static void register(ISqlConfigFunctionFactory sqlConfigFunctionFactory){
		Collection<ISqlConfigFunction> sqlConfigFunctions = sqlConfigFunctionFactory.getSqlConfigFunctions();
		if(null != sqlConfigFunctions){
			for(ISqlConfigFunction sqlConfigFunction : sqlConfigFunctions){
				register(sqlConfigFunction);
			}
		}
	}

	/**
	 * 获取节点的语句级解析器
	 * @param node
	 * @return
	 */
	public static IStatementHandler getStatementHandler(Node node) {
		String namespaceUri = node.getNamespaceURI();
		if(Tool.CHECK.isBlank(namespaceUri)){
			namespaceUri = SQL_MAPPER_NAMESPACE;
		}
		Map<String, IStatementHandler> hs = statements.get(namespaceUri);
		return null == hs ? null : hs.get(node.getLocalName());
	}

	/**
	 * 获取节点的脚本级解析器
	 * @param node
	 * @return
	 */
	public static IScriptHandler getScriptHandler(Node node) {
		String namespaceUri = node.getNamespaceURI();
		if(Tool.CHECK.isBlank(namespaceUri)){
			namespaceUri = SQL_MAPPER_NAMESPACE;
		}
		Map<String, IScriptHandler> hs = scripts.get(namespaceUri);
		return null == hs ? null : hs.get(node.getLocalName());
	}
	
	/**
	 * 获取表达式处理器
	 * @param node
	 * @return
	 */
	public static IExpressionHandler getExpressionHandler(String expression){
		for(IExpressionHandler handler : expressions){
			if(handler.isSupport(expression)){
				return handler;
			}
		}
		return defaultExpressionHandler;
	}
	
	/**
	 * 获取SQL配置函数
	 * @param name
	 * @return
	 */
	public static ISqlConfigFunction getSqlConfigFunction(String name){
		return sqlConfigFunctions.get(name.toUpperCase());
	}
	
	private static void registerExtend(String name, IStatementHandler statement, IScriptHandler script) {
		registerExtend(name, statement);
		registerExtend(name, script);
	}
	
	private static void register(String name, IStatementHandler handler) {
		register(SQL_MAPPER_NAMESPACE, name, handler);
	}
	
	private static void registerExtend(String name, IStatementHandler handler) {
		register(SQL_MAPPER_EXTEND_NAMESPACE, name, handler);
	}
	
	private static void register(String name, IScriptHandler handler) {
		register(SQL_MAPPER_NAMESPACE, name, handler);
	}
	
	private static void registerExtend(String name, IScriptHandler handler){
		register(SQL_MAPPER_EXTEND_NAMESPACE, name, handler);
	}
}

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
package org.dysd.dao.mybatis.component.impl;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.dysd.dao.mybatis.component.MybatisComponentFactory;
import org.dysd.dao.mybatis.mapper.IParamResolver;
import org.dysd.dao.mybatis.mapper.impl.ParamNameResolver;
import org.dysd.dao.mybatis.schema.expression.ExpressionParameterHandler;
import org.dysd.dao.mybatis.schema.script.SchemaXMLScriptBuilder;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;

/**
 * Mybatis组件工厂默认实现
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class DefaultMybatisComponentFactory implements MybatisComponentFactory{

	@Override
	public SqlSessionFactoryBuilder newSqlSessionFactoryBuilder() {
		return new SqlSessionFactoryBuilder();
	}

	@Override
	public XMLConfigBuilder newXMLConfigBuilder(InputStream inputStream, String environment, Properties props) {
		return new XMLConfigBuilder(inputStream, environment, props);
	}

	@Override
	public XMLMapperBuilder newXMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource,
			Map<String, XNode> sqlFragments) {
		return new XMLMapperBuilder(inputStream, configuration, resource, sqlFragments);
	}
	
	@Override
	public XMLStatementBuilder newXMLStatementBuilder(Configuration configuration, MapperBuilderAssistant builder, XNode node, String requiredDatabaseId){
		return new XMLStatementBuilder(configuration, builder, node, requiredDatabaseId);
	}
	
	@Override
	public XMLScriptBuilder newXMLScriptBuilder(Configuration configuration, XNode node, Class<?> parameterType){
		return new SchemaXMLScriptBuilder(configuration, node, parameterType);
	}
	
	@Override
	public SqlSourceBuilder newSqlSourceBuilder(Configuration configuration){
		return new org.dysd.dao.mybatis.schema.script.sqlsource.SqlSourceBuilder(configuration);
	}

	@Override
	public Configuration newConfiguration() {
		return new Configuration();
	}

	@Override
	public Environment newEnvironment(String id, TransactionFactory transactionFactory, DataSource dataSource) {
		return new Environment(id, transactionFactory, dataSource);
	}

	@Override
	public TransactionFactory newTransactionFactory() {
		return new SpringManagedTransactionFactory();
	}
	
	@Override
	public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql){
		return new ExpressionParameterHandler(mappedStatement, parameterObject, boundSql);
	}

	@Override
	public IParamResolver newParamResolver(Configuration config, Method method, boolean isBatch) {
		return new ParamNameResolver(config, method, isBatch);
	}
}

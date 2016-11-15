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
package org.dysd.dao.mybatis.schema.expression;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.dysd.dao.exception.DaoExceptionCodes;
import org.dysd.dao.mybatis.schema.SchemaHandlers;
import org.dysd.util.Tool;
import org.dysd.util.exception.Throw;

/**
 * 可处理表达式的参数处理器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class ExpressionParameterHandler implements ParameterHandler {

	private TypeHandlerRegistry typeHandlerRegistry;
	private MappedStatement mappedStatement;
	private Object parameterObject;
	private BoundSql boundSql;
	private Configuration configuration;
	private String databaseId;
	
	public ExpressionParameterHandler(){}
	
	public ExpressionParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
		this.mappedStatement = mappedStatement;
		this.configuration = mappedStatement.getConfiguration();
		this.typeHandlerRegistry = configuration.getTypeHandlerRegistry();
		this.parameterObject = parameterObject;
		this.boundSql = boundSql;
		this.databaseId = configuration.getDatabaseId();
	}

	public void setParameters(PreparedStatement ps) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					setParameter(metaObject, parameterMapping, ps, i+1);
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void setParameter(MetaObject metaObject, ParameterMapping parameterMapping, PreparedStatement ps, int parameterIndex) throws SQLException{
		Object value = evaluateValue(databaseId, boundSql, parameterObject, typeHandlerRegistry, metaObject, parameterMapping);
		JdbcType jdbcType = parameterMapping.getJdbcType();
		if (value == null && jdbcType == null){
			jdbcType = configuration.getJdbcTypeForNull();
		}
		TypeHandler typeHandler = parameterMapping.getTypeHandler();
		typeHandler.setParameter(ps, parameterIndex, value, jdbcType);
	}
	
	public static Object evaluateValue(String databaseId, BoundSql boundSql, Object parameterObject,
			TypeHandlerRegistry typeHandlerRegistry, MetaObject metaObject, ParameterMapping parameterMapping) {
		Object value = null;
		String expression = parameterMapping.getExpression();
		if (!Tool.CHECK.isBlank(expression)) {
			IExpressionHandler handler = SchemaHandlers.getExpressionHandler(expression);
			if (null != handler) {
				return handler.eval(expression, parameterObject, databaseId);
			}else{
				Throw.throwException(DaoExceptionCodes.DYSD020019, expression);
			}
		} else {
			String propertyName = parameterMapping.getProperty();
			if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448  ask first for  additional  params
				value = boundSql.getAdditionalParameter(propertyName);
			} else if (parameterObject == null) {
				value = null;
			} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				value = parameterObject;
			} else {
				value = metaObject == null ? null : metaObject.getValue(propertyName);
			}
		}
		return value;
	}
	
	public Object getParameterObject() {
		return parameterObject;
	}
	
	public Configuration getConfiguration(){
		return configuration;
	}
	
	public MappedStatement getMappedStatement(){
		return mappedStatement;
	}
	
	public BoundSql getBoundSql(){
		return boundSql;
	}
	
	public TypeHandlerRegistry getTypeHandlerRegistry(){
		return typeHandlerRegistry;
	}
	
	public String getDatabaseId() {
		return databaseId;
	}
}

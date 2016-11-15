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
package org.dysd.dao.mybatis.schema.statement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

/**
 * 语句级元素处理器抽象支持类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class StatementHandlerSupport implements IStatementHandler {

	protected Pattern parseExpression(String regex, String defaultValue) {
		return Pattern.compile(regex == null ? defaultValue : regex);
	}

	protected Boolean booleanValueOf(String value, Boolean defaultValue) {
		return value == null ? defaultValue : Boolean.valueOf(value);
	}

	protected Integer integerValueOf(String value, Integer defaultValue) {
		return value == null ? defaultValue : Integer.valueOf(value);
	}

	protected Set<String> stringSetValueOf(String value, String defaultValue) {
		value = (value == null ? defaultValue : value);
		return new HashSet<String>(Arrays.asList(value.split(",")));
	}

	protected JdbcType resolveJdbcType(String alias) {
		if (alias == null) {
			return null;
		}
		try {
			return JdbcType.valueOf(alias);
		} catch (IllegalArgumentException e) {
			throw new BuilderException("Error resolving JdbcType. Cause: " + e, e);
		}
	}

	protected ResultSetType resolveResultSetType(String alias) {
		if (alias == null) {
			return null;
		}
		try {
			return ResultSetType.valueOf(alias);
		} catch (IllegalArgumentException e) {
			throw new BuilderException("Error resolving ResultSetType. Cause: " + e, e);
		}
	}

	protected ParameterMode resolveParameterMode(String alias) {
		if (alias == null) {
			return null;
		}
		try {
			return ParameterMode.valueOf(alias);
		} catch (IllegalArgumentException e) {
			throw new BuilderException("Error resolving ParameterMode. Cause: " + e, e);
		}
	}

	protected Object createInstance(Configuration configuration, String alias) {
		Class<?> clazz = resolveClass(configuration, alias);
		if (clazz == null) {
			return null;
		}
		try {
			return resolveClass(configuration, alias).newInstance();
		} catch (Exception e) {
			throw new BuilderException("Error creating instance. Cause: " + e, e);
		}
	}

	protected Class<?> resolveClass(Configuration configuration, String alias) {
		if (alias == null) {
			return null;
		}
		try {
			return resolveAlias(configuration, alias);
		} catch (Exception e) {
			throw new BuilderException("Error resolving class. Cause: " + e, e);
		}
	}

	protected TypeHandler<?> resolveTypeHandler(Configuration configuration, Class<?> javaType,
			String typeHandlerAlias) {
		if (typeHandlerAlias == null) {
			return null;
		}
		Class<?> type = resolveClass(configuration, typeHandlerAlias);
		if (type != null && !TypeHandler.class.isAssignableFrom(type)) {
			throw new BuilderException("Type " + type.getName()
					+ " is not a valid TypeHandler because it does not implement TypeHandler interface");
		}
		@SuppressWarnings("unchecked") // already verified it is a TypeHandler
		Class<? extends TypeHandler<?>> typeHandlerType = (Class<? extends TypeHandler<?>>) type;
		return resolveTypeHandler(configuration, javaType, typeHandlerType);
	}

	protected TypeHandler<?> resolveTypeHandler(Configuration configuration, Class<?> javaType,
			Class<? extends TypeHandler<?>> typeHandlerType) {
		if (typeHandlerType == null) {
			return null;
		}
		// javaType ignored for injected handlers see issue #746 for full detail
		TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
		TypeHandler<?> handler = typeHandlerRegistry.getMappingTypeHandler(typeHandlerType);
		if (handler == null) {
			// not in registry, create a new one
			handler = typeHandlerRegistry.getInstance(javaType, typeHandlerType);
		}
		return handler;
	}

	protected <T> Class<T> resolveAlias(Configuration configuration, String alias) {
		return configuration.getTypeAliasRegistry().resolveAlias(alias);
	}
}

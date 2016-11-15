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
package org.dysd.dao.mybatis.schema.script.sqlsource;

import java.util.HashMap;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;
import org.dysd.dao.mybatis.component.impl.MybatisComponents;

/**
 * 支持表达式的RawSqlSource
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class RawSqlSource implements SqlSource {

	private final SqlSource sqlSource;

	public RawSqlSource(Configuration configuration, SqlNode rootSqlNode, Class<?> parameterType) {
		this(configuration, getSql(configuration, rootSqlNode), parameterType);
	}

	public RawSqlSource(Configuration configuration, String sql, Class<?> parameterType) {
		SqlSourceBuilder sqlSourceParser = MybatisComponents.newSqlSourceBuilder(configuration);
		Class<?> clazz = parameterType == null ? Object.class : parameterType;
		sqlSource = sqlSourceParser.parse(sql, clazz, new HashMap<String, Object>());
	}

	private static String getSql(Configuration configuration, SqlNode rootSqlNode) {
		DynamicContext context = new DynamicContext(configuration, null);
		rootSqlNode.apply(context);
		return context.getSql();
	}

	@Override
	public BoundSql getBoundSql(Object parameterObject) {
		return sqlSource.getBoundSql(parameterObject);
	}
}

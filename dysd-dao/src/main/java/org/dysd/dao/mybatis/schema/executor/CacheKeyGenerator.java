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
package org.dysd.dao.mybatis.schema.executor;

import java.util.List;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.dysd.dao.mybatis.schema.expression.ExpressionParameterHandler;

/**
 * 缓存主键生成器，引入表达式解析功能
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class CacheKeyGenerator {

	public static CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds,
			BoundSql boundSql) {
		CacheKey cacheKey = new CacheKey();
		cacheKey.update(ms.getId());
		cacheKey.update(Integer.valueOf(rowBounds.getOffset()));
		cacheKey.update(Integer.valueOf(rowBounds.getLimit()));
		cacheKey.update(boundSql.getSql());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		TypeHandlerRegistry typeHandlerRegistry = ms.getConfiguration().getTypeHandlerRegistry();
		// mimic DefaultParameterHandler logic

		Configuration configuration = ms.getConfiguration();
		String databaseId = configuration.getDatabaseId();
		MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
		for (int i = 0; i < parameterMappings.size(); i++) {
			ParameterMapping parameterMapping = parameterMappings.get(i);
			if (parameterMapping.getMode() != ParameterMode.OUT) {
				Object value = ExpressionParameterHandler.evaluateValue(databaseId, boundSql, parameterObject,
						typeHandlerRegistry, metaObject, parameterMapping);
				cacheKey.update(value);
			}
		}
		if (configuration.getEnvironment() != null) { // issue #176
			cacheKey.update(configuration.getEnvironment().getId());
		}
		return cacheKey;
	}

}

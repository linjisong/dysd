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
package org.dysd.dao.mybatis.schema.statement.original;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.ResultMapResolver;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.Discriminator;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.dysd.dao.mybatis.schema.SchemaSqlMapperParserDelegate;
import org.dysd.dao.mybatis.schema.statement.StatementHandlerSupport;

/**
 * resultMap元素处理器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class ResultMapStatementHandler extends StatementHandlerSupport{

	@Override
	public void handleStatementNode(Configuration configuration, SchemaSqlMapperParserDelegate delegate, XNode node) {
		try {
			MapperBuilderAssistant builder = delegate.getBuilderAssistant();
			resultMapElement(configuration, builder, node, Collections.<ResultMapping>emptyList());
		} catch (IncompleteElementException e) {
			// ignore, it will be retried
		}
	}
	
	private ResultMap resultMapElement(Configuration configuration, MapperBuilderAssistant builder, XNode resultMapNode, List<ResultMapping> additionalResultMappings) {
		ErrorContext.instance().activity("processing " + resultMapNode.getValueBasedIdentifier());
		String id = resultMapNode.getStringAttribute("id", resultMapNode.getValueBasedIdentifier());
		String type = resultMapNode.getStringAttribute("type", resultMapNode.getStringAttribute("ofType",
				resultMapNode.getStringAttribute("resultType", resultMapNode.getStringAttribute("javaType"))));
		String extend = resultMapNode.getStringAttribute("extends");
		Boolean autoMapping = resultMapNode.getBooleanAttribute("autoMapping");
		Class<?> typeClass = resolveClass(configuration, type);
		Discriminator discriminator = null;
		List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
		resultMappings.addAll(additionalResultMappings);
		List<XNode> resultChildren = resultMapNode.getChildren();
		for (XNode resultChild : resultChildren) {
			if ("constructor".equals(resultChild.getName())) {
				processConstructorElement(configuration, builder, resultChild, typeClass, resultMappings);
			} else if ("discriminator".equals(resultChild.getName())) {
				discriminator = processDiscriminatorElement(configuration, builder, resultChild, typeClass, resultMappings);
			} else {
				List<ResultFlag> flags = new ArrayList<ResultFlag>();
				if ("id".equals(resultChild.getName())) {
					flags.add(ResultFlag.ID);
				}
				resultMappings.add(buildResultMappingFromContext(configuration, builder, resultChild, typeClass, flags));
			}
		}
		ResultMapResolver resultMapResolver = new ResultMapResolver(builder, id, typeClass, extend,
				discriminator, resultMappings, autoMapping);
		try {
			return resultMapResolver.resolve();
		} catch (IncompleteElementException e) {
			configuration.addIncompleteResultMap(resultMapResolver);
			throw e;
		}
	}
	
	private void processConstructorElement(Configuration configuration, MapperBuilderAssistant builder, XNode resultChild, Class<?> resultType, List<ResultMapping> resultMappings)
	{
		List<XNode> argChildren = resultChild.getChildren();
		for (XNode argChild : argChildren) {
			List<ResultFlag> flags = new ArrayList<ResultFlag>();
			flags.add(ResultFlag.CONSTRUCTOR);
			if ("idArg".equals(argChild.getName())) {
				flags.add(ResultFlag.ID);
			}
			resultMappings.add(buildResultMappingFromContext(configuration, builder, argChild, resultType, flags));
		}
	}

	private Discriminator processDiscriminatorElement(Configuration configuration, MapperBuilderAssistant builder, XNode context, Class<?> resultType,
		List<ResultMapping> resultMappings) {
		String column = context.getStringAttribute("column");
		String javaType = context.getStringAttribute("javaType");
		String jdbcType = context.getStringAttribute("jdbcType");
		String typeHandler = context.getStringAttribute("typeHandler");
		Class<?> javaTypeClass = resolveClass(configuration, javaType);
		@SuppressWarnings("unchecked")
		Class<? extends TypeHandler<?>> typeHandlerClass = (Class<? extends TypeHandler<?>>) resolveClass(configuration, typeHandler);
		JdbcType jdbcTypeEnum = resolveJdbcType(jdbcType);
		Map<String, String> discriminatorMap = new HashMap<String, String>();
		for (XNode caseChild : context.getChildren()) {
			String value = caseChild.getStringAttribute("value");
			String resultMap = caseChild.getStringAttribute("resultMap",
					processNestedResultMappings(configuration, builder, caseChild, resultMappings));
			discriminatorMap.put(value, resultMap);
		}
		return builder.buildDiscriminator(resultType, column, javaTypeClass, jdbcTypeEnum, typeHandlerClass,
				discriminatorMap);
	}
	
	private ResultMapping buildResultMappingFromContext(Configuration configuration, MapperBuilderAssistant builder, XNode context, Class<?> resultType, List<ResultFlag> flags)
	 {
		String property = context.getStringAttribute("property");
		String column = context.getStringAttribute("column");
		String javaType = context.getStringAttribute("javaType");
		String jdbcType = context.getStringAttribute("jdbcType");
		String nestedSelect = context.getStringAttribute("select");
		String nestedResultMap = context.getStringAttribute("resultMap",
				processNestedResultMappings(configuration, builder, context, Collections.<ResultMapping>emptyList()));
		String notNullColumn = context.getStringAttribute("notNullColumn");
		String columnPrefix = context.getStringAttribute("columnPrefix");
		String typeHandler = context.getStringAttribute("typeHandler");
		String resultSet = context.getStringAttribute("resultSet");
		String foreignColumn = context.getStringAttribute("foreignColumn");
		boolean lazy = "lazy".equals(
				context.getStringAttribute("fetchType", configuration.isLazyLoadingEnabled() ? "lazy" : "eager"));
		Class<?> javaTypeClass = resolveClass(configuration, javaType);
		@SuppressWarnings("unchecked")
		Class<? extends TypeHandler<?>> typeHandlerClass = (Class<? extends TypeHandler<?>>) resolveClass(configuration, typeHandler);
		JdbcType jdbcTypeEnum = resolveJdbcType(jdbcType);
		return builder.buildResultMapping(resultType, property, column, javaTypeClass, jdbcTypeEnum,
				nestedSelect, nestedResultMap, notNullColumn, columnPrefix, typeHandlerClass, flags, resultSet,
				foreignColumn, lazy);
	}
	
	private String processNestedResultMappings(Configuration configuration, MapperBuilderAssistant builder, XNode context, List<ResultMapping> resultMappings) {
		if ("association".equals(context.getName()) || "collection".equals(context.getName())
				|| "case".equals(context.getName())) {
			if (context.getStringAttribute("select") == null) {
				ResultMap resultMap = resultMapElement(configuration, builder, context, resultMappings);
				return resultMap.getId();
			}
		}
		return null;
	}
}

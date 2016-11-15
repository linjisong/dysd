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
import java.util.List;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.dysd.dao.mybatis.schema.SchemaSqlMapperParserDelegate;
import org.dysd.dao.mybatis.schema.statement.StatementHandlerSupport;

/**
 * parameterMap元素处理器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class ParameterMapStatementHandler extends StatementHandlerSupport{

	@Override
	public void handleStatementNode(Configuration configuration, SchemaSqlMapperParserDelegate delegate, XNode node) {
		MapperBuilderAssistant builder = delegate.getBuilderAssistant();
		String id = node.getStringAttribute("id");
		String type = node.getStringAttribute("type");
		Class<?> parameterClass = resolveClass(configuration, type);
		List<XNode> parameterNodes = node.evalNodes("parameter");
		List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
		for (XNode parameterNode : parameterNodes) {
			String property = parameterNode.getStringAttribute("property");
			String javaType = parameterNode.getStringAttribute("javaType");
			String jdbcType = parameterNode.getStringAttribute("jdbcType");
			String resultMap = parameterNode.getStringAttribute("resultMap");
			String mode = parameterNode.getStringAttribute("mode");
			String typeHandler = parameterNode.getStringAttribute("typeHandler");
			Integer numericScale = parameterNode.getIntAttribute("numericScale");
			ParameterMode modeEnum = resolveParameterMode(mode);
			Class<?> javaTypeClass = resolveClass(configuration, javaType);
			JdbcType jdbcTypeEnum = resolveJdbcType(jdbcType);
			@SuppressWarnings("unchecked")
			Class<? extends TypeHandler<?>> typeHandlerClass = (Class<? extends TypeHandler<?>>) resolveClass(
					configuration, typeHandler);
			ParameterMapping parameterMapping = builder.buildParameterMapping(parameterClass, property,
					javaTypeClass, jdbcTypeEnum, resultMap, modeEnum, typeHandlerClass, numericScale);
			parameterMappings.add(parameterMapping);
		}
		builder.addParameterMap(id, parameterClass, parameterMappings);
	}
}

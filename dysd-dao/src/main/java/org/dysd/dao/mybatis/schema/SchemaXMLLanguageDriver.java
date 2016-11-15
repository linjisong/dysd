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

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;
import org.dysd.dao.mybatis.component.impl.MybatisComponents;

/**
 * XSD模式下的XMLLanguageDriver
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class SchemaXMLLanguageDriver extends XMLLanguageDriver {

	/**
	 * 调用工厂类创建参数处理器
	 */
	@Override
	public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject,
			BoundSql boundSql) {
		return MybatisComponents.newParameterHandler(mappedStatement, parameterObject, boundSql);
	}

	/**
	 * 调用工厂类创建脚本级元素解析器，并解析为SqlSource返回
	 */
	@Override
	public SqlSource createSqlSource(Configuration configuration, XNode script, Class<?> parameterType) {
		/**
		 * 如果script是SchemaXNode，则返回SchemaXMLScriptBuilder，否则返回XMLScriptBuilder
		 */
		XMLScriptBuilder builder = MybatisComponents.newXMLScriptBuilder(configuration, script, parameterType);
		return builder.parseScriptNode();
	}
}

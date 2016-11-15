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

import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.dysd.dao.mybatis.schema.SchemaSqlMapperParserDelegate;
import org.dysd.dao.mybatis.schema.statement.StatementHandlerSupport;

/**
 * sql元素处理器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class SqlStatementHandler extends StatementHandlerSupport{

	@Override
	public void handleStatementNode(Configuration configuration, SchemaSqlMapperParserDelegate delegate, XNode node) {
		String databaseId = configuration.getDatabaseId();
		if(databaseId != null){
			sqlElement(configuration, delegate, node, databaseId);
		}
		sqlElement(configuration, delegate, node, null);
	}
	
	private void sqlElement(Configuration configuration, SchemaSqlMapperParserDelegate delegate, XNode context, String requiredDatabaseId)  {
		String databaseId = context.getStringAttribute("databaseId");
		String id = context.getStringAttribute("id");
		id = delegate.getBuilderAssistant().applyCurrentNamespace(id, false);
		if (databaseIdMatchesCurrent(configuration, id, databaseId, requiredDatabaseId)) {
			configuration.getSqlFragments().put(id, context);
		}
	}
	
	private boolean databaseIdMatchesCurrent(Configuration configuration, String id, String databaseId, String requiredDatabaseId) {
		if (requiredDatabaseId != null) {
			if (!requiredDatabaseId.equals(databaseId)) {
				return false;
			}
		} else {
			if (databaseId != null) {
				return false;
			}
			// skip this fragment if there is a previous one with a not null
			// databaseId
			if (configuration.getSqlFragments().containsKey(id)) {
				XNode context = configuration.getSqlFragments().get(id);
				if (context.getStringAttribute("databaseId") != null) {
					return false;
				}
			}
		}
		return true;
	}
}

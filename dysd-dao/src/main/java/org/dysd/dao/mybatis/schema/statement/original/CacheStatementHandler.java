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

import java.util.Properties;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.dysd.dao.mybatis.schema.SchemaSqlMapperParserDelegate;
import org.dysd.dao.mybatis.schema.statement.StatementHandlerSupport;

/**
 * cache元素处理器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class CacheStatementHandler extends StatementHandlerSupport{

	@Override
	public void handleStatementNode(Configuration configuration, SchemaSqlMapperParserDelegate delegate, XNode node) {
		String type = node.getStringAttribute("type", "PERPETUAL");
		Class<? extends Cache> typeClass = resolveAlias(configuration, type);
		String eviction = node.getStringAttribute("eviction", "LRU");
		Class<? extends Cache> evictionClass = resolveAlias(configuration, eviction);
		Long flushInterval = node.getLongAttribute("flushInterval");
		Integer size = node.getIntAttribute("size");
		boolean readWrite = !node.getBooleanAttribute("readOnly", false);
		boolean blocking = node.getBooleanAttribute("blocking", false);
		Properties props = node.getChildrenAsProperties();
		delegate.getBuilderAssistant().useNewCache(typeClass, evictionClass, flushInterval, size, readWrite, blocking, props);
	}
}

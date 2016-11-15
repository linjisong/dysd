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
package org.dysd.dao.mybatis.schema.script.extend;

import java.util.List;

import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;
import org.dysd.dao.DaoUtils;
import org.dysd.dao.mybatis.schema.script.ScriptHandlerSupport;

/**
 * 扩展空间的db元素处理器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class DbScriptHandler extends ScriptHandlerSupport {

	@Override
	public void handleScriptNode(Configuration configuration, XNode node, List<SqlNode> targetContents) {
		if(!DaoUtils.isMatchDatabase(configuration.getDatabaseId(), node.getStringAttribute("type"))){
			List<SqlNode> contents = parseDynamicTags(configuration, node);
			final MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
			targetContents.add(new SqlNode(){
				@Override
				public boolean apply(DynamicContext context) {
					mixedSqlNode.apply(context);
					return true;
				}
			});
		}
	}
}

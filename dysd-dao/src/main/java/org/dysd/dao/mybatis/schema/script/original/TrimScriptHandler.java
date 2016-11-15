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
package org.dysd.dao.mybatis.schema.script.original;

import java.util.List;

import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.TrimSqlNode;
import org.apache.ibatis.session.Configuration;
import org.dysd.dao.mybatis.schema.script.ScriptHandlerSupport;

/**
 * trim元素处理器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class TrimScriptHandler extends ScriptHandlerSupport {

	@Override
	public void handleScriptNode(Configuration configuration, XNode node, List<SqlNode> targetContents) {
		List<SqlNode> contents = parseDynamicTags(configuration, node);
		MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
		String prefix = node.getStringAttribute("prefix");
		String prefixOverrides = node.getStringAttribute("prefixOverrides");
		String suffix = node.getStringAttribute("suffix");
		String suffixOverrides = node.getStringAttribute("suffixOverrides");
		TrimSqlNode trim = new TrimSqlNode(configuration, mixedSqlNode, prefix, prefixOverrides, suffix,
				suffixOverrides);
		targetContents.add(trim);
	}
}
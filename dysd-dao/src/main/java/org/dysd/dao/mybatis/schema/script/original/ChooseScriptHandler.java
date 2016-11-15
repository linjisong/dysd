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

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.xmltags.ChooseSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;
import org.dysd.dao.mybatis.schema.SchemaHandlers;
import org.dysd.dao.mybatis.schema.script.IScriptHandler;

/**
 * choose元素处理器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class ChooseScriptHandler implements IScriptHandler {

	@Override
	public void handleScriptNode(Configuration configuration, XNode node, List<SqlNode> targetContents) {
		List<SqlNode> whenSqlNodes = new ArrayList<SqlNode>();
		List<SqlNode> otherwiseSqlNodes = new ArrayList<SqlNode>();
		handleWhenOtherwiseNodes(configuration, node, whenSqlNodes, otherwiseSqlNodes);
		SqlNode defaultSqlNode = getDefaultSqlNode(otherwiseSqlNodes);
		ChooseSqlNode chooseSqlNode = new ChooseSqlNode(whenSqlNodes, defaultSqlNode);
		targetContents.add(chooseSqlNode);
	}
	
	private void handleWhenOtherwiseNodes(Configuration configuration, XNode chooseSqlNode, List<SqlNode> ifSqlNodes,
			List<SqlNode> defaultSqlNodes) {
		List<XNode> children = chooseSqlNode.getChildren();
		for (XNode child : children) {
			IScriptHandler handler = SchemaHandlers.getScriptHandler(child.getNode());
			if (handler instanceof IfScriptHandler) {
				handler.handleScriptNode(configuration, child, ifSqlNodes);
			} else if (handler instanceof OtherwiseScriptHandler) {
				handler.handleScriptNode(configuration, child, defaultSqlNodes);
			}
		}
	}

	private SqlNode getDefaultSqlNode(List<SqlNode> defaultSqlNodes) {
		SqlNode defaultSqlNode = null;
		if (defaultSqlNodes.size() == 1) {
			defaultSqlNode = defaultSqlNodes.get(0);
		} else if (defaultSqlNodes.size() > 1) {
			throw new BuilderException("Too many default (otherwise) elements in choose statement.");
		}
		return defaultSqlNode;
	}
}
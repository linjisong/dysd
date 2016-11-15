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
package org.dysd.dao.mybatis.schema.script;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;
import org.dysd.dao.mybatis.schema.SchemaHandlers;
import org.dysd.dao.mybatis.schema.SchemaXNode;
import org.dysd.dao.mybatis.schema.function.SqlConfigFunctionParser;
import org.dysd.dao.mybatis.schema.script.extend.ExpressionTextSqlNode;
import org.dysd.dao.mybatis.schema.script.sqlsource.DynamicSqlSource;
import org.dysd.dao.mybatis.schema.script.sqlsource.RawSqlSource;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XSD模式下的XMLScriptBuilder，是语句级元素处理器、SQL配置函数、表达式处理器的入口
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class SchemaXMLScriptBuilder extends XMLScriptBuilder {

	private XNode context;
	private Class<?> parameterType;
	private static final ThreadLocal<Boolean> dynamic = new ThreadLocal<Boolean>();
	
	/**
	 * 构造函数
	 * @param configuration
	 * @param context
	 * @param parameterType
	 */
	public SchemaXMLScriptBuilder(Configuration configuration, XNode context, Class<?> parameterType) {
		super(configuration, context, parameterType);
		this.context = context;
		this.parameterType = parameterType;
	}

	/**
	 * 解析语句级元素
	 */
	@Override
	public SqlSource parseScriptNode() {
		if(context instanceof SchemaXNode){
			setDynamic(false);
			List<SqlNode> contents = parseDynamicTags(configuration, context);
			MixedSqlNode rootSqlNode = new MixedSqlNode(contents);
			SqlSource sqlSource = null;
			if (isDynamic()) {
				sqlSource = new DynamicSqlSource(configuration, rootSqlNode);
			} else {
				sqlSource = new RawSqlSource(configuration, rootSqlNode, parameterType);
			}
			return sqlSource;
		}else{
			return super.parseScriptNode();
		}
	}

	/**
	 * 解析动态脚本元素
	 * @param configuration
	 * @param node
	 * @return
	 */
	public static List<SqlNode> parseDynamicTags(Configuration configuration, XNode node) {
		List<SqlNode> contents = new ArrayList<SqlNode>();
		NodeList children = node.getNode().getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			XNode child = node.newXNode(children.item(i));
			short nodeType = child.getNode().getNodeType();
			if (nodeType == Node.CDATA_SECTION_NODE || nodeType == Node.TEXT_NODE) {
				String data = child.getStringBody("");
				//对SQL文本进行装饰，从而嵌入SQL配置函数的处理
				data = decorate(configuration.getDatabaseId(), data);
				//使用表达式SQL文本，从而具有处理表达式的能力
				ExpressionTextSqlNode expressionTextSqlNode = new ExpressionTextSqlNode(data);
				if (expressionTextSqlNode.isDynamic()) {
					contents.add(expressionTextSqlNode);
					setDynamic(true);
				} else {
					contents.add(new StaticTextSqlNode(data));
				}
			} else if (nodeType == Node.ELEMENT_NODE) { // issue #628
				//使用可注册的处理器机制，从而可以方便、自由地扩展
				IScriptHandler handler = SchemaHandlers.getScriptHandler(child.getNode());
				if (handler == null) {
					throw new BuilderException("Unknown element <" + child.getNode().getNodeName() + "> in SQL statement.");
				}
				handler.handleScriptNode(configuration, child, contents);
				setDynamic(true);
			}
		}
		return contents;
	}
	
	/**
	 * 使用SQL配置函数装饰SQL语句
	 * @param databaseId
	 * @param context
	 * @return
	 */
	protected static String decorate(String databaseId, String context){
		return SqlConfigFunctionParser.evalSqlConfigFunction(databaseId, context);
	}
	
	private static void setDynamic(boolean dynamic){
		SchemaXMLScriptBuilder.dynamic.set(dynamic);
	}
	
	private static boolean isDynamic(){
		Boolean b = dynamic.get();
		return null != b && b.booleanValue();
	}
}
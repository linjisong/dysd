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

import java.util.Collection;
import java.util.Iterator;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.CacheRefResolver;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.ResultMapResolver;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.dysd.dao.mybatis.schema.context.ISqlMapperParserContext;
import org.dysd.dao.mybatis.schema.statement.IStatementHandler;
import org.dysd.util.Tool;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XSD模式的解析代理类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class SchemaSqlMapperParserDelegate {

	private final ISqlMapperParserContext parserContext;
	private final Document document;
	private final Resource resource;
	
	private final Configuration configuration;
	private final String location;
	private final XPathParser parser;
	private final MapperBuilderAssistant builderAssistant;
	
	/**
	 * 构造函数
	 * @param parserContext 解析上下文环境
	 * @param document      被解析的文档
	 * @param resource      被解析的资源
	 */
	public SchemaSqlMapperParserDelegate(ISqlMapperParserContext parserContext, Document document, Resource resource){
		this.parserContext = parserContext;
		this.document = document;
		this.resource = resource;
		this.configuration = parserContext.getConfiguration();
		this.location = resource.getDescription();
		this.parser = new XPathParser(document, false, configuration.getVariables(), null);
		this.builderAssistant = new MapperBuilderAssistant(parserContext.getConfiguration(), location);
	}
	
	/**
	 * 执行解析
	 */
	public void parse() {
		if (!configuration.isResourceLoaded(location)) {
			try {
				Element root = document.getDocumentElement();
				String namespace = root.getAttribute("namespace");
				if (Tool.CHECK.isBlank(namespace)) {
					throw new BuilderException("Mapper's namespace cannot be empty");
				}
				builderAssistant.setCurrentNamespace(namespace);
				doParseStatements(root);
			} catch (Exception e) {
				throw new BuilderException("Error parsing Mapper XML["+location+"]. Cause: " + e, e);
			}
			
			configuration.addLoadedResource(location);
			bindMapperForNamespace();
		}
		doParsePendings();
	}
	
	/**
	 * 解析包含statements及其相同级别的元素[cache|cache-ref|parameterMap|resultMap|sql|select|insert|update|delete]等
	 * @param parent
	 */
	public void doParseStatements(Node parent) {
		NodeList nl = parent.getChildNodes();
		for (int i = 0, l = nl.getLength(); i < l; i++) {
			Node node = nl.item(i);
			if (!(node instanceof Element)) {
				continue;
			}
			doParseStatement(node);
		}
	}
	
	/**
	 * 解析一个和statement同级别的元素
	 * @param node
	 */
	public void doParseStatement(Node node) {
		IStatementHandler handler = SchemaHandlers.getStatementHandler(node);
		if (null == handler) {
			throw new BuilderException("Unknown statement element <" + getDescription(node) + "> in SqlMapper ["+location+"].");
		} else {
			SchemaXNode context = new SchemaXNode(parser, node, configuration.getVariables());
			handler.handleStatementNode(configuration, this, context);
		}
	}

	public ISqlMapperParserContext getParserContext() {
		return parserContext;
	}

	public Document getDocument() {
		return document;
	}

	public Resource getResource() {
		return resource;
	}

	public String getLocation() {
		return location;
	}

	public XPathParser getParser() {
		return parser;
	}

	public MapperBuilderAssistant getBuilderAssistant() {
		return builderAssistant;
	}
	
	/**
	 * 再次尝试解析之前未完成的结果/缓存/语句
	 */
	public void doParsePendings(){
		parsePendingResultMaps();
		parsePendingChacheRefs();
		parsePendingStatements();
	}

	private void parsePendingResultMaps() {
		Collection<ResultMapResolver> incompleteResultMaps = configuration.getIncompleteResultMaps();
		synchronized (incompleteResultMaps) {
			Iterator<ResultMapResolver> iter = incompleteResultMaps.iterator();
			while (iter.hasNext()) {
				try {
					iter.next().resolve();
					iter.remove();
				} catch (IncompleteElementException e) {
					// ResultMap is still missing a resource...
				}
			}
		}
	}

	private void parsePendingChacheRefs() {
		Collection<CacheRefResolver> incompleteCacheRefs = configuration.getIncompleteCacheRefs();
		synchronized (incompleteCacheRefs) {
			Iterator<CacheRefResolver> iter = incompleteCacheRefs.iterator();
			while (iter.hasNext()) {
				try {
					iter.next().resolveCacheRef();
					iter.remove();
				} catch (IncompleteElementException e) {
					// Cache ref is still missing a resource...
				}
			}
		}
	}

	private void parsePendingStatements() {
		Collection<XMLStatementBuilder> incompleteStatements = configuration.getIncompleteStatements();
		synchronized (incompleteStatements) {
			Iterator<XMLStatementBuilder> iter = incompleteStatements.iterator();
			while (iter.hasNext()) {
				try {
					iter.next().parseStatementNode();
					iter.remove();
				} catch (IncompleteElementException e) {
					// Statement is still missing a resource...
				}
			}
		}
	}
	
	private void bindMapperForNamespace() {
		String namespace = builderAssistant.getCurrentNamespace();
		if (namespace != null) {
			Class<?> boundType = null;
			try {
				boundType = Resources.classForName(namespace);
			} catch (ClassNotFoundException e) {
				// ignore, bound type is not required
			}
			if (boundType != null) {
				if (!configuration.hasMapper(boundType)) {
					// Spring may not know the real resource name so we set a
					// flag
					// to prevent loading again this resource from the mapper
					// interface
					// look at MapperAnnotationBuilder#loadXmlResource
					configuration.addLoadedResource("namespace:" + namespace);
					configuration.addMapper(boundType);
				}
			}
		}
	}

	private String getDescription(Node ele) {
		StringBuffer sb = new StringBuffer();
		if (!Tool.CHECK.isBlank(ele.getNamespaceURI())) {
			sb.append("[").append(ele.getNamespaceURI()).append("]");
		}
		sb.append(ele.getLocalName());
		return sb.toString();
	}
}

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
package org.dysd.util.xml.parser;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.dysd.util.Tool;
import org.dysd.util.logger.CommonLogger;
import org.dysd.util.xml.XmlHelper;
import org.dysd.util.xml.context.IXmlParserContext;
import org.springframework.core.io.Resource;
import org.w3c.dom.Node;

/**
 * XML解析代理类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class XmlParserDelegate{
	
	private static final String[] defaultNamespaceMappingsLocation = {"classpath*:**/dysd-*-namespaces.ini"};
	
	@SuppressWarnings("rawtypes")
	private final ThreadLocal<Map<String, INamespaceParser>> parsers = new ThreadLocal<Map<String, INamespaceParser>>();
	
	private final String[] namespaceMappingsLocation;

	private volatile INIConfiguration config;
	
	public XmlParserDelegate() {
		this(defaultNamespaceMappingsLocation);
	}
	
	public XmlParserDelegate(String[] namespaceMappingsLocations) {
		if(null == namespaceMappingsLocations || 0 == namespaceMappingsLocations.length){
			this.namespaceMappingsLocation = defaultNamespaceMappingsLocation;
		}else{
			this.namespaceMappingsLocation = namespaceMappingsLocations;	
		}
	}

	/**
	 * 获取命名空间对应的XSD文件路径配置
	 * @param namespaceUri
	 * @return
	 */
	public String getNamespaceSchema(String namespaceUri){
		return getConfig(namespaceUri, "schema");
	}
	
	/**
	 * 获取命名空间对应的解析器实现类配置
	 * @param namespaceUri
	 * @return
	 */
	public String getNamespaceParser(String namespaceUri){
		return getConfig(namespaceUri, "parser");
	}
	
	/**
	 * 获取XML节点所在命名空间的解析器
	 * @param node
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <E extends IXmlParserContext>INamespaceParser<E> getNamespaceParser(Node node){
		String uri = XmlHelper.getNamespaceURI(node);
		if(null == uri){
			return null;
		}else {
			Map<String, INamespaceParser> pss = parsers.get();
			if(null == pss){
				pss = new HashMap<String, INamespaceParser>();
				parsers.set(pss);
			}
			INamespaceParser<E> parser = pss.get(uri);
			if(null == parser){
				String parserCls = getNamespaceParser(uri);
				if(null == parserCls){
					CommonLogger.warn("not found the namespace parser ["+ uri+"]");
					return null;
				}
				parser = Tool.REFLECT.newInstance(parserCls, INamespaceParser.class);
				parser.init();
				CommonLogger.info("parse the namespace ["+ uri+"] using parser ["+ parserCls+"]");
				pss.put(uri, parser);
			}
			return parser;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void onFinishParse() {
		Map<String, INamespaceParser> pss = parsers.get();
		if(null != pss){
			for(INamespaceParser parser : pss.values()){
				parser.destory();
			}
		}
	}
	
	private String getConfig(String namespaceUri, String key){
		if(namespaceUri.endsWith(".xsd")){
			namespaceUri = namespaceUri.substring(0, namespaceUri.length()-4);
		}
		initConfig();
		SubnodeConfiguration section = null == this.config ? null : this.config.getSection(namespaceUri);
		String value = section == null ? null : section.getString(key);
		return value;
	}
	
 	private void initConfig() {
		if (this.config == null) {
			synchronized (this) {
				if (this.config == null) {
					try {
						Set<Resource> resources = Tool.IO.getResources(namespaceMappingsLocation);
						INIConfiguration config = new INIConfiguration();
						for (Resource resource : resources) {
							config.read(new InputStreamReader(resource.getInputStream()));
						}
						this.config = config;
					} catch (Exception ex) {
						throw new IllegalStateException(
								"Unable to load ini config from location [" + this.namespaceMappingsLocation + "]", ex);
					}
				}
			}
		}
	}
}

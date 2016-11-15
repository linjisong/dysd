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
package org.dysd.util.xml.factory;

import org.dysd.util.parser.IParser;
import org.dysd.util.xml.context.IXmlParserContext;
import org.dysd.util.xml.parser.INamespaceParser;
import org.dysd.util.xml.parser.XmlParserDelegate;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

/**
 * XML组件工厂接口
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface IXmlComponentFactory {

	/**
	 * 创建XML解析环境
	 * @return
	 */
	public IXmlParserContext newXmlParserContext();
	
	/**
	 * 创建XML解析器
	 * @return
	 */
	public <E extends IXmlParserContext>IParser<E> newXmlParser();
	
	/**
	 * 使用指定命名空间解析器创建XML解析器
	 * @param parser
	 * @return
	 */
	public <E extends IXmlParserContext>IParser<E> newXmlParser(INamespaceParser<E> parser);

	/**
	 * 创建文档加载器
	 * @return
	 */
	public DocumentLoader newDocumentLoader();
	
	/**
	 * 创建XML解析代理
	 * @return
	 */
	public XmlParserDelegate newDelegate();
	
	/**
	 * 创建实体解析器
	 * @return
	 */
	public EntityResolver newEntityResolver();
	
	/**
	 * 创建错误处理器
	 * @return
	 */
	public ErrorHandler newErrorHandler();
}

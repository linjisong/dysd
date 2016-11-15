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
package org.dysd.util.xml.context;

import org.dysd.util.parser.IParserContext;
import org.dysd.util.xml.parser.XmlParserDelegate;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

/**
 * XML文件解析环境
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface IXmlParserContext extends IParserContext{
	
	/**
	 * 获取文档加载器
	 * @return
	 */
	public DocumentLoader getDocumentLoader();
	
	/**
	 * 获取实体解析器
	 * @return
	 */
	public EntityResolver getEntityResolver();
	
	/**
	 * 获取错误处理器
	 * @return
	 */
	public ErrorHandler getErrorHandler();
	
	/**
	 * 获取解析代理
	 * @return
	 */
	public XmlParserDelegate getDelegate();
	
}

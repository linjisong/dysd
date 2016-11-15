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

import org.dysd.util.xml.context.IXmlParserContext;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

/**
 * XML命名空间解析器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface INamespaceParser<E extends IXmlParserContext>{

	/**
	 * 初始化
	 */
	public void init();
	
	/**
	 * 执行解析
	 * @param parserContext 解析环境
	 * @param document      文档对象
	 * @param resource      和文档对象对应的资源对象
	 */
	public void parse(E parserContext, Document document, Resource resource);
	
	/**
	 * 结束
	 */
	public void destory();
}

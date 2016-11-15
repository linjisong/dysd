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

import org.dysd.util.parser.IParser;
import org.dysd.util.xml.context.IXmlParserContext;
import org.dysd.util.xml.factory.IXmlComponentFactory;
import org.dysd.util.xml.factory.impl.XmlComponentFactory;
import org.springframework.core.io.Resource;

/**
 * XML解析帮助类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class XmlParserUtils {
	
	private static IXmlComponentFactory factory = new XmlComponentFactory();
	
	/**
	 * 获取XML解析组件工厂类
	 * @return
	 */
	public static IXmlComponentFactory getFactory(){
		return factory;
	}
	
	/**
	 * 设置XML解析组件工厂类
	 * @param factory
	 */
	public void setFactory(IXmlComponentFactory factory){
		if(null != factory){
			XmlParserUtils.factory = factory;
		}
	}
	
	/**
	 * 解析XML文件
	 * @param locationPattern
	 */
	public static void parseXml(String locationPattern){
		parseXml(new String[]{locationPattern});
	}
	
	/**
	 * 使用指定命名空间解析器来解析XML文件
	 * @param parser
	 * @param locationPattern
	 */
	public static void parseXml(INamespaceParser<IXmlParserContext> parser, String locationPattern){
		parseXml(parser, new String[]{locationPattern});
	}
	
	/**
	 * 在预设的解析环境中解析XML文件
	 * @param context
	 * @param locationPattern
	 */
	public static void parseXml(IXmlParserContext context, String locationPattern){
		parseXml(context, new String[]{locationPattern});
	}
	
	/**
	 * 解析XML文件
	 * @param locationPatterns
	 */
	public static void parseXml(String[] locationPatterns){
		parseXml((INamespaceParser<IXmlParserContext>)null, locationPatterns);
	}
	
	/**
	 * 使用指定命名空间解析器来解析XML文件
	 * @param parser
	 * @param locationPatterns
	 */
	public static void parseXml(INamespaceParser<IXmlParserContext> parser, String[] locationPatterns){
		IXmlParserContext parserContext = factory.newXmlParserContext();
		IParser<IXmlParserContext> xp = factory.newXmlParser(parser);
		xp.parse(parserContext, locationPatterns);
	}
	
	/**
	 * 在预设的解析环境中解析XML文件
	 * @param context
	 * @param locationPatterns
	 */
	public static void parseXml(IXmlParserContext context, String[] locationPatterns){
		IParser<IXmlParserContext> xp = factory.newXmlParser();
		xp.parse(context, locationPatterns);
	}
	
	/**
	 * 解析XML文件
	 * @param resource
	 */
	public static void parseXml(Resource resource){
		parseXml((INamespaceParser<IXmlParserContext>)null, resource);
	}
	
	/**
	 * 使用指定命名空间解析器来解析XML文件
	 * @param parser
	 * @param resource
	 */
	public static void parseXml(INamespaceParser<IXmlParserContext> parser, Resource resource){
		IXmlParserContext parserContext = factory.newXmlParserContext();
		IParser<IXmlParserContext> xp = factory.newXmlParser(parser);
		xp.parse(parserContext, resource);
	}
	
	/**
	 * 在预设的解析环境中解析XML文件
	 * @param context
	 * @param resource
	 */
	public static void parseXml(IXmlParserContext context, Resource resource){
		IParser<IXmlParserContext> xp = factory.newXmlParser();
		xp.parse(context, resource);
	}
}

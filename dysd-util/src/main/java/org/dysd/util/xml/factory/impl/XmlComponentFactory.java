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
package org.dysd.util.xml.factory.impl;

import org.dysd.util.parser.IParser;
import org.dysd.util.xml.BaseErrorHandler;
import org.dysd.util.xml.IniConfigEntityResolver;
import org.dysd.util.xml.context.IXmlParserContext;
import org.dysd.util.xml.context.impl.XmlParserContext;
import org.dysd.util.xml.factory.IXmlComponentFactory;
import org.dysd.util.xml.parser.INamespaceParser;
import org.dysd.util.xml.parser.XmlParserDelegate;
import org.dysd.util.xml.parser.impl.CallbackXmlParser;
import org.dysd.util.xml.parser.impl.PluggableXmlParser;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

/**
 * XML组件工厂
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class XmlComponentFactory implements IXmlComponentFactory{

	@Override
	public IXmlParserContext newXmlParserContext(){
		return new XmlParserContext();
	}
	
	@Override
	public <E extends IXmlParserContext>IParser<E> newXmlParser(){
		return newXmlParser(null);
	}
	
	@Override
	public <E extends IXmlParserContext>IParser<E> newXmlParser(INamespaceParser<E> parser){
		if(null != parser){
			return new CallbackXmlParser<E>(parser);
		}else{
			return new PluggableXmlParser<E>();
		}
	}
	
	@Override
	public DocumentLoader newDocumentLoader(){
		return new DefaultDocumentLoader();
	}
	
	@Override
	public XmlParserDelegate newDelegate(){
		return new XmlParserDelegate();
	}
	
	@Override
	public EntityResolver newEntityResolver() {
		return new IniConfigEntityResolver();
	}

	@Override
	public ErrorHandler newErrorHandler() {
		return new BaseErrorHandler();
	}
}

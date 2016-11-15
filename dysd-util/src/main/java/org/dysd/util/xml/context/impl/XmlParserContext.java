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
package org.dysd.util.xml.context.impl;

import org.dysd.util.parser.impl.ParserContext;
import org.dysd.util.xml.context.IXmlParserContext;
import org.dysd.util.xml.parser.XmlParserDelegate;
import org.dysd.util.xml.parser.XmlParserUtils;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

public class XmlParserContext extends ParserContext implements IXmlParserContext{

	private DocumentLoader documentLoader;

	private EntityResolver entityResolver;

	private ErrorHandler errorHandler;
	
	private XmlParserDelegate delegate;
	
	@Override
	public DocumentLoader getDocumentLoader() {
		if(null == this.documentLoader){
			this.documentLoader = XmlParserUtils.getFactory().newDocumentLoader();
		}
		return this.documentLoader;
	}

	@Override
	public EntityResolver getEntityResolver() {
		if(null == this.entityResolver){
			this.entityResolver = XmlParserUtils.getFactory().newEntityResolver();
		}
		return this.entityResolver;
	}

	@Override
	public ErrorHandler getErrorHandler() {
		if(null == this.errorHandler){
			this.errorHandler = XmlParserUtils.getFactory().newErrorHandler();
		}
		return this.errorHandler;
	}
	
	@Override
	public XmlParserDelegate getDelegate() {
		if(null == this.delegate){
			this.delegate = XmlParserUtils.getFactory().newDelegate();
		}
		return delegate;
	}

	public void setDocumentLoader(DocumentLoader documentLoader) {
		this.documentLoader = documentLoader;
	}

	public void setEntityResolver(EntityResolver entityResolver) {
		this.entityResolver = entityResolver;
	}

	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	public void setDelegate(XmlParserDelegate delegate) {
		this.delegate = delegate;
	}
}

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
package org.dysd.util.xml.parser.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.dysd.util.Tool;
import org.dysd.util.exception.ExceptionCodes;
import org.dysd.util.exception.Throw;
import org.dysd.util.logger.CommonLogger;
import org.dysd.util.parser.impl.AbstractParser;
import org.dysd.util.xml.context.IXmlParserContext;
import org.dysd.util.xml.parser.INamespaceParser;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 抽象的XML解析实现类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class AbstractXmlParser<E extends IXmlParserContext> extends AbstractParser<E> {
	
	private static final String PROFILE_ATTRIBUTE = "profile";
	
	private static final String MULTI_VALUE_ATTRIBUTE_DELIMITERS = ",; ";
	
	private final XmlValidationModeDetector validationModeDetector = new XmlValidationModeDetector();

	/**
	 * 执行解析，实现父类中抽象方法
	 */
	@Override
	protected void doParse(E parserContext, EncodedResource resource)throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = resource.getResource().getInputStream();
			InputSource inputSource = new InputSource(inputStream);
			if (resource.getEncoding() != null) {
				inputSource.setEncoding(resource.getEncoding());
			}
			doParse(parserContext, inputSource, resource.getResource());
		} finally {
			Tool.IO.closeQuietly(inputStream);
		}
	}
	
	protected void afterBuildDocument(E parserContext, Document document, Resource resource){
		
	}
	
	/**
	 * 解析文档
	 * @param parserContext
	 * @param document
	 * @param resource
	 */
	protected abstract void parseDocument(E parserContext, Document document, Resource resource);
	
	/**
	 * 获取节点所在命名空间的解析器
	 * @param node
	 * @return
	 */
	protected INamespaceParser<E> getNamespaceParser(Node node){
		E parserContext = getParserContext();
		if(null != parserContext && null != parserContext.getDelegate()){
			return parserContext.getDelegate().getNamespaceParser(node);
		}
		return null;
	}
	
	/**
	 * 执行解析，根据根元素的profile属性判断环境是否匹配，不匹配则终止解析
	 * @param parserContext
	 * @param inputSource
	 * @param resource
	 */
	private void doParse(E parserContext, InputSource inputSource, Resource resource){
		try {
			Document doc = doLoadDocument(parserContext, inputSource, resource);
			Element root = doc.getDocumentElement();
			Environment environment = extractEnvironment(parserContext);
			
			String profileSpec = root.getAttribute(PROFILE_ATTRIBUTE);
			if (null != environment && !Tool.CHECK.isBlank(profileSpec)) {
				String[] specifiedProfiles = StringUtils.tokenizeToStringArray(profileSpec, MULTI_VALUE_ATTRIBUTE_DELIMITERS);
				if (!environment.acceptsProfiles(specifiedProfiles)) {
					CommonLogger.info("Skipped XML file due to specified profiles [" + profileSpec + "] not matching: " + resource);
					return;
				}
			}
			afterBuildDocument(parserContext, doc, resource);
			parseDocument(parserContext, doc, resource);
		}catch (SAXParseException ex) {
			Throw.throwException(ExceptionCodes.DYSD010009, ex,"Line " + ex.getLineNumber() + " in XML document from " + resource.getDescription() + " is invalid");
		}
		catch (SAXException ex) {
			Throw.throwException(ExceptionCodes.DYSD010009, ex,"XML document from " + resource.getDescription() + " is invalid");
		}
		catch (ParserConfigurationException ex) {
			Throw.throwException(ExceptionCodes.DYSD010009, ex,"Parser configuration exception parsing XML from " + resource.getDescription());
		}
		catch (IOException ex) {
			Throw.throwException(ExceptionCodes.DYSD010009, ex,"IOException parsing XML document from " + resource.getDescription());
		}
		catch (Throwable ex) {
			Throw.throwException(ExceptionCodes.DYSD010009, ex, "Unexpected exception parsing XML document from " + resource.getDescription());
		}
	}
	
	/**
	 * 加载文档
	 * @param parserContext
	 * @param inputSource
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	private Document doLoadDocument(E parserContext, InputSource inputSource, Resource resource) throws Exception {
		DocumentLoader loader = extractDocumentLoader(parserContext);
		if(null == loader){
			Throw.createException(ExceptionCodes.DYSD010009,"DocumentLoader is null when parsing XML document from " + resource.getDescription());
		}
		EntityResolver entityResolver = extractEntityResolver(parserContext);
		ErrorHandler errorHandler = extractErrorHandler(parserContext);
		int validationMode = detectValidationMode(resource);
		// 如果是XSD，加载器会强制设置namespaceAware为true
		Document document = loader.loadDocument(inputSource, entityResolver, errorHandler, validationMode, false);
		return document;
	}

	protected ErrorHandler extractErrorHandler(E parserContext) {
		ErrorHandler errorHandler = parserContext.getErrorHandler();
		return errorHandler;
	}

	protected EntityResolver extractEntityResolver(E parserContext) {
		EntityResolver entityResolver = parserContext.getEntityResolver();
		return entityResolver;
	}

	protected DocumentLoader extractDocumentLoader(E parserContext) {
		return parserContext.getDocumentLoader();
	}
	
	/**
	 * 侦测校验模式
	 * @param resource
	 * @return
	 */
	private int detectValidationMode(Resource resource) {
		if (resource.isOpen()) {
			throw Throw.createException(ExceptionCodes.DYSD010009,
					"Passed-in Resource [" + resource + "] contains an open stream: " +
					"cannot determine validation mode automatically.");
		}

		InputStream inputStream;
		try {
			inputStream = resource.getInputStream();
		}
		catch (IOException ex) {
			throw Throw.createException(ExceptionCodes.DYSD010009, ex,
					"Unable to determine validation mode for [" + resource + "]: cannot open InputStream.");
		}

		try {
			return validationModeDetector.detectValidationMode(inputStream);
		}
		catch (IOException ex) {
			throw Throw.createException(ExceptionCodes.DYSD010009, ex, "Unable to determine validation mode for [" + resource + "]: an error occurred whilst reading from the InputStream.");
		}
	}
}

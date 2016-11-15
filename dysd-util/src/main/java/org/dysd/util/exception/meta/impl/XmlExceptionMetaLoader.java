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
package org.dysd.util.exception.meta.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dysd.util.Tool;
import org.dysd.util.exception.level.ExceptionLevel;
import org.dysd.util.exception.meta.IExceptionMeta;
import org.dysd.util.logger.CommonLogger;
import org.dysd.util.sync.impl.SingleonRunStatus;
import org.dysd.util.xml.context.IXmlParserContext;
import org.dysd.util.xml.parser.INamespaceParser;
import org.dysd.util.xml.parser.XmlParserUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 使用XML存储的异常元信息加载器实现类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class XmlExceptionMetaLoader extends AbstractExceptionMetaLoader implements INamespaceParser<IXmlParserContext>, NamespaceHandler{

	/**
	 * 是否加载的标志
	 */
	private static final SingleonRunStatus run = SingleonRunStatus.newInstance("ExceptionMetaLoader");
	
	private final Map<String, Class<? extends Throwable>> classCache = new HashMap<String, Class<? extends Throwable>>();
	
	/**
	 * 查找元信息，覆盖父类查找方法，确保查找之前已经加载
	 */
	@Override
	public IExceptionMeta lookup(String code, Throwable cause) {
		load();
		return super.lookup(code, cause);
	}
	
	/**
	 * 加载配置
	 */
	protected void load(){
		if(run.start()){
			try{
				String location = "classpath*:**/dysd-exception.xml";
				XmlParserUtils.parseXml(this, location);
				run.success();
			}catch(Exception e){
				run.failure();
			}
		}
	}
	
	@Override
	public void init() {
	}

	@Override
	public void parse(IXmlParserContext parserContext, Document document, Resource resource) {
		doParse(document.getDocumentElement());
	}

	@Override
	public void destory() {
		classCache.clear();
	}
	
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		doParse(element);
		return null;
	}

	@Override
	public BeanDefinitionHolder decorate(Node source, BeanDefinitionHolder definition, ParserContext parserContext) {
		return null;
	}
	
	private void doParse(Element root) {
		if(nodeNameEquals(root, "exception-config")){
			NodeList childNodes = root.getChildNodes();
			if(null != childNodes && childNodes.getLength() >= 1){
				for(int i = 0, l = childNodes.getLength(); i < l; i++){
					Node node = childNodes.item(i);
					if((node instanceof Element) && nodeNameEquals(node, "exception")){
						loadExceptionElement(null, (Element)node, classCache);
					}
				}
			}
		}else if(nodeNameEquals(root, "exception")){
			loadExceptionElement(null, root, classCache);
		}
	}

	/**
	 * 加载exception元素
	 * @param parentCode
	 * @param element
	 * @param classCache
	 */
	private void loadExceptionElement(String parentCode, Element element, Map<String, Class<? extends Throwable>> classCache){
		List<Element> children = new ArrayList<Element>();
		NodeList childNodes = element.getChildNodes();
		if(null != childNodes && childNodes.getLength() >= 1){
			for(int i = 0, l = childNodes.getLength(); i < l; i++){
				Node node = childNodes.item(i);
				if((node instanceof Element) && nodeNameEquals(node, "exception")){
					children.add((Element)node);
				}
			}
		}
		
		String code = element.getAttribute("code");
		if(Tool.CHECK.isBlank(code)){
			if(children.isEmpty()){//没有code，同时没有子配置
				throw new IllegalArgumentException("exception code is blank, please check the exception config... ");
			}else{
				code = generateExceptionCode();
			}
		}else{
			validateExceptionCode(code);
		}
		
		ExceptionMeta meta = new ExceptionMeta();
		meta.setCode(code);
		meta.setParentCode(parentCode);
		
		String description = element.getAttribute("description");
		if(!Tool.CHECK.isBlank(description)){
			meta.setDescription(description);
		}
		
		loadMetaProperties(meta, element, classCache);
		cacheMetaByCode(code, meta);
		if(null != meta.getCauses() && !meta.getCauses().isEmpty()){
			for(Class<?> cls : meta.getCauses()){
				cacheMetaByType(cls, meta);
			}
		}
		super.logExceptionMeta(meta);
		if(!children.isEmpty()){
			for(Element e : children){
				loadExceptionElement(code, e, classCache);
			}
		}
	}
	
	/**
	 * 加载exception的属性
	 * @param meta
	 * @param element
	 * @param classCache
	 */
	private void loadMetaProperties(ExceptionMeta meta, Element element, Map<String, Class<? extends Throwable>> classCache){
		String localeKey = element.getAttribute("localeKey");
		if(!Tool.CHECK.isBlank(localeKey)){
			meta.setLocaleKey(localeKey.trim());
		}
		
		String level = element.getAttribute("level");
		if(!Tool.CHECK.isBlank(level)){
			meta.setLevel(ExceptionLevel.instance(level.trim()));
		}
		
		String causes = element.getAttribute("causes");
		if(!Tool.CHECK.isBlank(causes)){
			String[] hs = causes.trim().split("\\s+");
			if(null != hs && hs.length >= 1){
				Set<Class<? extends Throwable>> ehs = new LinkedHashSet<Class<? extends Throwable>>();
				for(String h : hs){
					Class<? extends Throwable> t = getCauseClass(h, classCache);
					if(null != t){
						ehs.add(t);
					}
				}
				meta.setCauses(ehs);
			}
		}
	}
	
	/**
	 * 获取异常原因
	 * @param cause
	 * @param classCache
	 * @return
	 */
	private Class<? extends Throwable> getCauseClass(String cause, Map<String, Class<? extends Throwable>> classCache){
		if(classCache.containsKey(cause)){
			return classCache.get(cause);
		}else{
			try{
				Class<?> h = Tool.REFLECT.forName(cause);
				if(Throwable.class.isAssignableFrom(h)){
					@SuppressWarnings("unchecked")
					Class<? extends Throwable> rs = (Class<? extends Throwable>)h;
					classCache.put(cause, rs);
					return rs;
				}else{
					CommonLogger.error(" the exception config error, the cause is not a Throwable : " + cause);
					return null;
				}
			}catch(Exception e){
				CommonLogger.error(" the exception config error, the cause can not instance : " + cause);
				return null;
			}
		}
	}
	
	/**
	 * 判断元素名称是否相同
	 * @param node
	 * @param desiredName
	 * @return
	 */
	private boolean nodeNameEquals(Node node, String desiredName) {
		return desiredName.equals(node.getNodeName()) || desiredName.equals(node.getLocalName());
	}
}

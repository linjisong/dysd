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
package org.dysd.util.xml;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.dysd.util.Tool;
import org.dysd.util.exception.ExceptionCodes;
import org.dysd.util.exception.Throw;
import org.dysd.util.xml.parser.XmlParserDelegate;
import org.dysd.util.xml.parser.XmlParserUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 根据Ini配置读取XSD的实体解析器，也可手工添加命名空间和XSD文件对应的Resource的对应关系
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class IniConfigEntityResolver implements EntityResolver{
	
	private final Map<String, Resource> resources = new HashMap<String, Resource>();
	
	private XmlParserDelegate iniHelper = XmlParserUtils.getFactory().newDelegate();
	
	@Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
		if(Tool.CHECK.isBlank(systemId)){
			return null;
		}
		
		Resource resource = resources.get(systemId);
		if(null != resource){
			return resolveInputSource(publicId, systemId, resource);
		}else{
			String resourceLocation = iniHelper.getNamespaceSchema(systemId);
			if (resourceLocation != null) {
				return resolveInputSource(publicId, systemId, new ClassPathResource(resourceLocation));
			}
		}
		return null;
	}

	private InputSource resolveInputSource(String publicId, String systemId, Resource resource) throws IOException {
		InputSource source = new InputSource(resource.getInputStream());
		source.setPublicId(publicId);
		source.setSystemId(systemId);
		return source;
	}
	
	public void addResource(String systemId, String location){
		if(null != location && null != systemId){
			Resource resource = Tool.IO.getResource(location);
			if(null != resource && resource.exists()){
				this.resources.put(systemId, resource);
			}else{
				Throw.throwException(ExceptionCodes.DYSD010009,"the resource ["+ location+"] is not exist.");
			}
		}
	}

	public void setResources(Map<String, Resource> resources){
		if(null != resources){
			this.resources.putAll(resources);
		}
	}
}

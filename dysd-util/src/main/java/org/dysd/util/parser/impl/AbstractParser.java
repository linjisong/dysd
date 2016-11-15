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
package org.dysd.util.parser.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.dysd.util.Tool;
import org.dysd.util.parser.IParseEventListener;
import org.dysd.util.parser.IParser;
import org.dysd.util.parser.IParserContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

/**
 * 抽象的解析器实现类，防止资源不被重复解析，解析侦听等
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class AbstractParser<E extends IParserContext> implements IParser<E> {
	
	/**
	 * 已经被解析的资源
	 */
	private final ThreadLocal<Set<Resource>> resourcesCurrentlyBeingLoaded = new ThreadLocal<Set<Resource>>();
	
	/**
	 * 当前解析环境
	 */
	private final ThreadLocal<E> currentParserContext = new ThreadLocal<E>();

	@Override
	public void parse(E parserContext, String location) {
		parse(parserContext, new String[]{location});
	}
	
	@Override
	public void parse(E parserContext, String[] locationPatterns) {
		Environment environment = extractEnvironment(parserContext);
		Set<Resource> resources = Tool.IO.getResources(environment, locationPatterns);
		doParse(parserContext, resources);
	}
	
	@Override
	public void parse(E parserContext, InputStream inputStream) {
		parse(parserContext, new InputStreamResource(inputStream));
	}
	
	@Override
	public void parse(E parserContext, Resource resource) {
		doParse(parserContext, Arrays.asList(resource));
	}
	
	/**
	 * 执行解析
	 * @param parserContext
	 * @param resource
	 * @throws IOException
	 */
	protected abstract void doParse(E parserContext, EncodedResource resource) throws IOException;
	
	/**
	 * 解析前处理，供子类覆盖
	 * @param parserContext
	 */
	protected void onBeforeParse(E parserContext){
		
	}
	
	/**
	 * 解析后处理，供子类覆盖
	 * @param parserContext
	 */
	protected void onFinishParse(E parserContext){
		
	}
	
	/**
	 * 执行解析，解析一组资源
	 * @param parserContext
	 * @param resources
	 */
	private void doParse(E parserContext, Collection<Resource> resources) {
		if(null != resources && !resources.isEmpty()){
			IParseEventListener listener = parserContext.getEventListener();
			try{
				if(null != listener){
					listener.onBeforeParse(parserContext, resources);
				}
				currentParserContext.set(parserContext);
				this.onBeforeParse(parserContext);
				for (Resource resource : resources) {
					nonRepeatedParse(parserContext, resource, listener);
				}
			}finally{
				try{
					this.onFinishParse(parserContext);
					if(null != listener){
						listener.onFinishParse(parserContext, resources);
					}
				}finally{
					currentParserContext.remove();
				}
			}
		}
	}
	
	/**
	 * 获取解析上下文环境，供子类调用
	 * @return
	 */
	protected E getParserContext(){
		return currentParserContext.get();
	}
	
	/**
	 * 执行解析，保证不重复解析资源
	 * @param parserContext
	 * @param resource
	 * @param listener
	 */
	private void nonRepeatedParse(E parserContext, Resource resource, IParseEventListener listener) {
		Set<Resource> currentResources = this.resourcesCurrentlyBeingLoaded.get();
		if (currentResources == null) {
			currentResources = new HashSet<Resource>(4);
			this.resourcesCurrentlyBeingLoaded.set(currentResources);
		}
		if (!currentResources.add(resource)) {
			throw new RuntimeException("Detected cyclic loading of " + resource + " !");
		}
		try {
			if(null != listener){
				listener.onBeforeParseOneResource(parserContext, resource);
			}
			EncodedResource encodedResource = decorateEncodedResource(resource);
			doParse(parserContext, encodedResource);
			if(null != listener){
				listener.onSuccessParseOneResource(parserContext, resource);
			}
		} catch (Exception ex) {
			if(null != listener){
				listener.onFailureParseOneResource(parserContext, resource, ex);
			}
			throw new RuntimeException("IOException parsing from " + resource, ex);
		}finally {
			currentResources.remove(resource);
			if (currentResources.isEmpty()) {
				this.resourcesCurrentlyBeingLoaded.remove();
			}
		}
	}
	
	/**
	 * 编码装饰，子类可覆盖，也可不覆盖
	 * @param resource
	 * @return
	 */
	protected EncodedResource decorateEncodedResource(Resource resource){
		EncodedResource encodedResource = null;
		if(!(resource instanceof EncodedResource)){
			encodedResource = new EncodedResource(resource);
		}else{
			encodedResource = (EncodedResource)resource;
		}
		return encodedResource;
	}
	
	/**
	 * 根据当前环境解析path，子类也可调用
	 * @param environment
	 * @param path
	 * @return
	 */
	protected String resolvePath(Environment environment, String path) {
		return environment == null ? path : environment.resolveRequiredPlaceholders(path);
	}

	/**
	 * 获取当前环境
	 * @param parserContext
	 * @return
	 */
	protected Environment extractEnvironment(E parserContext) {
		Environment environment = parserContext.getEnvironment();
		return environment;
	}
}

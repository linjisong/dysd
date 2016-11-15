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

import org.dysd.util.config.BaseConfig;
import org.dysd.util.parser.IParseEventListener;
import org.dysd.util.parser.IParserContext;
import org.dysd.util.parser.ParserUtils;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

/**
 * 解析器上下文环境实现类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class ParserContext implements IParserContext{

	private ResourceLoader resourceLoader;

	private ProblemReporter problemReporter;

	private IParseEventListener eventListener;

	private SourceExtractor sourceExtractor;
	
	private Environment environment;

	public ResourceLoader getResourceLoader() {
		if(null == this.resourceLoader){
			this.resourceLoader = BaseConfig.getResourcePatternResolver();
		}
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public ProblemReporter getProblemReporter() {
		if(null == this.problemReporter){
			this.problemReporter = ParserUtils.getFactory().newProblemReporter();
		}
		return problemReporter;
	}

	public void setProblemReporter(ProblemReporter problemReporter) {
		this.problemReporter = problemReporter;
	}

	public IParseEventListener getEventListener() {
		if(null == this.eventListener){
			this.eventListener = ParserUtils.getFactory().newEventListener();
		}
		return eventListener;
	}

	public void setEventListener(IParseEventListener eventListener) {
		this.eventListener = eventListener;
	}

	public SourceExtractor getSourceExtractor() {
		if(null == this.sourceExtractor){
			this.sourceExtractor = ParserUtils.getFactory().newSourceExtractor();
		}
		return sourceExtractor;
	}

	public void setSourceExtractor(SourceExtractor sourceExtractor) {
		this.sourceExtractor = sourceExtractor;
	}

	public Environment getEnvironment() {
		if(null == this.environment){
			this.environment = ParserUtils.getFactory().newEnvironment();
		}
		return environment;
	}
	
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}

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

import org.dysd.util.parser.IParseEventListener;
import org.dysd.util.parser.IParserComponentFactory;
import org.dysd.util.parser.IParserContext;
import org.springframework.beans.factory.parsing.FailFastProblemReporter;
import org.springframework.beans.factory.parsing.NullSourceExtractor;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;

/**
 * 解析器组件工厂实现类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class ParserComponentFactory implements IParserComponentFactory{

	@Override
	public IParserContext newParserContext(){
		return new ParserContext();
	}
	
	@Override
	public ProblemReporter newProblemReporter() {
		return new FailFastProblemReporter();
	}

	@Override
	public IParseEventListener newEventListener() {
		return null;
	}

	@Override
	public SourceExtractor newSourceExtractor() {
		return new NullSourceExtractor();
	}

	@Override
	public Environment newEnvironment() {
		return new StandardEnvironment();
	}
}

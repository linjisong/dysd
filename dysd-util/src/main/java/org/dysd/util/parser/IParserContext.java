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
package org.dysd.util.parser;

import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.core.env.Environment;

/**
 * 解析器上下文环境接口
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface IParserContext {
	
	/**
	 * 获取问题报告者
	 * @return
	 */
	public ProblemReporter getProblemReporter();
	
	/**
	 * 获取解析侦听器
	 * @return
	 */
	public IParseEventListener getEventListener();
	
	/**
	 * 获取源提取者
	 * @return
	 */
	public SourceExtractor getSourceExtractor();
	
	/**
	 * 获取环境
	 * @return
	 */
	public Environment getEnvironment();
}

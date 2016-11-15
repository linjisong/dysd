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

import java.io.InputStream;

import org.springframework.core.io.Resource;

/**
 * 通用解析器接口
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface IParser<E extends IParserContext> {

	/**
	 * 解析一个模式字符串表示的资源
	 * @param parserContext    解析上下文环境
	 * @param locationPattern  资源模式字符串
	 */
	public void parse(E parserContext, String locationPattern);
	
	/**
	 * 解析一组模式字符串表示的资源
	 * @param parserContext    解析上下文环境
	 * @param locationPatterns 资源模式字符串数组
	 */
	public void parse(E parserContext, String[] locationPatterns);
	
	/**
	 * 解析一个输入流表示的资源
	 * @param parserContext    解析上下文环境
	 * @param inputStream      输入流
	 */
	public void parse(E parserContext, InputStream inputStream);
	
	/**
	 * 解析一个资源
	 * @param parserContext    解析上下文环境
	 * @param resource         资源
	 */
	public void parse(E parserContext, Resource resource);
}

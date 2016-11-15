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

import java.util.Collection;

import org.springframework.core.io.Resource;

/**
 * 解析器侦听接口
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface IParseEventListener {

	/**
	 * 整个解析开始前事件
	 * @param context
	 * @param resources
	 */
	public void onBeforeParse(IParserContext context, Collection<Resource> resources);
	
	/**
	 * 整个解析完成后事件，可能解析成功，也可能解析失败
	 * @param context
	 * @param resources
	 */
	public void onFinishParse(IParserContext context, Collection<Resource> resources);
	
	/**
	 * 解析单个资源前事件
	 * @param context
	 * @param resource
	 */
	public void onBeforeParseOneResource(IParserContext context, Resource resource);
	
	/**
	 * 解析单个资源出现后事件
	 * @param context
	 * @param resource
	 * @param e
	 */
	public void onFailureParseOneResource(IParserContext context, Resource resource, Exception e);
	
	/**
	 * 解析单个资源成功后事件
	 * @param context
	 * @param resource
	 */
	public void onSuccessParseOneResource(IParserContext context, Resource resource);
}

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
package org.dysd.dao.call;

import java.util.Iterator;

/**
 * 调用存储过程的返回结果
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface ICallResult {
	
	/**
	 * 根据参数名称返回输出参数
	 * @param name 输出参数名称
	 * @return 和输出参数名称相对应的返回结果，如果不存在输出参数，抛出平台运行时异常{@link Beneform4jRuntimeException}
	 */
	public <T> T getOutputParam(String name);
	
	/**
	 * 返回输出参数名称的迭代器
	 * @return 输出参数名迭代器
	 */
	public Iterator<String> iterator();
}

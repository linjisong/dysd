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
package org.dysd.util.logger.stack;

import java.io.Serializable;

import org.slf4j.Logger;

/**
 * 日志堆栈
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public interface IStack extends Serializable{
	
	/**
	 * 获取当前环境的跟踪ID
	 * @return
	 */
	public String getTrackId();
	
	/**
	 * 获取日志类
	 * @return
	 */
	public Logger getLogger();
	
	/**
	 * 获取堆栈
	 * @return
	 */
	public StackTraceElement getStack();

	/**
	 * 获取描述信息
	 * @return
	 */
	public String getMessage();
	
	/**
	 * 获取异常，如果没有异常，返回null
	 * @return
	 */
	public Throwable getThrowable();
	
}

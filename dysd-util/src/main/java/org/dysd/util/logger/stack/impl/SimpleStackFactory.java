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
package org.dysd.util.logger.stack.impl;

import org.dysd.util.logger.stack.IStack;
import org.dysd.util.logger.stack.IStackFactory;
import org.slf4j.Logger;

/**
 * 简单的日志堆栈工厂
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class SimpleStackFactory implements IStackFactory{
	
	/**
	 * 根据简单提示信息、参数和异常产生堆栈信息
	 * @param logger    日志类
	 * @param stack     调用堆栈
	 * @param message   日志信息
	 * @param throwable 异常
	 * @return 日志堆栈对象
	 */
	public IStack getStack(Logger logger, StackTraceElement stack, String message, Throwable throwable){
		return new SimpleStack(logger, stack, message, throwable);
	}
}

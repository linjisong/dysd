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
package org.dysd.util.exception.handler.impl;

import org.dysd.util.exception.DysdException;
import org.dysd.util.exception.Throw;
import org.dysd.util.exception.handler.IExceptionHandler;

/**
 * 抽象的异常处理器实现类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public abstract class AbstractExceptionHandler implements IExceptionHandler{

	/**
	 * 是否忽略处理过程中的异常，默认为忽略
	 */
	private boolean ignoreHandlerException = true;

	public boolean ignoreHandlerException() {
		return ignoreHandlerException;
	}

	public void setIgnoreHandlerException(boolean ignoreHandlerException) {
		this.ignoreHandlerException = ignoreHandlerException;
	}
	
	/**
	 * 先转换为DYSD异常，再进行处理
	 * @param throwable
	 */
	public Object handler(Throwable throwable){
		DysdException exception = Throw.createException(throwable);
		return this.handlerDysdException(exception);
	}
	
	/**
	 * 处理DYSD异常
	 * @param exception
	 * @return
	 */
	protected abstract Object handlerDysdException(DysdException exception);
}

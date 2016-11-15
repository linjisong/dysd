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
import org.dysd.util.logger.CommonLogger;

/**
 * 返回明细信息的异常处理器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class DetailExceptionHandler extends AbstractExceptionHandler {
	
	private boolean writeErrorLogger = true;

	/**
	 * 处理异常，返回详细信息
	 */
	@Override
	protected Object handlerDysdException(DysdException exception) {
		String msg =  Throw.getMessage(exception);
		if(isWriteErrorLogger()){
			CommonLogger.error(exception);
		}
		return msg;
	}

	public boolean isWriteErrorLogger() {
		return writeErrorLogger;
	}

	public void setWriteErrorLogger(boolean writeErrorLogger) {
		this.writeErrorLogger = writeErrorLogger;
	}

}

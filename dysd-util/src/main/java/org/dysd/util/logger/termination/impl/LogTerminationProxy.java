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
package org.dysd.util.logger.termination.impl;

import java.util.HashMap;
import java.util.Map;

import org.dysd.util.logger.level.LogLevel;
import org.dysd.util.logger.stack.IStack;
import org.dysd.util.logger.termination.ILogTermination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用Log4j的日志终端代理类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class LogTerminationProxy implements ILogTermination{
	
	/**
	 * 根据日志级别写日志
	 * @param level
	 * @param stack
	 */
	@Override
	public void write(LogLevel level, IStack stack){
		Logger logger = getLogger(stack);
		String message = stack.getMessage();
		Throwable e = stack.getThrowable();
		switch(level){
		case TRACE:
			if(logger.isTraceEnabled()){
				logger.trace(message, e);
			}
			break;
		case DEBUG:
			if(logger.isDebugEnabled()){
				logger.debug(message, e);
			}
			break;
		case INFO:
			if(logger.isInfoEnabled()){
				logger.info(message, e);
			}
			break;
		case WARN:
			if(logger.isWarnEnabled()){
				logger.warn(message, e);
			}
			break;
		case ERROR:
			if(logger.isErrorEnabled()){
				logger.error(message, e);
			}
			break;
		}
	}
	
	/**
	 * 获取日志
	 * @param stack
	 * @return
	 */
	protected Logger getLogger(IStack stack){
		Logger logger = stack.getLogger();
		if(null == logger){
			String key = stack.getStack().getClassName();
			logger = map.get(key);
			if(null == logger){
				synchronized(map){
					logger = map.get(key);
					if(null == logger){
						logger = LoggerFactory.getLogger(key);
						map.put(key, logger);
					}
				}
			}
		}
		return logger;
	}
	
	private static final Map<String, Logger> map = new HashMap<String, Logger>();
}

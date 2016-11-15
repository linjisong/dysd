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
package org.dysd.util.logger.level;

/**
 * 日志级别，和Log4j中同名日志级别相对应，日志级别 ERROR > WARN > INFO > DEBUG > TRACE
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public enum LogLevel {
	TRACE,
	DEBUG,
	INFO,
	WARN,
	ERROR;
	
	/**
	 * 获取日志级别描述
	 * @return
	 */
	public String getDescription() {
		switch(this){//这里为了打印的时候格式一致，补上相应的空格
		case TRACE:
			return "TRACE";
		case DEBUG:
			return "DEBUG";
		case INFO:
			return "INFO ";
		case WARN:
			return "WARN ";
		case ERROR:
			return "ERROR";
		}
		return null;
	}
}

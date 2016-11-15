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
package org.dysd.util.exception.level;

/**
 * 异常级别
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public enum ExceptionLevel {
	/**
	 * 业务异常，只需要将异常信息提示给业务人员即可，一般由数据校验组件或者开发人员抛出
	 */
	INFO,
	/**
	 * 运行时异常，影响本模块，或者局部非重点功能，一般需要技术人员跟踪，在处理该级别异常时，要求同时打印异常堆栈信息，写好日志记录
	 */
	ERROR,
	/**
	 * 严重异常，影响整个系统正常运行或者重要模块功能，不可控的系统错误导致的异常，会导致系统无法正常运行
	 */
	FATAL,
	/**
	 * 未知
	 */
	UNKNOWN;

	/**
	 * 根据名称获取异常级别实例
	 * @param name
	 * @return
	 */
	public static ExceptionLevel instance(String name){
		for(ExceptionLevel el : values()){
			if(el.name().equalsIgnoreCase(name)){
				return el;
			}
		}
		return UNKNOWN;
	}

	/**
	 * 获取异常级别的描述
	 * @return
	 */
	public String getDescription() {
		return "Exception-Level:"+this.name();
	}
}

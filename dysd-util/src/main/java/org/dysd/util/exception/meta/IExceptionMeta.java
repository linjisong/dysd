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
package org.dysd.util.exception.meta;

import java.util.Set;

import org.dysd.util.exception.handler.IExceptionHandler;
import org.dysd.util.exception.level.ExceptionLevel;

/**
 * 异常元信息
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public interface IExceptionMeta {
	
	/**
	 * 父代码
	 * @return
	 */
	public String getParentCode();
	
	/**
	 * 异常代码
	 * @return
	 */
	public String getCode();
	
	/**
	 * 国际化描述信息的Key值
	 * @return
	 */
	public String getLocaleKey();
	
	/**
	 * 获取异常级别
	 * @return
	 */
	public ExceptionLevel getLevel();
	
	/**
	 * 获取异常返回逻辑视图
	 * @return
	 */
	public String getView();
	
	/**
	 * 异常处理器列表
	 * @return
	 */
	public Set<IExceptionHandler> getHandlers();
	
	/**
	 * 获取可能导致该异常的原始异常列表
	 * @return
	 */
	public Set<Class<? extends Throwable>> getCauses();
}

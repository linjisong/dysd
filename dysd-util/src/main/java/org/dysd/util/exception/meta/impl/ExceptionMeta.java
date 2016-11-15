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
package org.dysd.util.exception.meta.impl;

import java.util.Set;

import org.dysd.util.Tool;
import org.dysd.util.exception.handler.IExceptionHandler;
import org.dysd.util.exception.level.ExceptionLevel;
import org.dysd.util.exception.meta.IExceptionMeta;

/**
 * 异常配置元信息
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class ExceptionMeta implements IExceptionMeta {
	
	/**
	 * 父代码
	 */
	private String parentCode;

	/**
	 * 异常代码
	 */
	private String code;
	
	/**
	 * 国际化描述信息的Key值
	 */
	private String localeKey;
	
	/**
	 * 获取异常级别
	 */
	private ExceptionLevel level;
	
	/**
	 * 异常返回逻辑视图
	 */
	private String view;
	
	/**
	 * 异常描述，只给开发人员使用
	 */
	private String description;
	
	/**
	 * 异常处理器列表
	 */
	private Set<IExceptionHandler> handlers;
	
	/**
	 * 获取导致该异常的原始异常列表
	 */
	private Set<Class<? extends Throwable>> causes;
	
	@Override
	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	@Override
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getLocaleKey() {
		return localeKey;
	}

	public void setLocaleKey(String localeKey) {
		this.localeKey = localeKey;
	}

	@Override
	public ExceptionLevel getLevel() {
		return level;
	}

	public void setLevel(ExceptionLevel level) {
		this.level = level;
	}

	@Override
	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Set<IExceptionHandler> getHandlers() {
		return handlers;
	}

	public void setHandlers(Set<IExceptionHandler> handlers) {
		this.handlers = handlers;
	}

	@Override
	public Set<Class<? extends Throwable>> getCauses() {
		return causes;
	}

	public void setCauses(Set<Class<? extends Throwable>> causes) {
		this.causes = causes;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(" ==>{code=").append(getCode());
		
		String desc = getDescription();
		if(!Tool.CHECK.isBlank(desc)){
			sb.append(",description=").append(desc);
		}
		
		String pCode = getParentCode();
		if(!Tool.CHECK.isBlank(pCode) && !pCode.startsWith("##")){
			sb.append(",parentCode=").append(pCode);
		}
		if(!Tool.CHECK.isBlank(getLocaleKey())){
			sb.append(",messageKey=").append(getLocaleKey());
		}
		if(null != getLevel()){
			sb.append(",level=").append(getLevel());
		}
		if(!Tool.CHECK.isBlank(getView())){
			sb.append(",view=").append(getView());
		}
		Set<Class<? extends Throwable>> causes = getCauses();
		if(null != causes && !causes.isEmpty()){
			sb.append(",causes=").append(causes);
		}
		sb.append("}");
		return sb.toString();
	}
}

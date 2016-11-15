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
package org.dysd.util.base;

import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.LocaleUtils;
import org.dysd.util.config.BaseConfig;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 本地化工具类
 * @author linjisong
 * @version 0.0.1
 * @since 0.0.1
 * @date 2016-11-8
 */
public abstract class BaseLocaleUtils {

	private static final BaseLocaleUtils instance = new BaseLocaleUtils(){};
	private BaseLocaleUtils(){
	}
	
	/**
	 * 获取单实例
	 * @return
	 */
	/*package*/ static BaseLocaleUtils getInstance(){
		return instance;
	}
	
	/**
	 * 获取本地化的信息
	 * @param code           本地化代码
	 * @param defaultMessage 默认信息
	 * @return 本地化信息
	 */
	public String getMessage(String code, String defaultMessage){
		return getMessage(code, (Object[])null, defaultMessage, getLocale());
	}
	
	/**
	 * 获取本地化的信息
	 * @param code           本地化代码
	 * @param args           占位符参数
	 * @param defaultMessage 默认信息
	 * @return 本地化信息
	 */
	public String getMessage(String code, Object[] args, String defaultMessage){
		return getMessage(code, args, defaultMessage, getLocale());
	}
	
	/**
	 * 获取本地化的信息
	 * @param code           本地化代码
	 * @param defaultMessage 默认信息
	 * @param locale         指定本地化对象
	 * @return 本地化信息
	 */
	public String getMessage(String code, String defaultMessage, Locale locale){
		return getMessage(code, (Object[])null, defaultMessage, locale);
	}
	
	/**
	 * 获取本地化的信息
	 * @param code           本地化代码
	 * @param args           占位符参数
	 * @param defaultMessage 默认信息
	 * @param locale         指定本地化对象
	 * @return 本地化信息
	 */
	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale){
		MessageSource ms = getMessageSource();
		return ms.getMessage(code, args, defaultMessage, locale);
	}
	
	/**
	 * 获取本地化的信息
	 * @param code           本地化代码
	 * @return 本地化信息
	 */
	public String getMessage(String code){
		return getMessage(code, (Object[])null, getLocale());
	}
	
	/**
	 * 获取本地化的信息
	 * @param code           本地化代码
	 * @param args           占位符参数
	 * @return 本地化信息
	 */
	public String getMessage(String code, Object[] args){
		return getMessage(code, args, getLocale());
	}
	
	/**
	 * 获取本地化的信息
	 * @param code           本地化代码
	 * @param locale         指定本地化对象
	 * @return 本地化信息
	 */
	public String getMessage(String code, Locale locale){
		return getMessage(code, (Object[])null, locale);
	}

	/**
	 * 获取本地化的信息
	 * @param code           本地化代码
	 * @param args           占位符参数
	 * @param locale         指定本地化对象
	 * @return 本地化信息
	 */
	public String getMessage(String code, Object[] args, Locale locale){
		MessageSource ms = getMessageSource();
		return ms.getMessage(code, args, locale);
	}
	
	/**
	 * 改变默认的本地化对象
	 * @param locale 新的本地化对象
	 */
	public void changeDefaultLocale(Locale locale){
		Locale.setDefault(locale);
	}
	
	/**
	 * 获取当前上下文中的本地化对象
	 * @return
	 */
	public Locale getLocale(){
		return LocaleContextHolder.getLocale();
	}
	
	/**
	 * 设置当前上下文的本地化对象
	 * @param locale
	 */
	public void setLocale(Locale locale) {
		LocaleContextHolder.setLocale(locale, true);
	}
	
	/**
	 * 获取当前上下文的时区对象
	 * @return
	 */
	public TimeZone getTimeZone() {
		return LocaleContextHolder.getTimeZone();
	}
	
	/**
	 * 设置当前上下文的时区对象
	 * @param timeZone
	 */
	public void setTimeZone(TimeZone timeZone) {
		LocaleContextHolder.setTimeZone(timeZone, true);
	}
	
	/**
	 * 将字符串解析为Locale对象
	 * @param str locale字符串
	 * @return Locale对象
	 */
	public Locale toLocale(String str) {
		return LocaleUtils.toLocale(str);
	}

	private MessageSource getMessageSource(){
		return BaseConfig.getMessageSource();
	}
}

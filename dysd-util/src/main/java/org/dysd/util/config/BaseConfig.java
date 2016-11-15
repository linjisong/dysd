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
package org.dysd.util.config;

import java.nio.charset.Charset;
import java.util.LinkedHashSet;
import java.util.Set;

import org.dysd.util.Tool;
import org.dysd.util.exception.ExceptionCodes;
import org.dysd.util.exception.Throw;
import org.dysd.util.exception.handler.IExceptionHandler;
import org.dysd.util.exception.meta.IExceptionMetaLoader;
import org.dysd.util.logger.stack.IStackFactory;
import org.dysd.util.logger.termination.ILogTermination;
import org.dysd.util.track.ITracker;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 基础配置
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class BaseConfig implements InitializingBean, ApplicationContextAware{
	
	/**
	 * 视作为空的整型值
	 */
	protected static final int EQUAL_EMPTY_INT_VALUE = Integer.MIN_VALUE;
	
	/**
	 * Spring容器上下文
	 */
	protected static ApplicationContext applicationContext;

	/**
	 * 应用名称
	 */
	private static String appName;
	
	/**
	 * 字符集
	 */
	private static String encoding;
	
	/**
	 * 日期格式
	 */
	private static String dateFormat;
	
	/**
	 * 时间格式
	 */
	private static String timeFormat;
	
	/**
	 * 日期时间格式
	 */
	private static String datetimeFormat;
	
	/**
	 * 加密密钥
	 */
	private static String encryptKey;
	
	/**
	 * 国际化配置的属性文件名
	 */
	private static Set<String> localeBasenames;
	
	/**
	 * 国际化消息解析器
	 */
	private static MessageSource messageSource;
	
	/**
	 * 堆栈工厂
	 */
	private static IStackFactory stackFactory;
	
	/**
	 * 打印日志时不需要打印的堆栈
	 */
	private static Set<String> ignoreStacks;
	
	/**
	 * 日志终端列表
	 */
	private static Set<ILogTermination> logTerminations;
	
	/**
	 * 异常元数据加载器
	 */
	private static IExceptionMetaLoader exceptionMetaLoader;
	
	/**
	 * 异常处理器列表
	 */
	private static Set<IExceptionHandler> exceptionHandlers;
	
	/**
	 * 资源加载器
	 */
	private static ResourcePatternResolver resourcePatternResolver;
	
	/**
	 * 类型转换服务
	 */
	private static ConversionService conversionService;
	
	/**
	 * 跟踪器
	 */
	private static ITracker tracker;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.validate();
		if(null != applicationContext){
			initSpringComponents();
		}
	}
	
	protected void initSpringComponents(){
		if(null == messageSource){
			messageSource = applicationContext;
		}
		if(null == resourcePatternResolver){
			resourcePatternResolver = applicationContext;
		}
		if(null == conversionService){
			try{
				AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
				if(beanFactory instanceof ConfigurableBeanFactory){
					conversionService = ((ConfigurableBeanFactory)beanFactory).getConversionService();
				}
				if(null == conversionService){
					conversionService = applicationContext.getBean(ConversionService.class);
				}
			}catch(Exception e){}
		}
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		BaseConfig.applicationContext = applicationContext;
	}

	/**
	 * 校验
	 */
	public void validate(){
		//校验字符集是否支持
		boolean isSupported = false;
		try{
			isSupported = Charset.isSupported(encoding);
		}catch(Exception e){
		}
		if(!isSupported){
			Throw.createException(ExceptionCodes.DYSD010002, encoding);
		}
		
		// 校验日期、时间、日期时间格式
	}

	/**
	 * 获取默认字符集
	 * @return 字符集
	 */
	public static String getEncoding() {
		return getValue(encoding, "encoding");
	}

	/**
	 * 设置默认字符集
	 * @param encoding 字符集
	 */
	public void setEncoding(String encoding) {
		BaseConfig.encoding = encoding;
	}

	/**
	 * 获取日期格式
	 * @return 日期格式
	 */
	public static String getDateFormat() {
		return getValue(dateFormat, "date_format");
	}

	/**
	 * 注入日期格式
	 * @param dateFormat 日期格式
	 */
	public void setDateFormat(String dateFormat) {
		BaseConfig.dateFormat = dateFormat;
	}

	/**
	 * 获取时间格式
	 * @return 时间格式
	 */
	public static String getTimeFormat() {
		return getValue(timeFormat, "time_format");
	}

	/**
	 * 注入时间格式
	 * @param timeFormat 时间格式
	 */
	public void setTimeFormat(String timeFormat) {
		BaseConfig.timeFormat = timeFormat;
	}

	/**
	 * 获取日期时间格式
	 * @return 日期时间格式
	 */
	public static String getDatetimeFormat() {
		return getValue(datetimeFormat, "datetime_format");
	}

	/**
	 * 注入日期时间格式
	 * @param datetimeFormat 日期时间格式
	 */
	public void setDatetimeFormat(String datetimeFormat) {
		BaseConfig.datetimeFormat = datetimeFormat;
	}

	/**
	 * 获取应用名称
	 * @return 应用名称
	 */
	public static String getAppName() {
		return getValue(appName, "app_name");
	}

	/**
	 * 注入应用名称
	 * @param appName 应用名称
	 */
	public void setAppName(String appName) {
		BaseConfig.appName = appName;
	}
	
	/**
	 * 获取加密密钥
	 * @return 加密密钥
	 */
	public static String getEncryptKey() {
		return getValue(encryptKey, "encrypt_key");
	}

	/**
	 * 注入加密密钥
	 * @param encryptKey 加密密钥
	 */
	public static void setEncryptKey(String encryptKey) {
		BaseConfig.encryptKey = encryptKey;
	}

	/**
	 * 获取国际化配置的基本名
	 * @return 国际化配置的基本名
	 */
	public static Set<String> getLocaleBasenames() {
		return getValues(localeBasenames, "locale_basenames");
	}

	/**
	 * 获取国际化消息解析器
	 * @return 国际化消息解析器
	 */
	public static MessageSource getMessageSource() {
		return getComponent(messageSource, MessageSource.class);
	}

	/**
	 * 注入国际化消息解析器
	 * @param localeMessageResolver 国际化消息解析器
	 */
	public void setMessageSource(MessageSource messageSource) {
		BaseConfig.messageSource = messageSource;
	}

	/**
	 * 获取资源解析器
	 * @return 资源解析器
	 */
	public static ResourcePatternResolver getResourcePatternResolver() {
		return getComponent(resourcePatternResolver, ResourcePatternResolver.class);
	}

	/**
	 * 获取堆栈工厂
	 * @return 堆栈工厂
	 */
	public static IStackFactory getStackFactory() {
		return getComponent(stackFactory, IStackFactory.class);
	}

	/**
	 * 注入堆栈工厂
	 * @param stackFactory 堆栈工厂
	 */
	public void setStackFactory(IStackFactory stackFactory) {
		BaseConfig.stackFactory = stackFactory;
	}
	
	/**
	 * 获取打印日志时不需要打印堆栈的类列表
	 * @return 忽略堆栈
	 */
	public static Set<String> getIgnoreStacks() {
		return combineValues(ignoreStacks, "ignore_stacks");
	}

	/**
	 * 注入忽略堆栈
	 * @param ignoreStacks 忽略堆栈
	 */
	public void setIgnoreStacks(Set<String> ignoreStacks) {
		if(null != ignoreStacks && !ignoreStacks.isEmpty()){
			if(null == BaseConfig.ignoreStacks){
				BaseConfig.ignoreStacks = ignoreStacks;
			}else{
				BaseConfig.ignoreStacks.addAll(ignoreStacks);
			}
		}
	}

	/**
	 * 获取日志打印终端
	 * @return 日志打印终端
	 */
	public static Set<ILogTermination> getLogTerminations() {
		return getComponents(logTerminations, ILogTermination.class);
	}

	/**
	 * 注入日志打印终端
	 * @param logTerminations 日志打印终端
	 */
	public void setLogTerminations(Set<ILogTermination> logTerminations) {
		BaseConfig.logTerminations = logTerminations;
	}

	/**
	 * 获取异常元信息加载器
	 * @return 异常元信息加载器
	 */
	public static IExceptionMetaLoader getExceptionMetaLoader() {
		return getComponent(exceptionMetaLoader, IExceptionMetaLoader.class);
	}

	/**
	 * 设置异常元信息加载器
	 * @param exceptionMetaLoader 异常元信息加载器
	 */
	public void setExceptionMetaLoader(IExceptionMetaLoader exceptionMetaLoader) {
		BaseConfig.exceptionMetaLoader = exceptionMetaLoader;
	}

	/**
	 * 获取异常处理器列表
	 * @return 异常处理器列表
	 */
	public static Set<IExceptionHandler> getExceptionHandlers() {
		return getComponents(exceptionHandlers, IExceptionHandler.class);
	}

	/**
	 * 注入异常处理器列表
	 * @param exceptionHandlers 异常处理器列表
	 */
	public void setExceptionHandlers(Set<IExceptionHandler> exceptionHandlers) {
		BaseConfig.exceptionHandlers = exceptionHandlers;
	}

	/**
	 * 获取类型转换服务
	 * @return 类型转换服务
	 */
	public static ConversionService getConversionService() {
		return getComponent(conversionService, ConversionService.class);
	}

	/**
	 * 注入类型转换服务
	 * @param conversionService 类型转换服务
	 */
	public void setConversionService(ConversionService conversionService) {
		BaseConfig.conversionService = conversionService;
	}
	
	/**
	 * 获取上下文环境跟踪器
	 * @return 上下文环境跟踪器
	 */
	public static ITracker getTracker() {
		return getComponent(tracker, ITracker.class);
	}

	/**
	 * 注入上下文环境跟踪器
	 * @param tracker 上下文环境跟踪器
	 */
	public void setTracker(ITracker tracker) {
		BaseConfig.tracker = tracker;
	}

	/**
	 * 获取整型值，如果值为空，返回默认配置名称相对应的值
	 * @param value              用户配置值
	 * @param defaultConfigName  默认配置名称
	 * @return 配置值
	 */
	protected static int getValue(int value, String defaultConfigName){
		if(EQUAL_EMPTY_INT_VALUE == value){
			return Defaults.getDefaultConfig(defaultConfigName, int.class);
		}
		return value;
	}
	
	/**
	 * 获取字符串值，如果值为空，返回默认配置名称相对应的值
	 * @param value              用户配置值
	 * @param defaultConfigName  默认配置名称
	 * @return 配置值
	 */
	protected static String getValue(String value, String defaultConfigName){
		if(Tool.CHECK.isBlank(value)){
			return Defaults.getDefaultConfig(defaultConfigName);
		}
		return value;
	}
	
	/**
	 * 获取一组值组，如果值组为空，返回默认配置名称相对应的值组
	 * @param values			用户配置值
	 * @param defaultConfigName 默认配置名称
	 * @return 配置值
	 */
	protected static Set<String> getValues(Set<String> values, String defaultConfigName){
		if(null == values || values.isEmpty()){
			Set<String> s = Defaults.getDefaultConfigs(defaultConfigName);
			if(null != s && s.size() >= 1){
				return new LinkedHashSet<String>(s);
			}
		}
		return values;
	}
	
	/**
	 * 合并值组，如果值组为空，返回默认配置名称相对应的值组，否则在原基础上添加默认的配置
	 * @param values			用户配置值
	 * @param defaultConfigName 默认配置名称
	 * @return 配置值
	 */
	protected static Set<String> combineValues(Set<String> values, String defaultConfigName){
		Set<String> s = Defaults.getDefaultConfigs(defaultConfigName);
		if(null != s && s.size() >= 1){
			if(null == values || values.isEmpty()){
				return new LinkedHashSet<String>(s);
			}else{
				values.addAll(s);
			}
		}
		return values;
	}
	
	/**
	 * 获取组件，如果组件为空，将组件类型作为key值查找对应的默认组件
	 * @param component 用户配置组件
	 * @param cls       组件类型
	 * @return 配置组件
	 */
	protected static <E>E getComponent(E component, Class<E> cls){
		if(null == component){
			return Defaults.getDefaultComponent(cls);
		}
		return component;
	}
	
	/**
	 * 获取组件组，如果组进组为空，将组件类型作为key值查找对应的默认组件组
	 * @param components 用户配置组件组
	 * @param cls        组件类型
	 * @return 配置组件组
	 */
	protected static <E>Set<E> getComponents(Set<E> components, Class<E> cls){
		if(null == components || components.isEmpty()){
			Set<E> d = Defaults.getDefaultComponents(cls);
			if(null != d && d.size() > 0){
				return new LinkedHashSet<E>(d);
			}
		}
		return components;
	}
	
	/**
	 * 合并组件组，如果组件组为空，返回组件类型作为key值对应的默认组件组，否则在原基础上添加默认的配置组件
	 * @param components 用户配置组件组
	 * @param cls        组件类型
	 * @return 配置组件组
	 */
	protected static <E>Set<E> combineComponents(Set<E> components, Class<E> cls){
		Set<E> d = Defaults.getDefaultComponents(cls);
		if(null != d && d.size() > 0){
			if(null == components || components.isEmpty()){
				return new LinkedHashSet<E>(d);
			}else{
				components.addAll(d);
			}
		}
		return components;
	}
	
	/**
	 * 获取组件，如果组件为空，将组件类型加上井号#，再加上名称作为key值查找对应的默认组件
	 * @param component 用户配置组件
	 * @param cls       组件类型
	 * @param name      组件名称
	 * @return 配置组件
	 */
	protected static <E>E getComponent(E component, Class<E> cls, String name){
		if(null == component){
			return Defaults.getDefaultComponent(cls, name);
		}
		return component;
	}
	
	/**
	 * 获取组件组，如果组件组为空，将组件类型加上井号#，再加上名称作为key值查找对应的默认组件
	 * @param components  用户配置组件组
	 * @param cls         组件类型
	 * @param name        组件名称
	 * @return 配置组件组
	 */
	protected static <E>Set<E> getComponents(Set<E> components, Class<E> cls, String name){
		if(null == components || components.isEmpty()){
			Set<E> d = Defaults.getDefaultComponents(cls, name);
			if(null != d && d.size() > 0){
				return new LinkedHashSet<E>(d);
			}
		}
		return components;
	}
	
	/**
	 * 合并组件组，如果组件组为空，将组件类型加上井号#，再加上名称作为key值查找对应的默认组件，否则在原基础上添加默认的配置组件
	 * @param components  用户配置组件组
	 * @param cls         组件类型
	 * @param name        组件名称
	 * @return 配置组件组
	 */
	protected static <E>Set<E> combineComponents(Set<E> components, Class<E> cls, String name){
		Set<E> d = Defaults.getDefaultComponents(cls, name);
		if(null != d && d.size() > 0){
			if(null == components || components.isEmpty()){
				return new LinkedHashSet<E>(d);
			}else{
				components.addAll(d);
			}
		}
		return components;
	}
}

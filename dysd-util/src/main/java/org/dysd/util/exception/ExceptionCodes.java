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
package org.dysd.util.exception;

/**
 * 异常代码
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class ExceptionCodes {
	
	/**
	 * 未配置的异常
	 */
	public static final String DYSD_UNCONFIG_EXCEPTION = "DYSD999999";
	
	/**
	 * 基础工具层异常
	 */
	public static final String DYSD010000 = "DYSD010000";
	/**
	 * JDK版本过低
	 */
	public static final String DYSD010001 = "DYSD010001";
	/**
	 * 不支持的字符集
	 */
	public static final String DYSD010002 = "DYSD010002";
	/**
	 * 日期解析错误
	 */
	public static final String DYSD010003 = "DYSD010003";
	/**
	 * 资源加载异常
	 */
	public static final String DYSD010004 = "DYSD010004";
	/**
	 * 类加载异常
	 */
	public static final String DYSD010005 = "DYSD010005";
	/**
	 * 调用构造器异常
	 */
	public static final String DYSD010006 = "DYSD010006";
	/**
	 * 类属性访问异常
	 */
	public static final String DYSD010007 = "DYSD010007";
	/**
	 * 方法访问异常
	 */
	public static final String DYSD010008 = "DYSD010008";
	/**
	 * XML解析异常
	 */
	public static final String DYSD010009 = "DYSD010009";
	/**
	 * Spring容器尚未初始化
	 */
	public static final String DYSD010010 = "DYSD010010";
	/**
	 * 获取Spring容器管理的Bean失败
	 */
	public static final String DYSD010011 = "DYSD010011";
}

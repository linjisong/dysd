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
package org.dysd.util.env;

import java.net.Inet4Address;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.SystemUtils;

/**
 * 系统环境相关的常量
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public abstract class EnvConsts{

	/**
	 * 是否为Windows操作系统
	 */
	public static final boolean IS_WINDOWS = SystemUtils.IS_OS_WINDOWS;
	
	/**
	 * 文件编码
	 */
	public static final String FILE_ENCODING = SystemUtils.FILE_ENCODING;
	
	/**
	 * 行分隔符
	 */
	public static final String LINE_SEPARATOR = SystemUtils.LINE_SEPARATOR;
	
	/**
	 * JDK版本，只读取前面3位，如1.6,1.7
	 */
	public static final double JDK_VERSION = Double.parseDouble(SystemUtils.JAVA_VERSION.substring(0,3));
	
	/**
	 * 临时路径
	 */
	public final static String TMP_DIR = SystemUtils.JAVA_IO_TMPDIR;
	
	/**
	 * 用户路径
	 */
	public static final String USER_HOME = SystemUtils.USER_HOME;
	
	/**
	 * 用户地区
	 */
	public static final String USER_COUNTRY = SystemUtils.USER_COUNTRY;
	
	/**
	 * 用户语言
	 */
	public static final String USER_LANGUAGE = SystemUtils.USER_LANGUAGE;
	
	/**
	 * 用户时区
	 */
	public static final String USER_TIMEZONE = SystemUtils.USER_TIMEZONE;
	
	/**
	 * 当前LOCALE
	 */
	public static final Locale SYSTEM_LOCALE = Locale.getDefault();
	
	/**
	 * 当前时区
	 */
	public static final TimeZone SYSTEM_TIMEZONE = TimeZone.getDefault();
	
	/**
	 * 当前主机IP
	 */
	public static final String LOCALE_HOST = ToolInnerConsts.LOCALE_HOST;
	
	//利用内部类实现需要初始化，但是初始化之后就不能再修改的常量
	private static class ToolInnerConsts{
		private static String LOCALE_HOST;
		static{
			try{LOCALE_HOST = Inet4Address.getLocalHost().getHostAddress();}catch(Exception e){}
		}
	}
}

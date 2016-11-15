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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.dysd.util.Tool;

/**
 * 字符串工具类
 * @author linjisong
 * @version 0.0.1
 * @since 0.0.1
 * @date 2016-11-8
 */
public abstract class BaseStringUtils {

	private static final BaseStringUtils instance = new BaseStringUtils(){};
	private BaseStringUtils(){
	}
	
	/**
	 * 获取单实例
	 * @return
	 */
	/*package*/ static BaseStringUtils getInstance(){
		return instance;
	}
	
	/**
	 * 格式化字符串，去掉首尾空白字符，压缩中间连续的空白字符为一个
	 * @param src 原始字符串
	 * @return 压缩后的字符串
	 */
	public String formatWhitespace(String src){
		if(null == src){
	    	return null;
	    }
		StringTokenizer st = new StringTokenizer(src);
	    StringBuilder builder = new StringBuilder();
	    while (st.hasMoreTokens()) {
	      builder.append(st.nextToken()).append(" ");
	    }
	    return builder.toString();
	}
	
	/**
	 * 将下划线格式的字段转为驼峰式，多个连续下划线作为一个处理
	 * 
	 * @param str 原始字符串
	 * @return 对应的驼峰式字符串
	 */
	public String convertToCamel(String str) {
		if (null != str) {
			StringBuilder rs = new StringBuilder();
			boolean upper = false, first = true;
			for (char ch : str.trim().toCharArray()) {
				if (ch == '-' || ch == '_') {
					upper = !first;
				} else {
					rs.append(upper ? Character.toUpperCase(ch) : Character.toLowerCase(ch));
					upper = false;
					first = false;
				}
			}
			return rs.toString();
		}
		return null;
	}
	
	/**
	 * 字符串分割
	 * 
	 * @param src		要分割的字符串
	 * @param separator 分隔符
	 * @return 分割后的字符串列表
	 */
	public List<String> splitToList(String src, String separator) {
		List<String> l = new ArrayList<String>();
		if (null == src) {
			return null;
		} else if (null == separator) {
			l.add(src);
		} else {
			for (int i = src.indexOf(separator), o = separator.length(); i != -1; i = src.indexOf(separator)) {
				l.add(src.substring(0, i));
				src = src.substring(i + o);
			}
			if (!Tool.CHECK.isBlank(src)) {
				l.add(src);
			}
		}
		return l;
	}

	/**
	 * 字符串分割成固定长度的List，如果不够长度，使用默认字符串填充，如果长度超过指定长度，也不截断
	 * 
	 * @param src			  要分割的字符串
	 * @param separator      分隔符
	 * @param minSize        返回集合的最小长度
	 * @param defaultString  填充的默认字符串
	 * @return 分割后的字符串列表
	 */
	public List<String> splitToList(String src, String separator, int minSize, String defaultString) {
		List<String> l = splitToList(src, separator);
		if (null == l) {
			l = new ArrayList<String>();
		}
		for (int i = minSize - l.size() - 1; i >= 0; i--) {
			l.add(defaultString);
		}
		return l;
	}

	/**
	 * 字符串分割
	 * 
	 * @param src		要分割的字符串
	 * @param separator 分隔符
	 * @return 分割后的字符串数组
	 */
	public String[] split(String src, String separator) {
		List<String> l = splitToList(src, separator);
		return null == l ? null : l.toArray(new String[l.size()]);
	}

	/**
	 * 字符串分割分割成固定数目的数组，如果不够，使用默认字符串填充，如果长度超过指定长度，也不截断
	 * 
	 * @param src			要分割的字符串
	 * @param separator     分隔符
	 * @param minLength     分割后数组的最小长度
	 * @param defaultString 默认字符串
	 * @return 分割后的字符串数组
	 */
	public String[] split(String src, String separator, int minLength, String defaultString) {
		List<String> l = splitToList(src, separator, minLength, defaultString);
		return null == l ? null : l.toArray(new String[l.size()]);
	}

	/**
	 * 用连接符连接列表中的项
	 * 
	 * @param list      对象列表
	 * @param separator 连接符
	 * @return 连接后的字符串
	 */
	public String join(List<?> list, String separator) {
		if (null == list || list.isEmpty()) {
			return null;
		}
		if (null == separator) {
			separator = ",";
		}
		StringBuffer sb = new StringBuffer();
		for (Object obj : list) {
			if (null != obj) {
				sb.append(separator).append(obj.toString());
			}
		}
		return sb.substring(separator.length()).toString();
	}

	/**
	 * 用连接符连接数组中的项
	 * 
	 * @param arr		对象数组
	 * @param separator 连接符
	 * @return 连接后的字符串
	 */
	public String join(Object[] arr, String separator) {
		if (null == arr || arr.length == 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (Object obj : arr) {
			if (null != obj) {
				sb.append(separator).append(obj.toString());
			}
		}
		return sb.substring(separator.length()).toString();
	}
	
	/**
	 * 安全比较两个字符串是否相同
	 * 
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @return 是否相同
	 */
	public boolean safeEquals(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	/**
	 * 安全比较两个字符串忽略大小写是否相同
	 * 
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @return 忽略大小写是否相同
	 */
	public boolean safeEqualsIgnoreCase(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
	}
}

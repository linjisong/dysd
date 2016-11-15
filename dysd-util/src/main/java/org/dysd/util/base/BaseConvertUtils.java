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
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dysd.util.Tool;
import org.dysd.util.config.BaseConfig;
import org.dysd.util.env.EnvConsts;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.util.NumberUtils;

/**
 * 类型转换工具类
 * @author linjisong
 * @version 0.0.1
 * @since 0.0.1
 * @date 2016-11-8
 */
public abstract class BaseConvertUtils {

	private static final BaseConvertUtils instance = new BaseConvertUtils(){};
	private BaseConvertUtils(){
	}
	
	/**
	 * 获取单实例
	 * @return
	 */
	/*package*/ static BaseConvertUtils getInstance(){
		return instance;
	}
	
	/**
	 * 字符串转换为布尔类型
	 * @param str 字符串   1、Y、TRUE、ON（忽略大小写）返回true，否则返回false
	 * @return 布尔值
	 */
	public boolean string2Boolean(String str){
		return string2Boolean(str, false);
	}
	
	/**
	 * 字符串转换为布尔类型
	 * @param str 字符串   1、Y、TRUE、ON（忽略大小写）返回true，否则返回false
	 * @param str为null、空白字符串时的返回值
	 * @return 布尔值
	 */
	public boolean string2Boolean(String str, boolean blankValue){
		if(Tool.CHECK.isBlank(str)){
			return blankValue;
		}
		str = str.trim();
		return "1".equals(str) || "Y".equalsIgnoreCase(str) || "TRUE".equalsIgnoreCase(str) || "ON".equalsIgnoreCase(str);
	}
	
	/**
	 * 源类型到目标类型的转换， 默认使用{@link DefaultFormattingConversionService#convert(Object, Class)}实现
	 * @param source	  对象
	 * @param targetType 目标类型
	 * @return 转换后的对象
	 */
	public <T> T convert(Object source, Class<T> targetType){
		return BaseConfig.getConversionService().convert(source, targetType);
	}
	
	/**
	 * 转换为List类型，如果不为空，只校验第一个元素的类型和目标类型是否匹配
	 * @param source      源对象
	 * @param elementType 元素类型
	 * @return 转换后的集合
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> convertToList(Object source, Class<E> elementType){
		if(null == source){
			return null;
		}else if(source.getClass().isArray()){
			List<E> list = new ArrayList<E>();
			Class<?> c = source.getClass();
			if(c.equals(int[].class)){
				int[] arr = (int[])source;
				if(arr.length == 0){
					return Collections.<E>emptyList();
				}else if(elementType.isAssignableFrom(Integer.class)){
					for(Integer a : arr){
						list.add((E)a);
					}
					return list;
				}else{
					throw new ClassCastException("cannot convert the list, the element type is int, but the target type is " + elementType);
				}
			}else if(c.equals(short[].class)){
				short[] arr = (short[])source;
				if(arr.length == 0){
					return Collections.<E>emptyList();
				}else if(elementType.isAssignableFrom(Short.class)){
					for(Short a : arr){
						list.add((E)a);
					}
					return list;
				}else{
					throw new ClassCastException("cannot convert the list, the element type is short, but the target type is " + elementType);
				}
			}else if(c.equals(long[].class)){
				long[] arr = (long[])source;
				if(arr.length == 0){
					return Collections.<E>emptyList();
				}else if(elementType.isAssignableFrom(Long.class)){
					for(Long a : arr){
						list.add((E)a);
					}
					return list;
				}else{
					throw new ClassCastException("cannot convert the list, the element type is long, but the target type is " + elementType);
				}
			}else if(c.equals(byte[].class)){
				byte[] arr = (byte[])source;
				if(arr.length == 0){
					return Collections.<E>emptyList();
				}else if(elementType.isAssignableFrom(Byte.class)){
					for(Byte a : arr){
						list.add((E)a);
					}
					return list;
				}else{
					throw new ClassCastException("cannot convert the list, the element type is byte, but the target type is " + elementType);
				}
			}else if(c.equals(boolean[].class)){
				boolean[] arr = (boolean[])source;
				if(arr.length == 0){
					return Collections.<E>emptyList();
				}else if(elementType.isAssignableFrom(Boolean.class)){
					for(Boolean a : arr){
						list.add((E)a);
					}
					return list;
				}else{
					throw new ClassCastException("cannot convert the list, the element type is boolean, but the target type is " + elementType);
				}
			}else if(c.equals(char[].class)){
				char[] arr = (char[])source;
				if(arr.length == 0){
					return Collections.<E>emptyList();
				}else if(elementType.isAssignableFrom(Character.class)){
					for(Character a : arr){
						list.add((E)a);
					}
					return list;
				}else{
					throw new ClassCastException("cannot convert the list, the element type is char, but the target type is " + elementType);
				}
			}else if(c.equals(float[].class)){
				float[] arr = (float[])source;
				if(arr.length == 0){
					return Collections.<E>emptyList();
				}else if(elementType.isAssignableFrom(Float.class)){
					for(Float a : arr){
						list.add((E)a);
					}
					return list;
				}else{
					throw new ClassCastException("cannot convert the list, the element type is float, but the target type is " + elementType);
				}
			}else if(c.equals(double[].class)){
				double[] arr = (double[])source;
				if(arr.length == 0){
					return Collections.<E>emptyList();
				}else if(elementType.isAssignableFrom(Double.class)){
					for(Double a : arr){
						list.add((E)a);
					}
					return list;
				}else{
					throw new ClassCastException("cannot convert the list, the element type is double, but the target type is " + elementType);
				}
			}else{
				Object[] arr = (Object[])source;
				if(arr.length == 0){
					return Collections.<E>emptyList();
				}else{
					Object first = arr[0];
					if(elementType.isAssignableFrom(first.getClass())){
						for(Object a : arr){
							list.add((E)a);
						}
						return list;
					}else{
						throw new ClassCastException("cannot convert the list, the element type is " + first.getClass() + ", but the target type is " + elementType);
					}
				}
			}
		}else if(source instanceof List){
			List<E> list = (List<E>)source;
			if(list.isEmpty()){
				return Collections.<E>emptyList();
			}else{
				Object first = list.get(0);
				if(elementType.isAssignableFrom(first.getClass())){
					return list;
				}else{
					throw new ClassCastException("cannot convert the list, the element type is " + first.getClass() + ", but the target type is " + elementType);
				}
			}
		}else if(source instanceof Enumeration){
			Enumeration<E> i = (Enumeration<E>)source;
			if(!i.hasMoreElements()){
				return Collections.<E>emptyList(); 
			}else{
				Object first = i.nextElement();
				if(elementType.isAssignableFrom(first.getClass())){
					List<E> rs = new ArrayList<E>();
					rs.add((E)first);
					while(i.hasMoreElements()){
						rs.add(i.nextElement());
					}
					return rs;
				}else{
					throw new ClassCastException("cannot convert the list, the element type is " + first.getClass() + ", but the target type is " + elementType);
				}
			}
		}else if(source instanceof Iterator){
			Iterator<E> i = (Iterator<E>)source;
			if(!i.hasNext()){
				return Collections.<E>emptyList(); 
			}else{
				Object first = i.next();
				if(elementType.isAssignableFrom(first.getClass())){
					List<E> rs = new ArrayList<E>();
					rs.add((E)first);
					while(i.hasNext()){
						rs.add(i.next());
					}
					return rs;
				}else{
					throw new ClassCastException("cannot convert the list, the element type is " + first.getClass() + ", but the target type is " + elementType);
				}
			}
		}else if(source instanceof Iterable){
			return convertToList(((Iterable<?>)source).iterator(), elementType);
		}else if(source instanceof Map){
			return convertToList(((Map<?,?>)source).values(), elementType);
		}else{
			return convertToList(new Object[]{source}, elementType);
		}
	}
	
	/**
	 * Number类型转换，将Number类型转换为子类型
	 * @param number		源对象
	 * @param targetClass	目标对象类型
	 * @return 转换后的子类型对象
	 */
	public <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass){
		return NumberUtils.convertNumberToTargetClass(number, targetClass);
	}
	
	/**
	 * 字符串解析为Number类型
	 * @param text			源字符串
	 * @param targetClass   目标对象类型
	 * @return 转换后的Number子类型对象
	 */
	public <T extends Number> T convertStringToTargetClass(String text, Class<T> targetClass){
		return NumberUtils.parseNumber(text, targetClass);
	}
	
	/**
	 * 转换IP地址，127.0.0.1、0:0:0:0:0:0:0:1、0.0.0.0转换为Inet4Address.getLocalHost().getHostAddress()，其它不变
	 * @param ip 原始IP地址
	 * @return 转换后的IP地址
	 */
	public String convertIp(String ip){
		if("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "0.0.0.0".equals(ip)){
			return EnvConsts.LOCALE_HOST;	
		}else{
			return ip;
		}
	}
}

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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.dysd.util.Tool;
import org.dysd.util.logger.CommonLogger;
import org.springframework.cache.Cache;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;


/**
 * 默认配置工具类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class Defaults {
	
	private static final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	private static final Cache defaultConfigCache = Tool.CACHE.getCache(Defaults.class+"defaultConfig");
	
	private static final Cache defaultValueCache = Tool.CACHE.getCache(Defaults.class+"defaultValue");
	
	static{
		synchronized(Defaults.class){
			loadDefaults();
			loadCombineDefaults();
		}
	}
	
	/**
	 * 获取默认设置
	 * @param name 配置名称
	 * @return 字符串类型的设置值
	 */
	public static String getDefaultConfig(String name){
		return getDefaultConfig(name, String.class);
	}
	
	/**
	 * 获取默认设置组
	 * @param name 配置名称
	 * @return 字符串数组类型的设置值，使用逗号分隔配置多个值
	 */
	public static Set<String> getDefaultConfigs(String name){
		return getDefaultConfigs(name, String.class);
	}
	
	/**
	 * 获取默认设置
	 * @param name 配置名称
	 * @param cls 目标类型，一般为八种基本类型和字符串
	 * @return 目标类型的设置值
	 */
	public static <E>E getDefaultConfig(String name, Class<E> cls){
		return getDefaultInner(name, cls);
	}
	
	/**
	 * 获取默认设置组
	 * @param name 配置名称
	 * @param cls  目标类型，一般为八种基本类型和字符串
	 * @return 目标类型数组的设置值，使用逗号分隔配置多个值
	 */
	public static <E>Set<E> getDefaultConfigs(String name, Class<E> cls){
		return getDefaultInners(name, cls);
	}
	
	/**
	 * 获取默认组件，将组件类型的类名称作为key值从属性文件中获取相应配置的实现类，然后实例化并返回
	 * @param cls 组件类型，一般为接口
	 * @return 配置的组件实现类
	 */
	public static <T> T getDefaultComponent(Class<T> cls){
		return getDefaultInner(cls.getName(), cls);
	}
	
	/**
	 * 获取默认组件组，将组件类型的类名称作为key值从属性文件中获取相应配置的实现类，然后实例化并返回
	 * @param cls 组件类型，一般为接口
	 * @return 配置的组件实现类组，使用逗号分隔配置多个值
	 */
	public static <T> Set<T> getDefaultComponents(Class<T> cls){
		return getDefaultInners(cls.getName(), cls);
	}
	
	/**
	 * 获取默认组件，将组件类型的类名称加井号#，再加上name作为key值从属性文件中获取相应配置的实现类，然后实例化并返回
	 * @param cls  组件类型，一般为接口
	 * @param name 名称
	 * @return 配置的组件实现类组
	 */
	public static <T> T getDefaultComponent(Class<T> cls, String name){
		return getDefaultInner(cls.getName()+ "#" + name, cls);
	}
	
	/**
	 * 获取默认组件组，将组件类型的类名称加井号#，再加上name作为key值从属性文件中获取相应配置的实现类，然后实例化并返回
	 * @param cls  组件类型，一般为接口
	 * @param name 名称
	 * @return 配置的组件实现类组，使用逗号分隔配置多个值
	 */
	public static <T> Set<T> getDefaultComponents(Class<T> cls, String name){
		return getDefaultInners(cls.getName()+ "#" + name, cls);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private static <T> T getDefaultInner(String name, Class<T> cls){
		T rs = defaultValueCache.get(name, cls);
		if(null == rs){
			synchronized(defaultValueCache){
				rs = defaultValueCache.get(name, cls);
				if(null == rs){
					String vp = defaultConfigCache.get(name, String.class);
					if(Tool.CHECK.isBlank(vp)){
						if(cls.equals(String.class) && null != vp){
							rs = cls.cast("");
						}else{
							rs = null;
						}
					}else if(String.class.equals(cls)){
						rs = cls.cast(vp);
					}else if(boolean.class.equals(cls) || Boolean.class.equals(cls)){
						Boolean b = Tool.CONVERT.string2Boolean(vp);
						return (T)b; 
					}else if(cls.isPrimitive()){
						rs = Tool.CONVERT.convert(vp, cls);
					}else if(Number.class.isAssignableFrom(int.class)){
						rs = cls.cast(Tool.CONVERT.convertStringToTargetClass(vp, (Class<? extends Number>)cls));
					}else if(Enum.class.isAssignableFrom(cls)){
						rs = cls.cast(Enum.valueOf((Class)cls, vp));
					}else{
						rs = cls.cast(Tool.REFLECT.newInstance(vp));
					}
					defaultConfigCache.evict(name);
					defaultValueCache.put(name, rs);
				}
			}
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> Set<T> getDefaultInners(String name, Class<T> cls){
		Set<T> rs = defaultValueCache.get(name, Set.class);
		if(null == rs){
			synchronized(defaultValueCache){
				rs = defaultValueCache.get(name, Set.class);
				if(null == rs){
					String vp = defaultConfigCache.get(name, String.class);
					if(Tool.CHECK.isBlank(vp)){
						if(null != vp){
							rs = new LinkedHashSet<T>();
						}else{
							rs = null;
							CommonLogger.debug("the default value of ["+name+"] is null");
						}
					}else if("[]".equals(vp)){
						rs = new LinkedHashSet<T>();
					}else{
						Set<String> names = new LinkedHashSet<String>(Arrays.asList(vp.split("\\s*,\\s*")));
						rs = new LinkedHashSet<T>(names.size());
						for(String n : names){
							if(cls.equals(String.class)){
								rs.add(cls.cast(n));
							}else{
								rs.add(cls.cast(Tool.REFLECT.newInstance(n)));
							}
						}
					}
					defaultConfigCache.evict(name);
					defaultValueCache.put(name, rs);
				}
			}
		}
		return rs;
	}
	
	/**
	 * 加载默认配置
	 */
	private static void loadDefaults(){
		/**
		 * 加载平台包下面的defaults.properties文件
		 */
		List<Properties> list = loadProperties("classpath*:defaults/dysd-defaults.properties");
		if(null == list){
			return;
		}
		
		for(Properties p : list){
			for(String key : p.stringPropertyNames()){
				defaultConfigCache.put(key, p.getProperty(key));
			}
		}
	}
	
	/**
	 * 加载需要将多个文件组合在一起的默认配置
	 */
	private static void loadCombineDefaults(){
		/**
		 * 加载平台包下面的combineDefaults.properties文件
		 */
		List<Properties> list = loadProperties("classpath*:defaults/dysd-combine-defaults.properties");
		if(null == list){
			return;
		}
		
		Map<String, Set<String>> combines = new HashMap<String, Set<String>>();
		for(Properties p : list){
			for(String key : p.stringPropertyNames()){
				String value = p.getProperty(key);
				if(!Tool.CHECK.isBlank(value)){
					String[] values = value.split("\\s*,\\s*");
					for(String v: values){
						if(!Tool.CHECK.isBlank(v)){
							Set<String> l = combines.get(key);
							if(null == l){
								l = new LinkedHashSet<String>();
								combines.put(key, l);
							}
							l.add(v);
						}
					}
				}
			}
		}
		for(String key : combines.keySet()){
			Set<String> l = combines.get(key);
			if(null != l && !l.isEmpty()){
				StringBuffer sb = new StringBuffer();
				for(String s : l){
					sb.append(",").append(s);
				}
				defaultConfigCache.put(key, sb.substring(1));
			}
		}
	}

	private static List<Properties> loadProperties(String locationPattern) {
		try{
			Resource[] resources = resolver.getResources(locationPattern);
			if(null != resources){
				List<Properties> list = new ArrayList<Properties>();
				for(Resource resource : resources){
					InputStream input = null;
					try{
						Properties properties = new Properties();
						input = resource.getInputStream();
						properties.load(input);
						list.add(properties);
					}catch(Exception e){
						// ignore
					}finally{
						Tool.IO.closeQuietly(input);
					}
				}
				/**
				 * 根据配置文件中的order排序
				 */
				Collections.sort(list, new Comparator<Properties>(){
					@Override
					public int compare(Properties o1, Properties o2) {
						return Integer.parseInt(o2.getProperty("order", "0"))-Integer.parseInt(o1.getProperty("order", "0"));
					}
				});
				for(Properties p : list){
					p.remove("order");
				}
				return list;
			}
		}catch(IOException ignore){
		}
		return null;
	}
}

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

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

/**
 * 简单缓存工具类，缓存至内存，适用于小对象的缓存
 * @author linjisong
 * @version 0.0.1
 * @since 0.0.1
 * @date 2016-11-8
 */
public abstract class BaseCacheUtils {

	private static final BaseCacheUtils instance = new BaseCacheUtils(){};
	private static final CacheManager cacheManager = new ConcurrentMapCacheManager();
	private BaseCacheUtils(){
	}
	
	/**
	 * 获取单实例
	 * @return
	 */
	/*package*/ static BaseCacheUtils getInstance(){
		return instance;
	}
	
	/**
	 * 获取缓存
	 * @param name 名称
	 * @return 缓存
	 */
	public Cache getCache(String name){
		return getCache(null, name);
	}
	
	/**
	 * 获取缓存
	 * @param cls 以cls.getName()作为缓存名称
	 * @return 缓存
	 */
	public Cache getCache(Class<?> cls){
		return getCache(cls, null);
	}
	
	/**
	 * 获取缓存
	 * @param cls 以cls.getName() + "###" + name作为缓存名称
	 * @param name
	 * @return 缓存
	 */
	public Cache getCache(Class<?> cls, String name){
		return getManager().getCache(resolveCacheName(cls, name));
	}
	
	/**
	 * 获取缓存实体值
	 * @param name 缓存名称
	 * @param key  实体键值
	 * @return 缓存实体值
	 */
	public Object getCacheValue(String name, Object key){
		Cache cache = getCache(name);
		return resolveCacheValue(cache, key);
	}
	
	/**
	 * 获取缓存实体值
	 * @param cls 以cls.getName()作为缓存名称
	 * @param key 实体键值
	 * @return 缓存实体值
	 */
	public Object getCacheValue(Class<?> cls, Object key){
		Cache cache = getCache(cls);
		return resolveCacheValue(cache, key);
	}
	
	/**
	 * 获取缓存实体值
	 * @param cls  以cls.getName() + "###" + name作为缓存名称
	 * @param name
	 * @param key  缓存键值
	 * @return 缓存实体值
	 */
	public Object getCacheValue(Class<?> cls, String name, Object key){
		Cache cache = getCache(cls, name);
		return resolveCacheValue(cache, key);
	}
	
	/**
	 * 获取所有缓存名称集合
	 * @return 缓存名称集合
	 */
	public Collection<String> getCacheNames(){
		return getManager().getCacheNames();
	}
	
	/**
	 * 获取缓存管理器
	 * @return 缓存管理器
	 */
	public CacheManager getManager(){
		return cacheManager;
	}
	
	private Object resolveCacheValue(Cache cache, Object key){
		if(null == cache || null == key){
			return null;
		}
		ValueWrapper vm = cache.get(key);
		return vm == null ? null : vm.get();
	}
	
	private String resolveCacheName(Class<?> cls, String name){
		if(null == cls){
			return name;
		}else if(null == name){
			return cls.getName();
		}else {
			return cls.getName() + "###" + name;
		}
	}
}

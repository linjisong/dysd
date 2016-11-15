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

import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomStringUtils;
import org.dysd.util.Tool;
import org.dysd.util.exception.meta.IExceptionMeta;
import org.dysd.util.exception.meta.IExceptionMetaLoader;
import org.dysd.util.logger.CommonLogger;
import org.springframework.cache.Cache;

/**
 * 抽象的异常元信息加载器实现类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class AbstractExceptionMetaLoader implements IExceptionMetaLoader{

	/**
	 * 异常代码校验正则表达式
	 */
	protected static final Pattern exceptionCodePatter = Pattern.compile("^[0-9A-Za-z]{10}$");
	
	/**
	 * 根据异常代码的缓存，所有子类共享
	 */
	private static final Cache codeMetaCache = Tool.CACHE.getCache(IExceptionMeta.class, "code");
	
	/**
	 * 根据异常类型的缓存，所有子类共享
	 */
	private static final Cache classMetaCache =Tool.CACHE.getCache(IExceptionMeta.class, "type");
	
	/**
	 * 查找异常元信息
	 */
	@Override
	public IExceptionMeta lookup(String code, Throwable cause) {
		IExceptionMeta meta = null;
		if(null != code){
			meta = codeMetaCache.get(code, IExceptionMeta.class);
		}
		if(null == meta && null != cause){
			Class<?> oCls = cause.getClass();
			meta = classMetaCache.get(oCls, IExceptionMeta.class);
			if(null != meta){
				return meta;
			}else{
				Class<?> cls = oCls;
				while(!Object.class.equals(cls)){
					meta = classMetaCache.get(cls, IExceptionMeta.class);
					if(null != meta){
						classMetaCache.put(oCls, meta);
						return meta;
					}
					cls = cls.getSuperclass();
				}
			}
		}
		return meta;
	}
	
	/**
	 * 生成异常代码，只有不是底层异常配置才允许异常代码为空，如果异常代码为空，使用该方法生成
	 * @return
	 */
	protected String generateExceptionCode(){
		return "##" + RandomStringUtils.randomNumeric(8); 
	}
	
	/**
	 * 校验异常代码
	 * @param code
	 */
	protected void validateExceptionCode(String code){
		if(null != codeMetaCache.get(code)){
			throw new IllegalArgumentException("found duplicate exception code: " + code);
		}else if(!exceptionCodePatter.matcher(code).find()){
			throw new IllegalArgumentException("the exception code is incorrect: " + code);
		}
	}
	
	/**
	 * 按代码缓存元信息
	 * @param code
	 * @param meta
	 */
	protected void cacheMetaByCode(String code, IExceptionMeta meta){
		codeMetaCache.put(code, meta);
	}
	
	/**
	 * 按类型缓存元信息
	 * @param cls
	 * @param meta
	 */
	protected void cacheMetaByType(Class<?> cls, IExceptionMeta meta){
		classMetaCache.put(cls, meta);
	}
	
	/**
	 * 记录异常加载日志
	 * @param meta
	 */
	protected void logExceptionMeta(IExceptionMeta meta){
		CommonLogger.info(meta.toString());
	}
}

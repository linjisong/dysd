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

import java.util.List;

import org.dysd.util.exception.meta.IExceptionMeta;
import org.dysd.util.exception.meta.IExceptionMetaLoader;

/**
 * 组合的异常元信息加载器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class CompositeExceptionMetaLoader implements IExceptionMetaLoader{

	private List<IExceptionMetaLoader> loaders;
	
	public CompositeExceptionMetaLoader() {
	}
	
	public CompositeExceptionMetaLoader(List<IExceptionMetaLoader> loaders) {
		this.loaders = loaders;
	}
	
	public List<IExceptionMetaLoader> getLoaders() {
		return loaders;
	}

	public void setLoaders(List<IExceptionMetaLoader> loaders) {
		this.loaders = loaders;
	}

	/**
	 * 查找异常元信息
	 * 逐个使用内部的加载器查找，一旦找到就返回
	 */
	@Override
	public IExceptionMeta lookup(String code, Throwable cause) {
		List<IExceptionMetaLoader> loaders = getLoaders();
		if(null != loaders){
			for(IExceptionMetaLoader loader : loaders){
				IExceptionMeta meta = loader.lookup(code, cause);
				if(null != meta){
					return meta;
				}
			}
		}
		return null;
	}
}

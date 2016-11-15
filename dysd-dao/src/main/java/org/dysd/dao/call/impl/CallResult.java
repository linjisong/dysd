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
package org.dysd.dao.call.impl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dysd.dao.call.ICallResult;
import org.dysd.dao.exception.DaoExceptionCodes;
import org.dysd.util.exception.Throw;

/**
 * 存储过程的返回结果
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class CallResult implements ICallResult{
	
	private final Map<String, Object> resultMaps = new LinkedHashMap<String, Object>();
	
	/**
	 * 添加一个返回结果
	 * @param name   参数名称
	 * @param result 返回结果
	 */
	public void addResult(String name, Object result){
		resultMaps.put(name, result);
	}
	
	/**
	 * 添加一组返回结果
	 * @param results 一组返回结果
	 */
	public void addAllResult(Map<String, Object> results){
		resultMaps.putAll(results);
	}
	
	/**
	 * 根据输出参数名称获取返回结果
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getOutputParam(String name) {
		if(!resultMaps.containsKey(name)){
			Throw.createException(DaoExceptionCodes.DYSD020001, name);
		}
		return (T)resultMaps.get(name);
	}
	
	/**
	 * 返回输出参数名称的迭代器
	 */
	@Override
	public Iterator<String> iterator(){
		return resultMaps.keySet().iterator();
	}
}
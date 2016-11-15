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
package org.dysd.dao.util;

import java.util.HashMap;
import java.util.Map;

import org.dysd.util.Tool;

/**
 * 数据库驱动工具类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class BaseDriverUtilsImpl{

	private static final BaseDriverUtilsImpl instance = new BaseDriverUtilsImpl(){};
	private BaseDriverUtilsImpl(){
	}
	
	static BaseDriverUtilsImpl getInstance(){
		return instance;
	}
	
	private static final Map<String, Class<?>> driver = new HashMap<String, Class<?>>();
	
	/**
	 * 加载数据库驱动
	 * @param driverClassName
	 * @return
	 */
	public boolean load(String driverClassName) {
		if(!driver.containsKey(driverClassName)){
			synchronized(driver){
				if(!driver.containsKey(driverClassName)){
					try{
						Class<?> cls = Tool.REFLECT.forName(driverClassName);
						driver.put(driverClassName, cls);
						return true;
					}catch(Exception e){
						driver.put(driverClassName, null);
						return false;
					}
				}else{
					return null != driver.get(driverClassName);
				}
			}
		}else{
			return null != driver.get(driverClassName);
		}
	}
}

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

/**
 * JavaBean工具类，暂未添加具体的方法，拟加入commons-beanutils和Spring中BeanWrapper类似方法
 * @author linjisong
 * @version 0.0.1
 * @since 0.0.1
 * @date 2016-11-8
 */
public abstract class BaseBeanUtils {

	private static final BaseBeanUtils instance = new BaseBeanUtils(){};
	private BaseBeanUtils(){
	}
	
	/**
	 * 获取单实例
	 * @return
	 */
	/*package*/ static BaseBeanUtils getInstance(){
		return instance;
	}
}

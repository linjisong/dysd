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
package org.dysd.dao.page;

/**
 * 分页对象工厂接口
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface IPageFactory {

	/**
	 * 根据上下文环境创建分页对象
	 * @return 分页对象
	 */
	public IPage createPage();
	
	/**
	 * 根据上下文环境创建分页对象（适用于多个分页对象）
	 * @param pageKey 表示分页类型的键值
	 * @return 分页对象
	 */
	public IPage createPage(String pageKey);
}

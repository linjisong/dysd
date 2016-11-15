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
package org.dysd.dao.mybatis.schema.function;

/**
 * SQL配置函数
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface ISqlConfigFunction {
	
	/**
	 * 优先级，如果有多个同名函数，使用order值小的
	 * @return
	 */
	public int getOrder();
	
	/**
	 * 函数名称
	 * @return
	 */
	public String getName();
	
	/**
	 * 执行SQL配置函数
	 * @param databaseId 数据库ID
	 * @param args       字符串参数
	 * @return 
	 */
	public String eval(String databaseId, String[] args);
}

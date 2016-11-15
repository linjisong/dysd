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
package org.dysd.dao.dialect;

/**
 * 数据库方言接口
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface IDialect {
	
	/**
	 * 获取数据库类型
	 * @return
	 */
	public String getType();
	
	/**
	 * 获取可能的数据库驱动类名称
	 * @return 驱动类名数组
	 */
	public String[] getDriverClassNames();
	
	/**
	 * 是否匹配databaseId
	 * @param databaseId
	 * @return
	 */
	public boolean match(String databaseId);
	
	/**
	 * 生成计算总记录数的SQL
	 * @param sql 原始SQL
	 * @return 计算总记录数的SQL
	 */
	public String getTotalSql(String sql);
	
	/**
	 * 获取查询指定范围记录的SQL
	 * @param sql     原始SQL
	 * @param offset  返回的开始记录索引
	 * @param limit   查询的数据大小
	 * @return 查询第(offset, offset + limit]条记录的SQL，索引从1开始
	 */
	public String getScopeSql(String sql, long offset, int limit);
}

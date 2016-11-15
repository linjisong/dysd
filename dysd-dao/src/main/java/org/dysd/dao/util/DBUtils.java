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

import java.sql.Connection;

/**
 * DB帮助类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface DBUtils {
	
	/**
	 * 驱动
	 */
	BaseDriverUtilsImpl Driver = BaseDriverUtilsImpl.getInstance();
	
	/**
	 * 元信息
	 */
	BaseMetaUtilsImpl Meta = BaseMetaUtilsImpl.getInstance();
	
	/**
	 * 连接
	 */
	BaseConnectionUtilsImpl Connection = BaseConnectionUtilsImpl.getInstance();
	
	/**
	 * 语句
	 */
	BaseStatementUtilsImpl Statement = BaseStatementUtilsImpl.getInstance();
	
	/**
	 * 结果集
	 */
	BaseResultSetUtilsImpl ResultSet = BaseResultSetUtilsImpl.getInstance();
	
	/**
	 * 关闭
	 */
	BaseCloseUtilsImpl Closer = BaseCloseUtilsImpl.getInstance();
	
	/**
	 * 数据库连接回调接口
	 */
	interface IConnectionCallback<T> {

		/**
		 * 回调方法
		 * 
		 * @param conn 数据库连接
		 * @return 回调返回的结果
		 */
		T callback(Connection conn);
	}
}

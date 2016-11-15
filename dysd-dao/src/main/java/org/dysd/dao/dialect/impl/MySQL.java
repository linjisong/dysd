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
package org.dysd.dao.dialect.impl;

import org.dysd.dao.dialect.IDialect;

/**
 * MySQL方言
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class MySQL extends AbstractDialect{

	private static final IDialect instance = new MySQL(){};//唯一实例
	
	/**
	 * 构造函数私有化
	 */
	private MySQL(){
		super.setType("MySQL");
		super.setDriverClassName("com.mysql.jdbc.Driver");
	}
	
	/**
	 * 获取唯一实例
	 */
	public static IDialect getInstance() {
		return instance;
	}

	public String getTotalSql(String sql) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT(1) FROM (").append(sql).append(") T");
		return sb.toString();
	}

	public String getScopeSql(String sql, long offset, int limit) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM (").append(sql).append(") T LIMIT ")
				.append(offset).append(",").append(limit);
		return sb.toString();
	}
}

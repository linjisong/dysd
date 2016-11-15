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

import java.util.regex.Pattern;

import org.dysd.dao.dialect.IDialect;

/**
 * SybaseIQ方言
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class SybaseIQ extends AbstractDialect{
	
	private static final IDialect instance = new SybaseIQ(){};//唯一实例
	
	private final static Pattern SELECT = Pattern.compile("^\\s*select\\s+", Pattern.CASE_INSENSITIVE);
	
	private final static Pattern FROM = Pattern.compile("\\s+from\\s+", Pattern.CASE_INSENSITIVE);
	
	private final static Pattern ORDER = Pattern.compile("\\s+order\\s+by\\s+(?![^\\)]+\\))[^\\)]+$", Pattern.CASE_INSENSITIVE);
	
	/**
	 * 构造函数私有化
	 */
	private SybaseIQ(){
		super.setType("SybaseIQ");
		super.setDriverClassName("com.sybase.jdbc3.jdbc.SybDriver");
	}
	
	/**
	 * 获取唯一实例
	 */
	public static IDialect getInstance() {
		return instance;
	}

	public String getTotalSql(String sql) {
		StringBuilder rs = new StringBuilder();
		sql = ORDER.matcher(sql).replaceFirst("");
		rs.append("select count(1) total_ from (").append(sql).append(") total_ ");
		return rs.toString();
	}

	public String getScopeSql(String sql, long offset, int limit) {
		String tempTableName = "#temptablename";
		sql = SELECT.matcher(sql).replaceFirst("SELECT TOP " + (offset + limit) + " ");
		sql = FROM.matcher(sql).replaceFirst(",PAGE_RECORD_ROW_NUMBER=NUMBER(*) INTO " + tempTableName + " FROM ");
		sql +=  " set chained off  SELECT * FROM " + tempTableName + " WHERE PAGE_RECORD_ROW_NUMBER > " + offset;
		sql += " drop table " + tempTableName ;
		return sql;
	}
}

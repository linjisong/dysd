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
 * SybaseASE方言
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class SybaseASE extends AbstractDialect {

	private final static Pattern SELECT = Pattern.compile("select", Pattern.CASE_INSENSITIVE);

	// private final static Pattern FROM = Pattern.compile("from", Pattern.CASE_INSENSITIVE);

	// private final static Pattern ORDER = Pattern.compile("order by", Pattern.CASE_INSENSITIVE);

	private static final IDialect instance = new SybaseASE(){};// 唯一实例

	/**
	 * 构造函数私有化
	 */
	private SybaseASE() {
		super.setType("SybaseASE");
		super.setDriverClassName("com.sybase.jdbc3.jdbc.SybDriver");
	}

	/**
	 * 获取唯一实例
	 */
	public static IDialect getInstance() {
		return instance;
	}

	/**
	 * 生成计算总记录数的SQL
	 * 
	 * @param sql
	 * @return
	 */
	public String getTotalSql(String sql) {
		StringBuilder rs = new StringBuilder();
		int index = sql.toLowerCase().lastIndexOf("order by");
		if (-1 != index) {
			sql = sql.substring(0, index);
		}
		// sql = ORDER.matcher(sql).replaceAll("");//去掉子查询中的order by语句
		rs.append("select count(1) total_ from (").append(sql)
				.append(") total_ ");
		return rs.toString();
	}

	/**
	 * 获取查询指定范围记录的SQL
	 * 
	 * @param sql
	 * @param offset
	 * @param limit
	 * @return
	 */
	public String getScopeSql(String sql, long offset, int limit) {
		sql = SELECT.matcher(sql).replaceFirst("select sybid=identity(12),");// 替换第一个匹配的select
		int index = sql.toLowerCase().indexOf("from");
		sql = sql.substring(0, index) + "into #temptable1 "
				+ sql.substring(index);// 替换最后一个匹配的from
		// sql = FROM.matcher(sql).replaceFirst("into #temptable1 from");
		sql = " set chained off " + sql
				+ " select * from #temptable1 where sybid> " + offset
				+ " and sybid <= " + (offset + limit);
		sql += " drop table #temptable1 ";
		return sql;
	}
}

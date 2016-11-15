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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.dysd.util.exception.Throw;

/**
 * 数据库连接关闭工具类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class BaseCloseUtilsImpl{

	private static final BaseCloseUtilsImpl instance = new BaseCloseUtilsImpl(){};
	private BaseCloseUtilsImpl(){
	}
	
	static BaseCloseUtilsImpl getInstance(){
		return instance;
	}
	
	/**
	 * 关闭连接
	 * @param conn
	 */
	public void close(Connection conn) {
		try {
			if(null != conn && !conn.isClosed())
			{
				conn.close();
			}
		} catch (SQLException e) {
			Throw.throwException(e);
		}
	}

	/**
	 * 关闭结果集
	 * @param rs
	 */
	public void close(ResultSet rs) {
		try {
			if(null != rs)
			{
				rs.close();
			}
		} catch (SQLException e) {
			Throw.throwException(e);
		}
	}

	/**
	 * 关闭语句
	 * @param stat
	 */
	public void close(Statement stat) {
		try {
			if(null != stat)
			{
				stat.close();
			}
		} catch (SQLException e) {
			Throw.throwException(e);
		}
	}

	/**
	 * 关闭语句和结果集
	 * @param stat
	 * @param rs
	 */
	public void close(Statement stat, ResultSet rs) {
		close(rs);
		close(stat);
	}

	/**
	 * 关闭连接、语句和结果集
	 * @param conn
	 * @param stat
	 * @param rs
	 */
	public void close(Connection conn, Statement stat, ResultSet rs) {
		close(rs);
		close(stat);
		close(conn);
	}
	
	/**
	 * 关闭连接和语句
	 * @param conn
	 * @param stat
	 */
	public void close(Connection conn, Statement stat) {
		close(stat);
		close(conn);
	}
}

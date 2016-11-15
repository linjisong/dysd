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
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dysd.dao.util.DBUtils.IConnectionCallback;
import org.dysd.util.exception.Throw;

/**
 * 数据库元信息工具类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class BaseMetaUtilsImpl{

	private static final BaseMetaUtilsImpl instance = new BaseMetaUtilsImpl(){};
	private BaseMetaUtilsImpl(){
	}
	
	static BaseMetaUtilsImpl getInstance(){
		return instance;
	}
	
	/**
	 * 获取数据库元信息
	 * 
	 * @param dataSource
	 *            数据源
	 * @return 数据库元信息
	 */
	public DatabaseMetaData getDatabaseMetaData(DataSource dataSource) {
		return DBUtils.Connection.doInConnection(dataSource, new IConnectionCallback<DatabaseMetaData>() {
			@Override
			public DatabaseMetaData callback(Connection conn) {
				return getDatabaseMetaData(conn);
			}
		});
	}

	/**
	 * 获取数据库元信息
	 * 
	 * @param conn 数据库连接
	 * @return 数据库元信息
	 */
	public DatabaseMetaData getDatabaseMetaData(Connection conn) {
		try {
			return conn.getMetaData();
		} catch (SQLException e) {
			throw Throw.createException(e);
		}
	}

	/**
	 * 获取数据库产品名称
	 * 
	 * @param dataSource 数据源
	 * @return 数据库产品名称
	 */
	public String getDatabaseProductName(DataSource dataSource) {
		return DBUtils.Connection.doInConnection(dataSource, new IConnectionCallback<String>() {
			@Override
			public String callback(Connection conn) {
				return getDatabaseProductName(conn);
			}
		});
	}

	/**
	 * 获取数据库产品名称
	 * 
	 * @param conn 数据库连接
	 * @return 数据库产品名称
	 */
	public String getDatabaseProductName(Connection conn) {
		try {
			return conn.getMetaData().getDatabaseProductName();
		} catch (SQLException e) {
			throw Throw.createException(e);
		}
	}

	/**
	 * 判断表是否存在
	 * 
	 * @param dataSource 数据源
	 * @param tableName 表名称
	 * @return 表是否存在
	 */
	public boolean exist(DataSource dataSource, final String tableName) {
		return DBUtils.Connection.doInConnection(dataSource, new IConnectionCallback<Boolean>() {
			@Override
			public Boolean callback(Connection conn) {
				return exist(conn, tableName);
			}
		});
	}

	/**
	 * 判断表是否存在
	 * 
	 * @param conn 数据库连接
	 * @param tableName 表名称
	 * @return 表是否存在
	 */
	public boolean exist(Connection conn, String tableName) {
		ResultSet rs = null;
		try {
			if (null == tableName) {
				return false;
			}
			DatabaseMetaData meta = conn.getMetaData();
			String[] t = getCatalogSchemaTable(tableName);
			rs = meta.getTables(t[0], t[1], t[2], new String[] { "TABLE" });
			return rs.next();
		} catch (SQLException e) {
			throw Throw.createException(e);
		} finally {
			DBUtils.Closer.close(rs);
		}
	}

	/**
	 * 判断一组表是否存在
	 * 
	 * @param dataSource 数据源
	 * @param tableNames 表名称数组
	 * @return 表示表是否存在的数组，和表名称一一对应
	 */
	public boolean[] exist(DataSource dataSource, final String[] tableNames) {
		return DBUtils.Connection.doInConnection(dataSource, new IConnectionCallback<boolean[]>() {
			@Override
			public boolean[] callback(Connection conn) {
				return exist(conn, tableNames);
			}
		});
	}

	/**
	 * 判断一组表是否存在
	 * 
	 * @param conn 数据库连接
	 * @param tableNames 表名称数组
	 * @return 表示表是否存在的数组，和表名称一一对应
	 */
	public boolean[] exist(Connection conn, String[] tableNames) {
		if (null == tableNames || 0 == tableNames.length) {
			return null;
		}
		boolean[] r = new boolean[tableNames.length];
		for (int i = 0, l = tableNames.length; i < l; i++) {
			String tableName = tableNames[i];
			r[i] = exist(conn, tableName);
		}
		return r;
	}

	private String[] getCatalogSchemaTable(String tableName) {
		String catalog = null;
		String schema = null;
		int index = tableName.lastIndexOf('.');
		if (-1 != index) {
			schema = tableName.substring(0, index);
			tableName = tableName.substring(index + 1);
			index = schema.lastIndexOf('.');
			if (-1 != index) {
				catalog = schema.substring(0, index);
				schema = schema.substring(index + 1);
			}
		}
		return new String[] { catalog, schema, tableName };
	}
}

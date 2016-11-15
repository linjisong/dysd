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
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dysd.dao.util.DBUtils.IConnectionCallback;
import org.dysd.util.exception.Throw;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * 数据库连接工具类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class BaseConnectionUtilsImpl{

	private static final BaseConnectionUtilsImpl instance = new BaseConnectionUtilsImpl(){};
	private BaseConnectionUtilsImpl(){
	}
	
	static BaseConnectionUtilsImpl getInstance(){
		return instance;
	}
	
	/**
	 * 获取数据库连接
	 * @param dataSource
	 * @return
	 */
	public Connection get(DataSource dataSource){
		try{
			return DataSourceUtils.getConnection(dataSource);
		}catch(Exception e){
			throw Throw.createException(e);
		}
	}
	
	/**
	 * 释放数据库连接
	 * @param dataSource
	 * @return
	 */
	public void release(Connection conn, DataSource dataSource){
		DataSourceUtils.releaseConnection(conn, dataSource);
	}
	
	/**
	 * 获取数据库直连连接
	 * @param driver
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public Connection get(String driver, String url, String username, String password){
		try{
			DBUtils.Driver.load(driver);
			return DriverManager.getConnection(url, username, password);
		}catch(Exception e){
			throw Throw.createException(e);
		}
	}
	
	/**
	 * 提交
	 * @param conn
	 */
	public void commit(Connection conn) {
		try {
			if(null != conn)
			{
				conn.commit();
			}
		} catch (SQLException e) {
			throw Throw.createException(e);
		}
	}

	/**
	 * 回滚
	 * @param conn
	 */
	public void rollback(Connection conn) {
		try {
			if(null != conn)
			{
				conn.rollback();
			}
		} catch (SQLException e) {
			throw Throw.createException(e);
		}
	}
	
	/**
	 * 开始事务
	 * @param conn
	 * @param newTransaction
	 */
	public void beginTransaction(Connection conn, boolean newTransaction) {
		if(newTransaction){
			this.commit(conn);
			this.setAutoCommit(conn, false);
		}else{
			this.setAutoCommit(conn, false);
		}
	}
	
	/**
	 * 结束事务
	 * @param conn
	 */
	public void endTransaction(Connection conn) {
		this.commit(conn);
		this.setAutoCommit(conn, true);
	}
	
	/**
	 * 设置是否自动提交
	 * @param conn
	 * @param autoCommit
	 */
	public void setAutoCommit(Connection conn, boolean autoCommit) {
		try {
			if(null != conn)
			{
				conn.setAutoCommit(autoCommit);
			}
		} catch (SQLException e) {
			throw Throw.createException(e);
		}
	}
	
	/**
	 * 在数据库连接中执行
	 * 
	 * @param dataSource 数据源
	 * @param callback   回调接口实例
	 * @return 回调返回的结果
	 */
	public <T> T doInConnection(DataSource dataSource, IConnectionCallback<T> callback) {
		Connection conn = null;
		try {
			conn = DataSourceUtils.getConnection(dataSource);
			return callback.callback(conn);
		} finally {
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
	}
}

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

import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import org.dysd.dao.DaoConfig;
import org.dysd.dao.exception.DaoExceptionCodes;
import org.dysd.dao.util.DBUtils;
import org.dysd.dao.util.DBUtils.IConnectionCallback;
import org.dysd.util.Tool;
import org.dysd.util.exception.Throw;

/**
 * 数据库方言帮助类
 * @author linjisong
 * @version 0.0.1
 * @since 0.0.1
 * @date 2016-11-15
 */
public class Dialects {
	
	/**
	 * 根据数据源获取方言
	 * @param dataSource 数据源
	 * @return 数据库方言
	 */
	public static IDialect getDialect(DataSource dataSource) {
		return DBUtils.Connection.doInConnection(dataSource, new IConnectionCallback<IDialect>(){
			@Override
			public IDialect callback(Connection conn) {
				return getDialect(conn);
			}
		});
	}
	
	/**
	 * 根据数据库连接获取方言
	 * @param conn 数据库连接
	 * @return 数据库方言
	 */
	public static IDialect getDialect(Connection conn) {
		String databaseProductName = DBUtils.Meta.getDatabaseProductName(conn);
		return getDialect(databaseProductName);
	}
	
	/**
	 * 根据数据库产品名称获取数据库方言
	 * @param databaseProductName 数据库产品名称
	 * @return 数据库方言
	 */
	public static IDialect getDialect(String databaseProductName){
		Map<String, IDialect> databaseProductNameDialectMapping = DaoConfig.getDatabaseProductNameDialectMapping();
		if(Tool.CHECK.isBlank(databaseProductName)){
			Throw.throwException(DaoExceptionCodes.DYSD020002);
		}else if(null == databaseProductNameDialectMapping){
			Throw.throwException(DaoExceptionCodes.DYSD020003);
		}else{
			for(String key : databaseProductNameDialectMapping.keySet()){
				if(null != key && -1 != databaseProductName.toLowerCase().indexOf(key.toLowerCase())){
					return databaseProductNameDialectMapping.get(key);
				}
			}
			Throw.throwException(DaoExceptionCodes.DYSD020004, databaseProductName);
		}
		return null;
	}
}

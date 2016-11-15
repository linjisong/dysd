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
package org.dysd.dao.mybatis;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.dysd.dao.dialect.Dialects;
import org.dysd.dao.dialect.IDialect;

/**
 * 使用数据库方言实现的数据库提供商接口实现类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class DialectDatabaseIdProvider implements DatabaseIdProvider {

	/**
	 * 获取数据库产品的简写名称
	 */
	@Override
	public String getDatabaseId(DataSource dataSource) throws SQLException{
		IDialect dialect = Dialects.getDialect(dataSource);
		return dialect.getType();
	}

	@Override
	public void setProperties(Properties p) {
	}
}

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

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dysd.util.Tool;
import org.dysd.util.exception.Throw;

/**
 * 数据库结果集工具类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class BaseResultSetUtilsImpl{

	private static final BaseResultSetUtilsImpl instance = new BaseResultSetUtilsImpl(){};
	private BaseResultSetUtilsImpl(){
	}
	
	static BaseResultSetUtilsImpl getInstance(){
		return instance;
	}
	
	/**
	 * 将结果集转换为List<Map<String, Object>>
	 * @param rs 结果集对象
	 * @return List<Map<String, Object>>对象
	 */
	public List<Map<String, Object>> handlerResultSet(ResultSet rs) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try{
			if(null != rs){
				String[][] fields = getFields(rs);
				int length = fields[0].length;
				while(rs.next()){
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					for(int i=0; i<length; i++){
						Object value = rs.getObject(fields[0][i]);
						map.put(fields[1][i], rs.wasNull()? null : value);
					}
					list.add(map);
				}
			}
		}catch(SQLException e){
			Throw.throwException(e);
		}
		return list;
	}
	
	/**
	 * 获取结果集的字段信息
	 * @param rs 结果集对象
	 * @return 字段信息的二维数组，如[["CODE","NAME","FIELD_NAME"],["code","name", "fieldName"]]，其中第一个数组为SQL中字段，第二个数组为相应的驼峰式属性名
	 */
	public String[][] getFields(ResultSet rs){
		String[][] fields = null;
		try{
			ResultSetMetaData meta = rs.getMetaData();
			int length = meta.getColumnCount();
			fields = new String[2][length];
			for(int i=1; i<=length; i++){
				fields[0][i-1] = meta.getColumnLabel(i); 
				fields[1][i-1] = Tool.STRING.convertToCamel(fields[0][i-1]);
			}
		}catch(SQLException e){
			Throw.throwException(e);
		}
		return fields;
	}
	
}

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
package org.dysd.dao;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.Configuration;
import org.dysd.dao.annotation.SqlRef;
import org.dysd.util.Tool;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Dao帮助类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
@Component
public class DaoUtils implements ApplicationContextAware{
	
	private static final Map<Configuration, IDaoTemplate> templates = new HashMap<Configuration, IDaoTemplate>();
	
	private static DataSource defaultDataSource;
	
	public static IDaoTemplate getDaoTemplate(Configuration configuration){
		return templates.get(configuration);
	}
	
	public static void addDaoTemplate(Configuration configuration, IDaoTemplate template){
		templates.put(configuration, template);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		setDefaultDataSource(applicationContext);
	}

	/**
	 * 获取默认数据源
	 * @return
	 */
	public static DataSource getDefaultDataSource(){
		return defaultDataSource;
	}
	
	/**
	 * 数据库ID是否匹配
	 * @param databaseId       实际值
	 * @param allowDatabaseIds 允许值
	 * @return
	 */
	public static boolean isMatchDatabase(String databaseId, String allowDatabaseIds){
		if(!Tool.CHECK.isBlank(databaseId) && !Tool.CHECK.isBlank(allowDatabaseIds)){
			String[] allows = allowDatabaseIds.split("\\s+");//使用空白字符分隔
			boolean mode = !"!".equals(allows[0]);//是否肯定模式
			if(mode){//肯定模式
				for(int i = 0, l = allows.length; i < l; i++){
					if(databaseId.equalsIgnoreCase(allows[i])){
						return true;
					}
				}
			}else{// 否定模式，从第1项开始匹配
				for(int i = 1, l = allows.length; i < l; i++){
					if(databaseId.equalsIgnoreCase(allows[i])){
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取真正需要执行的sqlId，实现SQL-ID的整体替换
	 * @param sqlId 原SQL-ID
	 * @return 需要实际执行的SQL-ID
	 */
	public static String getExecuteSqlId(String sqlId){
		Map<String, String> sqlIdMapping = DaoConfig.getSqlIdMapping();
		if(null != sqlIdMapping && null != sqlId && sqlIdMapping.containsKey(sqlId)){
			return sqlIdMapping.get(sqlId);
		}else{
			return sqlId;
		}
	}
	
	/**
	 * 结合@SqlRef注解和方法，解析为sqlId
	 * @param sqlRef  注解
	 * @param method  方法
	 * @return 
	 */
	public static String resolveSqlId(SqlRef sqlRef, Method method){
		String joinSymbol = ".";
		if(null == sqlRef){
			return method.getDeclaringClass().getName() + joinSymbol + method.getName();
		}
		
		String rs = sqlRef.value();
		if(Tool.CHECK.isBlank(rs)){
			rs = method.getName();
		}
		Class<?> cls = sqlRef.cls();
		if(null != cls && !SqlRef.Null.class.equals(cls)){
			rs = cls.getName()+joinSymbol+rs;
		}else if(sqlRef.classpath()){
			rs = method.getDeclaringClass().getName() + joinSymbol +rs;
		}else{
			//直接返回
		}
		return rs;
	}
	
	/**
	 * 设置默认数据源
	 * @param applicationContext
	 * @throws BeansException
	 */
	private void setDefaultDataSource(ApplicationContext applicationContext) throws BeansException {
		Map<String, DataSource> dataSources = applicationContext.getBeansOfType(DataSource.class);
		if(null != dataSources && !dataSources.isEmpty()){
			DataSource firstDataSource = null;
			for(Map.Entry<String, DataSource> entry : dataSources.entrySet()){
				if("dataSource".equalsIgnoreCase(entry.getKey())){
					DaoUtils.defaultDataSource = entry.getValue();
					return;
				}if(null == firstDataSource){
					firstDataSource = entry.getValue();
				}
			}
			DaoUtils.defaultDataSource = firstDataSource;
		}
	}
}

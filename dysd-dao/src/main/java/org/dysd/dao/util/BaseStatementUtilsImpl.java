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

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.dysd.util.exception.Throw;


/**
 * 数据库语句工具类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class BaseStatementUtilsImpl{

	private static final BaseStatementUtilsImpl instance = new BaseStatementUtilsImpl(){};
	private BaseStatementUtilsImpl(){
	}
	
	static BaseStatementUtilsImpl getInstance(){
		return instance;
	}
	
	/**
	 * 设置数据库SQL执行参数
	 * @param ps
	 * @param args
	 */
	public void setParameters(PreparedStatement ps, Object[] args){
		setParameters(ps, args, null);
	}
	
	/**
	 * 设置数据库SQL执行参数
	 * @param ps
	 * @param args
	 * @param argTypes
	 */
	public void setParameters(PreparedStatement ps, Object[] args, int[] argTypes){
		try {
			if(null != args){
				if(null != argTypes && args.length == argTypes.length){
					for(int i=0,l=args.length; i<l; i++){
						ps.setObject(i+1, args[i], argTypes[i]);
					}
				}else{
					for(int i=0,l=args.length; i<l; i++){
						ps.setObject(i+1, args[i]);
					}
				}
			}
		} catch (SQLException e) {
			throw Throw.createException(e);
		}
	}
}

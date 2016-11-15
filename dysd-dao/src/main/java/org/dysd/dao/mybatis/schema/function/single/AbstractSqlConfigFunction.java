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
package org.dysd.dao.mybatis.schema.function.single;

import org.dysd.dao.exception.DaoExceptionCodes;
import org.dysd.dao.mybatis.schema.function.ISqlConfigFunction;
import org.dysd.util.exception.Throw;

/**
 * 抽象的SQL配置函数支持，设定了默认的优先级
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class AbstractSqlConfigFunction implements ISqlConfigFunction{

	@Override
	public int getOrder() {
		return 0;
	}
	
	/**
	 * 声明参数个数，和声明不同时抛出异常
	 * @param args
	 * @param count
	 */
	protected void assertEqualArgsCount(String[] args, int count){
		if(null != args && args.length != count){
			Throw.throwException(DaoExceptionCodes.DYSD020020, getName(), count, args.length);
		}
	}
	
	/**
	 * 声明参数个数，和声明不同时抛出异常
	 * @param args
	 * @param count
	 */
	protected void assertAtLeastArgsCount(String[] args, int count){
		if(null != args && args.length < count){
			Throw.throwException(DaoExceptionCodes.DYSD020021, getName(), count, args.length);
		}
	}
}

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
package org.dysd.dao.mybatis.schema.function.factory;

import java.util.Arrays;
import java.util.Collection;

import org.dysd.dao.mybatis.schema.function.ISqlConfigFunction;
import org.dysd.dao.mybatis.schema.function.ISqlConfigFunctionFactory;
import org.dysd.dao.mybatis.schema.function.single.AbstractSqlConfigFunction;

/**
 * 模糊查询的SQL配置函数工厂
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class LikeSqlConfigFunctionFactory implements ISqlConfigFunctionFactory{

	@Override
	public Collection<ISqlConfigFunction> getSqlConfigFunctions() {
		return Arrays.asList(getLeftLikeSqlConfigFunction(),getRightLikeSqlConfigFunction(),getLikeSqlConfigFunction());
	}
	
	private ISqlConfigFunction getLeftLikeSqlConfigFunction(){
		return new AbstractLikeSqlConfigFunction(){
			@Override
			public String getName() {
				return "llike";
			}

			@Override
			protected String eval(String arg) {
				return "LIKE $concat{'%',"+arg+"}";
			}
		};
	}
	
	private ISqlConfigFunction getRightLikeSqlConfigFunction(){
		return new AbstractLikeSqlConfigFunction(){
			@Override
			public String getName() {
				return "rlike";
			}

			@Override
			protected String eval(String arg) {
				return "LIKE $concat{"+arg+", '%'}";
			}
		};
	}
	
	private ISqlConfigFunction getLikeSqlConfigFunction(){
		return new AbstractLikeSqlConfigFunction(){
			@Override
			public String getName() {
				return "like";
			}

			@Override
			protected String eval(String arg) {
				return "LIKE $concat{'%',"+arg+", '%'}";
			}
		};
	}

	private abstract class AbstractLikeSqlConfigFunction extends AbstractSqlConfigFunction{
		@Override
		public String eval(String databaseId, String[] args) {
			assertEqualArgsCount(args, 1);
			return eval(args[0]);
		}
		protected abstract String eval(String arg);
	}
}

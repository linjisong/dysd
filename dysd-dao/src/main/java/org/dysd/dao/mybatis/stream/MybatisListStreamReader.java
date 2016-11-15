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
package org.dysd.dao.mybatis.stream;

import java.util.List;

import org.dysd.dao.IDaoTemplate;
import org.dysd.dao.page.IPage;
import org.dysd.dao.stream.impl.AbstractListStreamReader;

/**
 * 使用Mybatis实现的流式查询返回结果实现类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class MybatisListStreamReader<T> extends AbstractListStreamReader<T>{

	/**
	 * dao模板
	 */
	private final IDaoTemplate daoTemplate;
	/**
	 * 执行的SQLID
	 */
	private final String statement;
	
	/**
	 * 查询参数
	 */
	private final Object parameter;
	
	/**
	 * 使用sql-id和查询参数的构造函数
	 * @param daoTemplate dao模板
	 * @param statement sql-id
	 * @param parameter 查询参数
	 */
	public MybatisListStreamReader(IDaoTemplate daoTemplate, String statement, Object parameter) {
		super();
		this.daoTemplate = daoTemplate;
		this.statement = statement;
		this.parameter = parameter;
	}
	
	/**
	 * 使用sql-id和查询参数的构造函数
	 * @param daoTemplate dao模板
	 * @param statement sql-id
	 * @param parameter 查询参数
	 * @param fetchSize 每次读取的记录条数
	 */
	public MybatisListStreamReader(IDaoTemplate daoTemplate, String statement, Object parameter, int fetchSize) {
		super(fetchSize);
		this.daoTemplate = daoTemplate;
		this.statement = statement;
		this.parameter = parameter;
	}
	
	/**
	 * 执行实际的当前页数据读取
	 */
	@Override
	protected List<T> doRead(IPage page) {
		return daoTemplate.selectList(statement, parameter, page);
	}
}

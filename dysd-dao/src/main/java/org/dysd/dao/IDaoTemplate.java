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

import java.util.List;

import org.dysd.dao.call.ICallResult;
import org.dysd.dao.page.IPage;
import org.dysd.dao.stream.IListStreamReader;

/**
 * Dao操作模板接口
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface IDaoTemplate{

	/**
	 * 查询单笔数据
	 * @param sqlId SQL-ID
	 * @return 单个对象
	 */
	public <T> T selectOne(String sqlId);

	/**
	 * 查询单笔数据
	 * @param sqlId     SQL-ID
	 * @param parameter 参数对象
	 * @return 单个对象
	 */
	public <T> T selectOne(String sqlId, Object parameter);

	/**
	 * 查询列表数据
	 * @param sqlId SQL-ID
	 * @return 对象列表
	 */
	public <E> List<E> selectList(String sqlId);

	/**
	 * 查询列表数据
	 * @param sqlId     SQL-ID
	 * @param parameter 参数对象
	 * @return 对象列表
	 */
	public <E> List<E> selectList(String sqlId, Object parameter);
	
	/**
	 * 查询分页列表数据
	 * @param sqlId  SQL-ID
	 * @param page   分页对象
	 * @return 指定页的对象列表
	 */
	public <E> List<E> selectList(String sqlId, IPage page);

	/**
	 * 查询分页列表数据
	 * @param sqlId     SQL-ID
	 * @param parameter 参数对象
	 * @param page      分页对象
	 * @return 指定页的对象列表
	 */
	public <E> List<E> selectList(String sqlId, Object parameter, IPage page);
	
	/**
	 * 流式查询
	 * @param sqlId SQL-ID
	 * @return 流式操作接口
	 */
	public <E>IListStreamReader<E> selectListStream(String sqlId);
	
	/**
	 * 流式查询
	 * @param sqlId     SQL-ID
	 * @param parameter 参数对象
	 * @return 流式操作接口
	 */
	public <E>IListStreamReader<E> selectListStream(String sqlId, Object parameter);
	
	/**
	 * 流式查询
	 * @param sqlId      SQL-ID
	 * @param fetchSize  每次读取的记录条数(0, 5000]
	 * @return 流式操作接口
	 */
	public <E>IListStreamReader<E> selectListStream(String sqlId, int fetchSize);

	/**
	 * 流式查询
	 * @param sqlId      SQL-ID
	 * @param parameter  参数对象
	 * @param fetchSize  每次读取的记录条数(0, 5000]
	 * @return 流式操作接口
	 */
	public <E>IListStreamReader<E> selectListStream(String sqlId, Object parameter, int fetchSize);
	
	/**
	 * 新增
	 * @param sqlId SQL-ID
	 * @return 影响的记录条数
	 */
	public int insert(String sqlId);

	/**
	 * 新增
	 * @param sqlId     SQL-ID
	 * @param parameter 参数对象
	 * @return 影响的记录条数
	 */
	public int insert(String sqlId, Object parameter);

	/**
	 * 修改
	 * @param sqlId SQL-ID
	 * @return 影响的记录条数
	 */
	public int update(String sqlId);

	/**
	 * 修改
	 * @param sqlId     SQL-ID
	 * @param parameter 参数对象
	 * @return 影响的记录条数
	 */
	public int update(String sqlId, Object parameter);
	
	/**
	 * 删除
	 * @param sqlId SQL-ID
	 * @return 影响的记录条数
	 */
	public int delete(String sqlId);

	/**
	 * 删除
	 * @param sqlId     SQL-ID
	 * @param parameter 参数对象
	 * @return 影响的记录条数
	 */
	public int delete(String sqlId, Object parameter);
	
	/**
	 * 执行批量：一个SQL执行多次
	 * @param sqlId      SQL-ID
	 * @param parameters 参数对象数组
	 * @return 批量执行的影响记录数组
	 */
	public int[] executeBatch(String sqlId, List<?> parameters);
	
	/**
	 * 执行批量：一次执行多个SQL
	 * @param sqlIds  要执行的一组SQL-ID
	 * @return 批量执行的影响记录数组
	 */
	public int[] executeBatch(List<String> sqlIds);

	/**
	 * 执行批量：一次执行多个SQL
	 * @param sqlIds     要执行的一组SQL-ID
	 * @param parameters 参数对象数组
	 * @return 批量执行的影响记录数组
	 */
	public int[] executeBatch(List<String> sqlIds, List<?> parameters);
	
	/**
	 * 打开批量执行模式
	 */
	public void openBatchType();
	
	/**
	 * 恢复打开批量执行模式之前的执行模式
	 */
	public void resetExecutorType();
	
	/**
	 * 获取批量执行结果
	 * @return
	 */
	public int[] flushBatch();
	
	/**
	 * 调用存储过程
	 * @param sqlId  SQL-ID
	 * @return 存储过程返回结果接口
	 */
	public ICallResult call(String sqlId);
	  
	/**
	 * 调用存储过程
	 * @param sqlId     SQL-ID
	 * @param parameter 参数对象
	 * @return 存储过程返回结果接口
	 */
	public ICallResult call(String sqlId, Object parameter);
}

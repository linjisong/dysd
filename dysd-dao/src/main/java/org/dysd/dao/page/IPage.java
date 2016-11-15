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
package org.dysd.dao.page;

import java.io.Serializable;

/**
 * 分页对象接口
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface IPage extends Serializable {
	
	/**
	 * 默认分页键值，用于单页面多个分页对象
	 */
	public String DEFAULT_PAGE_KEY = "DEFAULT_PAGE_KEY";

	/**
	 * 获取记录总条数
	 * @return 记录总条数
	 */
	public long getTotalRecords();

	/**
	 * 是否需要计算总记录数，一般来说，分页查询在翻页时不用计算总记录数
	 * @return 是否需要计算总记录数
	 */
	public boolean isNeedCalTotal();

	/**
	 * 根据记录总条数设置分页属性
	 * @param totalRecords 总记录数
	 */
	public void setPageProperty(long totalRecords);

	/**
	 * 根据总记录数、当前页数和分页大小设置分页属性
	 * 
	 * @param totalRecords 总记录数
	 * @param currentPage 当前页数
	 * @param pageSize 分页大小
	 */
	public void setPageProperty(long totalRecords, long currentPage, int pageSize);

	/**
	 * 获取总页数
	 * @return 总页数
	 */
	public long getTotalPages();

	/**
	 * 获取分页大小
	 * @return 分页大小
	 */
	public int getPageSize();

	/**
	 * 获取当前页数
	 * @return 当前页数
	 */
	public long getCurrentPage();

	/**
	 * 获取当前页开始记录数
	 * @return 当前页开始记录索引（含），索引从0开始
	 */
	public long getStart();

	/**
	 * 获取当前页结束记录数
	 * @return 当前页结束记录索引（不含）
	 */
	public long getEnd();

	/**
	 * 是否有上一页
	 * @return 是否有上一页
	 */
	public boolean hasPrevPage();

	/**
	 * 是否有下一页
	 * @return 是否有下一页
	 */
	public boolean hasNextPage();

	/**
	 * 是否第一页
	 * @return 是否第一页
	 */
	public boolean isFirstPage();

	/**
	 * 是否最后一页
	 * @return 是否最后一页
	 */
	public boolean isLastPage();
}

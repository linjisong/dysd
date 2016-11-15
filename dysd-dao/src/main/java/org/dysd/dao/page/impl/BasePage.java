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
package org.dysd.dao.page.impl;

import org.dysd.dao.page.IPage;

/**
 * 简单分页对象的实现类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class BasePage implements IPage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5436993928357442743L;

	// 是否需要计算总记录数
	private boolean needCalTotal = true;

	// 记录总条数
	private long totalRecords;

	// 每页记录条数
	private int pageSize;

	// 总页数
	private long totalPages;

	// 当前页数
	private long currentPage = 1;

	// 是否有下一页
	private boolean hasPrevPage = false;

	// 是否有上一页
	private boolean hasNextPage = false;

	// 是否是第一页
	private boolean firstPage = false;

	// 是否为最后一页
	private boolean lastPage = false;

	// 当前页的起始记录序号（偏移量，不含start位置）
	private long start;

	// 当前页的结束记录序号（结束，含end位置）
	private long end;
	
	/**
	 * 根据总记录数其它属性
	 * 
	 * @param totalRecords
	 * @return
	 */
	@Override
	public void setPageProperty(long totalRecords) {
		setPageProperty(totalRecords, this.currentPage, this.pageSize);
	}

	/**
	 * 根据总记录数、当前页数、每页大小设置其它属性
	 * 
	 * @param totalRecords
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public void setPageProperty(long totalRecords, long currentPage, int pageSize) {
		this.totalRecords = totalRecords;// 总记录数
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		if(this.totalRecords >= 0){
			this.totalPages = totalRecords / pageSize + (totalRecords % pageSize == 0 ? 0 : 1);// 总页数
			this.currentPage = Math.min(currentPage, totalPages);// 当前页数（不超过总页数）
			this.start = (0 >= this.currentPage ? 0 : pageSize * (this.currentPage - 1));// 当前页起始记录数，从0开始
			this.end = start + pageSize;// 当前页结束记录数
			this.hasPrevPage = this.totalPages >= 1 && this.currentPage > 1;
			this.hasNextPage = this.totalPages >= 1 && this.currentPage != this.totalPages;
			this.firstPage = this.totalPages >= 1 && this.currentPage == 1;
			this.lastPage = this.totalPages >= 1 && this.currentPage == this.totalPages;
			this.needCalTotal = false;// 将总记录数设置为不需重新计算
		}else{
			this.hasPrevPage = false;
			this.hasNextPage = false;
			this.firstPage = false;
			this.lastPage = false;
			this.needCalTotal = true;		
		}
	}

	@Override
	public long getTotalRecords() {
		return this.totalRecords;
	}

	@Override
	public boolean isNeedCalTotal() {
		return this.needCalTotal;
	}

	@Override
	public long getTotalPages() {
		return this.totalPages;
	}

	@Override
	public int getPageSize() {
		return this.pageSize;
	}

	@Override
	public long getCurrentPage() {
		return this.currentPage;
	}

	@Override
	public long getStart() {
		return this.start;
	}

	@Override
	public long getEnd() {
		return this.end;
	}

	@Override
	public boolean hasPrevPage() {
		return this.hasPrevPage;
	}

	@Override
	public boolean hasNextPage() {
		return this.hasNextPage;
	}

	@Override
	public boolean isFirstPage() {
		return this.firstPage;
	}

	@Override
	public boolean isLastPage() {
		return this.lastPage;
	}

	public void setHasPrevPage(boolean hasPrevPage) {
		this.hasPrevPage = hasPrevPage;
	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public void setNeedCalTotal(boolean needCalTotal) {
		this.needCalTotal = needCalTotal;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

	public void setCurrentPage(long currentPage) {
		this.currentPage = currentPage;
	}

	public void setFirstPage(boolean firstPage) {
		this.firstPage = firstPage;
	}

	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public void setEnd(long end) {
		this.end = end;
	}
}

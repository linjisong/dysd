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
package org.dysd.dao.stream.impl;

import java.util.List;

import org.dysd.dao.exception.DaoExceptionCodes;
import org.dysd.dao.page.IPage;
import org.dysd.dao.page.impl.BasePage;
import org.dysd.dao.stream.IListStreamReader;
import org.dysd.util.exception.Throw;

/**
 * 抽象的流式查询结果实现类，内置分页对象实现流式查询
 * <p>
 * 	默认每次读取的记录数为1000条，该参数有效范围为(0, 5000]，如超出范围，抛出运行时异常
 * </p>
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class AbstractListStreamReader<T> implements IListStreamReader<T>{
	
	/**
	 * 默认的每次读取记录数
	 */
	private static final int defaultFetchSize = 1000;
	
	/**
	 * 最大的每次读取记录数
	 */
	private static final int maxFetchSize = 5000;
	
	/**
	 * 实际的每次读取记录数
	 */
	private final int fetchSize;
	
	/**
	 * 分页对象
	 */
	private final IPage page;
	
	/**
	 * 是否完成的标志
	 */
	private transient boolean finish = false;//是否完成
	
	/**
	 * 无参构造函数
	 */
	public AbstractListStreamReader() {
		this(defaultFetchSize);
	}
	
	/**
	 * 使用指定读取数大小的构造函数
	 * @param fetchSize 每次读取的记录条数
	 */
	public AbstractListStreamReader(int fetchSize) {
		if(fetchSize <= 0){
			fetchSize = defaultFetchSize;
		}else if(fetchSize > maxFetchSize){
			Throw.throwException(DaoExceptionCodes.DYSD020012, fetchSize, "(0, "+maxFetchSize+"]");
		}
		this.fetchSize = fetchSize;
		BasePage page = new BasePage();
		page.setPageSize(fetchSize);
		this.page = page;
	}
	
	/**
	 * 读取当前批次的列表数据，读取的时候会加锁
	 */
	@Override
	public synchronized List<T> read() {
		if(!finish){
			List<T> rs = doRead(page);//查询当前页数据
			if(page.hasNextPage()){//有下一页，游标指向下一页
				page.setPageProperty(page.getTotalRecords(), page.getCurrentPage()+1, fetchSize);
			}else{//没有下一页，完成
				finish = true;
			}
			return rs;
		}
		return null;
	}
	
	/**
	 * 执行实际的读取操作
	 * @param page 分页对象
	 * @return 和分页对象相对应的数据记录列表
	 */
	abstract protected List<T> doRead(IPage page);
	
	/**
	 * 重置读取批次，重置过程中会加锁
	 */
	@Override
	public synchronized void reset(){
		this.finish = false;
		this.page.setPageProperty(this.page.getTotalPages(), 1, fetchSize);
	}
}

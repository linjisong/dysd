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
package org.dysd.dao.mybatis.adapter;

import org.apache.ibatis.session.RowBounds;
import org.dysd.dao.dialect.IDialect;
import org.dysd.dao.page.IPage;

/**
 * Mybatis分页适配器
 * @author linjisong
 * @version 0.0.1
 * @since 0.0.1
 * @date 2016-11-15
 */
public class PageAdapter extends RowBounds{

	private IPage page;
	
	private IDialect dialect;
	
	public PageAdapter(IPage page) {
		super();
		this.page = page;
	}
	
	public PageAdapter(IPage page, IDialect dialect) {
		super();
		this.page = page;
		this.dialect = dialect;
	}

	public IPage getPage() {
		return page;
	}

	public void setPage(IPage page) {
		this.page = page;
	}

	public IDialect getDialect() {
		return dialect;
	}

	public void setDialect(IDialect dialect) {
		this.dialect = dialect;
	}
}

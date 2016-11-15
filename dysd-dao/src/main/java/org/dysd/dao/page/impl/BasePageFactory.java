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
import org.dysd.dao.page.IPageFactory;

/**
 * 简单分页对象工厂实现类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class BasePageFactory implements IPageFactory{

	public IPage createPage(){
		return createPage(IPage.DEFAULT_PAGE_KEY);
	}
	
	public IPage createPage(String pageKey){
		BasePage page = this.createBasePage(pageKey);
		this.setPageProperties(page, pageKey);
		return page;
	}
	
	protected BasePage createBasePage(String pageKey){
		return new BasePage();
	}
	
	protected void setPageProperties(BasePage page, String pageKey){
		
	}
}

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
package org.dysd.dao.dialect.impl;

import org.dysd.dao.dialect.IDialect;

/**
 * 抽象的方言类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class AbstractDialect implements IDialect {

	private String type;
	private String[] driverClassNames;

	/**
	 * 获取数据库类型
	 */
	@Override
	public String getType() {
		return type;
	}

	/**
	 * 获取可能的数据库驱动类名
	 */
	@Override
	public String[] getDriverClassNames() {
		return driverClassNames;
	}

	/**
	 * 是否匹配databaseId
	 */
	@Override
	public boolean match(String databaseId) {
		return null != databaseId && databaseId.equalsIgnoreCase(getType());
	}

	/**
	 * 设置可能的驱动类名称
	 * 
	 * @param driverClassNames
	 */
	protected void setDriverClassNames(String[] driverClassNames) {
		this.driverClassNames = driverClassNames;
	}

	/**
	 * 设置可能的驱动类名称
	 * 
	 * @param driverClassName
	 */
	protected void setDriverClassName(String driverClassName) {
		setDriverClassNames(new String[] { driverClassName });
	}

	/**
	 * 设置数据库类型
	 * @param type
	 */
	protected void setType(String type) {
		this.type = type;
	}
}

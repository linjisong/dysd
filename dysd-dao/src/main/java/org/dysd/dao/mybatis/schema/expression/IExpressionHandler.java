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
package org.dysd.dao.mybatis.schema.expression;

/**
 * 表达式处理器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface IExpressionHandler {
	
	/**
	 * 是否可解析该表达式
	 * @param expression
	 * @return
	 */
	public boolean isSupport(String expression);

	/**
	 * 对表达式求值
	 * @param expression
	 * @param parameter
	 * @param databaseId
	 * @return
	 */
	public Object eval(String expression, Object parameter, String databaseId);
}

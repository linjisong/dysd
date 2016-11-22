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
package org.dysd.dao.mybatis.schema.expression.impl;

import java.util.HashMap;
import java.util.Map;

import org.dysd.dao.mybatis.schema.expression.IExpressionHandler;
import org.dysd.util.spring.SpringHelp;

/**
 * SpEL表达式处理器，如果表达式以spel:为前缀，则包装一个root对象，否则直接将mybatis包装的参数作为root对象
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class SpelExpressionHandler implements IExpressionHandler {
	
	/**
	 * 直接返回true，也就是说不做进一步判断，支持所有的${(exp)}、#{(exp)}内的表达式
	 * 由于支持所有表达式，实际上起到了一种拦截作用，所以需要注意，注册该实现时必须最低优先级
	 */
	@Override
	public boolean isSupport(String expression) {
		return true;
	}

	/**
	 * 对SqlMapper配置中的表达式求值
	 */
	@Override
	public Object eval(String expression, Object parameter, String databaseId) {
		/**
		 * 如果以spel:为前缀，则将mybatis包装后的参数、数据库id以及表达式自身一起封装一个新的root对象
		 * 因此在exp表达式中可以通过params.paramName、databaseId等形式访问
		 */
		if(expression.toLowerCase().startsWith("spel:")){
			expression = expression.substring(5);
			Root root = new Root(parameter, databaseId, expression);
			return SpringHelp.evaluate(root, expression);
		}
		/**
		 * 否则将databaseId作为一个特殊名称的变量
		 * 因此在exp表达式中可以通过paramName、#databaseId等形式访问
		 */
		else{
			Map<String, Object> vars = new HashMap<String, Object>();
			vars.put("databaseId", databaseId);
			return SpringHelp.evaluate(parameter, expression, vars);
		}
	}
	
	public class Root {

		private final Object params;
		private final String databaseId;
		private final String expression;

		public Root(Object params, String databaseId, String expression) {
			this.params = params;
			this.databaseId = databaseId;
			this.expression = expression;
		}

		public Object getParams() {
			return params;
		}

		public String getDatabaseId() {
			return databaseId;
		}

		public String getExpression() {
			return expression;
		}
	}
}

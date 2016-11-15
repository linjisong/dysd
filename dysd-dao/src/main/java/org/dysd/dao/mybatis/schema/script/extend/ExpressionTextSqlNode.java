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
package org.dysd.dao.mybatis.schema.script.extend;

import java.util.regex.Pattern;

import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.apache.ibatis.scripting.ScriptingException;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.OgnlCache;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.type.SimpleTypeRegistry;
import org.dysd.dao.mybatis.schema.SchemaHandlers;
import org.dysd.dao.mybatis.schema.expression.IExpressionHandler;

/**
 * 可处理表达式的SQL文本节点
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class ExpressionTextSqlNode extends TextSqlNode {
	
	private String text;
	private Pattern injectionFilter;

	public ExpressionTextSqlNode(String text) {
		this(text, null);
	}

	public ExpressionTextSqlNode(String text, Pattern injectionFilter) {
		super(text, injectionFilter);
		this.text = text;
		this.injectionFilter = injectionFilter;
	}

	@Override
	public boolean apply(DynamicContext context) {
		BindingTokenParser handler = new BindingTokenParser(context, injectionFilter);
		GenericTokenParser parser = new GenericTokenParser("${", "}", handler);
		context.appendSql(parser.parse(text));
		return true;
	}

	private class BindingTokenParser implements TokenHandler {

		private DynamicContext context;
		private Pattern injectionFilter;

		public BindingTokenParser(DynamicContext context, Pattern injectionFilter) {
			this.context = context;
			this.injectionFilter = injectionFilter;
		}

		@Override
		public String handleToken(String expression) {
			Object value = resolveExpression(expression.trim());
			String srtValue = (value == null ? "" : String.valueOf(value)); // issue #274 return "" instead of "null"
			checkInjection(srtValue);
			return srtValue;
		}

		private Object resolveExpression(String expression) {
			Object parameter = context.getBindings().get(DynamicContext.PARAMETER_OBJECT_KEY);
			String databaseId = (String)context.getBindings().get(DynamicContext.DATABASE_ID_KEY);
			if(expression.startsWith("(") && expression.endsWith(")")){
				expression = expression.substring(1, expression.length()-1);
				IExpressionHandler handler = SchemaHandlers.getExpressionHandler(expression);
				if(null != handler){
					return handler.eval(expression, parameter, databaseId);
				}
				return null;
			}else{
				if (parameter == null) {
					context.getBindings().put("value", null);
				} else if (SimpleTypeRegistry.isSimpleType(parameter.getClass())) {
					context.getBindings().put("value", parameter);
				}
				return OgnlCache.getValue(expression, context.getBindings());
			}
		}

		private void checkInjection(String value) {
			if (injectionFilter != null && !injectionFilter.matcher(value).matches()) {
				throw new ScriptingException("Invalid input. Please conform to regex" + injectionFilter.pattern());
			}
		}
	}
}
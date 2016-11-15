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
package org.dysd.dao.mybatis.schema.function;


import org.junit.Test;

public class SqlConfigFunctionParserTest {

	@Test
	public void testName() throws Exception {
//		test("");
//		test("#{paramCode,jdbcType=INTEGER}, ${paramName}, c");
//		test("#{${paramName2},jdbcType=INTEGER}, ${paramName}, c");
//		test("(#{${paramName2},jdbcType=INTEGER}, ${paramName}), c");
//		test("#{${paramName2}},jdbcType=INTEGER}, ${paramName}, c");
		
		String sql = "select field, field2, $A{  }, $R1{$R2{${abbb}, d, #{ cc,  jdbcType=INTEGER}},b} from table where field = #{filed1, javaType=int, jdbcType=INTEGER}";
		String a = SqlConfigFunctionParser.evalSqlConfigFunction("", sql);
		System.out.println(a);
	}
	
//	private void test(String arg){
//		System.out.println("============");
//		System.out.println(arg);
//		String[] as = SqlConfigFunctionParser.resolveArgs(arg);
//		System.out.println(as.length);
//		for(String s : as){
//			System.out.println(s);
//		}
//		System.out.println();
//	}

}

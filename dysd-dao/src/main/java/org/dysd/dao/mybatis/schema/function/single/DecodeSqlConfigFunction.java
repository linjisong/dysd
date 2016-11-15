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
package org.dysd.dao.mybatis.schema.function.single;

import org.dysd.dao.dialect.impl.Oracle;
import org.dysd.util.Tool;

/**
 * 类似于Oracle中DECODE功能的SQL配置函数
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class DecodeSqlConfigFunction extends AbstractSqlConfigFunction{

	@Override
	public String getName() {
		return "decode";
	}

	@Override
	public String eval(String databaseId, String[] args) {
		super.assertAtLeastArgsCount(args, 3);
		if(Oracle.getInstance().match(databaseId)){
			return "DECODE("+Tool.STRING.join(args, ",")+")";
		}else{
			StringBuffer sb = new StringBuffer();
			sb.append("CASE ").append(args[0]);
			int i=2, l = args.length;
			for(; i < l; i= i+2){
				sb.append(" WHEN ").append(args[i-1]).append(" THEN ").append(args[i]);
			}
			if(i == l){//结束循环时，两者相等说明最后一个参数未使用
				sb.append(" ELSE ").append(args[l-1]);
			}
			sb.append(" END");
			return sb.toString();
		}
	}
}

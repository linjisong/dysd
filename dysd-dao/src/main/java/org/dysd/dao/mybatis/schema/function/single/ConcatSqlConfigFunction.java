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

import org.dysd.dao.dialect.impl.MySQL;
import org.dysd.util.Tool;

/**
 * 字符串连接的SQL配置函数
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class ConcatSqlConfigFunction extends AbstractSqlConfigFunction{

	@Override
	public String getName() {
		return "concat";
	}

	@Override
	public String eval(String databaseId, String[] args) {
		super.assertAtLeastArgsCount(args, 2);
		if(MySQL.getInstance().match(databaseId)){
			return "CONCAT("+Tool.STRING.join(args, ",")+")";
		}else{
			return Tool.STRING.join(args, "||");
		}
	}
}

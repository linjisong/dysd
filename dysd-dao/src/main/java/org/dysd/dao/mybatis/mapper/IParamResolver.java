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
package org.dysd.dao.mybatis.mapper;

import java.util.List;

/**
 * mybatis动态代理时的参数解析器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface IParamResolver {

	/**
	 * 包装普通方法的执行参数
	 */
	Object getNamedParams(Object[] args);

	/**
	 * 包装批量执行的参数
	 * @param args
	 * @param batchCount
	 * @return
	 */
	List<Object> getBatchNamedParams(Object[] args, int batchCount);

	/**
	 * 设置含Executes注解的批量执行参数
	 * @param args
	 * @param sqlIds
	 * @param params
	 */
	void resolveExecuteNamedParams(Object[] args, List<String> sqlIds, List<Object> params);
}
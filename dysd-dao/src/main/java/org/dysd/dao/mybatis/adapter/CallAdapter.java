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
import org.dysd.dao.call.ICallResult;

/**
 * 调用存储过程的Mybatis参数适配类
 * @author linjisong
 * @version 0.0.1
 * @since 0.0.1
 * @date 2016-11-15
 */
public class CallAdapter extends RowBounds{
	
	/**
	 * 存储过程返回结果
	 */
	private ICallResult callResult;

	/**
	 * 返回调用存储过程的返回结果
	 * @return ICallResult对象
	 */
	public ICallResult getCallResult() {
		return callResult;
	}

	/**
	 * 设置调用存储过程的返回结果
	 * @param callResult
	 */
	public void setCallResult(ICallResult callResult) {
		this.callResult = callResult;
	}
}

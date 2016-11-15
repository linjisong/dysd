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
package org.dysd.util.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Set;

import org.dysd.util.exception.Throw.DysdExceptionInnerProxy;
import org.dysd.util.exception.handler.IExceptionHandler;
import org.dysd.util.exception.level.ExceptionLevel;

/**
 * Dysd异常
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class DysdException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2159369710297472601L;
	
	private final DysdExceptionInnerProxy proxy;
	
	/*package*/DysdException(DysdExceptionInnerProxy proxy) {
		super(proxy.getCause());
		this.proxy = proxy;
	}
	
	/**
	 * 获取跟踪ID
	 * @return
	 */
	public String getTrackId(){
		return this.proxy.getTrackId();
	}
	
	/**
	 * 获取异常父代码
	 * @return
	 */
	public String getParentCode(){
		return this.proxy.getParentCode();
	}
	
	/**
	 * 获取异常代码
	 * @return
	 */
	public String getCode(){
		return this.proxy.getCode();
	}
	
	/**
	 * 获取异常逻辑视图
	 * @return
	 */
	public String getView() {
		return this.proxy.getView();
	}
	
	/**
	 * 获取异常级别
	 * @return
	 */
	public ExceptionLevel getLevel(){
		return this.proxy.getLevel();
	}
	
	/**
	 * 获取异常简短描述
	 * @return
	 */
	public String getShortMessage(){
		return this.proxy.getMessage();
	}
	
	/**
	 * 获取异常描述
	 * @return
	 */
	@Override
	public String getMessage() {
		return Throw.getMessage(this);
	}
	
	/**
	 * 获取异常详细信息描述
	 * @return
	 */
	public String getStackMessage() {
		return Throw.getStackMessage(this);
	}
	
	/**
	 * 获取异常处理器列表
	 * @return
	 */
	public Set<IExceptionHandler> getHandlers(){
		return this.proxy.getHandlers();
	}
	
	@Override
	public void printStackTrace(PrintStream s) {
		s.print(Throw.getStackMessage(this));
		//super.printStackTrace(s);
	}

	@Override
	public void printStackTrace(PrintWriter s) {
		s.print(Throw.getStackMessage(this));
		//super.printStackTrace(s);
	}
	
	/**
	 * 获取代理
	 */
	/*package*/ DysdExceptionInnerProxy getProxy() {
		return proxy;
	}
}

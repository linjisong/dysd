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
package org.dysd.util.logger.stack.impl;

import java.util.Set;

import org.dysd.util.Tool;
import org.dysd.util.config.BaseConfig;
import org.dysd.util.env.EnvConsts;
import org.dysd.util.logger.stack.IStack;
import org.dysd.util.track.Tracker;
import org.slf4j.Logger;

/**
 * 简单的日志堆栈
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class SimpleStack implements IStack {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6403970075093676020L;
	
	private String trackId;
	
	private Logger logger;
	
	private StackTraceElement stack; 
	
	private String message;
	
	private Throwable throwable;
	
	public SimpleStack(){
		this.trackId = Tracker.getTrackId();
	}
	
	public SimpleStack(Logger logger, StackTraceElement stack, String message, Throwable throwable) {
		this.logger = logger;
		this.message = message;
		this.throwable = throwable;
		this.stack = stack;
		this.trackId = Tracker.getTrackId();
	}
	
	@Override
	public String getTrackId() {
		return trackId;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public StackTraceElement getStack() {
		return this.stack;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder()
			.append("Host:[")
			.append(EnvConsts.LOCALE_HOST)
			.append("] ")
			.append("App-Name:[")
			.append(BaseConfig.getAppName())
			.append("] ");
		
		String trackId = getTrackId();
		if(!Tool.CHECK.isBlank(trackId)){
		  sb.append("trackId:[").append(trackId).append("] ");
		}
		
		StackTraceElement s = getStack();
		if(null != s && !isIgnoreStack(s.getClassName())){
			sb.append(s);
		}
		
		String msg = getCustomMessage();
		if(!Tool.CHECK.isBlank(this.message)){
			msg += this.message.trim();
		}
		
		if(!Tool.CHECK.isBlank(msg)){
			sb.append(msg);
		}
		
		return sb.toString();
	}
	
	protected String getCustomMessage(){
		return "";
	}
	
	@Override
	public Throwable getThrowable() {
		return this.throwable;
	}
	
	private boolean isIgnoreStack(String stack){
		Set<String> ignores = BaseConfig.getIgnoreStacks();
		return null != ignores && ignores.contains(stack);
	}
}

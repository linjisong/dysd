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
package org.dysd.util.sync.impl;

import org.dysd.util.logger.CommonLogger;

/**
 * 单例运行状态控制器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class SingleonRunStatus extends SynchronizedSupport{

	/**
	 * 运行状态 
	 */
	enum State{INIT, RUNNING, FAILURE, SUCCESS};
	
	/**
	 * 初始状态
	 */
	private State state = State.INIT;
	
	/**
	 * 任务名称
	 */
	private String name;
	
	/**
	 * 私有化构造函数，统一使用{@link #SingleonRunStatus(String)}创建实例
	 * @param name
	 */
	private SingleonRunStatus(String name){
		this.name = name;
	}
	
	/**
	 * 创建实例
	 * @param name
	 * @return
	 */
	public static SingleonRunStatus newInstance(String name){
		return new SingleonRunStatus(name);
	}
	
	private void log(State state, State state2){
		CommonLogger.debug("SingleonRunStatus("+name+") cannot change: [" + state + "] --> [" + state2 + "].");
	}
	
	/**
	 * 开始运行
	 * @return
	 */
	public synchronized boolean start(){
		switch(state){
		case INIT:
			lock(LockMode.READ);
			this.state = State.RUNNING;
			return true;
		case RUNNING:
		case FAILURE:
			this.log(state, State.RUNNING);
			return false;
		case SUCCESS:
			return false;
		}
		return false;
	}
	
	/**
	 * 设置为运行成功
	 * @return
	 */
	public synchronized boolean success(){
		switch(state){
		case INIT:
		case FAILURE:
			this.log(state, State.SUCCESS);
			return false;
		case RUNNING:
		case SUCCESS:
			unlock(LockMode.READ);
			this.state = State.SUCCESS;
			return true;
		}
		return false;
	} 
	
	/**
	 * 设置为运行失败
	 * @return
	 */
	public synchronized boolean failure(){
		switch(state){
		case INIT:
		case SUCCESS:
			this.log(state, State.FAILURE);
			return false;
		case RUNNING:
		case FAILURE:
			unlock(LockMode.READ);
			this.state = State.FAILURE;
			return true;
		}
		return false;
	} 
}

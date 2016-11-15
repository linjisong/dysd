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
package org.dysd.util.track.impl;

import org.dysd.util.Tool;
import org.dysd.util.logger.CommonLogger;
import org.dysd.util.track.ITracker;

/**
 * 使用线程本地变量实现的跟踪器抽象实现类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public abstract class AbstractThreadLocalTracker implements ITracker{
	
	private static final ThreadLocal<String> trackIdLocal = new ThreadLocal<String>();
	
	/**
	 * 生成跟踪号
	 * @return
	 */
	abstract protected String generateTrackId();
	
	/**
	 * 开始跟踪
	 */
	@Override
	public void start() {
		String id = trackIdLocal.get();
		if(!Tool.CHECK.isBlank(id)){
			CommonLogger.debug("the tracker has started, the trackId is " + id + ".");
		}else{
			this.startNewTracker();
		}
	}
	
	/**
	 * 开始新的跟踪
	 */
	private void startNewTracker(){
		trackIdLocal.set(generateTrackId());
	}

	/**
	 * 使用指定跟踪号开始新的跟踪
	 */
	@Override
	public void start(String trackId) {
		String id = trackIdLocal.get();
		if(!Tool.CHECK.isBlank(id)){
			CommonLogger.debug("the old trackId is " + id + ", and override using new trackId " + trackId +".");
		}
		trackIdLocal.set(trackId);
	}

	/**
	 * 判断是否正在跟踪
	 */
	@Override
	public boolean isTracking() {
		return !Tool.CHECK.isBlank(trackIdLocal.get());
	}

	/**
	 * 获取目前的跟踪号
	 */
	@Override
	public String getTrackId() {
		return trackIdLocal.get();
	}

	/**
	 * 停止跟踪
	 */
	@Override
	public void stop() {
		trackIdLocal.remove();
	}
}

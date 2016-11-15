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
package org.dysd.util.track;

/**
 * 跟踪器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface ITracker {

	/**
	 * 开始跟踪
	 */
	public void start();
	
	/**
	 * 使用传入的跟踪号开始跟踪
	 * @param trackId 跟踪号
	 */
	public void start(String trackId);
	
	/**
	 * 是否正在跟踪
	 * @return 是否正在跟踪
	 */
	public boolean isTracking();
	
	/**
	 * 获取当前跟踪号
	 * @return 当前跟踪号
	 */
	public String getTrackId();
	
	/**
	 * 停止跟踪
	 */
	public void stop();
}

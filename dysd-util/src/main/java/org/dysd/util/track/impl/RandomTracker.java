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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 使用包含日期时间的随机数作为跟踪号的跟踪器实现类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class RandomTracker extends AbstractThreadLocalTracker{

	private static final SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/**
	 * 是否包含日期时间
	 */
	private boolean includeTime = true;
	
	/**
	 * 随机部分的长度
	 */
	private int randomCount = 8;
	
	/**
	 * 生成跟踪号
	 */
	@Override
	protected String generateTrackId() {
		StringBuffer sb = new StringBuffer();
		if(isIncludeTime()){
			sb.append(df.format(new Date()));
		}
		int randomCount = Math.max(4, getRandomCount());
		sb.append(RandomStringUtils.randomNumeric(randomCount));
		return sb.toString();
	}

	public boolean isIncludeTime() {
		return includeTime;
	}

	public void setIncludeTime(boolean includeTime) {
		this.includeTime = includeTime;
	}

	public int getRandomCount() {
		return randomCount;
	}

	public void setRandomCount(int randomCount) {
		this.randomCount = randomCount;
	}
}

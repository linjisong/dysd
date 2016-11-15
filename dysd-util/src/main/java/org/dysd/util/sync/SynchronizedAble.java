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
package org.dysd.util.sync;

/**
 * 具有同步器的类接口
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public interface SynchronizedAble {

	/**
	 * 读写模式
	 */
	public enum LockMode{READ, WRITE}
	
	/**
	 * 获取同步器
	 * @return
	 */
	public ISynchronizer getSynchronizer();

    /**
     * 设置同步器
     * @param sync
     */
	public void setSynchronizer(ISynchronizer sync);

    /**
     * 锁定
     * @param mode
     */
	public void lock(LockMode mode);

    /**
     * 解锁
     * @param mode
     */
	public void unlock(LockMode mode);
}

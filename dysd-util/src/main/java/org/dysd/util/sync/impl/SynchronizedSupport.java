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

import org.dysd.util.sync.ISynchronizer;
import org.dysd.util.sync.SynchronizedAble;

/**
 * 同步支持类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */ 
public class SynchronizedSupport implements SynchronizedAble{

	private volatile ISynchronizer synchronizer;

	@Override
	public ISynchronizer getSynchronizer() {
		if(null == synchronizer){
			synchronized(this){
				if(null == synchronizer){
					synchronizer = new ReadWriteSynchronizer();
				}
			}
		}
		return synchronizer;
	}

	@Override
	public void setSynchronizer(ISynchronizer sync) {
		this.synchronizer = synchronizer;
	}

	@Override
	public void lock(LockMode mode) {
		switch (mode)
        {
        case READ:
        	getSynchronizer().beginRead();;
            break;
        case WRITE:
        	getSynchronizer().beginWrite();;
            break;
        default:
            throw new IllegalArgumentException("Unsupported LockMode: " + mode);
        }
	}

	@Override
	public void unlock(LockMode mode) {
		switch (mode)
        {
        case READ:
        	getSynchronizer().endRead();
            break;
        case WRITE:
        	getSynchronizer().endWrite();
            break;
        default:
            throw new IllegalArgumentException("Unsupported LockMode: " + mode);
        }
	}
}

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

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.dysd.util.sync.ISynchronizer;

/**
 * 使用ReentrantReadWriteLock实现的读写同步器
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class ReadWriteSynchronizer implements ISynchronizer
{
    private final ReadWriteLock lock;

    public ReadWriteSynchronizer(ReadWriteLock l)
    {
        lock = (l != null) ? l : createDefaultLock();
    }

    public ReadWriteSynchronizer()
    {
        this(null);
    }

    @Override
    public void beginRead()
    {
        lock.readLock().lock();
    }

    @Override
    public void endRead()
    {
        lock.readLock().unlock();
    }

    @Override
    public void beginWrite()
    {
        lock.writeLock().lock();
    }

    @Override
    public void endWrite()
    {
        lock.writeLock().unlock();
    }

    private static ReadWriteLock createDefaultLock()
    {
        return new ReentrantReadWriteLock();
    }
}


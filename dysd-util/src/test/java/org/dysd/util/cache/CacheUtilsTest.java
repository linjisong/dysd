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
package org.dysd.util.cache;

import java.util.Collection;

import org.dysd.util.Tool;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.cache.Cache;

public class CacheUtilsTest {

	@Test
	public void testCacheUtils(){
		Cache cache = Tool.CACHE.getCache(CacheUtilsTest.class);
		cache.put("cache1", "cache1-value");
		cache.put("cache2", "cache2-value");
		
		Cache cache2 = Tool.CACHE.getCache(CacheUtilsTest.class);
		Assert.assertEquals(cache2 , cache);
		Assert.assertTrue(cache2 == cache);
		
		Object cache1 = Tool.CACHE.getCacheValue(CacheUtilsTest.class, "cache1");
		Assert.assertEquals("cache1-value", cache1);
		
		//Cache cache3 = Tool.CACHE.getCache("Test");
		
		Collection<String> cacheNames = Tool.CACHE.getCacheNames();
		for(String name : cacheNames){
			System.out.println(name);
		}
	}
}

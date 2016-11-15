package org.dysd.util.spring.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheExampleService implements ICacheExampleService{

	private final Map<String, String> mockDb = new HashMap<String, String>();
	
	@Override
	public void init(){
		mockDb.put("key1", "value1");
	}
	
	@Cacheable(key="#key",cacheNames="test1")
	@Override
	public String select(String key) {
		System.out.println("actually execute select("+key+")");
		return mockDb.get(key);
	}

	@CachePut(key="#key",cacheNames="test1")
	@Override
	public String insert(String key, String value) {
		System.out.println("actually execute insert("+key+","+value+")");
		if(mockDb.containsKey(key)){
			throw new RuntimeException("the data has exists, key is ["+key+"].");
		}
		mockDb.put(key, value);
		return value;
	}

	//@CachePut(key="#key",cacheNames="test1")
	@Override
	public String update(String key, String value) {
		System.out.println("actually execute update("+key+","+value+")");
		if(mockDb.containsKey(key)){
			mockDb.put(key, value);
		}else{
			System.out.println("no data to update, key is ["+key+"].");
		}
		return value;
	}

	@CacheEvict(key="#key", cacheNames="test1", beforeInvocation=true)
	@Override
	public String delete(String key) {
		System.out.println("actually execute delete("+key+")");
		return mockDb.remove(key);
	}
}

package org.dysd.util.spring.cache;

public interface ICacheExampleService {
	
	public void init();

	public String select(String key);
	
	public String insert(String key, String value);
	
	public String update(String key, String value);
	
	public String delete(String key);
}

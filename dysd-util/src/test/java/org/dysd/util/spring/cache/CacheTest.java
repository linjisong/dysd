package org.dysd.util.spring.cache;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"classpath:applicationContext.xml"	
})
@Component
public class CacheTest {

	@Resource
	private ICacheExampleService service;
	
	@Test
	public void testCache() throws Exception {
		String rs = "";
		String key = "key1";
		String value = "";
		int i = 1;
		
		System.out.println("初始化");
		service.init();
		
		System.out.println("第一次查询，实际执行");
		rs = service.select(key);
		System.out.println("select data for key ["+key+"]==="+(i++)+", the value is ["+rs+"]");
		
		System.out.println("第二次查询，从缓存取数");
		rs = service.select(key);
		System.out.println("select data for key ["+key+"]==="+(i++)+", the value is ["+rs+"]");
		
		key = "key2";
		value = "value2";
		
		System.out.println("添加第2个值，并缓存");
		service.insert(key, value);
		System.out.println("insert data ["+key+", "+value+"]");
		
		System.out.println("第一次查询第2个值，添加时已缓存，所有从缓存取数");
		rs = service.select(key);
		System.out.println("select data for key ["+key+"]==="+(i++)+", the value is ["+rs+"]");
		
		System.out.println("第二次查询第2个值，从缓存取数");
		rs = service.select(key);
		System.out.println("select data for key ["+key+"]==="+(i++)+", the value is ["+rs+"]");
		
		value = "new-value";
		
		System.out.println(" 更新新值，但未缓存");
		service.update(key, value);
		System.out.println("update data ["+key+", "+value+"]");
		
		System.out.println(" 更新后第一次查询，从缓存取数");
		rs = service.select(key);
		System.out.println("select data for key ["+key+"]==="+(i++)+", the value is ["+rs+"]");
		
		System.out.println(" 更新后第二次查询，从缓存取数");
		rs = service.select(key);
		System.out.println("select data for key ["+key+"]==="+(i++)+", the value is ["+rs+"]");
		
		System.out.println(" 删除，并删除缓存");
		rs = service.delete(key);
		System.out.println("delete data for key ["+key+"], the value is ["+rs+"]");
		
		System.out.println(" 删除后，实际取数");
		rs = service.select(key);
		System.out.println("select data for key ["+key+"]==="+(i++)+", the value is ["+rs+"]");
	}
}

package org.dysd.util.spring;

import java.util.HashMap;
import java.util.Map;

import org.dysd.util.Tool;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
	"classpath:applicationContext.xml"	
})
@Component  // 该测试类本身作为一个Spring管理的bean，便于后面的测试
public class SpringHelpTest {
	
	public String getBeanValue(String arg){
		return "beanValue:"+arg;
	}

	@Test
	public void testSpelHelp(){
		// 准备root对象 {key1 : 'root-value1', key2 : 'root-value2'}
		Root root = new Root("root-value1", "root-value2");
		// 准备一般变量
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("var1", "value1");
		vars.put("var2", "value2");
		
		// 直接计算简单表达式
		Object rs = SpringHelp.evaluate("1+2");
		Assert.assertEquals(3, rs);
		// 按指定类型计算简单表达式
		rs = SpringHelp.evaluate("1+2", String.class);
		Assert.assertEquals("3", rs);
		// 访问root对象的属性
		rs = SpringHelp.evaluate(root, "key1");
		Assert.assertEquals("root-value1", rs);
		// 访问一般变量
		rs = SpringHelp.evaluate(root, "#var2", vars);
		Assert.assertEquals("value2", rs);
		// 访问root对象
		rs = SpringHelp.evaluate(root, "#root", vars);
		Assert.assertTrue(rs == root);
		// 访问Spring管理的bean，同时传入的参数又是root对象的属性
		rs = SpringHelp.evaluate(root, "@springHelpTest.getBeanValue(key2)", vars);
		Assert.assertEquals("beanValue:root-value2", rs);
		// 设置root对象的属性
		SpringHelp.setValue(root, "key1", "new-root-value1");
		rs = SpringHelp.evaluate(root, "key1");
		Assert.assertEquals("new-root-value1", rs);
		//访问工具类，其中Tool.DATE.getDate()的作用是获取当前日期
		rs = SpringHelp.evaluate("#Tool.DATE.getDate()");
		Assert.assertEquals(Tool.DATE.getDate(), rs);
	}
	
	public class Root{
		String key1;
		String key2;
		Root(String key1, String key2){
			this.key1 = key1;
			this.key2 = key2;
		}
		public String getKey1() {
			return key1;
		}
		public void setKey1(String key1) {
			this.key1 = key1;
		}
		public String getKey2() {
			return key2;
		}
		public void setKey2(String key2) {
			this.key2 = key2;
		}
	}
}

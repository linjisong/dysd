package org.dysd.dao.mybatis.mapper.update;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.dysd.dao.SpringTestSupport;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IUpdateDaoTest extends SpringTestSupport{

	@Autowired(required=false)
	private IUpdateDao dao;
	
	/**
	 * 测试
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		Map<String, Object> param = getParam();
		String paramCode = (String)param.get("paramCode");
		
		// 测试新增
		int rs = dao.insert(param);
		Assert.assertEquals(1, rs);
		
		Map<String, Object> select = dao.select(paramCode);
		Assert.assertEquals("测试", (String)select.get("des"));
		
		// 测试修改
		param.put("des", "测试更新"); 
		rs = dao.update(param);
		Assert.assertEquals(1, rs);
		
		select = dao.select(paramCode);
		Assert.assertEquals("测试更新", (String)select.get("des"));
		
		// 测试删除
		rs = dao.delete(paramCode);
		Assert.assertEquals(1, rs);
		select = dao.select(paramCode);
		Assert.assertNull(select);
	}
	
	private Map<String, Object> getParam(){
		Map<String, Object> item = new HashMap<String, Object>();
		String random = RandomStringUtils.randomAlphabetic(8);
		item.put("paramCode", random);
		item.put("paramName", "更新测试");
		item.put("paramGroup", "测试组别");
		item.put("paramAttr", "参数属性" + random);
		item.put("seqno", 0);
		item.put("des", "测试");
		return item;
	}
}

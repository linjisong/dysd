package org.dysd.dao.mybatis.mapper.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.dysd.dao.SpringTestSupport;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IBatchDaoTest extends SpringTestSupport{

	@Autowired(required=false)
	private IBatchDao dao;
	
	/**
	 * 一个SQL-ID执行多次
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void batch1() throws Exception {
		try{
			//DaoUtils.openBatchType();
			String paramCode = RandomStringUtils.randomAlphabetic(8);
			int size = 10;
			
			int[] rs = dao.batch1(paramCode, getBatch1Param(size));
			for(int r : rs){
				System.out.print(r+",");
			}
			System.out.println();
			
			List<Map<String, Object>> list = dao.selectList(paramCode);
			System.out.println(list);
			Assert.assertEquals(size, list.size());
			
			//数据清理
			//int rs2 = 
					dao.delete(paramCode);
			//int[] rss = DaoUtils.flushBatch();
			//Assert.assertEquals(size, rss[0]);
		}finally{
			//DaoUtils.resetExecutorType();
		}
	}
	
	private List<Map<String, Object>> getBatch1Param(int size){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < size; i++){
			Map<String, Object> item = new HashMap<String, Object>();
			String random = RandomStringUtils.randomAlphabetic(8);
			item.put("dataCode", random);
			item.put("dataText", "数据" + random);
			item.put("dataParam", "参数" + random);
			item.put("seqno", i);
			item.put("des", "");
			list.add(item);
		}
		return list;
	}
	
	/**
	 * 一次执行多个SQL-ID
	 * @throws Exception
	 */
	//@Test
	public void batch2() throws Exception {
		
		// 不同参数
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		String paramCode = RandomStringUtils.randomAlphabetic(8);
		datas.add(getBatch2FirstParam(paramCode));
		datas.add(getBatch2SecondParam());
		int[] rs = dao.batch2(paramCode, datas);
		for(int r : rs){
			System.out.print(r+",");
		}
		System.out.println();
		
		List<Map<String, Object>> list = dao.selectList(paramCode);
		System.out.println(list);
		Assert.assertEquals(1, list.size());
		
		// 相同参数，执行批量
		dao.batchDelete(paramCode);
		list = dao.selectList(paramCode);
		System.out.println(list);
		Assert.assertEquals(0, list.size());
	}
	
	
	private Map<String, Object> getBatch2FirstParam(String paramCode){
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("paramName", "更新测试");
		item.put("paramGroup", "类型" + paramCode);
		item.put("paramAttr", "类型参数" + paramCode);
		item.put("seqno", 0);
		item.put("des", "测试批量执行2-1");
		return item;
	}
	
	private Map<String, Object> getBatch2SecondParam(){
		Map<String, Object> item = new HashMap<String, Object>();
		String random = RandomStringUtils.randomAlphabetic(8);
		item.put("dataCode", random);
		item.put("dataText", "数据" + random);
		item.put("dataParam", "参数" + random);
		item.put("index", 0);
		item.put("memo", "测试批量执行2-2");
		return item;
	}
}

package org.dysd.dao.mybatis.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dysd.dao.SpringTestSupport;
import org.dysd.dao.page.impl.BasePage;
import org.dysd.dao.stream.IListStreamReader;
import org.dysd.util.Tool;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.stereotype.Service;

@Service
public class ExampleDaoTest extends SpringTestSupport{

	@Resource
	private IExampleDao dao;
	
	@Test
	public void testSelectString(){
		try {
			ParamEnumDef param = new ParamEnumDef();
			param.setParamCode("DISPLAY_AREA");
			String a = dao.selectString("DISPLAY_AREA");
			Assert.assertEquals("显示区域", a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testSelectStringList(){
		List<String> list = dao.selectStringList("MENU");
		Assert.assertEquals("显示区域,授权级别,菜单标志", Tool.STRING.join(list, ","));
	}
	
	@Test
	public void testSelectStringArray(){
		String[] arr = dao.selectStringArray("MENU");
		Assert.assertEquals("显示区域,授权级别,菜单标志", Tool.STRING.join(arr, ","));
	}
	
	@Test
	public void testSelectIntArray(){
		int[] arr = dao.selectIntArray("MENU");
		Assert.assertEquals(11, arr[0]);
		
		//List<Integer> list = dao.selectIntArray("MENU");
		//Assert.assertEquals("11,12,13", CoreUtils.join(list, ","));
	}
	
	@Test
	public void testSelectOneJavaBean(){
		ParamEnumDef bean = dao.selectOneJavaBean("DISPLAY_AREA");
		Assert.assertEquals("显示区域", bean.getParamName());
		Assert.assertNull(bean.getDatas());
	}
	
	@Test
	public void testSelectOneMap(){
		Map<String, Object> bean = dao.selectOneMap("DISPLAY_AREA");
		Assert.assertEquals("显示区域", bean.get("paramName"));
		Assert.assertFalse(bean.containsKey("datas"));
	}
	
	@Test
	public void testSelectOneNestJavaBean(){
		ParamEnumDef bean = dao.selectOneNestJavaBean("DISPLAY_AREA");
		Assert.assertEquals("显示区域", bean.getParamName());
		Assert.assertNotNull(bean.getDatas());
	}
	
	@Test
	public void testSelectListJavaBean(){
		List<ParamEnumDef> list = dao.selectListJavaBean("MENU");
		Assert.assertEquals(3, list.size());
	}
	
	@Test
	public void testSelectListMap(){
		List<Map<String, Object>> list = dao.selectListMap("MENU");
		Assert.assertEquals(3, list.size());
	}
	
	@Test
	public void testSelectListNestJavaBean(){
		List<ParamEnumDef> list = dao.selectListNestJavaBean("MENU");
		Assert.assertEquals(3, list.size());
	}
	
	@Test
	public void testSelectMap(){
		Map<String, ParamEnumDef> map = dao.selectMap("MENU");
		Assert.assertEquals(3, map.size());
	}
	
	@Test
	public void testSelectPageListJavaBean(){
		BasePage page = new BasePage();//在Web应用中，由PageFactory根据传入的参数创建IPage对象
		page.setPageSize(2);//每页查询两笔
		page.setCurrentPage(1);//当前第1也
		List<ParamEnumDef> list = dao.selectPageListJavaBean("MENU", page);
		Assert.assertEquals(2, list.size());//当前数据量
		
		Assert.assertEquals(3, page.getTotalRecords());//总记录数
		Assert.assertTrue(page.hasNextPage());//有下一页
		Assert.assertTrue(!page.hasPrevPage());//没有上一页
	}
	
	@Test
	public void testSelectListStream(){
		
		List<ParamEnumDef> list = null;
		IListStreamReader<ParamEnumDef> reader = dao.selectListStream("MENU");
		int batch = 0;
		System.out.println("###### 1 ");//第1次测试，调用固定size的方法
		while((list = reader.read()) != null){
			System.out.println("第["+(++batch)+"]批次，共["+list.size()+"]笔");
		}
		Assert.assertEquals(2, batch);//当前数据量
		
		System.out.println("###### 2 ");//第2次测试，调用可变size的方法，size和固定的相同
		reader = dao.selectListStream("MENU", 2);
		batch = 0;
		while((list = reader.read()) != null){
			System.out.println("第["+(++batch)+"]批次，共["+list.size()+"]笔");
		}
		Assert.assertEquals(2, batch);//当前数据量
		
		System.out.println("###### 3 ");//第3次测试，调用可变size的方法，size和固定的不同
		reader = dao.selectListStream("MENU", 3);
		batch = 0;
		while((list = reader.read()) != null){
			System.out.println("第["+(++batch)+"]批次，共["+list.size()+"]笔");
		}
		Assert.assertEquals(1, batch);//当前数据量
	}
	
	@Test
	public void testUpdate(){
		// 组装数据
		String paramCode = "TEST"+System.currentTimeMillis();
		ParamEnumDef param = buildParamEnumDef(paramCode);
		
		// 新增前
		ParamEnumDef dbParam = dao.select(paramCode);
		Assert.assertNull(dbParam);
		
		// 新增
		int count = dao.insert(param);
		Assert.assertEquals(1, count);
		
		// 新增后、修改前
		dbParam = dao.select(paramCode);
		Assert.assertNotNull(dbParam);
		Assert.assertEquals("测试", dbParam.getParamName());
		
		// 修改
		param.setParamName("修改后");
		count = dao.update(param);
		Assert.assertEquals(1, count);
		
		// 修改后、删除前
		dbParam = dao.select(paramCode);
		Assert.assertNotNull(dbParam);
		Assert.assertEquals("修改后", dbParam.getParamName());
		
		// 删除
		count = dao.delete(paramCode);
		Assert.assertEquals(1, count);
		
		// 删除后
		dbParam = dao.select(paramCode);
		Assert.assertNull(dbParam);
	}
	
	@Test
	public void testBatchSingleSql(){
		// 组装测试数据
		String paramCode = "TEST"+System.currentTimeMillis();
		ParamEnumDef param = buildParamEnumDef(paramCode);
		
		List<ParamEnumData> datas = new ArrayList<ParamEnumData>();
		param.setDatas(datas);
		for(int i = 0; i < 10; i++){
			ParamEnumData data = buildParamEnumData(paramCode, i);
			datas.add(data);
		}
		
		// 测试前
		List<ParamEnumData> dbDatas = dao.selectParamEnumDatas(paramCode);
		Assert.assertTrue(null == dbDatas || dbDatas.isEmpty());
		
		// 第一种方式执行批量
		int[] rs = dao.batchSingleSql1(datas);
		Assert.assertEquals(10, rs[0]);
		
		// 清理
		int count = dao.deleteParamEnumDatas(paramCode);
		Assert.assertEquals(10, count);
		
		// 第二种方式执行批量
		rs = dao.batchSingleSql2(param);
		Assert.assertEquals(10, rs[0]);
		
		// 清理
		count = dao.deleteParamEnumDatas(paramCode);
		Assert.assertEquals(10, count);
		
		List<Map<String, Object>> mapDatas = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < 10; i++){
			mapDatas.add(buildParamEnumDataMap(paramCode, i));
		}
		
		// 第三种方式执行批量
		rs = dao.batchSingleSql3(paramCode, mapDatas);
		Assert.assertEquals(10, rs[0]);
		
		// 清理
		count = dao.deleteParamEnumDatas(paramCode);
		Assert.assertEquals(10, count);
	}
	
	@Test
	public void testBatchMultiSql(){
		// 组装测试数据
		String paramCode = "TEST"+System.currentTimeMillis();
		ParamEnumDef param = buildParamEnumDef(paramCode);
		ParamEnumData data = buildParamEnumData(paramCode, 0);
		
		// 多SQL单参数：但是实际执行时每个SQL参数各不相同
		int[] rs = dao.batchMultiSql(paramCode, Arrays.<Object>asList(param, data));
		Assert.assertEquals(1, rs[0]);
		Assert.assertEquals(1, rs[1]);
		
		// 多SQL单参数：每次执行不同SQL时，参数也完全相同
		rs = dao.batchMultiSqlButSingleParam(paramCode);
		Assert.assertEquals(1, rs[0]);
		Assert.assertEquals(1, rs[1]);
		
		// 多SQL单参数：每次执行不同SQL时，参数也完全相同，并且这个参数是一个数组
		dao.batchMultiSql(paramCode, Arrays.<Object>asList(param, data));
		String paramCode2 = "TEST"+System.currentTimeMillis();
		dao.batchMultiSql(paramCode2, Arrays.<Object>asList(buildParamEnumDef(paramCode2), buildParamEnumData(paramCode2, 0)));
		rs = dao.batchMultiSqlButSingleParam(new String[]{paramCode, paramCode2});
		Assert.assertEquals(2, rs[0]);
		Assert.assertEquals(2, rs[1]);
	}

	private ParamEnumData buildParamEnumData(String paramCode, int i) {
		ParamEnumData data = new ParamEnumData();
		data.setDataCode("testDataCode"+i);
		data.setDataText("testDataText"+i);
		data.setSeqno(i+1);
		data.setParamCode(paramCode);
		return data;
	}
	
	private Map<String, Object> buildParamEnumDataMap(String paramCode, int i) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("dataCode", "testDataCode"+i);
		data.put("dataText", "testDataText"+i);
		data.put("seqno", i+1);
		return data;
	}

	private ParamEnumDef buildParamEnumDef(String paramCode) {
		ParamEnumDef param = new ParamEnumDef();
		param.setParamCode(paramCode);
		param.setParamName("测试");
		param.setParamGroup("TEST");
		param.setParamAttr("LIST");
		param.setSeqno(1);
		return param;
	}
}

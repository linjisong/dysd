package org.dysd.dao.mybatis.mapper.select;

import java.util.List;
import java.util.Map;

import org.dysd.dao.SpringTestSupport;
import org.dysd.dao.page.IPage;
import org.dysd.dao.page.impl.BasePage;
import org.dysd.dao.stream.IListStreamReader;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ISelectDaoTest extends SpringTestSupport{

	@Autowired(required=false)
	private ISelectDao dao;
	
	/**
	 * 查询单笔
	 * @throws Exception
	 */
	@Test
	public void selectOne() throws Exception {
		String paramCode = "BOOLEAN";
		Map<String, Object> o = dao.selectOne(paramCode);
		System.out.println(o);
		
		o = dao.selectOne(paramCode);
		System.out.println(o);
	}
	
	/**
	 * 查询多笔
	 * @throws Exception
	 */
	//@Test
	public void selectList() throws Exception {
		List<Map<String, Object>> o = dao.selectList("");
		System.out.println(o);
	}
	
	/**
	 * 查询分页
	 * @throws Exception
	 */
	//@Test
	public void selectPageList() throws Exception {
		IPage page = new BasePage();
		page.setPageProperty(0, 1, 15);
		List<Map<String, Object>> list = dao.selectList(page);
		System.out.println(list);
		//Assert.assertEquals(15, list.size());
	}
	
	/**
	 * 流式查询
	 * @throws Exception
	 */
	//@Test
	public void selectListStream() throws Exception {
		IListStreamReader<Map<String, Object>> reader = dao.selectListStream();
		List<Map<String, Object>> list = null;
		int batch = 0;
		while((list = reader.read()) != null){
			System.out.println("第"+(++batch)+"批次");
			System.out.println(list);
		}
	}
}

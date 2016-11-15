package org.dysd.dao.mybatis.mapper.call;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dysd.dao.SpringTestSupport;
import org.dysd.dao.call.ICallResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ICallDaoTest extends SpringTestSupport{

	@Autowired(required=false)
	private ICallDao dao;
	
	@Test
	public void call() throws Exception {
		ICallResult rs = dao.call("1");
		// 直接根据输出参数名称访问
		System.out.println("直接访问指定元素：================");
		String output1 = rs.getOutputParam("output1");
		System.out.println("output1==" + output1);
		
		String output2 = rs.getOutputParam("output2");
		System.out.println("output2==" + output2);
		
		List<Map<String, Object>> rs1 = rs.getOutputParam("rs1");
		System.out.println("rs1==" + rs1);
		
		List<Map<String, Object>> rs2 = rs.getOutputParam("rs2");
		System.out.println("rs2==" + rs2);
		
		// 循环访问输出参数
		Iterator<String> i = rs.iterator();
		System.out.println("循环访问所有元素：================");
		while(i.hasNext()){
			String name = i.next();
			System.out.println(name + "==" + rs.getOutputParam(name));
		}
	}
}

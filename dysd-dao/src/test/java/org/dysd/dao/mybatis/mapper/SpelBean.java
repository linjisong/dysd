package org.dysd.dao.mybatis.mapper;

import org.dysd.dao.mybatis.schema.expression.impl.SpelExpressionHandler;
import org.springframework.stereotype.Component;

@Component("spelBean")
public class SpelBean {

	public String param(String paramName){
		// 测试的是${()}，所以返回结果中添加单引号
		return "'PARAM-"+paramName+"'";
	}
	
	public String root(SpelExpressionHandler.Root root,String paramName){
		// 测试spel:为前缀的表达式，所以可以直接访问SpelExpressionHandler.Root对象
		return "ROOT-"+root.getDatabaseId()+"-"+paramName;
	}
}

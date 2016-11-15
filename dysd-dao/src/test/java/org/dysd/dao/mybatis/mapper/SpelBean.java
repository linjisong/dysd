package org.dysd.dao.mybatis.mapper;

import org.springframework.stereotype.Component;

@Component("spelBean")
public class SpelBean {

	public String resolve(Object param){
		System.out.println(param);
		return "TEST";
	}
}

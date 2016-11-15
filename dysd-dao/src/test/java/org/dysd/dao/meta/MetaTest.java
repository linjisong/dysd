package org.dysd.dao.meta;

import java.sql.Connection;

import javax.sql.DataSource;

import org.dysd.dao.DaoUtils;
import org.dysd.dao.SpringTestSupport;
import org.dysd.dao.util.DBUtils;
import org.dysd.dao.util.DBUtils.IConnectionCallback;
import org.junit.Assert;
import org.junit.Test;


public class MetaTest extends SpringTestSupport{

	/**
	 * 测试表是否存在
	 */
	@Test
	public void testTableExists() {
		DataSource dataSource = getDataSource();
		boolean exists = DBUtils.Meta.exist(dataSource, "BF_PARAM_ENUM_DEF");
		Assert.assertEquals(true, exists);
		
		boolean notExists = DBUtils.Meta.exist(dataSource, "BF_PARAM_ENUM_DEF_dd");
		Assert.assertEquals(false, notExists);
	}
	
	/**
	 * 测试连接回调函数
	 */
	@Test
	public void testConnectionCallback() {
		DBUtils.Connection.doInConnection(getDataSource(), new IConnectionCallback<Object>(){
			@Override
			public Object callback(Connection conn) {
				// 这里处理业务逻辑，而不用管Connection是如果打开，又是如何释放的
				return null;
			}
		});
	}
	
	private DataSource getDataSource(){
		return DaoUtils.getDefaultDataSource();
	}
}

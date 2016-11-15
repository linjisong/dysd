package org.dysd.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang3.RandomStringUtils;
import org.dysd.dao.DaoUtils;
import org.dysd.dao.SpringTestSupport;
import org.junit.Test;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class JdbcBatchTest extends SpringTestSupport{
	
	public void jdbcTransaction() throws Throwable {
		Connection conn = null;
		try{
			// 1.获取数据库连接
			conn = getConnection();
			// 2.设置不要自动提交，开启事务
			conn.setAutoCommit(false);
			
			// 执行sql1
			
			// 执行sql2
			
			// 3.执行完成，提交事务
			conn.commit();
		}catch(Throwable e){
			// 4.出现异常回滚事务
			conn.rollback();
			throw e;
		}finally{
			// 5.关闭数据库连接
			close(conn);
		}
	}

	private Connection getConnection(){
		return DataSourceUtils.getConnection(DaoUtils.getDefaultDataSource());
	}
	
	private void close(Connection conn){
		DataSourceUtils.releaseConnection(conn, DaoUtils.getDefaultDataSource());
	}
	
	@Test
	public void testName() throws Exception {
		Connection conn = null;
		try{
			// 1.获取数据库连接
			conn = DataSourceUtils.getConnection(DaoUtils.getDefaultDataSource());
			boolean auto = conn.getAutoCommit();
			// 2.设置不要自动提交，开启事务
			conn.setAutoCommit(false);
			// 执行sql1
			batchUpdate(conn);
			// 执行sql2
			clearTestData(conn);
			// 3.执行完成，提交事务
			conn.commit();
			conn.setAutoCommit(auto);
		}catch(Exception e){
			if(null != conn){
				// 4.出现异常回滚事务
				conn.rollback();
			}
			throw e;
		}finally{
			// 5.关闭数据库连接
			DataSourceUtils.releaseConnection(conn, DaoUtils.getDefaultDataSource());
		}
		
	}

	private void clearTestData(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement("delete from BF_PARAM_ENUM_DATA where PARAM_CODE = ? ");
			ps.setString(1, "TEST");
			int d = ps.executeUpdate();
			System.out.println("delete counts : " + d);
		}finally{
			try{
				ps.close();
			}catch(Exception e){}
		}
	}

	private void batchUpdate(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		try{
			String sql = "INSERT INTO BF_PARAM_ENUM_DATA(PARAM_CODE,DATA_CODE,DATA_TEXT,DATA_PARAM,SEQNO,DES)VALUES(?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			for(int i = 0; i < 10; i++){
				String random = RandomStringUtils.randomAlphabetic(8);
				ps.setString(1, "TEST");//PARAM_CODE
				ps.setString(2, random);//DATA_CODE
				ps.setString(3, "数据" + random);//DATA_TEXT
				ps.setString(4, "参数" + random);//DATA_PARAM
				ps.setInt(5, i);//SEQNO
				ps.setString(6, "");//MEMO
				ps.addBatch();
			}
			int[] rs = ps.executeBatch();
			System.out.print("batch insert : ");
			for(int r : rs){
				System.out.print(r+",");
			}
			System.out.println();
		}finally{
			try{
				ps.close();
			}catch(Exception e){}
		}
	}
}

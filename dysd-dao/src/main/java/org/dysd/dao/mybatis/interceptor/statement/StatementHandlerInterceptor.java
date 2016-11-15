/**
 * Copyright (c) 2016-2017, the original author or authors (dysd_2016@163.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dysd.dao.mybatis.interceptor.statement;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.RowBounds;
import org.dysd.dao.dialect.Dialects;
import org.dysd.dao.dialect.IDialect;
import org.dysd.dao.dialect.impl.SybaseASE;
import org.dysd.dao.exception.DaoExceptionCodes;
import org.dysd.dao.mybatis.adapter.PageAdapter;
import org.dysd.dao.mybatis.interceptor.AbstractInterceptor;
import org.dysd.dao.page.IPage;
import org.dysd.dao.util.DBUtils;
import org.dysd.util.Tool;
import org.dysd.util.exception.Throw;

/**
 * 语句预处理的拦截器插件
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
@Intercepts({ 
	@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class })
})
public class StatementHandlerInterceptor extends AbstractInterceptor {
	
	private static Field delegate;
	static{
		try {
			delegate = RoutingStatementHandler.class.getDeclaredField("delegate");
			delegate.setAccessible(true);
		} catch (Exception e) {
		}
	}

	/**
	 * 执行拦截器逻辑：
	 * <p>
	 * 	<ul>
	 * 		<li> 1.格式化字符串，去掉首尾空白字符，压缩中间连续多个空白字符为一个空白字符
	 *      <li> 2.如果是分页查询，根据需要计算总记录数，并替换为查询区间段的记录的SQL语句，实现物理分页
	 *  </ul>
	 * <p>
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler handler = super.getTarget(invocation, StatementHandler.class);
		if(handler instanceof RoutingStatementHandler){
			handler = (StatementHandler)delegate.get(handler);
		}
		MetaObject meta = SystemMetaObject.forObject(handler);
		
		//拦截SQL：SQL执行参数替换,SQL语句静态替换
		BoundSql boundSql = handler.getBoundSql();
    	MappedStatement mappedStatement =(MappedStatement) meta.getValue("mappedStatement");
    	ParameterHandler ph = handler.getParameterHandler();
    	
		String sql = boundSql.getSql();
		sql = Tool.STRING.formatWhitespace(sql);//格式化SQL字符串
		meta.setValue("boundSql.sql", sql);
    	
    	// 分页
    	RowBounds bounds = (RowBounds)meta.getValue("rowBounds");
		if(bounds instanceof PageAdapter){
    		PageAdapter pa = (PageAdapter)bounds;
    		IPage page = null;
    		if(null != pa.getPage()){
    			page = pa.getPage();
    			Connection conn = super.getArgument(invocation, Connection.class, 0);
    	        IDialect dialect = pa.getDialect();
    	        if(null == dialect){
    	        	dialect = Dialects.getDialect(conn);//数据库方言
    	        }
    	        setPageProperties(page, mappedStatement.getStatementLog(), conn, ph, dialect, sql);
	            //分页SQL
	        	String pageSql = dialect.getScopeSql(sql, page.getStart(), page.getPageSize());
		        meta.setValue("rowBounds", RowBounds.DEFAULT);//替换之前的指标
		        meta.setValue("boundSql.sql", pageSql); 
		        //特殊处理
		        if(dialect instanceof SybaseASE){
		        	// 一般地，分页查询不处于一个事务中，但由于Spring的注解事务，会处于事务中，这里先结束事务，以便可以执行分页查询SQL
		        	sybaseAse(conn);
		        }
    		}
    	}
		return invocation.proceed();
	}
	
	/**
	 * 设置分页属性
	 * @param page     分页对象
	 * @param log      日志对象
	 * @param conn     数据库
	 * @param handler  参数处理器
	 * @param dialect  数据库方言
	 * @param sql      原始SQL
	 */
	private void setPageProperties(IPage page, Log log, Connection conn, ParameterHandler handler, IDialect dialect, String sql) {
        PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			long total = page.getTotalRecords();
	        if(page.isNeedCalTotal()){
	        	pstmt = conn.prepareStatement(dialect.getTotalSql(sql));
				handler.setParameters(pstmt);
				rs = pstmt.executeQuery();
				if(rs.next())
				{
					total = rs.getInt(1);
				}
				if(null != log){
					log.debug("<==      Total:(Page) calculate page totals [" + total + "]");
				}
	        }else if(null != log){
				log.debug("<==      Total:(Page) use old page totals [" + total + "]");
	        }
	        page.setPageProperty(total);
		} catch (SQLException e) {
			Throw.throwException(DaoExceptionCodes.DYSD020005, e);
		} finally
		{
			DBUtils.Closer.close(pstmt, rs);
		}
	}
	
	private void sybaseAse(Connection conn){
		try{
			conn.commit();
			conn.setAutoCommit(false);
		}catch(SQLException e){
			//ignore
		}
	}
}

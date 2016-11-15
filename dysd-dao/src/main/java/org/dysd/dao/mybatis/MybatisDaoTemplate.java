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
package org.dysd.dao.mybatis;

import java.io.Reader;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dysd.dao.DaoUtils;
import org.dysd.dao.IDaoTemplate;
import org.dysd.dao.call.ICallResult;
import org.dysd.dao.exception.DaoExceptionCodes;
import org.dysd.dao.mybatis.adapter.CallAdapter;
import org.dysd.dao.mybatis.adapter.PageAdapter;
import org.dysd.dao.mybatis.stream.MybatisListStreamReader;
import org.dysd.dao.page.IPage;
import org.dysd.dao.stream.IListStreamReader;
import org.dysd.util.Tool;
import org.dysd.util.exception.Throw;
import org.dysd.util.logger.CommonLogger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 使用Mybatis实现的dao模板类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class MybatisDaoTemplate implements IDaoTemplate{
	
	/**
	 * 会话工厂
	 */
	private final SqlSessionFactory sqlSessionFactory;
	
	/**
	 * 事务管理
	 */
	private final PlatformTransactionManager transactionManager;
	
	/**
	 * mybatis配置
	 */
	private final Configuration configuration;
	
	/**
	 * SqlSession模板（普通模式执行），该模板使用Spring管理事务
	 */
	private final SqlSessionTemplate sqlSession;
	
	/**
	 * SqlSession模板（批量模式执行），该模板使用Spring管理事务
	 */
	private final SqlSessionTemplate batchSqlSession;
	
	/**
	 * 修改批量模式之前的批量模式
	 */
	private final ThreadLocal<Boolean> prevBatchType = new ThreadLocal<Boolean>();
	
	/**
	 * 是否批量模式
	 */
	private final ThreadLocal<Boolean> batchType = new ThreadLocal<Boolean>(){
		@Override
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}
	};
	
	/**
	 * 构造函数
	 * @param sqlSessionFactory
	 */
	public MybatisDaoTemplate(SqlSessionFactory sqlSessionFactory){
		this(sqlSessionFactory, null);
	}
	
	/**
	 * 构造函数
	 * @param sqlSessionFactory
	 * @param transactionManager
	 */
	public MybatisDaoTemplate(SqlSessionFactory sqlSessionFactory, PlatformTransactionManager transactionManager){
		this.sqlSessionFactory = sqlSessionFactory;
		this.sqlSession = new SqlSessionTemplate(sqlSessionFactory);
		this.batchSqlSession = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
		this.transactionManager = transactionManager;
		this.configuration = sqlSessionFactory.getConfiguration();
		DaoUtils.addDaoTemplate(configuration, this);
	}
	
	/**
	 * 普通模式会话
	 * @return
	 */
	public SqlSessionTemplate getSqlSession() {
		return sqlSession;
	}

	/**
	 * 批量模式会话
	 * @return
	 */
	public SqlSessionTemplate getBatchSqlSession() {
		return batchSqlSession;
	}

	/**
	 * 会话工厂
	 * @return
	 */
	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	/**
	 * 事务管理
	 * @return
	 */
	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	/**
	 * mybatis配置
	 * @return
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * 查询单笔
	 */
	@Override
	public <T> T selectOne(String sqlId) {
		return selectOne(sqlId, null);
	}

	/**
	 * 查询单笔
	 */
	@Override
	public <T> T selectOne(String sqlId, Object parameter) {
		SqlSession sqlSession = getExecuteSqlSession();
		T rs = sqlSession.<T>selectOne(DaoUtils.getExecuteSqlId(sqlId), parameter);
		return convertCamelProperties(rs);
	}

	/**
	 * 查询多笔
	 */
	@Override
	public <E> List<E> selectList(String sqlId) {
		return selectList(sqlId, null);
	}

	/**
	 * 查询多笔
	 */
	@Override
	public <E> List<E> selectList(String sqlId, Object parameter) {
		final List<E> list = new ArrayList<E>();
		SqlSession sqlSession = getExecuteSqlSession();
		sqlSession.select(DaoUtils.getExecuteSqlId(sqlId), parameter, new ResultHandler<E>(){
			public void handleResult(ResultContext<? extends E> context) {
				E result = convertCamelProperties(context.getResultObject());
				list.add(result);
			}
		});
		return list;
	}
	
	/**
	 * 分页查询
	 */
	@Override
	public <E> List<E> selectList(String sqlId, IPage page) {
		return selectList(sqlId, null, page);
	}

	/**
	 * 分页查询
	 */
	@Override
	public <E> List<E> selectList(String sqlId, Object parameter, IPage page) {
		final RowBounds adapter = new PageAdapter(page);
		final List<E> list = new ArrayList<E>();
		SqlSession sqlSession = getExecuteSqlSession();
		sqlSession.select(DaoUtils.getExecuteSqlId(sqlId), parameter, adapter, new ResultHandler<E>(){
			public void handleResult(ResultContext<? extends E> context) {
				E result = convertCamelProperties(context.getResultObject());
				list.add(result);
			}
		});
		return list;
	}

	/**
	 * 流式查询
	 */
	@Override
	public <E> IListStreamReader<E> selectListStream(String sqlId) {
		return new MybatisListStreamReader<E>(this, sqlId, null);
	}
	
	/**
	 * 流式查询
	 */
	@Override
	public <E> IListStreamReader<E> selectListStream(String sqlId, Object parameter) {
		return new MybatisListStreamReader<E>(this, sqlId, parameter);
	}
	
	/**
	 * 流式查询
	 */
	@Override
	public <E> IListStreamReader<E> selectListStream(String sqlId, int fetchSize) {
		return new MybatisListStreamReader<E>(this, sqlId, null, fetchSize);
	}

	/**
	 * 流式查询
	 */
	@Override
	public <E> IListStreamReader<E> selectListStream(String sqlId, Object parameter, int fetchSize) {
		return new MybatisListStreamReader<E>(this, sqlId, parameter, fetchSize);
	}
	
	/**
	 * 新增
	 */
	@Override
	public int insert(String sqlId) {
		return update(sqlId);
	}

	/**
	 * 新增
	 */
	@Override
	public int insert(String sqlId, Object parameter) {
		return update(sqlId, parameter);
	}
	
	/**
	 * 修改
	 */
	@Override
	public int update(String sqlId) {
		return update(sqlId, null);
	}

	/**
	 * 修改
	 */
	@Override
	public int update(String sqlId, Object parameter) {
		SqlSession sqlSession = getExecuteSqlSession();
		return sqlSession.update(DaoUtils.getExecuteSqlId(sqlId), parameter);
	}

	/**
	 * 删除
	 */
	@Override
	public int delete(String sqlId) {
		return update(sqlId);
	}

	/**
	 * 删除
	 */
	@Override
	public int delete(String sqlId, Object parameter) {
		return update(sqlId, parameter);
	}
	
	/**
	 * 执行批量，一个SQL执行多次
	 */
	@Override
	public int[] executeBatch(String sqlId, List<?> parameters) {
		List<String> sqlIds = new ArrayList<String>();
		for(int i = 0, s = parameters.size(); i < s; i++){
			sqlIds.add(sqlId);
		}
		return this.doExecuteBatch(sqlIds, parameters);
	}

	/**
	 * 执行批量，一次执行多个SQL
	 */
	@Override
	public int[] executeBatch(List<String> sqlIds) {
		return executeBatch(sqlIds, null);
	}
	
	/**
	 * 执行批量，一次执行多个SQL，且每次批量的参数不相同
	 */
	@Override
	public int[] executeBatch(List<String> sqlIds, List<?> parameters) {
		if(null == sqlIds){
			return null;
		} else if(sqlIds.isEmpty()){
			return ArrayUtils.EMPTY_INT_ARRAY;
		}else if(null != parameters && sqlIds.size() != parameters.size()){
			throw Throw.createException(DaoExceptionCodes.DYSD020015, sqlIds.size(), null == parameters ? 0 : parameters.size());
		}else {
			return this.doExecuteBatch(sqlIds, parameters);
		}
	}
	
	/**
	 * 打开批量执行模式
	 */
	@Override
	public void openBatchType() {
		this.prevBatchType.set(this.batchType.get());
		this.batchType.set(Boolean.TRUE);
	}

	/**
	 * 恢复打开批量模式之前的执行模式
	 */
	@Override
	public void resetExecutorType() {
		Boolean pbt = this.prevBatchType.get();
		if(null == pbt){
			pbt = Boolean.FALSE;
		}
		this.batchType.set(pbt);
		this.prevBatchType.remove();
	}
	
	/**
	 * 获取批量返回结果
	 */
	@Override
	public int[] flushBatch(){
		SqlSession batchSqlSession = getExecuteSqlSession();
		if(batchSqlSession == this.batchSqlSession){//这里直接比较是否同一个引用
			List<BatchResult> r = batchSqlSession.flushStatements();
			return resolveBatchResult(r);
		}
		return new int[0];
	}
	
	/**
	 * 实际执行批量
	 * @param sqlIds
	 * @param parameters
	 * @return
	 */
	private int[] doExecuteBatch(List<String> sqlIds, List<?> parameters){
		try{
			if(this.batchType.get()){
				this.flushBatch();//为避免影响本次批量执行结果，先执行之前已有的批量（如果有）
			}
			openBatchType();
			PlatformTransactionManager txManager = this.transactionManager;
			if(null == txManager){
				List<BatchResult> r = doExecuteBatch(sqlIds, parameters, batchSqlSession);
				return resolveBatchResult(r);
			}else{
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();  
				def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);  
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED); 
				TransactionStatus status = txManager.getTransaction(def); 
				try {  
					List<BatchResult> r = doExecuteBatch(sqlIds, parameters, batchSqlSession);
					txManager.commit(status); 
					return resolveBatchResult(r);
			    } catch (Throwable e) {  
			        txManager.rollback(status); 
			        throw Throw.createException(e);
			    }  
			}
		}finally{
			this.resetExecutorType();
		}
	}

	private List<BatchResult> doExecuteBatch(List<String> sqlIds, List<?> parameters, SqlSession batchSqlSession) {
		for(int i = 0, s = sqlIds.size(); i < s; i++){
			String sqlId = DaoUtils.getExecuteSqlId(sqlIds.get(i));
			Object param = parameters==null?null : parameters.get(i);
			CommonLogger.trace("batch " + (i+1) + " [sqlId : {"+ sqlId+"}, param : {" + (param == null ? "" : param.toString())+"}]");
			batchSqlSession.update(sqlId, param);
		}
		return batchSqlSession.flushStatements();
	}
	
	/**
	 * 调用存储过程
	 */
	@Override
	public ICallResult call(String sqlId) {
		return call(sqlId, null);
	}

	/**
	 * 调用存储过程
	 */
	@Override
	public ICallResult call(String sqlId, Object parameter) {
		CallAdapter adapter = new CallAdapter();
		SqlSession sqlSession = getExecuteSqlSession();
		sqlSession.selectList(DaoUtils.getExecuteSqlId(sqlId), parameter, adapter);
		return adapter.getCallResult();
	}
	
	/**
	 * 解析批量执行的返回结果
	 * @param result
	 * @return
	 */
	private static int[] resolveBatchResult(List<BatchResult> result){
		int[] rs = new int[result.size()];
		int i = 0;
		for(BatchResult br : result){
			int c = 0;
			for(int brr : br.getUpdateCounts()){
				c += brr;
			}
			rs[i++] = c;
		}
		return rs;
	}
	
	/**
	 * 将Map中Key值转换为驼峰式
	 * @param obj  原始对象
	 * @return 如果是Map，则将key值转换为驼峰式，否则直接转换
	 */
	@SuppressWarnings("unchecked")
	private <E>E convertCamelProperties(E obj){
		if(obj instanceof Map){
			Map<String, Object> map = (Map<String, Object>)obj;
			String[] key = new String[map.size()];
			map.keySet().toArray(key);
			for(String k : key){
				Object value = map.remove(k);
				if(value instanceof Clob){
					try{
						Clob clob = (Clob)value;
						Reader reader = clob.getCharacterStream(1, clob.length());
						value = IOUtils.toString(reader);	
					}catch(Exception e){}
				}
				map.put(Tool.STRING.convertToCamel(k),value);
			}
			return (E)map;
		}else{
			return obj;
		}
	}
	
	private SqlSession getExecuteSqlSession(){
		return this.batchType.get() ? batchSqlSession : sqlSession;
	}
}

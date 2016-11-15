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
package org.dysd.dao.mybatis.interceptor.resultset;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.dysd.dao.call.impl.CallResult;
import org.dysd.dao.exception.DaoExceptionCodes;
import org.dysd.dao.mybatis.adapter.CallAdapter;
import org.dysd.dao.mybatis.interceptor.AbstractInterceptor;
import org.dysd.util.exception.Throw;

/**
 * 结果集处理输出参数的拦截器插件
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleOutputParameters", args = {CallableStatement.class }) })
public class ResultSetHandlerInterceptor extends AbstractInterceptor{

	/**
	 * 执行拦截器处理
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		ResultSetHandler handler = super.getTarget(invocation, ResultSetHandler.class);
		MetaObject meta = SystemMetaObject.forObject(handler);
		final RowBounds rowBounds = (RowBounds)meta.getValue("rowBounds");
		if(rowBounds instanceof CallAdapter){
			CallableStatement statment = super.getArgument(invocation, CallableStatement.class, 0);
			handleOutputParameters(statment, meta, handler, (CallAdapter)rowBounds);
			return null;
		}else{
			return invocation.proceed();
		}
	}
	
	  private void handleOutputParameters(CallableStatement cs, MetaObject meta, ResultSetHandler handler, CallAdapter adapter) throws SQLException {
		final Map<String, Object> results = new LinkedHashMap<String, Object>(); 
		final ParameterHandler parameterHandler = (ParameterHandler)meta.getValue("parameterHandler");
		final Configuration configuration = (Configuration)meta.getValue("configuration");
		final BoundSql boundSql = (BoundSql)meta.getValue("boundSql");
	    final Object parameterObject = parameterHandler.getParameterObject();
	    final MetaObject metaParam = configuration.newMetaObject(parameterObject);
	    final MetaObject mapMetaParam = configuration.newMetaObject(results);
	    final List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
	    for (int i = 0; i < parameterMappings.size(); i++) {
	      final ParameterMapping parameterMapping = parameterMappings.get(i);
	      final String property = parameterMapping.getProperty();
	      if (parameterMapping.getMode() == ParameterMode.OUT || parameterMapping.getMode() == ParameterMode.INOUT) {
	    	  try{
	    		  if (ResultSet.class.equals(parameterMapping.getJavaType())) {
	  	        	MethodUtils.invokeExactMethod(handler, "handleRefCursorOutputParameter", new Object[]{cs.getObject(i + 1), parameterMapping, mapMetaParam});
	  	        } else {
	  	          final TypeHandler<?> typeHandler = parameterMapping.getTypeHandler();
	  	          results.put(property, typeHandler.getResult(cs, i + 1));
	  	        }
	    		  try{
	    			  metaParam.setValue(property, results.get(property));
	    		  }catch(Exception ignore){}
	    	  }catch(Exception e){
	    		  Throw.throwException(DaoExceptionCodes.DYSD020006, e);
	    	  }
	      }
	    }
	    CallResult callResult = new CallResult();
	    callResult.addAllResult(results);
	    adapter.setCallResult(callResult);
	  }
}

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
package org.dysd.dao.mybatis.schema;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.dysd.dao.mybatis.DialectDatabaseIdProvider;
import org.dysd.dao.mybatis.SqlSessionFactoryBean;
import org.dysd.dao.mybatis.schema.context.ISqlMapperParserContext;
import org.dysd.dao.mybatis.schema.context.impl.SqlMapperParserContext;
import org.dysd.util.xml.parser.XmlParserUtils;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.util.xml.XmlValidationModeDetector;

/**
 * XSD模式下的SqlSessionFactoryBean
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class SchemaSqlSessionFactoryBean extends SqlSessionFactoryBean{
	
	public SchemaSqlSessionFactoryBean() {
		super();
		this.setDatabaseIdProvider(new DialectDatabaseIdProvider());
	}

	/**
	 * 解析单个SqlMapper配置文件
	 */
	@Override
	protected void doParseSqlMapperResource(Configuration configuration, Resource mapperLocation)
			throws NestedIOException {
		int mode = detectValidationMode(mapperLocation);
		if(mode == XmlValidationModeDetector.VALIDATION_DTD){//如果是DTD，使用Mybatis官方的解析
			super.doParseSqlMapperResource(configuration, mapperLocation);
		}else{
			try {
				// 使用Schema校验
				this.doParseSqlMapperResourceWithSchema(configuration, mapperLocation);
			} catch (Exception e) {
				throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
			} finally {
				ErrorContext.instance().reset();
			}
		}
	}
	
	/**
	 * 解析使用XSD配置的SqlMapper文件
	 * @param configuration
	 * @param mapperLocation
	 */
	protected void doParseSqlMapperResourceWithSchema(Configuration configuration, Resource mapperLocation){
		ISqlMapperParserContext context = new SqlMapperParserContext(configuration);
		XmlParserUtils.parseXml(context, mapperLocation);
	}

	/**
	 * 自定义设置
	 */
	@Override
	protected void doCustomConfiguration(Configuration configuration) {
		super.doCustomConfiguration(configuration);
		/**
		 * 设置默认的返回结果类型为Map
		 */
		setDefaultResultType(configuration, Map.class);
	}

	private int detectValidationMode(Resource mapperLocation) throws NestedIOException {
		int mode = -1;
		try {
			XmlValidationModeDetector detector = new XmlValidationModeDetector();
			mode = detector.detectValidationMode(mapperLocation.getInputStream());
		} catch (Exception e) {
			throw new NestedIOException("Failed to parse mapping resource: '" + mapperLocation + "'", e);
		} finally {
			ErrorContext.instance().reset();
		}
		return mode;
	}
	
	private void setDefaultResultType(Configuration configuration, Class<?> cls){
		try {
			Field resultMaps = MappedStatement.class.getDeclaredField("resultMaps");
			resultMaps.setAccessible(true);
			for(Iterator<MappedStatement> i = configuration.getMappedStatements().iterator(); i.hasNext();){
				Object mappedStatement = i.next();
				if(mappedStatement instanceof MappedStatement){
					MappedStatement ms = (MappedStatement)mappedStatement;
					if(SqlCommandType.SELECT.equals(ms.getSqlCommandType()) && ms.getResultMaps().isEmpty()){
						ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(configuration,ms.getId()+"-Inline",cls,new ArrayList<ResultMapping>(),null);
						ResultMap resultMap = inlineResultMapBuilder.build();
						List<ResultMap> rm = new ArrayList<ResultMap>();
						rm.add(resultMap);
						resultMaps.set(ms, Collections.unmodifiableList(rm));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

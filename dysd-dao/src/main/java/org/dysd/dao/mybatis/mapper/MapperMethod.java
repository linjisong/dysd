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
package org.dysd.dao.mybatis.mapper;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Flush;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.TypeParameterResolver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.dysd.dao.DaoUtils;
import org.dysd.dao.IDaoTemplate;
import org.dysd.dao.annotation.Executes;
import org.dysd.dao.annotation.FetchSize;
import org.dysd.dao.annotation.OriginalUsage;
import org.dysd.dao.annotation.SqlRef;
import org.dysd.dao.annotation.SqlRefs;
import org.dysd.dao.call.ICallResult;
import org.dysd.dao.exception.DaoExceptionCodes;
import org.dysd.dao.mybatis.adapter.PageAdapter;
import org.dysd.dao.mybatis.component.impl.MybatisComponents;
import org.dysd.dao.page.IPage;
import org.dysd.dao.stream.IListStreamReader;
import org.dysd.util.Tool;
import org.dysd.util.exception.Throw;
import org.dysd.util.logger.CommonLogger;

/**
 * 代理方法，实现真正的逻辑
 * <pre>
 * 	<li> 1.返回int[]，并且没有OriginalUsage注解，作为批量处理
 *  <li> 2.返回ICallResult，作为存储过程调用
 *  <li> 3.返回IListStreamReader，作为流式查询处理
 *  <li> 4.insert|update|delete元素，分别作为新增|修改|删除处理
 *  <li> 5.select元素，作为查询处理
 * </pre>
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class MapperMethod {

	private final SqlCommand command;
	private final MethodSignature method;
	private final Method originMethod;
	private final IDaoTemplate dao;

	public MapperMethod(Class<?> mapperInterface, Method method, Configuration config) {
		this.command = new SqlCommand(config, mapperInterface, method);
		this.method = new MethodSignature(config, mapperInterface, method);
		this.originMethod = method;
		this.dao = DaoUtils.getDaoTemplate(config);
	}

	public Object execute(SqlSession sqlSession, Object[] args) {
		Object result;
		String sqlId = command.name;
		Class<?> returnType = method.returnType;

		if (method.returnBatch) {// 批量
			if (!originMethod.isAnnotationPresent(Executes.class)) {
				SqlRefs refs = this.originMethod.getAnnotation(SqlRefs.class);
				if (null == refs || refs.value() == null || refs.value().length == 0) {
					List<Object> param = method.paramNameResolver.getBatchNamedParams(args, 1);
					result = dao.executeBatch(sqlId, param);
				} else {
					List<String> sqlIds = new ArrayList<String>();
					for (SqlRef ref : refs.value()) {
						sqlIds.add(DaoUtils.resolveSqlId(ref, originMethod));
					}
					List<Object> param = method.paramNameResolver.getBatchNamedParams(args, sqlIds.size());
					result = dao.executeBatch(sqlIds, param);
				}
			} else {
				List<String> sqlIds = new ArrayList<String>();
				List<Object> params = new ArrayList<Object>();
				method.paramNameResolver.resolveExecuteNamedParams(args, sqlIds, params);
				if (null == params || params.isEmpty()) {
					CommonLogger.warn("the batch params is null or empty, so ignore the method [" + this.originMethod + "]");
					return null;
				}
				result = dao.executeBatch(sqlIds, params);
			}
		} else if (ICallResult.class.isAssignableFrom(returnType)) {
			Object param = method.convertArgsToSqlCommandParam(args);
			result = dao.call(sqlId, param);
		} else if (IListStreamReader.class.isAssignableFrom(returnType)) {// 流式查询
			Object param = method.convertArgsToSqlCommandParam(args);
			int fetchSize = getFetchSize(originMethod, args);
			if (-1 != fetchSize) {
				result = dao.selectListStream(sqlId, param, fetchSize);
			} else {
				result = dao.selectListStream(sqlId, param);
			}
		} else {
			switch (command.type) {
			case INSERT: {
				Object param = method.convertArgsToSqlCommandParam(args);
				result = rowCountResult(dao.insert(sqlId, param));
				break;
			}
			case UPDATE: {
				Object param = method.convertArgsToSqlCommandParam(args);
				result = rowCountResult(dao.update(sqlId, param));
				break;
			}
			case DELETE: {
				Object param = method.convertArgsToSqlCommandParam(args);
				result = rowCountResult(dao.delete(sqlId, param));
				break;
			}
			case SELECT:
				if (method.returnsVoid && method.hasResultHandler()) {
					executeWithResultHandler(sqlSession, args);
					result = null;
				} else if (method.returnsMany) {
					result = executeForMany(sqlSession, args);
				} else if (method.returnsMap) {
					result = executeForMap(sqlSession, args);
				} else if (method.returnsCursor) {
					result = executeForCursor(sqlSession, args);
				} else {
					Object param = method.convertArgsToSqlCommandParam(args);
					result = dao.selectOne(sqlId, param);
				}
				break;
			case FLUSH:
				result = sqlSession.flushStatements();
				break;
			default:
				throw new BindingException("Unknown execution method for: " + command.name);
			}
		}
		if (result == null && returnType.isPrimitive() && !method.returnsVoid) {
			throw new BindingException("Mapper method '" + command.name
					+ " attempted to return null from a method with a primitive return type (" + returnType + ").");
		}
		return result;
	}

	private int getFetchSize(Method method, Object[] args) {
		FetchSize fs = null;
		Object[][] annos = method.getParameterAnnotations();
		for (int i = 0, l = annos.length; i < l; i++) {
			for (Object aAnno : annos[i]) {
				if (aAnno instanceof FetchSize) {
					fs = (FetchSize) aAnno;
					Object arg = args[i];
					if (arg instanceof Integer) {
						return ((Integer) arg).intValue();
					} else {
						return fs.value();
					}
				}
			}
		}
		fs = this.originMethod.getAnnotation(FetchSize.class);
		if (null != fs) {
			return fs.value();
		}
		return -1;
	}

	private Object rowCountResult(int rowCount) {
		final Object result;
		if (method.returnsVoid) {
			result = null;
		} else if (Integer.class.equals(method.returnType) || Integer.TYPE.equals(method.returnType)) {
			result = rowCount;
		} else if (Long.class.equals(method.returnType) || Long.TYPE.equals(method.returnType)) {
			result = (long) rowCount;
		} else if (Boolean.class.equals(method.returnType) || Boolean.TYPE.equals(method.returnType)) {
			result = rowCount > 0;
		} else {
			throw new BindingException("Mapper method '" + command.name + "' has an unsupported return type: " + method.returnType);
		}
		return result;
	}

	private void executeWithResultHandler(SqlSession sqlSession, Object[] args) {
		String sqlId = command.name;
		MappedStatement ms = sqlSession.getConfiguration().getMappedStatement(sqlId);
		if (void.class.equals(ms.getResultMaps().get(0).getType())) {
			throw new BindingException(
					"method " + sqlId + " needs either a @ResultMap annotation, a @ResultType annotation,"
							+ " or a resultType attribute in XML so a ResultHandler can be used as a parameter.");
		}
		Object param = method.convertArgsToSqlCommandParam(args);
		if (method.hasRowBounds()) {
			RowBounds rowBounds = method.extractRowBounds(args);
			sqlSession.select(sqlId, param, rowBounds, method.extractResultHandler(args));
		} else {
			sqlSession.select(sqlId, param, method.extractResultHandler(args));
		}
	}

	private <E> Object executeForMany(SqlSession sqlSession, Object[] args) {
		List<E> result;
		Object param = method.convertArgsToSqlCommandParam(args);
		if (method.hasRowBounds()) {
			IPage page = method.extractPageParam(args);
			if (null != page) {
				return dao.selectList(command.name, param, page);
			} else {
				RowBounds rowBounds = method.extractRowBounds(args);
				result = sqlSession.<E>selectList(command.name, param, rowBounds);
			}
		} else {
			result = dao.<E>selectList(command.name, param);
		}
		// issue #510 Collections & arrays support
		if (!method.returnType.isAssignableFrom(result.getClass())) {
			if (method.returnType.isArray()) {
				return convertToArray(result);
			} else {
				return convertToDeclaredCollection(sqlSession.getConfiguration(), result);
			}
		}
		return result;
	}

	private <T> Cursor<T> executeForCursor(SqlSession sqlSession, Object[] args) {
		Cursor<T> result;
		Object param = method.convertArgsToSqlCommandParam(args);
		if (method.hasRowBounds()) {
			RowBounds rowBounds = method.extractRowBounds(args);
			result = sqlSession.<T>selectCursor(command.name, param, rowBounds);
		} else {
			result = sqlSession.<T>selectCursor(command.name, param);
		}
		return result;
	}

	private <E> Object convertToDeclaredCollection(Configuration config, List<E> list) {
		Object collection = config.getObjectFactory().create(method.returnType);
		MetaObject metaObject = config.newMetaObject(collection);
		metaObject.addAll(list);
		return collection;
	}

	@SuppressWarnings("unchecked")
	private <E> Object convertToArray(List<E> list) {
		Class<?> componentType = method.returnType.getComponentType();
		int size = list.size();
		if (componentType.equals(int.class)) {
			int[] rs = new int[size];
			int i = 0;
			for (E o : list) {
				rs[i++] = Tool.CONVERT.convert(o, int.class);
			}
			return rs;
		} else if (componentType.equals(short.class)) {
			short[] rs = new short[size];
			int i = 0;
			for (E o : list) {
				rs[i++] = Tool.CONVERT.convert(o, short.class);
			}
			return rs;
		} else if (componentType.equals(byte.class)) {
			byte[] rs = new byte[size];
			int i = 0;
			for (E o : list) {
				rs[i++] = Tool.CONVERT.convert(o, byte.class);
			}
			return rs;
		} else if (componentType.equals(char.class)) {
			char[] rs = new char[size];
			int i = 0;
			for (E o : list) {
				rs[i++] = Tool.CONVERT.convert(o, char.class);
			}
			return rs;
		} else if (componentType.equals(long.class)) {
			long[] rs = new long[size];
			int i = 0;
			for (E o : list) {
				rs[i++] = Tool.CONVERT.convert(o, long.class);
			}
			return rs;
		} else if (componentType.equals(float.class)) {
			float[] rs = new float[size];
			int i = 0;
			for (E o : list) {
				rs[i++] = Tool.CONVERT.convert(o, float.class);
			}
			return rs;
		} else if (componentType.equals(double.class)) {
			double[] rs = new double[size];
			int i = 0;
			for (E o : list) {
				rs[i++] = Tool.CONVERT.convert(o, double.class);
			}
			return rs;
		} else if (componentType.equals(boolean.class)) {
			boolean[] rs = new boolean[size];
			int i = 0;
			for (E o : list) {
				rs[i++] = o == null ? false : Tool.CONVERT.string2Boolean(o.toString());
			}
			return rs;
		} else {
			E[] array = (E[]) Array.newInstance(componentType, size);
			array = list.toArray(array);
			return array;
		}
	}

	private <K, V> Map<K, V> executeForMap(SqlSession sqlSession, Object[] args) {
		Map<K, V> result;
		Object param = method.convertArgsToSqlCommandParam(args);
		if (method.hasRowBounds()) {
			RowBounds rowBounds = method.extractRowBounds(args);
			result = sqlSession.<K, V>selectMap(command.name, param, method.mapKey, rowBounds);
		} else {
			result = sqlSession.<K, V>selectMap(command.name, param, method.mapKey);
		}
		return result;
	}

	public static class ParamMap<V> extends HashMap<String, V> {

		private static final long serialVersionUID = -2212268410512043556L;

		// @Override
		// public V get(Object key) {
		// if (!super.containsKey(key)) {
		// throw new BindingException("Parameter '" + key + "' not found.
		// Available parameters are " + keySet());
		// }
		// return super.get(key);
		// }

	}

	public static class SqlCommand {

		private final String name;
		private final SqlCommandType type;

		/* package */ SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
			SqlRef ref = method.getAnnotation(SqlRef.class);
			String statementName = DaoUtils.getExecuteSqlId(DaoUtils.resolveSqlId(ref, method));
			MappedStatement ms = null;
			if (null != statementName && configuration.hasStatement(statementName)) {
				ms = configuration.getMappedStatement(statementName);
			} else if (!mapperInterface.equals(method.getDeclaringClass())) { // issue #35
				String parentStatementName = DaoUtils
						.getExecuteSqlId(method.getDeclaringClass().getName() + "." + method.getName());
				if (configuration.hasStatement(parentStatementName)) {
					ms = configuration.getMappedStatement(parentStatementName);
				}
			}
			if (ms == null) {
				if (method.getAnnotation(Flush.class) != null) {
					name = null;
					type = SqlCommandType.FLUSH;
				} else if (method.isAnnotationPresent(SqlRefs.class) || method.isAnnotationPresent(Executes.class)) {
					name = null;
					type = null;
				} else {
					// throw new BindingException("Invalid bound statement (not
					// found): " + statementName);
					throw Throw.createException(DaoExceptionCodes.DYSD020016, statementName);
				}
			} else {
				name = ms.getId();
				type = ms.getSqlCommandType();
				if (type == SqlCommandType.UNKNOWN) {
					throw new BindingException("Unknown execution method for: " + name);
				}
			}
		}
	}

	public static class MethodSignature {

		private final boolean returnsMany;
		private final boolean returnsMap;
		private final boolean returnsVoid;
		private final boolean returnsCursor;
		private final boolean returnBatch;
		private final Class<?> returnType;
		private final String mapKey;
		private final Integer resultHandlerIndex;
		private final Integer rowBoundsIndex;
		private final IParamResolver paramNameResolver;

		public MethodSignature(Configuration configuration, Class<?> mapperInterface, Method method) {
			SqlRefs refs = method.getAnnotation(SqlRefs.class);
			if (null != refs && (null == refs.value() || refs.value().length <= 1)) {
				Throw.throwException(DaoExceptionCodes.DYSD020014, method);
			}

			Type resolvedReturnType = TypeParameterResolver.resolveReturnType(method, mapperInterface);
			if (resolvedReturnType instanceof Class<?>) {
				this.returnType = (Class<?>) resolvedReturnType;
			} else if (resolvedReturnType instanceof ParameterizedType) {
				this.returnType = (Class<?>) ((ParameterizedType) resolvedReturnType).getRawType();
			} else {
				this.returnType = method.getReturnType();
			}
			this.returnsVoid = void.class.equals(this.returnType);
			this.returnBatch = int[].class.equals(returnType) && !method.isAnnotationPresent(OriginalUsage.class);
			this.returnsMany = !returnBatch
					&& (configuration.getObjectFactory().isCollection(this.returnType) || this.returnType.isArray());
			this.returnsCursor = Cursor.class.equals(this.returnType);
			this.mapKey = getMapKey(method);
			this.returnsMap = (this.mapKey != null);
			this.rowBoundsIndex = getUniquePageParamIndex(method);
			this.resultHandlerIndex = getUniqueParamIndex(method, ResultHandler.class);
			this.paramNameResolver = MybatisComponents.newParamResolver(configuration, method, returnBatch);
		}

		/* package */ Object convertArgsToSqlCommandParam(Object[] args) {
			return paramNameResolver.getNamedParams(args);
		}

		/* package */ boolean hasRowBounds() {
			return rowBoundsIndex != null;
		}

		/* package */ RowBounds extractRowBounds(Object[] args) {
			PageAdapter rb = null;
			if (hasRowBounds()) {
				Object o = args[rowBoundsIndex];
				if (o instanceof IPage) {
					rb = new PageAdapter((IPage) o);
				} else {
					return (RowBounds) o;
				}
			}
			return rb;
			// return hasRowBounds() ? (RowBounds) args[rowBoundsIndex] : null;
		}

		/* package */ IPage extractPageParam(Object[] args) {
			IPage rb = null;
			if (hasRowBounds()) {
				Object o = args[rowBoundsIndex];
				if (o instanceof IPage) {
					return (IPage) o;
				}
			}
			return rb;
		}

		/* package */ boolean hasResultHandler() {
			return resultHandlerIndex != null;
		}

		/* package */ ResultHandler<?> extractResultHandler(Object[] args) {
			return hasResultHandler() ? (ResultHandler<?>) args[resultHandlerIndex] : null;
		}

		private Integer getUniquePageParamIndex(Method method) {
			Integer index = null;
			final Class<?>[] argTypes = method.getParameterTypes();
			Class<?> rcls = RowBounds.class;
			Class<?> pcls = IPage.class;
			for (int i = 0; i < argTypes.length; i++) {
				if (rcls.isAssignableFrom(argTypes[i]) || pcls.isAssignableFrom(argTypes[i])) {
					if (index == null) {
						index = i;
					} else {
						throw new BindingException(method.getName() + " cannot have multiple " + pcls.getSimpleName() + " parameters");
					}
				}
			}
			return index;
		}

		private Integer getUniqueParamIndex(Method method, Class<?> paramType) {
			Integer index = null;
			final Class<?>[] argTypes = method.getParameterTypes();
			for (int i = 0; i < argTypes.length; i++) {
				if (paramType.isAssignableFrom(argTypes[i])) {
					if (index == null) {
						index = i;
					} else {
						throw new BindingException(method.getName() + " cannot have multiple "
								+ paramType.getSimpleName() + " parameters");
					}
				}
			}
			return index;
		}

		private String getMapKey(Method method) {
			String mapKey = null;
			if (Map.class.isAssignableFrom(method.getReturnType())) {
				final MapKey mapKeyAnnotation = method.getAnnotation(MapKey.class);
				if (mapKeyAnnotation != null) {
					mapKey = mapKeyAnnotation.value();
				}
			}
			return mapKey;
		}
	}

}

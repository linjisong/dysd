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
package org.dysd.dao.mybatis.mapper.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.scripting.xmltags.OgnlCache;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.dysd.dao.DaoUtils;
import org.dysd.dao.annotation.BatchParam;
import org.dysd.dao.annotation.Execute;
import org.dysd.dao.annotation.Executes;
import org.dysd.dao.annotation.SqlRefs;
import org.dysd.dao.exception.DaoExceptionCodes;
import org.dysd.dao.mybatis.mapper.IParamResolver;
import org.dysd.dao.page.IPage;
import org.dysd.util.Tool;
import org.dysd.util.exception.Throw;
import org.dysd.util.logger.CommonLogger;

/**
 * mybatis动态代理时的参数解析器实现类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class ParamNameResolver implements IParamResolver {

	private static final String GENERIC_NAME_PREFIX = "param";
	private static final String PARAMETER_CLASS = "java.lang.reflect.Parameter";
	private static Method GET_NAME = null;
	private static Method GET_PARAMS = null;

	static {
		try {
			Class<?> paramClass = Resources.classForName(PARAMETER_CLASS);
			GET_NAME = paramClass.getMethod("getName");
			GET_PARAMS = Method.class.getMethod("getParameters");
		} catch (Exception e) {
			// ignore
		}
	}

	/**
	 * <p>
	 * The key is the index and the value is the name of the parameter.<br />
	 * The name is obtained from {@link Param} if specified. When {@link Param}
	 * is not specified, the parameter index is used. Note that this index could
	 * be different from the actual index when the method has special parameters
	 * (i.e. {@link RowBounds} or {@link ResultHandler}).
	 * </p>
	 * <ul>
	 * <li>aMethod(@Param("M") int a, @Param("N") int b) -&gt; {{0, "M"}, {1, "N"}}</li>
	 * <li>aMethod(int a, int b) -&gt; {{0, "0"}, {1, "1"}}</li>
	 * <li>aMethod(int a, RowBounds rb, int b) -&gt; {{0, "0"}, {2, "1"}}</li>
	 * </ul>
	 */
	private final SortedMap<Integer, String> names;

	private boolean hasParamAnnotation;

	private final Method method;
	private final Executes executes;
	private final int batchItemIndex;
	private final BatchParam batchParam;

	public ParamNameResolver(Configuration config, Method method, boolean returnBatch) {
		this.method = method;
		final Class<?>[] paramTypes = method.getParameterTypes();
		final Annotation[][] paramAnnotations = method.getParameterAnnotations();
		final SortedMap<Integer, String> map = new TreeMap<Integer, String>();
		int paramCount = paramAnnotations.length;
		// get names from @Param annotations
		for (int paramIndex = 0; paramIndex < paramCount; paramIndex++) {
			if (isSpecialParameter(paramTypes[paramIndex])) {
				// skip special parameters
				continue;
			}
			String name = null;
			for (Annotation annotation : paramAnnotations[paramIndex]) {
				if (annotation instanceof Param) {
					hasParamAnnotation = true;
					name = ((Param) annotation).value();
					break;
				}
			}

			// @Param was not specified.
			if (name == null && config.isUseActualParamName()) {
				name = getActualParamName(method, paramIndex);
			}

			if (name == null) {
				// use the parameter index as the name ("0", "1", ...)
				// gcode issue #71
				// name = String.valueOf(map.size());
				name = String.valueOf(paramIndex);
			}
			map.put(paramIndex, name);
		}
		names = Collections.unmodifiableSortedMap(map);

		Executes executes = null;
		int batchItemIndex = -1;
		BatchParam batchParam = null;
		if (returnBatch) {
			executes = method.getAnnotation(Executes.class);
			if (null == executes || null == executes.value() || 0 == executes.value().length) {
				Class<?>[] clss = method.getParameterTypes();
				if (null != clss) {
					final Object[][] paramAnnos = method.getParameterAnnotations();
					for (int i = 0, l = clss.length; i < l; i++) {
						batchParam = getBatchParamAnnoation(paramAnnos[i]);
						if (null != batchParam) {// 存在批量参数注解
							batchItemIndex = i;
							break;
						}
					}
				}
				executes = null;
			}
		} else {
			if (null != method.getAnnotation(Executes.class)) {
				Throw.throwException(DaoExceptionCodes.DYSD020018, Executes.class.getSimpleName(), method);
			} else if (null != method.getAnnotation(SqlRefs.class)) {
				Throw.throwException(DaoExceptionCodes.DYSD020018, SqlRefs.class.getSimpleName(), method);
			} else if (null != method.getAnnotation(Execute.class)) {
				Throw.throwException(DaoExceptionCodes.DYSD020018, Execute.class.getSimpleName(), method);
			} else {
				Class<?>[] clss = method.getParameterTypes();
				if (null != clss) {
					final Object[][] paramAnnos = method.getParameterAnnotations();
					for (int i = 0, l = clss.length; i < l; i++) {
						if (null != getBatchParamAnnoation(paramAnnos[i])) {// 存在批量参数注解
							Throw.throwException(DaoExceptionCodes.DYSD020018, BatchParam.class.getSimpleName(),
									method);
						}
					}
				}
			}
		}

		this.executes = executes;
		this.batchItemIndex = batchItemIndex;
		this.batchParam = batchParam;
	}

	private String getActualParamName(Method method, int paramIndex) {
		if (GET_PARAMS == null) {
			return null;
		}
		try {
			Object[] params = (Object[]) GET_PARAMS.invoke(method);
			return (String) GET_NAME.invoke(params[paramIndex]);
		} catch (Exception e) {
			throw new ReflectionException("Error occurred when invoking Method#getParameters().", e);
		}
	}

	private static boolean isSpecialParameter(Class<?> clazz) {
		return RowBounds.class.isAssignableFrom(clazz)
				|| ResultHandler.class.isAssignableFrom(clazz)
				|| IPage.class.isAssignableFrom(clazz);
	}

	/**
	 * Returns parameter names referenced by SQL providers.
	 */
//	private String[] getNames() {
//		return names.values().toArray(new String[0]);
//	}

	/**
	 * <p>
	 * A single non-special parameter is returned without a name.<br />
	 * Multiple parameters are named using the naming rule.<br />
	 * In addition to the default names, this method also adds the generic names
	 * (param1, param2, ...).
	 * </p>
	 */
	@Override
	public Object getNamedParams(Object[] args) {
		final int paramCount = names.size();
		if (args == null || paramCount == 0) {
			return null;
		} else if (!hasParamAnnotation && paramCount == 1) {
			return args[names.firstKey()];
		} else {
			final Map<String, Object> param = new ParamMap<Object>();
			int i = 0;
			for (Map.Entry<Integer, String> entry : names.entrySet()) {
				param.put(entry.getValue(), args[entry.getKey()]);
				// add generic param names (param1, param2, ...)
				final String genericParamName = GENERIC_NAME_PREFIX + String.valueOf(i + 1);
				// ensure not to overwrite parameter named with @Param
				if (!names.containsValue(genericParamName)) {
					param.put(genericParamName, args[entry.getKey()]);
				}
				i++;
			}
			return param;
		}
	}

	@Override
	public List<Object> getBatchNamedParams(Object[] args, int batchCount) {
		final int paramCount = names.size();
		if (args == null || paramCount == 0) {
			return null;
		} else {
			List<Object> list = new ArrayList<Object>();
			if (batchItemIndex == -1) {// 没有@BatchParam注解
				Object param = getNamedParams(args);
				for (int i = 0; i < batchCount; i++) {
					list.add(param);
				}
			} else {
				Object batchArg = this.resolveBatchParam(args[batchItemIndex], batchParam);
				if (null == batchArg || !isBatchParamType(batchArg.getClass())) {
					Throw.throwException(DaoExceptionCodes.DYSD020013, method);
				}
				List<Object> batchArgs = Tool.CONVERT.convertToList(batchArg, Object.class);
				if (batchCount != 1 && (null == batchArgs || batchArgs.size() != batchCount)) {
					Throw.throwException(DaoExceptionCodes.DYSD020015, batchCount,
							null == batchArgs ? 0 : batchArgs.size());
				}

				Object commParam = getNamedParams(args);
				String batchParamName = this.batchParam.item();
				String batchIndexName = this.batchParam.index();
				for (int index = 0, l = batchArgs.size(); index < l; index++) {
					Object arg = batchArgs.get(index);
					addOneBatchParam(list, commParam, batchParamName, batchIndexName, index, arg);
				}
			}
			return list;
		}
	}

	@Override
	public void resolveExecuteNamedParams(Object[] args, List<String> sqlIds, List<Object> params) {
		Object param = getNamedParams(args);
		int i = 0;
		for (Execute execute : executes.value()) {
			String condition = execute.condition();
			if (Tool.CHECK.isBlank(condition)) {
				convertArgsToBatchExecute(param, sqlIds, params, execute);
			} else {
				Object c = OgnlCache.getValue(condition, param);
				if (c instanceof Boolean && ((Boolean) c).booleanValue()) {
					convertArgsToBatchExecute(param, sqlIds, params, execute);
				} else {
					CommonLogger.info("the execute condition is not match, so ignore the execute [index : " + i
							+ ", method : " + method + "]");
				}
			}
			i++;
		}
	}

	@SuppressWarnings("unchecked")
	private void addOneBatchParam(List<Object> list, Object commParam, String batchParamName, String batchIndexName,
			int index, Object arg) {
		final Map<String, Object> param = new ParamMap<Object>();
		if (commParam instanceof ParamMap) {
			param.putAll((ParamMap<Object>) commParam);
		} else {
			param.put("param1", commParam);
		}
		if (!Tool.CHECK.isBlank(batchIndexName)) {
			param.put(batchIndexName, index);
		}
		if (!Tool.CHECK.isBlank(batchParamName)) {
			param.put(batchParamName, arg);
		}
		list.add(param);
	}

	private void convertArgsToBatchExecute(Object context, List<String> sqlIds, List<Object> params, Execute execute) {
		BatchParam batchParam = execute.param();
		String sqlId = DaoUtils.resolveSqlId(execute.sqlRef(), method);
		Object batchArg = this.resolveBatchParam(context, batchParam);
		if (null == batchArg) {
			CommonLogger.warn("the batch params is null, so ignore the sqlId [" + sqlId + "]");
			return;
		} else if (batchParam.value()) {
			if (!isBatchParamType(batchArg.getClass())) {
				Throw.throwException(DaoExceptionCodes.DYSD020013, method);
			}
			List<Object> batchArgs = Tool.CONVERT.convertToList(batchArg, Object.class);
			String batchParamName = batchParam.item();
			String batchIndexName = batchParam.index();
			for (int index = 0, l = batchArgs.size(); index < l; index++) {
				Object arg = batchArgs.get(index);
				addOneBatchParam(params, context, batchParamName, batchIndexName, index, arg);
				sqlIds.add(sqlId);
			}
		} else {
			sqlIds.add(sqlId);
			params.add(batchArg);
		}
	}

	private Object resolveBatchParam(Object param, BatchParam batchParam) {
		String property = batchParam.property();
		if (!"this".equals(property) && !Tool.CHECK.isBlank(property)) {
			Object rs = OgnlCache.getValue(property, param);// .getProperty(param,
															// property);
			return rs;
		}
		return param;
	}

	private BatchParam getBatchParamAnnoation(Object[] paramAnno) {
		for (Object aParamAnno : paramAnno) {
			if (aParamAnno instanceof BatchParam) {
				return (BatchParam) aParamAnno;
			}
		}
		return null;
	}

	private boolean isBatchParamType(Class<?> cls) {
		return cls.isArray() || Iterator.class.isAssignableFrom(cls) || Enumeration.class.isAssignableFrom(cls)
				|| Iterable.class.isAssignableFrom(cls); // 因此包含Collection，从而也包含List、Set、Queue等常见集合类型
	}
}

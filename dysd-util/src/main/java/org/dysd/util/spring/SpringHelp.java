package org.dysd.util.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dysd.util.Tool;
import org.dysd.util.exception.ExceptionCodes;
import org.dysd.util.exception.Throw;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cache.Cache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanExpressionContextAccessor;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.EnvironmentAccessor;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeConverter;
import org.springframework.expression.spel.support.StandardTypeLocator;
import org.springframework.stereotype.Component;

/**
 * Spring帮助类
 * @author linjisong
 * @version 0.0.1
 * @since 0.0.1
 * @date 2016-11-15
 */
@Component
public class SpringHelp implements ApplicationContextAware{
	
	/**
	 * Spring容器
	 */
	private static ApplicationContext applicationContext;
	
	/**
	 * PropertyPlaceholderConfigurer组件加载的属性
	 */
	private static Properties placeholderPropertis;
	
	/**
	 * 实现ApplicationContextAware接口
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
	{
		SpringHelp.applicationContext = applicationContext;
	}
	
	/**
	 * Spring容器是否已经初始化
	 * @return
	 */
	public static boolean hasInit(){
		return null != applicationContext;
	}
	
	/**
	 * 获取Spring容器
	 * @return
	 */
	public static ApplicationContext getApplicationContext(){
		if(null == applicationContext){
			Throw.throwException(ExceptionCodes.DYSD010010);
		}
		return applicationContext;
	}
	
	/**
	 * 根据name获取容器中配置的Bean
	 * @param name   bean名称
	 * @return bean实例
	 */
	public static Object getBean(String name){
		try{
			return getApplicationContext().getBean(name);
		}catch(BeansException e){
			throw Throw.createException(ExceptionCodes.DYSD010011, name);
		}
	}
	
	/**
	 * 判断是否包括ID为name的bean
	 * @param name  bean名称
	 * @return 是否包含bean
	 */
	public static boolean containsBean(String name){
		return getApplicationContext().containsBean(name);
	}
	
	/**
	 * 根据类型获取容器中配置的Bean
	 * @param cls bean类型
	 * @return bean实例
	 */
	public static <E>E getBean(Class<E> cls){
		try{
			return getApplicationContext().getBean(cls);	
		}catch(BeansException e){
			throw Throw.createException(ExceptionCodes.DYSD010011, cls);
		}
	}
	
	/**
	 * 根据name和类型获取容器中配置的Bean
	 * @param name  bean名称
	 * @param cls   bean类型
	 * @return bean实例
	 */
	public static <E>E getBean(String name, Class<E> cls){
		try{
			return getApplicationContext().getBean(name, cls);	
		}catch(BeansException e){
			throw Throw.createException(ExceptionCodes.DYSD010011, "name="+name+";class="+cls);
		}
	}
	
	/**
	 * 获取同一类型的所有bean实例
	 * @param cls bean类型
	 * @return 所有cls类型的beanId及其实例对象组成的Map
	 */
	public static <E> Map<String, E> getBeansOfType(Class<E> cls){
		try{
			return getApplicationContext().getBeansOfType(cls);	
		}catch(BeansException e){
			throw Throw.createException(ExceptionCodes.DYSD010011, cls);
		}
	}
	
	/**
	 * 获取同一类型的所有bean实例
	 * @param cls bean类型
	 * @return 所有cls类型的bean实例对象组成的List
	 */
	public static <E> List<E> getBeanslistOfType(Class<E> cls){
		Map<String, E> map = getBeansOfType(cls);
		if(null != map && !map.isEmpty()){
			List<E> list = new ArrayList<E>(map.size());
			for(E bean : map.values()){
				list.add(bean);
			}
			return list;
		}
		return null;
	}
	
	/**
	 * 获取Spel表达式的值
	 * @param expression  SpEL表达式
	 * @return 表达式值
	 */
	public static Object evaluate(String expression){
		return evaluate(null, expression, (Map<String,Object>)null);
	}
	
	/**
	 * 获取Spel表达式的值
	 * @param param       参数对象
	 * @param expression  SpEL表达式
	 * @return 表达式值
	 */
	public static Object evaluate(Object param, String expression){
		return evaluate(param, expression, (Map<String,Object>)null);
	}
	
	/**
	 * 获取Spel表达式的值
	 * @param param      参数对象
	 * @param expression SpEL表达式
	 * @param vars       变量
	 * @return 表达式值
	 */
	public static Object evaluate(Object param, String expression, Map<String, Object> vars){
		return SpelHelp.evaluate(param, expression, vars);
	}
	
	/**
	 * 获取Spel表达式的值
	 * @param expression  SpEL表达式
	 * @param type        期望返回的结果类型
	 * @return 表达式值
	 */
	public static <T>T evaluate(String expression, Class<T> type){
		return evaluate(null, expression, null, type);
	}
	
	/**
	 * 获取Spel表达式的值
	 * @param param       参数对象
	 * @param expression  SpEL表达式
	 * @param type        期望返回的结果类型
	 * @return 表达式值
	 */
	public static <T>T evaluate(Object param, String expression, Class<T> type){
		return evaluate(param, expression, null, type);
	}
	
	/**
	 * 获取Spel表达式的值
	 * @param param       参数对象
	 * @param expression  SpEL表达式
	 * @param vars        变量
	 * @param type        期望返回的结果类型
	 * @return 表达式值
	 */
	public static <T>T evaluate(Object param, String expression, Map<String, Object> vars, Class<T> type){
		return SpelHelp.evaluate(param, expression, vars, type);
	}
	
	/**
	 * 设置SpEL表达式值
	 * @param expression SpEL表达式
	 * @param value      值
	 */
	public static void setValue(String expression, Object value){
		setValue(null, expression, null, value);
	}
	
	/**
	 * 设置SpEL表达式值
	 * @param param      参数对象
	 * @param expression SpEL表达式
	 * @param value      值
	 */
	public static void setValue(Object param, String expression, Object value){
		setValue(param, expression, null, value);
	}
	
	/**
	 * 设置SpEL表达式值
	 * @param param      参数对象
	 * @param expression SpEL表达式
	 * @param vars       变量
	 * @param value      值
	 */
	public static void setValue(Object param, String expression, Map<String, Object> vars, Object value){
		SpelHelp.setValue(param, expression, vars, value);
	}

	/**
	 * 获取PropertyPlaceholderConfigurer组件加载的属性 
	 * @param key 属性KEY值
	 * @return 属性值
	 */
	public static String getPlaceholderProperty(String key) {
		return placeholderPropertis == null ? null : placeholderPropertis.getProperty(key);
	}

	/**
	 * 设置PropertyPlaceholderConfigurer组件加载的属性
	 * @param placeholderPropertis
	 */
	/*package*/ static void setPlaceholderPropertis(Properties placeholderPropertis) {
		SpringHelp.placeholderPropertis = placeholderPropertis;
	}
	
	/**
	 * 添加SpEL表达式执行时的变量
	 * @param name
	 * @param variable
	 */
	public static void addSpelVariable(String name, Object variable){
		checkSpelVariable(name);
		SpelHelp.customVariables.put(name, variable);
	}
	
	/**
	 * 移除SpEL表达式执行时的变量
	 * @param name
	 */
	public static void removeSpelVariable(String name){
		checkSpelVariable(name);
		SpelHelp.customVariables.remove(name);
	}
	
	private static void checkSpelVariable(String name){
		if(SpelHelp.protectedVariableNames.contains(name)){
			Throw.throwException(ExceptionCodes.DYSD010012, name);
		}
	}

	/**
	 * Spel表达式内部帮助类
	 */
	private static class SpelHelp{
		private static final ThreadLocal<StandardEvaluationContext> context = new ThreadLocal<StandardEvaluationContext>();
		private static final ThreadLocal<Map<String, Object>> variables = new ThreadLocal<Map<String, Object>>();
		private static final ExpressionParser expressionParser = new SpelExpressionParser();
		private static final Map<String, Object> customVariables = new HashMap<String, Object>();
		private static final Cache expressionCache = Tool.CACHE.getCache(SpelHelp.class);
		private static final List<String> protectedVariableNames = Arrays.asList("Tool");
		static{
			customVariables.put("Tool", new Tool());
		}
		
		private static void initialStandardEvaluationContext(StandardEvaluationContext evaluationContext) throws BeansException {
			if(applicationContext.getAutowireCapableBeanFactory() instanceof ConfigurableBeanFactory){
				ConfigurableBeanFactory factory = (ConfigurableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
				evaluationContext.addPropertyAccessor(new BeanExpressionContextAccessor());
				evaluationContext.addPropertyAccessor(new BeanFactoryAccessor());
				evaluationContext.addPropertyAccessor(new MapAccessor(){
					public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
						return true;
					}
					public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
						@SuppressWarnings("rawtypes")
						Map map = (Map) target;
						Object value = map.get(name);
						if (value == null) {
							return TypedValue.NULL;
						}
						return new TypedValue(value);
					}
				});
				evaluationContext.addPropertyAccessor(new EnvironmentAccessor());
				evaluationContext.setBeanResolver(new BeanFactoryResolver(factory));
				evaluationContext.setTypeLocator(new StandardTypeLocator(factory.getBeanClassLoader()));
				ConversionService conversionService = factory.getConversionService();
				if (conversionService != null) {
					evaluationContext.setTypeConverter(new StandardTypeConverter(conversionService));
				}
			}
		}
		
		private static Object evaluate(Object param, String expression, Map<String, Object> vars){
			try{
				Expression expr = getExpression(expression);
				EvaluationContext evaluationContext = getEvaluationContext(vars);
				if(null == param){
					return expr.getValue(evaluationContext);
				}else{
					return expr.getValue(evaluationContext, param);
				}
			}finally{
				resetContext();
			}
		}
		private static <T>T evaluate(Object param, String expression, Map<String, Object> vars, Class<T> type){
			try{
				Expression expr = getExpression(expression);
				EvaluationContext evaluationContext = getEvaluationContext(vars);
				if(null == param){
					return expr.getValue(evaluationContext, type);
				}else{
					return expr.getValue(evaluationContext, param, type);
				}
			}finally{
				resetContext();
			}
		}
		private static void setValue(Object param, String expression, Map<String, Object> vars, Object value){
			try{
				Expression expr = getExpression(expression);
				EvaluationContext evaluationContext = getEvaluationContext(vars);
				if(null == param){
					expr.setValue(evaluationContext, value);
				}else{
					expr.setValue(evaluationContext, param, value);
				}
			}finally{
				resetContext();
			}
		}
		private static void resetContext(){
			Map<String, Object> varis = variables.get();
			if(null != varis){
				varis.clear();
			}
		}
		
		private static EvaluationContext getEvaluationContext(Map<String, Object> vars){
			StandardEvaluationContext evaluationContext = context.get();
			if(null == evaluationContext){
				synchronized(SpelHelp.class){
					evaluationContext = context.get();
					if(null == evaluationContext){
						evaluationContext = new StandardEvaluationContext();
						initialStandardEvaluationContext(evaluationContext);
						@SuppressWarnings("unchecked")
						Map<String, Object> varis = (Map<String, Object>)Tool.REFLECT.readField(evaluationContext, "variables");
						context.set(evaluationContext);
						variables.set(varis);
					}
				}
			}
			evaluationContext.setVariables(customVariables);
			if(null != vars){
				evaluationContext.setVariables(vars);
			}
			return evaluationContext;
		}
		
		private static Expression getExpression(String expression){
			Expression expr = expressionCache.get(expression, Expression.class);
			if (expr == null) {
				synchronized(SpelHelp.class){
					expr = expressionCache.get(expression, Expression.class);
					if(expr == null){
						expr = expressionParser.parseExpression(expression);
						expressionCache.put(expression, expr);	
					}
				}
			}
			return expr;
		}
	}
}
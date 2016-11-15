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
package org.dysd.util.exception;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.dysd.util.Tool;
import org.dysd.util.config.BaseConfig;
import org.dysd.util.env.EnvConsts;
import org.dysd.util.exception.handler.IExceptionHandler;
import org.dysd.util.exception.level.ExceptionLevel;
import org.dysd.util.exception.meta.IExceptionMeta;
import org.dysd.util.exception.meta.IExceptionMetaLoader;
import org.dysd.util.sync.impl.SingleonRunStatus;
import org.dysd.util.track.Tracker;

/**
 * 异常工具类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class Throw {
	
	/**
	 * 抛出平台运行时异常
	 * @param code 异常代码
	 * @param args 国际化信息中的占位符参数
	 * @throws DysdException 平台受检异常
	 */
	public static void throwException(String code, Object... args)
	{
		throw createException(code, null, args);
	}

	/**
	 * 抛出平台运行时异常
	 * @param e 原始异常
	 * @throws DysdException 平台受检异常
	 */
	public static void throwException(Throwable e)
	{
		throw createException(null, e);
	}
	
	/**
	 * 抛出平台运行时异常
	 * @param code 异常代码
	 * @param e    原始异常
	 * @param args 国际化信息中的占位符参数
	 * @throws DysdException 平台受检异常
	 */
	public static void throwException(String code, Throwable e, Object... args){
		throw createException(code, e, args);
	}
	
	/**
	 * 创建平台运行时异常
	 * @param code  异常代码
	 * @param args  国际化信息中的占位符参数
	 * @return 平台运行时异常，只返回，不抛出
	 */
	public static DysdException createException(String code, Object... args)
	{
		return createException(code, null, args);
	}

	/**
	 * 创建平台运行时异常
	 * @param e  原始异常
	 * @return 平台运行时异常，只返回，不抛出
	 */
	public static DysdException createException(Throwable e)
	{
		return createException(null, e);
	}
	
	/**
	 * 创建DYSD运行时异常
	 * <p>
	 * 	<ul>
	 * 		<li>如果传入异常或传入异常的cause为{@link DysdException}，就直接返回传入异常或传入异常的cause
	 *      <li>如果传入异常为{@link InvocationTargetException}，将传入异常设置为getTargetException()，再包装为DYSD运行时异常返回
	 *      <li>其它情况，将传入异常包装为DYSD运行时异常返回
	 *  </ul>
	 * </p>
	 * @param code  异常代码
	 * @param e     原始异常
	 * @param args  国际化信息中的占位符参数
	 * @return DYSD运行时异常，只返回，不抛出
	 */
	public static DysdException createException(String code, Throwable e, Object... args){
		if(e instanceof DysdException){
			return (DysdException)e;
		}else{
			Throwable cause = getRootCause(e);
			if(cause instanceof DysdException){
				return (DysdException)cause;
			}else if(e instanceof InvocationTargetException){
				return createException(code,((InvocationTargetException)e).getTargetException(), args);
			}else{
				DysdExceptionInnerProxy proxy = new DysdExceptionInnerProxy(code, e, args);
				return new DysdException(proxy);	
			}
		}
	}
	
	/**
	 * 是否为未配置的异常
	 * @param t
	 * @return
	 */
	public static boolean isUnconfigException(Throwable t){
		DysdException exception = createException(t);
		return ExceptionCodes.DYSD_UNCONFIG_EXCEPTION.equals(exception.getCode());
	}
	
	/**
	 * 判断异常是否为指定代码或其子孙代码表示的异常
	 * @param e
	 * @param exceptionCode
	 * @return
	 */
	public static boolean isMatch(Throwable e, String exceptionCode){
		if(null == e || Tool.CHECK.isBlank(exceptionCode)){
			return false;
		}
		IExceptionMeta meta = lookupExceptionMeta(exceptionCode, null);
		if(null == meta){
			return false;
		}
		DysdException exception = createException(e);
		String code = exception.getCode();
		String pCode = exception.getParentCode();
		do{
			if(exceptionCode.equals(code)){
				return true;
			}else if(Tool.CHECK.isBlank(pCode)){
				return false;
			}else{
				meta = lookupExceptionMeta(pCode, null);
				if(null == meta){
					return false;
				}else{
					code = meta.getCode();
					pCode = meta.getParentCode();
				}
			}
		}while(!Tool.CHECK.isBlank(code));
		return false;
	}
	
	/**
	 * 获取简单的异常信息描述，使用系统默认的换行符
	 * @param e  异常
	 * @return  异常描述
	 */
	public static String getShortMessage(Throwable e){
		return getMessage0(e, EnvConsts.LINE_SEPARATOR, 1);
	}
	
	/**
	 * 获取异常信息描述，使用系统默认的换行符
	 * @param e  异常
	 * @return  异常描述
	 */
	public static String getMessage(Throwable e){
		return getMessage0(e, EnvConsts.LINE_SEPARATOR, 2);
	}
	
	/**
	 * 获取异常信息描述
	 * @param e              异常
	 * @param lineSeparator  换行符
	 * @return  异常描述
	 */
	public static String getMessage(Throwable e, String lineSeparator){
		return getMessage0(e, lineSeparator, 2);
	}
	
	/**
	 * 获取异常信息详细描述，使用系统默认的换行符
	 * @param e 异常
	 * @return  异常详细描述
	 */
	public static String getStackMessage(Throwable e){
		return getMessage0(e, EnvConsts.LINE_SEPARATOR, 3);
	}
	
	/**
	 * 获取异常信息详细描述，使用系统默认的换行符
	 * @param e              异常
	 * @param lineSeparator  换行符
	 * @return 异常详细描述
	 */
	public static String getStackMessage(Throwable e, String lineSeparator){
		return getMessage0(e, lineSeparator, 3);
	}
	
	private static String getMessage0(Throwable e, String lineSeparator, int level){
		DysdExceptionInnerProxy proxy = createException(e).getProxy();
		String message = proxy.getMessage();
		if(1 == level){
			return message;
		}else{
			StringBuffer sb = new StringBuffer();
			if(null != proxy.getTrackId()){
				sb.append("TrackId:"+proxy.getTrackId()).append(lineSeparator);
			}
			String code = proxy.getCode();
			if(null != code && !code.equals(message)){
				sb.append("Code:"+code).append(lineSeparator);
			}
			if(null != message){
				sb.append("Message:"+message).append(lineSeparator);
			}
			
			if(3 == level){
				String pCode = proxy.getParentCode();
				if(null != pCode && !pCode.startsWith("##")){
					sb.append("ParentCode:"+pCode).append(lineSeparator);
				}
				if(null != proxy.getLevel()){
					sb.append(proxy.getLevel().getDescription()).append(lineSeparator);
				}
			}
			
			Throwable cause = getRootCause(e);
			if(null != cause && !cause.equals(e)){
				String c = cause.getMessage();
				if(!Tool.CHECK.isBlank(c)){
					sb.append("Cause:"+c).append(lineSeparator);
				}
			}
			
			if(3 == level){
				StackTraceElement[] trace = e.getStackTrace();
				for (int i = 0; i < trace.length; i++)
				{
					sb.append("\tat ").append(trace[i]).append(lineSeparator);
				}
				Throwable t = e.getCause();
				if(null != t){
					setTrace(t, sb, trace, lineSeparator);
				}
			}
			return sb.toString();
		}
	}
	
	/**
	 * 获取导致异常的最起初的原因
	 * @param e
	 * @return
	 */
	private static Throwable getRootCause(Throwable e) {
		if(null == e){
			return null;
		}
		Throwable rootCause = null;
		Throwable cause = e.getCause();
		while (cause != null && cause != rootCause) {
			rootCause = cause;
			if(e instanceof InvocationTargetException){
				cause = ((InvocationTargetException)cause).getTargetException();
			}else{
				cause = cause.getCause();
			}
		}
		return rootCause;
	}
	
	/**
	 * 嵌套打印异常堆栈信息
	 * @param ourCause
	 * @param info
	 * @param causedTrace
	 * @param lineSeparator
	 */
	private static void setTrace(Throwable ourCause, StringBuffer info, StackTraceElement[] causedTrace,String lineSeparator)
	{
		StackTraceElement[] trace = ourCause.getStackTrace();
		int m = trace.length - 1, n = causedTrace.length - 1;
		while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n]))
		{
			m--;
			n--;
		}
		int framesInCommon = trace.length - 1 - m;

		info.append("Caused by: ").append(ourCause).append(lineSeparator);
		for (int i = 0; i <= m; i++)
		{
			info.append("\tat ").append(trace[i]).append(lineSeparator);
		}

		if (framesInCommon != 0)
		{
			info.append("\t... ").append(framesInCommon).append(" more").append(lineSeparator);
		}

		// Recurse if we have a cause
		Throwable cause = ourCause.getCause();
		if (cause != null)
		{
			setTrace(cause, info, trace, lineSeparator);
		}
	}
	
	/**
	 * 查找异常元信息
	 * @param code
	 * @param cause
	 * @return
	 */
	public static IExceptionMeta lookupExceptionMeta(String code, Throwable cause){
		IExceptionMetaLoader loader = BaseConfig.getExceptionMetaLoader();
		return null == loader ? null : loader.lookup(code, cause);
	}
	
	/*package*/static class DysdExceptionInnerProxy {
		private String trackId;
		private String parentCode;
		private String code;
		private String message;
		private String view;
		private ExceptionLevel level;
		private Set<IExceptionHandler> handlers;
		private Throwable cause;
		private SingleonRunStatus load;
		private Object[] args;
		
		DysdExceptionInnerProxy(String code, Throwable cause, Object... args){
			this.trackId = Tracker.getTrackId();
			this.code = code;
			this.cause = cause;
			this.args = args;
			this.load = SingleonRunStatus.newInstance("exception resolver "+ code);
		}
		
		private boolean doMessageResolver(IExceptionMeta meta, Object... args){
			String localeMessageKey = meta.getLocaleKey();
			if(!Tool.CHECK.isBlank(localeMessageKey)){//元信息中存在国际化key值
				this.message = Tool.I18N.getMessage(localeMessageKey, args);
				if(Tool.CHECK.isBlank(this.message)){//但是国际化配置文件中不存在对应的key配置
					throw new IllegalArgumentException("not found the message key["+localeMessageKey+"] in exception property files, exception code is "+this.code+".");
				}
				return true;
			}
			return false;
		}
		
		private boolean doViewResolver(IExceptionMeta meta){
			if(!Tool.CHECK.isBlank(meta.getView())){
				this.view = meta.getView();
				return true;
			}
			return false;
		}
		
		private boolean doLevelResolver(IExceptionMeta meta){
			if(null != meta.getLevel()){
				this.level = meta.getLevel();
				return true;
			}
			return false;
		}
		
		private boolean doHandlersResolver(IExceptionMeta meta){
			if(null != meta.getHandlers() && !meta.getHandlers().isEmpty()){
				this.handlers = meta.getHandlers();
				return true;
			}
			return false;
		}
		
		private void doResolver(){
			if(load.start()){
				try{
					boolean messageStatus = false, viewStatus = false, levelStatus = false, handlerStatus = false;
					IExceptionMeta meta = lookupExceptionMeta(code, cause);
					if(null == meta){//未找到元信息
						this.parentCode = null;
						this.code = Tool.CHECK.isBlank(code)? ExceptionCodes.DYSD_UNCONFIG_EXCEPTION : code;
					}else{
						this.parentCode = meta.getParentCode();
						this.code = meta.getCode();
						boolean status = false;
						while(!status){
							messageStatus = messageStatus || this.doMessageResolver(meta, args);
							viewStatus = viewStatus || this.doViewResolver(meta);
							levelStatus = levelStatus || this.doLevelResolver(meta);
							handlerStatus = handlerStatus || this.doHandlersResolver(meta);
							String type = meta.getParentCode();
							if(!Tool.CHECK.isBlank(type)){
								meta = lookupExceptionMeta(type, null);
							}else{
								meta = null;
							}
							status = (meta == null) || (messageStatus && viewStatus && levelStatus && handlerStatus);
						}
					}
					if(!messageStatus){
						this.message = Tool.I18N.getMessage(this.code, args);
						if(Tool.CHECK.isBlank(this.message)){
							this.message = this.code;
						}	
					}
//					if(!viewStatus){
//						this.view = BaseConfig.getExceptionView();
//					}
					if(!levelStatus){
						this.level = ExceptionLevel.UNKNOWN;
					}
					if(!handlerStatus){
						this.handlers = BaseConfig.getExceptionHandlers();
					}
					load.success();
				}catch(Exception e){
					load.failure();
				}
			}
		}
		
		String getTrackId(){
			return trackId;
		}
		String getParentCode() {
			doResolver();
			return parentCode;
		}
		String getCode() {
			doResolver();
			return code;
		}
		String getMessage() {
			doResolver();
			return message;
		}
		String getView() {
			doResolver();
			return view;
		}
		ExceptionLevel getLevel() {
			doResolver();
			return level;
		}
		Set<IExceptionHandler> getHandlers() {
			doResolver();
			return handlers;
		}
		Throwable getCause() {
			return cause;
		}
	}
}

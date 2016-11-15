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
package org.dysd.util.logger;


import java.util.Set;

import org.dysd.util.Tool;
import org.dysd.util.config.BaseConfig;
import org.dysd.util.logger.level.LogLevel;
import org.dysd.util.logger.stack.IStack;
import org.dysd.util.logger.stack.IStackFactory;
import org.dysd.util.logger.termination.ILogTermination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * 日志工具类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class CommonLogger {
	
	private static final Cache loggerCache = Tool.CACHE.getCache(CommonLogger.class);
	
	/**
	 * 获取日志
	 * @param name
	 * @return
	 */
	public static Logger getLogger(String name){
		Logger logger = loggerCache.get(name, Logger.class);
		if(null == logger){
			synchronized(loggerCache){
				logger = loggerCache.get(name, Logger.class);
				if(null == logger){
					logger = LoggerFactory.getLogger(name);
					loggerCache.put(name, logger);
				}
			}
		}
		return logger;
	}
	
	/**
	 * 是否已开启TRACE模式
	 * @return 是否已开启
	 */
	public static boolean isTraceEnabled(){
		StackTraceElement stack = getStack();
		return isEnabled(stack, LogLevel.TRACE);
	}
	
	/**
	 * 是否已开启DEBUG模式
	 * @return 是否已开启
	 */
	public static boolean isDebugEnabled(){
		StackTraceElement stack = getStack();
		return isEnabled(stack, LogLevel.DEBUG);
	}
	
	/**
	 * 是否已开启INFO模式
	 * @return 是否已开启
	 */
	public static boolean isInfoEnabled(){
		StackTraceElement stack = getStack();
		return isEnabled(stack, LogLevel.INFO);
	}
	
	/**
	 * 是否已开启WARN模式
	 * @return 是否已开启
	 */
	public static boolean isWarnEnabled(){
		StackTraceElement stack = getStack();
		return isEnabled(stack, LogLevel.WARN);
	}
	
	/**
	 * 是否已开启ERROR模式
	 * @return 是否已开启
	 */
	public static boolean isErrorEnabled(){
		StackTraceElement stack = getStack();
		return isEnabled(stack, LogLevel.ERROR);
	}
	
	/**
	 * 打印TRACE级别日志
	 * @param msg  日志内容
	 * @param args 消息占位符参数
	 */
	public static void trace(String msg) {
		StackTraceElement stack = getStack();
		write(null, LogLevel.TRACE, stack, msg, null);
	}
	
	/**
	 * 打印TRACE级别日志
	 * @param e  异常
	 */
	public static void trace(Throwable e) {
		StackTraceElement stack = getStack();
		write(null, LogLevel.TRACE, stack, null, e);
	}

	/**
	 * 打印TRACE级别日志
	 * @param msg  日志内容
	 * @param e    异常
	 * @param args 消息占位符参数
	 */
	public static void trace(String msg, Throwable e) {
		StackTraceElement stack = getStack();
		write(null, LogLevel.TRACE, stack, msg, e);
	}
	
	/**
	 * 向指定终端打印TRACE级别日志
	 * @param msg          日志内容
	 * @param e            异常
	 * @param termination  日志终端
	 * @param args         消息占位符参数
	 */
	public static void trace(String msg, Throwable e, ILogTermination termination) {
		StackTraceElement stack = getStack();
		IStack ss = getStack(null, stack, msg, e);
		termination.write(LogLevel.TRACE, ss);
	}
	
	/**
	 * 使用指定日志类打印TRACE级别日志
	 * @param msg    日志内容
	 * @param e      异常
	 * @param logger 日志类
	 * @param args   消息占位符参数
	 */
	public static void trace(String msg, Throwable e, Logger logger) {
		StackTraceElement stack = getStack();
		write(logger, LogLevel.TRACE, stack, msg, e);
	}
	
	/**
	 * 打印DEBUG级别日志
	 * @param msg  日志内容
	 * @param args 消息占位符参数
	 */
	public static void debug(String msg) {
		StackTraceElement stack = getStack();
		write(null, LogLevel.DEBUG, stack, msg, null);
	}
	
	/**
	 * 打印DEBUG级别日志
	 * @param e  异常
	 */
	public static void debug(Throwable e) {
		StackTraceElement stack = getStack();
		write(null, LogLevel.DEBUG, stack, null, e);
	}

	/**
	 * 打印DEBUG级别日志
	 * @param msg  日志内容
	 * @param e    异常
	 * @param args 消息占位符参数
	 */
	public static void debug(String msg, Throwable e) {
		StackTraceElement stack = getStack();
		write(null, LogLevel.DEBUG, stack, msg, e);
	}
	
	/**
	 * 向指定终端打印DEBUG级别日志
	 * @param msg          日志内容
	 * @param e            异常
	 * @param termination  日志终端
	 * @param args         消息占位符参数
	 */
	public static void debug(String msg, Throwable e, ILogTermination termination) {
		StackTraceElement stack = getStack();
		IStack ss = getStack(null, stack, msg, e);
		termination.write(LogLevel.DEBUG, ss);
	}
	
	/**
	 * 使用指定日志类打印DEBUG级别日志
	 * @param msg    日志内容
	 * @param e      异常
	 * @param logger 日志类
	 * @param args   消息占位符参数
	 */
	public static void debug(String msg, Throwable e, Logger logger) {
		StackTraceElement stack = getStack();
		write(logger, LogLevel.DEBUG, stack, msg, e);
	}
	
	/**
	 * 打印INFO级别日志
	 * @param msg  日志内容
	 * @param args 消息占位符参数
	 */
	public static void info(String msg) {
		StackTraceElement stack = getStack();
		write(null, LogLevel.INFO, stack, msg, null);
	}
	
	/**
	 * 打印INFO级别日志
	 * @param e 异常
	 */
	public static void info(Throwable e) {
		StackTraceElement stack = getStack();
		write(null, LogLevel.INFO, stack, null, e);
	}
	
	/**
	 * 打印INFO级别日志
	 * @param msg  日志内容
	 * @param e    异常
	 * @param args 消息占位符参数
	 */
	public static void info(String msg, Throwable e) {
		StackTraceElement stack = getStack();
		write(null, LogLevel.INFO, stack, msg, e);
	}
	
	/**
	 * 向指定终端打印INFO级别日志
	 * @param msg          日志内容
	 * @param e            异常
	 * @param termination  日志终端
	 * @param args         消息占位符参数
	 */
	public static void info(String msg, Throwable e, ILogTermination termination) {
		StackTraceElement stack = getStack();
		IStack ss = getStack(null, stack, msg, e);
		termination.write(LogLevel.INFO, ss);
	}
	
	/**
	 * 使用指定日志类打印INFO级别日志
	 * @param msg    日志内容
	 * @param e      异常
	 * @param logger 日志类
	 * @param args   消息占位符参数
	 */
	public static void info(String msg, Throwable e, Logger logger) {
		StackTraceElement stack = getStack();
		write(logger, LogLevel.INFO, stack, msg, e);
	}
	
	/**
	 * 打印WARN级别日志
	 * @param msg  日志内容
	 * @param args 消息占位符参数
	 */
	public static void warn(String msg) {
		StackTraceElement stack = getStack();
		write(null, LogLevel.WARN, stack, msg, null);
	}
	
	/**
	 * 打印WARN级别日志
	 * @param e  异常
	 */
	public static void warn(Throwable e) {
		StackTraceElement stack = getStack();
		write(null, LogLevel.WARN, stack, null, e);
	}

	/**
	 * 打印WARN级别日志
	 * @param msg  日志内容
	 * @param e    异常
	 * @param args 消息占位符参数
	 */
	public static void warn(String msg, Throwable e) {
		StackTraceElement stack = getStack();
		write(null, LogLevel.WARN, stack, msg, e);
	}
	
	/**
	 * 向指定终端打印WARN级别日志
	 * @param msg          日志内容
	 * @param e            异常
	 * @param termination  日志终端
	 * @param args         消息占位符参数
	 */
	public static void warn(String msg, Throwable e, ILogTermination termination) {
		StackTraceElement stack = getStack();
		IStack ss = getStack(null, stack, msg, e);
		termination.write(LogLevel.WARN, ss);
	}
	
	/**
	 * 使用指定日志类打印WARN级别日志
	 * @param msg    日志内容
	 * @param e      异常
	 * @param logger 日志类
	 */
	public static void warn(String msg, Throwable e, Logger logger) {
		StackTraceElement stack = getStack();
		write(logger, LogLevel.WARN, stack, msg, e);
	}
	
	/**
	 * 打印ERROR级别日志
	 * @param msg  日志内容
	 */
	public static void error(String msg) {
		StackTraceElement stack = getStack();
		write(null, LogLevel.ERROR, stack, msg, null);
	}
	
	/**
	 * 打印ERROR级别日志
	 * @param e  异常
	 */
	public static void error(Throwable e) {
		StackTraceElement stack = getStack();
		write(null, LogLevel.ERROR, stack, null, e);
	}

	/**
	 * 打印ERROR级别日志
	 * @param msg 日志内容
	 * @param e   异常
	 */
	public static void error(String msg, Throwable e) {
		StackTraceElement stack = getStack();
		write(null, LogLevel.ERROR, stack, msg, e);
	}
	
	/**
	 * 向指定终端打印ERROR级别日志
	 * @param msg          日志内容
	 * @param e            异常
	 * @param termination  日志终端
	 */
	public static void error(String msg, Throwable e, ILogTermination termination) {
		StackTraceElement stack = getStack();
		IStack ss = getStack(null, stack, msg, e);
		termination.write(LogLevel.ERROR, ss);
	}
	
	/**
	 * 使用指定日志类打印ERROR级别日志
	 * @param msg    日志内容
	 * @param e      异常
	 * @param logger 日志类
	 */
	public static void error(String msg, Throwable e, Logger logger) {
		StackTraceElement stack = getStack();
		write(logger, LogLevel.ERROR, stack, msg, e);
	}
	
	/**
	 * 是否已开启日志级别
	 * @param stack    日志堆栈
	 * @param logLevel 日志级别
	 * @return 是否已开启
	 */
	private static boolean isEnabled(StackTraceElement stack, LogLevel logLevel){
		Logger logger = LoggerFactory.getLogger(stack.getClassName());
		boolean enabled = false;
		switch(logLevel){
		case TRACE:
			enabled = logger.isTraceEnabled();
			break;
		case DEBUG:
			enabled = logger.isDebugEnabled();
			break;
		case INFO:
			enabled = logger.isInfoEnabled();
			break;
		case WARN:
			enabled = logger.isWarnEnabled();
			break;
		case ERROR:
			enabled = logger.isErrorEnabled();
			break;
		}
		return enabled;
	}
	
	/**
	 * 写日志
	 * @param logger   日志类
	 * @param level    日志级别
	 * @param stack    日志堆栈
	 * @param message  日志内容
	 * @param e        异常
	 */
	private static void write(Logger logger, LogLevel level, StackTraceElement stack, String message, Throwable e){
		IStackFactory stackFactory = BaseConfig.getStackFactory();
		Set<ILogTermination> logTerminationList = BaseConfig.getLogTerminations();
		if(null != stackFactory && null != logTerminationList && !logTerminationList.isEmpty()){
			IStack ss = getStack(logger, stack, message, e);
			for(ILogTermination logTermination: logTerminationList){
				logTermination.write(level, ss);
			}
		}
	}
	
	/**
	 * 获取日志堆栈
	 * @param logger    日志类
	 * @param stack     堆栈
	 * @param message   日志内容
	 * @param e         异常
	 * @return
	 */
	private static IStack getStack(Logger logger, StackTraceElement stack, String message, Throwable e){
		IStackFactory stackFactory = BaseConfig.getStackFactory();
		IStack ss = stackFactory.getStack(logger, stack, message, e);
		return ss;
	}
	
	/**
	 * 获取堆栈
	 * @return
	 */
	private static StackTraceElement getStack(){
		return new Exception().getStackTrace()[2];
	}
}

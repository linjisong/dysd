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
package org.dysd.dao.exception;

/**
 * 数据访问层异常代码
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class DaoExceptionCodes {

	/**
	 * 数据访问层异常
	 */
	public static final String DYSD020000 = "DYSD020000";
	/**
	 * 未注册的输出参数
	 */
	public static final String DYSD020001 = "DYSD020001";
	/**
	 * 数据库产品名称为空
	 */
	public static final String DYSD020002 = "DYSD020002";
	/**
	 * 未配置数据库方言映射
	 */
	public static final String DYSD020003 = "DYSD020003";
	/**
	 * 没有找到数据库产品对应的方言
	 */
	public static final String DYSD020004 = "DYSD020004";
	/**
	 * 分页查询时计算总记录条数出现异常
	 */
	public static final String DYSD020005 = "DYSD020005";
	/**
	 * 处理存储过程输出参数时出现异常
	 */
	public static final String DYSD020006 = "DYSD020006";
	/**
	 * 未找到对应的数据源
	 */
	public static final String DYSD020007 = "DYSD020007";
	/**
	 * 使用OGNL解析需要设置到数据库中的参数时出现异常
	 */
	public static final String DYSD020008 = "DYSD020008";
	/**
	 * 初始化Mybatis集成数据源时出现异常
	 */
	public static final String DYSD020009 = "DYSD020009";
	/**
	 * 获取数据库元信息异常
	 */
	public static final String DYSD020010 = "DYSD020010";
	/**
	 * 获取表元信息异常
	 */
	public static final String DYSD020011 = "DYSD020011";
	/**
	 * 单次读取数据量超出范围
	 */
	public static final String DYSD020012 = "DYSD020012";
	/**
	 * 批量执行的参数不正确
	 */
	public static final String DYSD020013 = "DYSD020013";
	/**
	 * 批量注解@SqlRefs使用不正确
	 */
	public static final String DYSD020014 = "DYSD020014";
	/**
	 * 批量执行参数个数不匹配
	 */
	public static final String DYSD020015 = "DYSD020015";
	/**
	 * 没有找到sqlId
	 */
	public static final String DYSD020016 = "DYSD020016";
	/**
	 * 一次批量执行必须在同一个数据源
	 */
	public static final String DYSD020017 = "DYSD020017";
	/**
	 * 含批量注解，但返回值不是整型数组
	 */
	public static final String DYSD020018 = "DYSD020018";
	/**
	 * 未找到可以处理表达式的处理器
	 */
	public static final String DYSD020019 = "DYSD020019";
	/**
	 * SQL配置函数的参数个数不合法：只能有多少个
	 */
	public static final String DYSD020020 = "DYSD020020";
	/**
	 * SQL配置函数的参数个数不合法，至少有多少个
	 */
	public static final String DYSD020021 = "DYSD020021";
	/**
	 * SQL配置函数未找到
	 */
	public static final String DYSD020022 = "DYSD020022";
}

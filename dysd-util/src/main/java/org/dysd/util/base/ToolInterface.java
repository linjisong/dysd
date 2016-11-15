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
package org.dysd.util.base;

/**
 * 基本工具类接口
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public interface ToolInterface {
	
	/**
	 * Bean工具类
	 */
	BaseBeanUtils BEAN = BaseBeanUtils.getInstance();
	
	/**
	 * 缓存工具类
	 */
	BaseCacheUtils CACHE = BaseCacheUtils.getInstance();
	
	/**
	 * 校验工具类
	 */
	BaseCheckUtils CHECK = BaseCheckUtils.getInstance();
	
	/**
	 * 类型转换工具类
	 */
	BaseConvertUtils CONVERT = BaseConvertUtils.getInstance();

	/**
	 * 日期工具类
	 */
	BaseDateUtils DATE = BaseDateUtils.getInstance();
	
	/**
	 * IO工具类
	 */
	BaseIOUtils IO = BaseIOUtils.getInstance();
	
	/**
	 * 国际化工具类
	 */
	BaseLocaleUtils I18N = BaseLocaleUtils.getInstance();
	
	/**
	 * 反射工具类
	 */
	BaseReflectUtils REFLECT = BaseReflectUtils.getInstance();
	
	/**
	 * 字符串工具类
	 */
	BaseStringUtils STRING = BaseStringUtils.getInstance();
}

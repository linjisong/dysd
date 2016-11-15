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
package org.dysd.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 批量执行时的批量参数注解，用于动态代理批量执行
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BatchParam {
	
	/**
	 * 是否为批量
	 * @return 是否为批量
	 */
	boolean value() default true;

	/**
	 * 批量参数中每一项存入map结构时的名称
	 * @return 数据项名称
	 */
	String item() default "item";
	
	/**
	 * 表示批量参数的属性
	 * @return 批量参数属性
	 */
	String property() default "this";
	
	/**
	 * 当前索引存入map结构时的名称
	 * @return 索引名称
	 */
	String index() default "index";
}

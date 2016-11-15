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
 * 批量执行时的批量SqlId注解，用于动态代理批量执行
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlRefs {

	/**
	 * 批处理的SqlId组
	 * <p>
	 * 	注意：使用该注解时，要求@SqlRef两个或两个以上，如果只有一个@SqlRef，请在方法上直接使用@SqlRef
	 * </p>
	 * @return {@link SqlRef}组，表示要执行的一组sqlId
	 */
	SqlRef[] value();
}

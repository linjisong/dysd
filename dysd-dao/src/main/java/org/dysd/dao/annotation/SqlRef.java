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
 * SQL-ID重定向注解
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SqlRef {
	/**
	 * 标识使用哪个SqlId
	 * @return sqlId值
	 */
	String value() default "";
	
	/**
	 * 指定相对于哪个类的路径，指定时，classpath选项将不起作用
	 * @return 指定前面的value()是相对于哪个类而言
	 */
	Class<?> cls() default Null.class;
	
	/**
	 * 是否为相对于当前类的路径
	 * @return 是否相对于当前类路径，当前类是指标示该注解方法所在的类
	 */
	boolean classpath() default true;
	
	/**
	 * 表示未配置类
	 */
	public class Null{};
}

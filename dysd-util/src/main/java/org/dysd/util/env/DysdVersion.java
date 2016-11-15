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
package org.dysd.util.env;

import org.dysd.util.exception.ExceptionCodes;
import org.dysd.util.exception.Throw;

/**
 * DYSD库版本号
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public enum DysdVersion {

	V_0_0_1("0.0.1", 1.7);
	
	/**
	 * 版本，指向最新版本
	 */
	public static final DysdVersion Version = V_0_0_1;
	
	/**
	 * 版本号
	 */
	private String version;
	/**
	 * 要求的最低JDK版本号
	 */
	private double jdkVersion;
	
	private DysdVersion(String version, double jdkVersion){
		this.version = version;
		this.jdkVersion = jdkVersion;
	}
	
	/**
	 * 校验JDK版本是否符合要求
	 */
	public static void checkJdkVersion(){
		if(EnvConsts.JDK_VERSION < Version.jdkVersion){
			Throw.throwException(ExceptionCodes.DYSD010001,EnvConsts.JDK_VERSION,Version.jdkVersion);
		}
	}
	
	public String toString(){
		return "dysd-"+version;
	}
}

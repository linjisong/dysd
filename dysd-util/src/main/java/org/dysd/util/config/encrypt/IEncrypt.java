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
package org.dysd.util.config.encrypt;

/**
 * 加解密接口
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public interface IEncrypt {

	/**
	 * 加密
	 * @param data  原始数据
	 * @param key   加密密钥
	 * @return 加密后数据
	 */
	String encode(String data, String key);

	/**
	 * 解密
	 * @param data  加密数据
	 * @param key   加密密钥
	 * @return 原始数据
	 */
	String decode(String data, String key);

}
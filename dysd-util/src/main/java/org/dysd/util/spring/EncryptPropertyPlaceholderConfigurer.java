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
package org.dysd.util.spring;

import java.util.Properties;
import java.util.Set;

import org.dysd.util.Tool;
import org.dysd.util.config.encrypt.IEncrypt;
import org.dysd.util.config.encrypt.impl.BaseConfigKeyEncrypt;
import org.dysd.util.logger.CommonLogger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 可以处理加密配置的Spring属性配置文件
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	
	/**
	 * 表示加密字段的附加键值
	 */
	private String encryptPropertyName = "encrypt";
	
	/**
	 * 加密算法
	 */
	private IEncrypt encryptAlgorithm = new BaseConfigKeyEncrypt();
	
	/**
	 * 属性持久化
	 */
	private IPropertiesStore propertiesStore;

	/**
	 * 处理加密数据
	 */
	@Override
	protected void convertProperties(Properties props) {
		String encryptPropertyName = getEncryptPropertyName();
		IEncrypt encryptAlgorithm = getEncryptAlgorithm();
		if(null == encryptAlgorithm || Tool.CHECK.isBlank(encryptPropertyName)){
			super.convertProperties(props);
		}else{
			encryptPropertyName = "." + encryptPropertyName;
			Set<String> propertyNames = props.stringPropertyNames();
			for (String propertyName : propertyNames) {
				String propertyValue = props.getProperty(propertyName);
				if(propertyName.endsWith(encryptPropertyName)){
					props.remove(propertyName);//将标志去掉
					if(isEncrypt(propertyValue)){
						String p = propertyName.substring(0, propertyName.lastIndexOf("."));
						if(props.containsKey(p)){
							String v = props.getProperty(p);
							String convertedValue = decode(encryptAlgorithm, propertyValue, v);
							if (null != convertedValue && !convertedValue.equals(v)) {
								props.setProperty(p, convertedValue);
							}
						}
					}
				}else{
					String convertedValue = convertProperty(propertyName, propertyValue);
					if (null != convertedValue && !convertedValue.equals(propertyValue)) {
						props.setProperty(propertyName, convertedValue);
					}
				}
			}
		}
		SpringHelp.setPlaceholderPropertis(props);
		storeProperties(props);
	}

	private String decode(IEncrypt encryptAlgorithm, String propertyValue, String v) {
		try{
			String convertedValue = encryptAlgorithm.decode(v, propertyValue);
			return convertedValue;
		}catch(Exception e){
			CommonLogger.error("decode the encrypt data is error, data is " + v);
			return null;
		}
	}

	/**
	 * 保存读取的属性
	 * @param props
	 */
	protected void storeProperties(Properties props) {
		IPropertiesStore propertiesStore = getPropertiesStore();
		if(null != propertiesStore){
			propertiesStore.store(props);
		}
	}
	
	/**
	 * 是否为加密数据
	 * @param encrypt 加密标志
	 * @return 是否加密
	 */
	protected boolean isEncrypt(String encrypt){
		return Tool.CONVERT.string2Boolean(encrypt);
	}

	public String getEncryptPropertyName() {
		return encryptPropertyName;
	}

	public void setEncryptPropertyName(String encryptPropertyName) {
		this.encryptPropertyName = encryptPropertyName;
	}

	public IEncrypt getEncryptAlgorithm() {
		return encryptAlgorithm;
	}

	public void setEncryptAlgorithm(IEncrypt encryptAlgorithm) {
		this.encryptAlgorithm = encryptAlgorithm;
	}

	public IPropertiesStore getPropertiesStore() {
		return propertiesStore;
	}

	public void setPropertiesStore(IPropertiesStore propertiesStore) {
		this.propertiesStore = propertiesStore;
	}
	
	/**
	 * 属性存储接口
	 */
	public interface IPropertiesStore {

		public void store(Properties properties);
	}
}

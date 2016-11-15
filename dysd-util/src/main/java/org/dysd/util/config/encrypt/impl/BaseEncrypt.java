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
package org.dysd.util.config.encrypt.impl;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.dysd.util.config.encrypt.IEncrypt;

/**
 * 加解密实现类，使用AES-128和Base64加密
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-10
 */
public class BaseEncrypt implements IEncrypt {
	
	private String algorithm = "AES";
	private String encoding = "UTF-8";
	private int length = 128;
  
	/**
	 * 加密
	 */
	@Override
	public String encode(String data, String key){
        try {
        	key = getEncryptKey(key);
			/*  在IBM的JDK下面此段代码加解密结果和SUN JDK不一致 暂时屏蔽        
			KeyGenerator keygen=KeyGenerator.getInstance(algorithm);
            //keygen.init(length, new SecureRandom(key.getBytes(encoding)));
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes(encoding));
            keygen.init(length, secureRandom);*/
            SecretKey secretKey=new SecretKeySpec(key.getBytes(encoding), algorithm);
            Cipher cipher=Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return new String(Base64.encodeBase64(cipher.doFinal(data.getBytes(encoding))));
        } catch (Exception e) {
        	throw new UnsupportedOperationException(e);
        }
    }
   
	/**
	 * 解密
	 */
    @Override
	public String decode(String data, String key){
        try {
        	key = getEncryptKey(key);
            /*在IBM的JDK下面此段代码加解密结果和SUN JDK不一致 暂时屏蔽   
             * KeyGenerator keygen= KeyGenerator.getInstance(algorithm);
            //keygen.init(length, new SecureRandom(key.getBytes(encoding)));
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes(encoding));
            keygen.init(length, secureRandom);*/
            SecretKey secretKey=new SecretKeySpec(key.getBytes(encoding), algorithm);
            Cipher cipher=Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.decodeBase64(data)), encoding);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }
    
    /**
     * 获取实际密钥，子类可覆盖
     * @param key
     * @return
     */
    protected String getEncryptKey(String key){
    	return key;
    }
    
    public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}

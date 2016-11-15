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

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import org.dysd.util.Tool;
import org.dysd.util.config.BaseConfig;
import org.dysd.util.exception.ExceptionCodes;
import org.dysd.util.exception.Throw;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ContextResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.WritableResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 输入输出工具类
 * @author linjisong
 * @version 0.0.1
 * @since 0.0.1
 * @date 2016-11-8
 * @see BaseConfig#getResourcePatternResolver()
 * @see PathMatchingResourcePatternResolver
 * @see Resource
 * @see WritableResource
 * @see ContextResource
 * @see UrlResource
 * @see ClassPathResource
 * @see FileSystemResource
 * @see PathResource
 * @see ByteArrayResource
 * @see InputStreamResource
 */
public abstract class BaseIOUtils {

	private static final BaseIOUtils instance = new BaseIOUtils(){};
	private BaseIOUtils(){
	}
	
	private static final int EOF = -1;
	
	/**
	 * 获取单实例
	 * @return
	 */
	/*package*/ static BaseIOUtils getInstance(){
		return instance;
	}
	
	/**
	 * 关闭对象，忽略关闭中的异常
	 * @param closeables 可变参数，可以传入关闭对象数组，或不定参数关闭对象
	 */
	public void closeQuietly(Closeable... closeables) {
		if(null != closeables && closeables.length >= 1){
			for(Closeable c : closeables){
				if(null != c){
					try{c.close();}catch(IOException i){}
				}
			}
		}
    }
	
	/**
	 * 加载资源对象
	 * @param location 资源路径
	 * @return 资源对象
	 */
	public Resource getResource(String location){
		return getResource(null, location);
	}
	
	/**
	 * 加载资源对象
	 * @param environment 环境
	 * @param location 资源路径
	 * @return 资源对象
	 */
	public Resource getResource(Environment environment, String location){
		if(null != environment){
			location = environment.resolveRequiredPlaceholders(location);
		}
		ResourcePatternResolver loader = BaseConfig.getResourcePatternResolver();
		Resource resource = loader.getResource(location);
		return resource;
	}
	
	/**
	 * 将模式字符串加载为资源对象集合
	 * @param locationPattern 模式字符串
	 * @return 资源对象集合
	 */
	public Set<Resource> getResources(String locationPattern){
		return getResources(null, new String[]{locationPattern});
	}
	
	/**
	 * 将一组模式字符串加载为资源对象集合
	 * @param locationPatterns 一组模式字符串
	 * @return 资源对象集合
	 */
	public Set<Resource> getResources(String[] locationPatterns){
		return getResources(null, locationPatterns);
	}
	
	/**
	 * 将指定环境下的一组模式字符串加载为资源对象集合
	 * @param environment      指定环境对象
	 * @param locationPatterns 一组模式字符串，可以使用环境中的变量
	 * @return 资源对象集合
	 */
	public Set<Resource> getResources(Environment environment, String[] locationPatterns){
		try{
			Set<Resource> resources = new LinkedHashSet<Resource>();
			ResourcePatternResolver loader = BaseConfig.getResourcePatternResolver();
			for (String location : locationPatterns) {
				if(null != environment){
					location = environment.resolveRequiredPlaceholders(location);
				}
				for (Resource resource : loader.getResources(location)) {
					resources.add(resource);
				}
			}
			return resources;
		}catch(IOException e){
			throw Throw.createException(ExceptionCodes.DYSD010004, e, Tool.STRING.join(locationPatterns, ","));
		}
	}
	
	public static long copyLarge(Reader input, Writer output, char [] buffer) throws IOException {
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}

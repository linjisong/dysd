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
package org.dysd.util.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 简单的XML解析错误处理类
 * @author linjisong
 * @version 0.0.1
 * @date 2016-11-12
 */
public class BaseErrorHandler implements ErrorHandler{
	
	/**
	 * 警告
	 */
	private final List<SAXParseException> warns = new ArrayList<SAXParseException>();
	
	/**
	 * 错误
	 */
	private final List<SAXParseException> errors = new ArrayList<SAXParseException>();
	
	/**
	 * 致命
	 */
	private final List<SAXParseException> fatals = new ArrayList<SAXParseException>();
	
	/**
	 * 有警告时是否抛出异常
	 */
	private boolean throwWhenWarning = false;
	
	/**
	 * 有错误时是否抛出异常，如果{@link #throwWhenWarning}为true，有错误时肯定抛出异常
	 */
	private boolean throwWhenError = true;
	
	/**
	 * 有致命错误时是否抛出异常，如果{@link #throwWhenWarning}或{@link #throwWhenError}为true，则由fatal时肯定抛出异常
	 */
	private boolean throwWhenFatal = true;

	@Override
	public void warning(SAXParseException exception) throws SAXException {
		warns.add(exception);
		if(this.isThrowWhenWarning()){
			throw exception;
		}
	}

	@Override
	public void error(SAXParseException exception) throws SAXException {
		errors.add(exception);
		if(this.isThrowWhenWarning() || this.isThrowWhenError()){
			throw exception;
		}
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		fatals.add(exception);
		if(this.isThrowWhenWarning() || this.isThrowWhenError()  || this.isThrowWhenFatal()){
			throw exception;
		}
	}

	public boolean isThrowWhenWarning() {
		return throwWhenWarning;
	}

	public void setThrowWhenWarning(boolean throwWhenWarning) {
		this.throwWhenWarning = throwWhenWarning;
	}

	public boolean isThrowWhenError() {
		return throwWhenError;
	}

	public void setThrowWhenError(boolean throwWhenError) {
		this.throwWhenError = throwWhenError;
	}

	public boolean isThrowWhenFatal() {
		return throwWhenFatal;
	}

	public void setThrowWhenFatal(boolean throwWhenFatal) {
		this.throwWhenFatal = throwWhenFatal;
	}

	public List<SAXParseException> getWarns() {
		return warns;
	}

	public List<SAXParseException> getErrors() {
		return errors;
	}

	public List<SAXParseException> getFatals() {
		return fatals;
	}
}

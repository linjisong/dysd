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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.dysd.util.config.BaseConfig;
import org.dysd.util.exception.ExceptionCodes;
import org.dysd.util.exception.Throw;

/**
 * 日期工具类
 * @author linjisong
 * @version 0.0.1
 * @since 0.0.1
 * @date 2016-11-8
 */
public abstract class BaseDateUtils {

	private static final BaseDateUtils instance = new BaseDateUtils(){};
	private BaseDateUtils(){
	}
	
	/**
	 * 每个月的最大日期
	 */
	private static final int[] MAX_DAY_OF_MONTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
	/**
	 * 获取单实例
	 * @return
	 */
	/*package*/ static BaseDateUtils getInstance(){
		return instance;
	}
	
	/**
	 * 获取当前日期，格式为平台配置中的BaseConfig.getDateFormat()，默认格式yyyyMMdd
	 * @return
	 */
	public String getDate(){
		 return getFormatDate(new Date(), BaseConfig.getDateFormat());
	}
	
	/**
	 * 获取当前时间，格式为平台配置中的BaseConfig.getTimeFormat()，默认格式HH:mm:ss
	 * @return
	 */
	public String getTime(){
		 return getFormatDate(new Date(), BaseConfig.getTimeFormat());
	}

	/**
	 * 获取当前日期时间，格式为平台配置中的BaseConfig.getDatetimeFormat()，默认格式yyyyMMdd-HH:mm:ss
	 * @return
	 */
	public String getDateAndTime()
	{
		return getDateAndTime(new Date());
	}
	
	/**
	 * 日期时间格式化
	 * @param date 日期对象，格式为平台配置中的BaseConfig.getDatetimeFormat()，默认格式yyyyMMdd-HH:mm:ss
	 * @return 格式化后的日期字符串
	 */
	public String getDateAndTime(Date date)
	{
		return getFormatDate(date, BaseConfig.getDatetimeFormat());
	}
	
	/**
	 * 日期时间格式化
	 * @param date	  日期对象
	 * @param format 日期格式
	 * @return 格式化后的日期字符串
	 */
	public String getFormatDate(Date date, String format)
	{
		SimpleDateFormat df = new SimpleDateFormat(format);
		String rs = df.format(date);
		return rs;
	}
	
	/**
	 * 日期计算
	 * @param date		日期字符串，格式为平台配置中的BaseConfig.getDateFormat()，默认格式yyyyMMdd
	 * @param mYear		需要增加的年数，如果需要减少，传入负数
	 * @param mMonth	需要增加的月数，如果需要减少，传入负数
	 * @param mDay		需要增加的日数，如果需要减少，传入负数
	 * @return 计算后并且格式化的日期字符串
	 */
	public String dateCalculate(String date, int mYear, int mMonth, int mDay)
	{
		return dateCalculate(date, BaseConfig.getDateFormat(), mYear, mMonth, mDay);
	}
	
	/**
	 * 日期计算
	 * @param date		日期字符串
	 * @param format	日期格式
	 * @param mYear		需要增加的年数，如果需要减少，传入负数
	 * @param mMonth	需要增加的月数，如果需要减少，传入负数
	 * @param mDay		需要增加的日数，如果需要减少，传入负数
	 * @return 计算后并且格式化的日期字符串
	 */
	public String dateCalculate(String date, String format, int mYear, int mMonth, int mDay)
	{
		try {
			DateFormat fo = new SimpleDateFormat(format);
			Date sDate = fo.parse(date);
			Date rDate = dateCalculate(sDate, mYear, mMonth, mDay);
			return fo.format(rDate);
		} catch (ParseException e) {
			throw Throw.createException(ExceptionCodes.DYSD010003, e, format);
		}
	}
	
	/**
	 * 日期计算
	 * @param date		日期对象
	 * @param mYear		需要增加的年数，如果需要减少，传入负数
	 * @param mMonth	需要增加的月数，如果需要减少，传入负数
	 * @param mDay		需要增加的日数，如果需要减少，传入负数
	 * @return 计算后的日期对象
	 */
	public Date dateCalculate(Date date, int mYear, int mMonth, int mDay) 
	{
		Calendar tempCal = Calendar.getInstance();
		tempCal.setTime(date);
		tempCal.add(Calendar.YEAR, mYear);
		tempCal.add(Calendar.MONTH, mMonth);
		tempCal.add(Calendar.DATE, mDay);
		return tempCal.getTime();
	}
	
	/**
	 * 计算是当年的第几个日期
	 * @param year  年份
	 * @param month 月份
	 * @param day	日数
	 * @return 当年的第几个日期
	 */
	public int dayOfYear(int year, int month, int day) {
		int day_of_year;
		if (isLeapYear(year)) {
			day_of_year = ((275 * month) / 9) - ((month + 9) / 12) + day - 30;
		} else {
			day_of_year = ((275 * month) / 9) - (((month + 9) / 12) << 1) + day - 30;
		}
		return day_of_year;
	}
	
	/**
	 * 判断是否闰年
	 * @param year 年份
	 * @return 是否闰年
	 */
	public boolean isLeapYear(int year) {
		return (year%4 == 0 && year % 100 !=0) || year%400==0;
	}

	/**
	 * 获取月份的最大日期
	 * @param year	年份
	 * @param month 月份
	 * @return 月份的最大日期
	 */
	public int getMaxDayOfMonth(int year, int month) {
		if ((month < 1) || (month > 12)) {
			throw new IllegalArgumentException("Invalid month: " + month);
		}
		if (month == 2) {
			return isLeapYear(year) ? 29 : 28;
		}
		if ((year == 1582) && (month == 10)) {
			return 21;
		}
		return MAX_DAY_OF_MONTH[month];
	}
	
	/**
	 * 根据日期格式获取指定时间的毫秒数
	 * @param datetime       日期时间
	 * @param datetimeFormat 日期时间格式
	 * @return 毫秒数
	 */
	public long getTime(String datetime, String datetimeFormat){
		try{
			DateFormat fo = new SimpleDateFormat(datetimeFormat);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fo.parse(datetime));
			return calendar.getTimeInMillis();
		}catch(ParseException e){
			throw Throw.createException(ExceptionCodes.DYSD010003, e, datetimeFormat);
		}
	}
}

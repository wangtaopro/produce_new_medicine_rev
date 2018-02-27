package edu.trade.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期Util 
 */
public class DateUtil {
	
	/** 按日期类型生成前一天 *
	 * @throws Exception */
	public Date getPreDate(Date date) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
		return parse;
	}
	
	
	/** 获取当前日期类型 *
	 * @throws Exception */
	public Date getToDayDate(Date date) throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
		return parse;
	}
	
	/** 获取当前日期的前一天 **/
	public String getEarlyDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
	
	/** 获取当天日期 **/
	public String getDate(){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date today = new Date();
        String tString = df.format(today);
        return tString;	
    }

	/** 获取当前时间 **/
	public String getTime(){
        String pattern = "HH:mm:ss";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date today = new Date();
        String tString = df.format(today);
        return tString;	
    }
	
}

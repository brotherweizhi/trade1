package com;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static Date toDate(String strdate){
		Date date=null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date=sdf.parse(strdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	//计算两个时间的相差
	public static String subTime(Date nowDate, Date myDate){
		String result = "";

		int d = 1000*24*60*60;//1天的秒数
		int h = 60*60;//1小时的秒数
		int m = 60;//1分钟的秒数

		long nowdate=nowDate.getTime();
		long mydate=myDate.getTime();
		long diff = nowDate.getTime()-myDate.getTime();
		long day = diff / d;//相隔天数
		long hour = diff % d / h;
		long minute = diff % d % h / m;

		if(day>0) {
			result = day+"天前";
		}else{
			if(hour>0) {
				result = hour+"小时前";
			} else {
				if(minute>0) result = minute+"分钟前"; else result="刚刚";
			}	
		}
		return result;
	}
	
}

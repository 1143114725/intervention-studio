package com.investigate.newsupper.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by EEH on 2018/3/31.
 */
public class DateUtil {
	// 一天86400000
	private static final String TAG = "DateUtil";
	private static SimpleDateFormat sf = null;

	/**
	 * 时间戳转为年月日，时分秒
	 * 
	 * @param cc_time
	 * @return
	 */
	public static String getStrTime(String cc_time) {
		String re_StrTime = null;
		sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sf.format(new Date(lcc_time));
		return re_StrTime;
	}

	/**
	 * 
	 * @param cc_time
	 *            时间戳
	 * @param type
	 *            输出格式 eg："yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String getStrTime(String cc_time, String type) {
		String re_StrTime = null;
		sf = new SimpleDateFormat(type);
		sf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sf.format(new Date(lcc_time));
		return re_StrTime;
	}

	/**
	 * 时间戳转为年月日
	 * 
	 * @param cc_time
	 * @return
	 */
	public static String getTime(String cc_time) {
		String re_StrTime = null;
		// 同理也可以转为其它样式的时间格式.例如："yyyy/MM/dd HH:mm"
		sf = new SimpleDateFormat("yyyy-MM-dd");
		sf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sf.format(new Date(lcc_time));
		return re_StrTime;
	}

	/**
	 * 时间转换为时间戳
	 * 
	 * @param timeStr
	 *            时间 例如: 2016-03-09
	 * @param format
	 *            时间对应格式 例如: yyyy-MM-dd
	 * @return
	 */
	public static long getTimeStamp(String timeStr, String format) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		java.util.Date date = null;
		try {
			date = (Date) simpleDateFormat.parse(timeStr);
			long timeStamp = date.getTime();
			return timeStamp;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/*
	 * 将时间转换为时间戳
	 */
	public static String dateToStamp(String s) throws ParseException {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm");
		java.util.Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}

	// 获得当天0点时间
	public static int getTimesmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (int) (cal.getTimeInMillis() / 1000);
	}

	// 获得当天24点时间
	public static int getTimesnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (int) (cal.getTimeInMillis() / 1000);
	}

	/**
	 * 
	 * @param time
	 *            目标时间“2018年4月2日 14：55：23”
	 * @param now
	 *            当前时间的毫秒数
	 * @return
	 */
	public static String CalculateTime(String target, String now) {
		long nowTime = Long.parseLong(now); // 获取当前时间的毫秒数
		long targetTime = Long.parseLong(target); // 获取目标时间的毫秒数
		String msg = null;

		// long reset = 0;
		// try {
		// reset = Long.parseLong(dateToStamp(time));
		// } catch (NumberFormatException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		long dateDiff = targetTime - nowTime;

		if (dateDiff < 0) {
			msg = "已过期";
		} else {

			long dateTemp1 = dateDiff / 1000; // 秒
			long dateTemp2 = dateTemp1 / 60; // 分钟
			long dateTemp3 = dateTemp2 / 60; // 小时
			long dateTemp4 = dateTemp3 / 24; // 天数
			long dateTemp5 = dateTemp4 / 30; // 月数
			long dateTemp6 = dateTemp5 / 12; // 年数

			if (dateTemp6 > 0) {
				msg = dateTemp6 + "年";

			} else if (dateTemp5 > 0) {
				msg = dateTemp5 + "个月";

			} else if (dateTemp4 > 0) {
				msg = dateTemp4 + "天";

			} else if (dateTemp3 > 0) {
				msg = dateTemp3 + "小时";

			} else if (dateTemp2 > 0) {
				msg = dateTemp2 + "分钟";

			} else if (dateTemp1 > 0) {
				msg = "一分钟内";

			}
		}
		return msg;

	}

	/**
	 * 获取当前时间 "yyyy年MM月dd日 HH:mm:ss"
	 * 
	 * @return
	 */
	public static String getNowTime(String type) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
		Date date = new Date(System.currentTimeMillis());
		return simpleDateFormat.format(date);
	}

	/**
	 * 当前时间转换为时间戳
	 * 
	 * @return
	 */
	public static String dateToStamp() {
		String time = getNowTime("yyyy-MM-dd HH:mm:ss");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		java.util.Date date = null;
		try {
			date = simpleDateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long ts = date.getTime();
		return String.valueOf(ts);
	}

	// 年 31536000000
	// 月 2678400000
	// 周 604800000
	// 日 86400000
	// 时 3600000
	// 分 60000
	/**
	 * 
	 * @param data 	间隔时间
	 * @param unit	间隔单位
	 * @return
	 */
	public static long DataChangeToTime(String data, String unit) {
		int dataint = Integer.parseInt(data);
		long unitint = 0;
		if (unit.equals("1")) {
			unitint = 31536000000l;
		}
		if (unit.equals("2")) {
			unitint = 2678400000l;
		}
		if (unit.equals("3")) {
			unitint = 604800000l;
		}
		if (unit.equals("4")) {
			unitint = 86400000l;
		}
		if (unit.equals("5")) {
			unitint = 3600000l;
		}
		if (unit.equals("6")) {
			unitint = 60000l;
		}
		return 	(dataint * unitint);

	}
}

package com.lanfeng.gupai.utils.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author root
 *
 */
public class DateUtil {

	private static final long ONE_HOUR_MILLIS = 60 * 60 * 1000;
	private static final long ONE_DAY_MILLIS = 24 * ONE_HOUR_MILLIS;
	protected static final Log log = LogFactory.getLog(DateUtil.class);

	public static Date createDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	public static Date parse(Object obj) {
		Date date = null;
		if (obj instanceof Date) {
			date = (Date) obj;
		} else if (obj instanceof String) {
			date = parse((String) obj);
		} else if (obj instanceof Long) {
			date = parse(obj);
		}
		return date;
	}

	public static Date parse(long date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date);
		return cal.getTime();
	}

	public static Date parse(String str) {
		if (str == null || str.length() < 8) {
			return null;
		}

		String format = str.charAt(4) == '-' ? "yyyy-MM-dd" : "MM-dd-yyyy";
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(str);
		} catch (ParseException e) {
			dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			try {
				return dateFormat.parse(str);
			} catch (ParseException ee) {
				log.debug("Invalid date format: " + str);
			}
		}

		return null;
	}

	public static Date parse(String str, String format) {
		if (str == null || str.isEmpty() || format == null || format.isEmpty()) {
			return null;
		}

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.parse(str);
		} catch (ParseException e) {
			log.debug("Invalid date string: " + str + ", format: " + format);
		} catch (IllegalArgumentException e) {
			log.debug("Invalid date string: " + str + ", format: " + format);
		}

		return null;
	}

	public static Date parse(String str, String format, Locale locate) {
		if (str == null || str.isEmpty() || format == null || format.isEmpty()) {
			return null;
		}

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format, locate);
			return dateFormat.parse(str);
		} catch (ParseException e) {
			log.debug("Invalid date string: " + str + ", format: " + format);
		} catch (IllegalArgumentException e) {
			log.debug("Invalid date string: " + str + ", format: " + format);
		}

		return null;
	}

	/**
	 * @param date
	 *            format is yyyymmdd
	 * @return
	 */
	public static Date parseLongDate(long date) {
		if (date < 10000000) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.set((int) (date / 10000), (int) (date % 10000) / 100 - 1, (int) date % 100, 0, 0, 0);
		return cal.getTime();
	}

	public static List<String> sortDates(List<String> dates, String format, boolean asc) {
		List<String> rtn = new ArrayList<String>();

		Map<Date, String> map = new HashMap<Date, String>();
		Date date;
		for (String i : dates) {
			date = parse(i, format);
			map.put(date, i);
		}
		List<Date> keys = new ArrayList(map.keySet());
		Collections.sort(keys);

		if (!asc) {
			Collections.reverse(keys);
		}

		for (Date key : keys) {
			rtn.add(map.get(key));
		}

		return rtn;
	}

	/**
	 * @param date
	 * @return format is yyyymmdd
	 */
	public static long getLongDate(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar dt = getCalendarByDate(date);
		int y = dt.get(Calendar.YEAR);
		int m = dt.get(Calendar.MONTH) + 1;
		int d = dt.get(Calendar.DATE);

		return y * 10000 + m * 100 + d;
	}

	public static Date getBaseDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(1970, 0, 1, 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return false;
		}

		return countDays(date1, date2) == 1;
	}

	public static String format(Date date) {
		if (date == null) {
			return "";
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	public static String format(Date date, String format) {
		if (date == null || format == null || format.isEmpty()) {
			return "";
		}

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(date);
		} catch (IllegalArgumentException e) {
			log.debug("Invalid date format: " + format);
		}

		return "";
	}

	public static String formatForUI(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
		return dateFormat.format(date);
	}

	/**
	 * @param date1
	 * @param date2
	 * @return -1 means date1 < date2, 0 means date1 = date2, 1 means date1 >
	 *         date2
	 */
	public static int compareDate(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return 0;
		}

		int result = 1 - countDays(date1, date2);
		return result > 0 ? 1 : result < 0 ? -1 : 0;
	}

	public static Date getLocalTime() {
		return new Date();
	}

	// public static Date getLocalTime(int timeZoneInMinute) {
	// if (timeZoneInMinute > 780 || timeZoneInMinute < -720) {
	// return null;
	// }
	// Date date = new Date();
	// int offset = getTimeZoneOffset(timeZoneInMinute);
	// date.setTime(date.getTime() + offset * 60000);
	// return date;
	// }

	public static void clearTime(Date date) {
		Calendar cal = getCalendarByDate(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);

		date.setTime(cal.getTime().getTime());
	}

	public static Date clearDateTime(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = getCalendarByDate(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	// date format is yyyymmdd, likes 20110211
	public static long addLongDays(long date, int days) {
		Date d = parseLongDate(date);

		if (d == null) {
			return 0;
		}

		Calendar cal = getCalendarByDate(d);
		cal.add(Calendar.DATE, days);

		return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DATE);
	}

	public static int getWeek(Date date) {
		if (date == null) {
			return -1;
		}
		Calendar cal = getCalendarByDate(date);
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}

	public static String getTime(Integer minute) {
		if (minute == null || minute <= 0) {
			return "00:00";
		}

		if (minute >= 1410) {
			minute = minute % 1440;
		}
		int hour = minute / 60;
		int min = minute % 60;
		String strMinute = min > 9 ? Integer.toString(min) : "0" + min;

		return hour + ":" + strMinute;
	}

	public static String getTimeZoneId(int timeZoneInMinute) {
		if (timeZoneInMinute > 780 || timeZoneInMinute < -720) {
			return "Wrong timeZoneInMinute";
		}

		String minute = ":00";
		if (Math.abs(timeZoneInMinute % 60) == 30) {
			minute = ":30";
		}

		if (timeZoneInMinute > 0) {
			return "GMT+" + timeZoneInMinute / 60 + minute;
		} else {
			return "GMT" + timeZoneInMinute / 60 + minute;
		}
	}

	public static boolean isYearEnd(Date date) {
		if (date == null || !isMonthEnd(date)) {
			return false;
		}

		Calendar calendar = getCalendarByDate(date);
		int m = calendar.get(Calendar.MONTH);
		if (m == 11) {
			return true;
		}
		return false;
	}

	public static boolean isQuarterEnd(Date date) {
		if (date == null || !isMonthEnd(date)) {
			return false;
		}

		Calendar calendar = getCalendarByDate(date);
		int m = calendar.get(Calendar.MONTH);
		if (m == 2 || m == 5 || m == 8 || m == 11) {
			return true;
		}
		return false;
	}

	public static boolean isMonthEnd(Date date) {
		if (date == null) {
			return false;
		}

		Calendar calendar = getCalendarByDate(date);
		calendar.add(Calendar.DATE, 1);
		int day = calendar.get(Calendar.DATE);
		if (day == 1) {
			return true;
		}
		return false;
	}

	public static boolean isWeekEnd(Date date) {
		if (date == null) {
			return false;
		}

		Calendar calendar = getCalendarByDate(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		if (day == 7) {
			return true;
		}
		return false;
	}

	public static boolean isYearStart(Date date) {
		if (date == null && !isMonthStart(date)) {
			return false;
		}

		Calendar calendar = getCalendarByDate(date);
		int m = calendar.get(Calendar.MONTH);
		if (m == 0) {
			return true;
		}
		return false;
	}

	public static boolean isQuarterStart(Date date) {
		if (date == null && !isMonthStart(date)) {
			return false;
		}

		Calendar calendar = getCalendarByDate(date);
		int m = calendar.get(Calendar.MONTH);
		if (m == 0 || m == 3 || m == 6 || m == 9) {
			return true;
		}
		return false;
	}

	public static boolean isMonthStart(Date date) {
		if (date == null) {
			return false;
		}

		Calendar calendar = getCalendarByDate(date);
		int day = calendar.get(Calendar.DATE);
		if (day == 1) {
			return true;
		}
		return false;
	}

	public static boolean isWeekStart(Date date) {
		if (date == null) {
			return false;
		}

		Calendar calendar = getCalendarByDate(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		if (day == 1) {
			return true;
		}
		return false;
	}

	public static int getMinuteOfDay(Date date) {
		if (date == null) {
			return -1;
		}
		Calendar calendar = getCalendarByDate(date);
		int minute = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
		return minute;
	}

	public static Date getDateWithZeroTime(Date date) {
		if (date == null) {
			return new Date();
		}
		Calendar calendar = getCalendarByDate(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		calendar.set(year, month, day, 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date addDays(Date date, int days) {
		if (date == null) {
			return null;
		}

		Calendar now = getCalendarByDate(date);
		now.add(Calendar.DATE, days);
		return now.getTime();
	}

	public static int countDays(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return Integer.MAX_VALUE;
		}

		Calendar calendar = getCalendarByDate(startDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		long startMS = calendar.getTimeInMillis();
		calendar.setTime(endDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		long endMS = calendar.getTimeInMillis();

		long diff = endMS > startMS ? ONE_HOUR_MILLIS : -ONE_HOUR_MILLIS;
		int diffDays = (int) ((endMS - startMS + diff) / ONE_DAY_MILLIS);
		return diffDays >= 0 ? diffDays + 1 : diffDays - 1;
	}

	public static int countWeeks(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return Integer.MAX_VALUE;
		}

		Calendar calendar = getCalendarByDate(clearDateTime(startDate));
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		long startMS = calendar.getTimeInMillis();
		calendar.setTime(clearDateTime(endDate));
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		long endMS = calendar.getTimeInMillis();

		int diffWeeks = (int) ((endMS - startMS) / ONE_DAY_MILLIS) / 7;
		return diffWeeks >= 0 ? diffWeeks + 1 : diffWeeks - 1;
	}

	public static int countMonths(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return Integer.MAX_VALUE;
		}

		Calendar calendar = getCalendarByDate(startDate);
		int sy = calendar.get(Calendar.YEAR);
		int sm = calendar.get(Calendar.MONTH);
		calendar.setTime(endDate);
		int ey = calendar.get(Calendar.YEAR);
		int em = calendar.get(Calendar.MONTH);

		int diffMonths = (ey - sy) * 12 + em - sm;
		return diffMonths >= 0 ? diffMonths + 1 : diffMonths - 1;
	}

	public static int countQuarters(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return Integer.MAX_VALUE;
		}
		Calendar calendar = getCalendarByDate(endDate);
		int em = calendar.get(Calendar.MONTH);
		boolean isQuarterEnd = (em + 1) % 3 == 0;
		int diffMonths = countMonths(startDate, endDate);
		int diffQuarters = diffMonths / 3;
		if (diffQuarters >= 0) {
			return (diffMonths % 3 == 0 && isQuarterEnd) ? diffQuarters : diffQuarters + 1;
		} else {
			return diffQuarters - 1;
		}
	}

	public static int countYears(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return Integer.MAX_VALUE;
		}

		Calendar start = getCalendarByDate(startDate);
		Calendar end = getCalendarByDate(endDate);
		int diffYears = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
		return diffYears >= 0 ? diffYears + 1 : diffYears - 1;
	}

	public static boolean isBetweenDate(Date date, Date startDate, Date endDate) {
		if (date == null || startDate == null || endDate == null) {
			return false;
		}
		if (DateUtil.compareDate(date, startDate) >= 0 && DateUtil.compareDate(date, endDate) <= 0) {
			return true;
		}
		return false;
	}

	public static Calendar getCalendarByDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		return calendar;
	}
}

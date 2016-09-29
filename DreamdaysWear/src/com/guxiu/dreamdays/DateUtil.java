package com.guxiu.dreamdays;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 
 * @author Fatty
 *
 */
@SuppressWarnings("deprecation")
public class DateUtil {

	/**
	 * 
	 * @param dateString
	 * @param pattern
	 * @return
	 * @throws java.text.ParseException
	 */
	public static String getWeekByDateFormat(String dateString, String pattern) throws java.text.ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern ,Locale.ENGLISH);
		Date date = format.parse(dateString);
		return getWeekByDateFormat(date);
	}

	/**
	 * 获取Week format
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekByDateFormat(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("E" ,Locale.ENGLISH);
		return format.format(date);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static int NextMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MONTH, date.getMonth() + 1);
		return c.getTime().getMonth() + 1;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static int LastMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.MONTH, date.getMonth() - 1);
		return c.getTime().getMonth() + 1;
	}

	/**
	 * 
	 * 
	 * @param date
	 * @return
	 */
	public static String getNextMonth(String date) {
		String[] number = date.split("-");
		if ((Integer.parseInt(number[1]) + 1) % 12 == 1) {
			number[0] = Integer.parseInt(number[0]) + 1 + "";
			number[1] = 1 + "";
		} else {
			number[1] = Integer.parseInt(number[1]) + 1 + "";
		}
		if (number.length == 2) {
			if (Integer.parseInt(number[1]) < 10)
				return (Integer.parseInt(number[0]) + "-0" + Integer.parseInt(number[1]));
			else
				return (Integer.parseInt(number[0]) + "-" + Integer.parseInt(number[1]));
		} else {
			if (Integer.parseInt(number[1]) < 10)
				return (Integer.parseInt(number[0]) + "-0"+ Integer.parseInt(number[1]) + "-" + Integer.parseInt(number[2]));
			else
				return (Integer.parseInt(number[0]) + "-"	+ Integer.parseInt(number[1]) + "-" + Integer.parseInt(number[2]));
		}
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getBeforeMonth(String date) {
		String[] number = date.split("-");
		if ((Integer.parseInt(number[1]) - 1) % 12 == 0) {
			number[0] = Integer.parseInt(number[0]) - 1 + "";
			number[1] = 12 + "";
		} else {
			number[1] = Integer.parseInt(number[1]) - 1 + "";
		}
		if (number.length == 2) {
			if (Integer.parseInt(number[1]) < 10) {
				return (Integer.parseInt(number[0]) + "-0" + Integer.parseInt(number[1]));
			} else {
				return (Integer.parseInt(number[0]) + "-" + Integer.parseInt(number[1]));
			}
		} else {
			if (Integer.parseInt(number[1]) < 10) {
				return (Integer.parseInt(number[0]) + "-0"+ Integer.parseInt(number[1]) + "-" + Integer.parseInt(number[2]));
			} else {
				return (Integer.parseInt(number[0]) + "-"+ Integer.parseInt(number[1]) + "-" + Integer.parseInt(number[2]));
			}
		}
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getBeginDate(String date) {
		String[] number = date.split("-");
		number[2] = "01";
		return number[0] + "-" + number[1] + "-" + number[2];
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getEndDate(String date) {
		String[] number = date.split("-");
		if ((Integer.parseInt(number[1]) + 1) % 12 == 1) {
			number[0] = Integer.parseInt(number[0]) + 1 + "";
			number[1] = 1 + "";
		} else {
			number[1] = Integer.parseInt(number[1]) + 1 + "";
		}
		if (Integer.parseInt(number[1]) < 10)
			number[1] = "0" + number[1];
		number[2] = "01";
		return number[0] + "-" + number[1] + "-" + number[2];
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getNextDay(String date) {
		String[] s = date.split("-");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(s[0]));
		cal.set(Calendar.MONTH, Integer.parseInt(s[1]) - 1);// 7�?
		int maxDate = cal.getActualMaximum(Calendar.DATE);
		if (Integer.parseInt(s[2]) + 1 > maxDate) {
			s[2] = 1 + "";
			if ((Integer.parseInt(s[1]) + 1) % 12 == 1) {
				s[0] = Integer.parseInt(s[0]) + 1 + "";
				s[1] = 1 + "";
			} else {
				s[1] = Integer.parseInt(s[1]) + 1 + "";
			}
		} else {
			s[1] = Integer.parseInt(s[1]) + "";
			s[2] = Integer.parseInt(s[2]) + 1 + "";
		}
		if (Integer.parseInt(s[2]) < 10) {
			s[2] = "0" + s[2];
		}
		if (Integer.parseInt(s[1]) < 10) {
			s[1] = "0" + s[1];
		}
		return s[0] + "-" + s[1] + "-" + s[2];

	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastDay(String date) {
		String[] s = date.split("-");
		if (Integer.parseInt(s[2]) - 1 == 0) {
			if (Integer.parseInt(s[1]) - 1 == 0) {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, Integer.parseInt(s[0]) - 1);
				cal.set(Calendar.MONTH, 12);
				int maxDate = cal.getActualMaximum(Calendar.DATE);
				s[0] = Integer.parseInt(s[0]) - 1 + "";
				s[1] = 12 + "";
				s[2] = maxDate + "";
			} else {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, Integer.parseInt(s[0]));
				cal.set(Calendar.MONTH, Integer.parseInt(s[1]) - 2);// 7�?
				int maxDate = cal.getActualMaximum(Calendar.DATE);
				s[1] = Integer.parseInt(s[1]) - 1 + "";
				s[2] = maxDate + "";
			}
		} else {
			s[1] = Integer.parseInt(s[1]) + "";
			s[2] = Integer.parseInt(s[2]) - 1 + "";
		}
		if (Integer.parseInt(s[2]) < 10) {
			s[2] = "0" + s[2];
		}
		if (Integer.parseInt(s[1]) < 10) {
			s[1] = "0" + s[1];
		}
		return s[0] + "-" + s[1] + "-" + s[2];
	}

	/**
	 * 
	 * @param dastDate
	 * @param currentDate
	 * @return
	 */
	public static long getMatterDay(Date dastDate, Date currentDate) {
		long dt1MillSeconds = dastDate.getTime();
		long dt2MillSeconds = currentDate.getTime();
		long julianSeconds = (dt1MillSeconds - dt2MillSeconds) / (60 * 1000 * 60 * 24);// 相差天数
		return julianSeconds;
	}

	/**
	 * 
	 * @param dastDate
	 * @return
	 */
	public static int getMatterDay(Date dastDate) {
		Calendar calendar1 = Calendar.getInstance(); 
		calendar1.setTime(dastDate); 
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(new Date());
		// 先判断是否同年
		int y1 = calendar1.get(Calendar.YEAR);
		int y2 = calendar2.get(Calendar.YEAR);
		int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
		int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
		int maxDays = 0;
		int day = 0;
		if (y1 - y2 > 0) {
			day = numerical(maxDays, d1, d2, y1, y2, calendar2);
		} else if (y2 - y1 > 0) {
			day = 0 - numerical(maxDays, d2, d1, y2, y1, calendar1);
		} else if (y2 == y1) {
			day = d1 - d2;
		}
		return day;
	}

	/**
	 * 
	 * @param maxDays
	 * @param d1
	 * @param d2
	 * @param y1
	 * @param y2
	 * @param calendar
	 * @return
	 */
	public static int numerical(int maxDays, int d1, int d2, int y1, int y2, Calendar calendar) {
		int day = d1 - d2;
		int betweenYears = y1 - y2;
		List<Integer> d366 = new ArrayList<Integer>();
		if (calendar.getActualMaximum(Calendar.DAY_OF_YEAR) == 366) {
			day += 1;
		}
		for (int i = 0; i < betweenYears; i++) {
			calendar.set(Calendar.YEAR, (calendar.get(Calendar.YEAR)) + 1);
			maxDays = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
			if (maxDays != 366) {
				day += maxDays;
			} else {
				d366.add(maxDays);
			}
			if (i == betweenYears - 1 && betweenYears > 1 && maxDays == 366) {
				day -= 1;
			}
		}
		for (int i = 0; i < d366.size(); i++) {
			if (d366.size() >= 1) {
				day += d366.get(i);
			}
		}
		return day;
	}

	/**
	 * 
	 * @param strDate
	 * @return
	 */
	public static long getDateTime(String strDate) {
		return getDateByFormat(strDate, "yyyyMMdd").getTime();
	}

	/**
	 * 
	 * @param strDate
	 * @param format
	 * @return
	 */
	public static Date getDateByFormat(String strDate, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.ENGLISH);
		try {
			return (sdf.parse(strDate));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * @param dateStr
	 * @param formatStr
	 * @return
	 */
	public static Date stringToDate(String dateStr, SimpleDateFormat formatStr) {
		Date date = null;
		try {
			date = formatStr.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取当前格林威治时间
	 * 
	 * @param gDateFormat
	 * @return
	 */
	public static String getCurrentGTime(SimpleDateFormat gDateFormat) {
		final int msInMin = 60000;
		final int minInHr = 60;
		Date date = new Date();
		int Hours, Minutes;
		TimeZone zone = gDateFormat.getTimeZone();
		Minutes = zone.getOffset(date.getTime()) / msInMin;
		Hours = Minutes / minInHr;
		zone = zone.getTimeZone("GMT Time" + (Hours >= 0 ? "+" : "") + Hours + ":" + Minutes);
		gDateFormat.setTimeZone(zone);
		return gDateFormat.format(date);
	}

	/**
	 * 
	 * @param calendarBirth
	 * @param calendarNow
	 * @return
	 */
	public static int[] getNeturalAge(Calendar calendarBirth, Calendar calendarNow) {
		int diffYears = 0, diffMonths, diffDays;
		int dayOfBirth = calendarBirth.get(Calendar.DAY_OF_MONTH);
		int dayOfNow = calendarNow.get(Calendar.DAY_OF_MONTH);
		if (dayOfBirth <= dayOfNow) {
			diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
			diffDays = dayOfNow - dayOfBirth;
			if (diffMonths == 0)
				diffDays++;
		} else {
			if (isEndOfMonth(calendarBirth)) {
				if (isEndOfMonth(calendarNow)) {
					diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
					diffDays = 0;
				} else {
					calendarNow.add(Calendar.MONTH, -1);
					diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
//					diffDays = dayOfNow + 1;
					diffDays = dayOfNow ;
				}
			} else {
				if (isEndOfMonth(calendarNow)) {
					diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
					diffDays = 0;
				} else {
					calendarNow.add(Calendar.MONTH, -1);
					diffMonths = getMonthsOfAge(calendarBirth, calendarNow);
					int maxDayOfLastMonth = calendarNow.getActualMaximum(Calendar.DAY_OF_MONTH);
					if (maxDayOfLastMonth > dayOfBirth) {
						diffDays = maxDayOfLastMonth - dayOfBirth + dayOfNow;
					} else {
						diffDays = dayOfNow;
					}
				}
			}
		}
		diffYears = diffMonths / 12;
		diffMonths = diffMonths % 12;
		return new int[] { diffYears, diffMonths, diffDays };
	}

	/**
	 * 获取两个日历的月份之差
	 * 
	 * @param calendarBirth
	 * @param calendarNow
	 * @return
	 */
	public static int getMonthsOfAge(Calendar calendarBirth, Calendar calendarNow) {
		return (calendarNow.get(Calendar.YEAR) - calendarBirth.get(Calendar.YEAR))* 12+ calendarNow.get(Calendar.MONTH) - calendarBirth.get(Calendar.MONTH);
	}

	/**
	 * 判断这一天是否是月底
	 * 
	 * @param calendar
	 * @return
	 */
	public static boolean isEndOfMonth(Calendar calendar) {
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth == calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
			return true;
		return false;
	}
}

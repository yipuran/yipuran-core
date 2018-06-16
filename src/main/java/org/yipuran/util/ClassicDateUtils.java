package org.yipuran.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
/**
 * 日付時刻文字列＜－＞Date 相互変換クラス.
 * <br> java.text.SimpleDateFormat の日付フォーマットに従う日付文字列とDate型の相互変換
 * @since 1.0.0
 */
public final class ClassicDateUtils {

	/**
	 * 時刻 "yyyy/MM/dd HH:mm:ss" フォーマット
	 */
	public static final String YMDHMS_FORM = "yyyy/MM/dd HH:mm:ss";

	/**
	 * 日付 "yyyy/MM/dd" フォーマット
	 */
	public static final String YMD_FORM = "yyyy/MM/dd";

	/**
	 * 時刻 "yyyyMMddHHmmss" フォーマット
	 */
	public static final String YMDHMSSTRING_FORM = "yyyyMMddHHmmss";

	/**
	 * 日付 "yyyyMMdd" フォーマット
	 */
	public static final String YMDSTRING_FORM = "yyyyMMdd";

	private ClassicDateUtils(){}
	/**
	 * 日付時刻文字列－＞Date 変換
	 * @param str 変換対象日付時刻文字列
	 * @param format strのフォーマット、java.text.SimpleDateFormat の日付フォーマットに従う。
	 * @return Ｄａｔｅオブジェクト
	 * @throws ParseException
	 */
	public static Date strToDate(String str,String format){
		try{
			return new SimpleDateFormat(format).parse(str);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 日付計算、日付時刻文字列－＞Date 変換
	 * @param str 変換対象日付時刻文字列
	 * @param format strのフォーマット、java.text.SimpleDateFormat の日付フォーマットに従う。
	 * @param day 加算または減算の日数
	 * @return Ｄａｔｅオブジェクト
	 * @throws ParseException
	 */
	public static Date strToDate(String str,String format,int day){
		Calendar cal = Calendar.getInstance();
		try{
			cal.setTime(new SimpleDateFormat(format).parse(str));
			cal.add(Calendar.DAY_OF_MONTH,day);
			return cal.getTime();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * Date－＞日付時刻文字列変換.
	 * <pre>以下、SimpleDateFormatに沿った指定で変換
	 * yyyy = 西暦４桁年
	 * MM = 月２桁
	 * dd = 日２桁
	 * HH = 時２桁
	 * mm = 分２桁
	 * ss = 秒２桁</pre>
	 * @param dt 時刻
	 * @param format SimpleDateFormatに沿った文字列
	 * @return formatに沿った日付時刻文字列結果
	 */
	public static String dateToString(Date dt,String format){
		SimpleDateFormat simpleform = new SimpleDateFormat(format);
		return simpleform.format(dt);
	}
	/**
	 * 指定日の年.
	 * @param date 指定日
	 * @return 年
	 */
	public static int getYear(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}
	/**
	 * 指定日の月.
	 * @param date 指定日
	 * @return 月
	 */
	public static int getMonth(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH) + 1;
	}

	/**
	 * 日付計算、日付時刻文字列変換.
	 * @param dt 時刻
	 * @param format 結果フォーマット
	 * @param day 加算または減算の日数
	 * @return 日付時刻文字列
	 */
	public static String dateToString(Date dt,String format,int day){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.DAY_OF_MONTH,day);
		SimpleDateFormat simpleform = new SimpleDateFormat(format);
		return simpleform.format(cal.getTime());
	}

	/**
	 * 日付計算（文字列）
	 * @param str 日付文字列
	 * @param format strのフォーマット、java.text.SimpleDateFormat の日付フォーマットに従う。
	 * @param day 加算または減算の日数
	 * @return 日付時刻文字列
	 * @throws ParseException
	 */
	public static String calDateString(String str,String format,int day){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleform = new SimpleDateFormat(format);
		try{
			cal.setTime(simpleform.parse(str));
			cal.add(Calendar.DAY_OF_MONTH,day);
		return simpleform.format(cal.getTime());
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 日付差算出.
	 * @param endDate 終了日
	 * @param startDate 開始日
	 * @return 日付差、開始日==終了日 の結果は、0
	 */
	public static int diff(Date endDate, Date startDate){
		if (endDate==null || startDate==null) return 0;
		long n2 = strToDate(dateToString(endDate, YMD_FORM), YMD_FORM).getTime();
		long n1 = strToDate(dateToString(startDate, YMD_FORM), YMD_FORM).getTime();
		long n = n2 - n1;
	   return (int)(n / 1000 / 60 / 60 / 24);
	}

	/**
	 * 日付チェック.
	 * @param datestr 日付文字列
	 * @param split 区切り文字
	 * @return false=存在できない日付
	 */
	public static boolean dateCheck(String datestr, String split){
		String[] ary = datestr.split(split);
		if (ary.length != 3) return false;
		try{
			int year = Integer.parseInt(ary[0]);
			int month = Integer.parseInt(ary[1]);
			int day = Integer.parseInt(ary[2]);
			return dateCheck(year, month, day);
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * 日付チェック.
	 * <br>指定する年、月、日が存在できる日付かチェックする。<br>
	 * Calendar等のオブジェクト生成することなく計算だけでチェックを行う。<br>
	 * 閏年は、400で割り切れる。または、100で割った余り＝０でなくて４で割り切れる<br>
	 * 場合である。
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return false=存在できない日付
	 */
	public static boolean dateCheck(String year,String month,String day){
		return dateCheck(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
	}

	/**
	 * 日付チェック.
	 * <br>指定する年、月、日が存在できる日付かチェックする。<br>
	 * Calendar等のオブジェクト生成することなく計算だけでチェックを行う。<br>
	 * 閏年は、400で割り切れる。または、100で割った余り＝０でなくて４で割り切れる<br>
	 * 場合である。
	 * <br>
	 * @since 1.0.1
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return false=存在できない日付
	 */
	public static boolean dateCheck(int year,int month,int day){
		if (year < 1) return false;
		if (month < 1 || 12 < month) return false;
		if (day < 1 || 31 < day) return false;
		boolean rtn = true;
		if (month==2 && day==29){
		int n = year % 400;
			if (n > 0){
				n %= 100;
				if (n > 0){
					if (n % 4 > 0) rtn = false;
				}else{
					rtn = false;
				}
			}
		}else{
			if (month==2){
				if (day > 28) rtn = false;
			}else{
				if (month > 7){
					if (month % 2 > 0){
						if (day == 31) rtn = false;
					}
				}else{
					if (month % 2 == 0){
						if (day == 31) rtn = false;
					}
				}
			}
		}
		return rtn;
	}

	/**
	 * 時刻（秒）計算.
	 * <br>Dateに対して、指定秒数を加算した時刻を求める。<br>
	 * 秒数＝負の値を指定することで過去の時刻を求める。<br>
	 * @since 1.3.0
	 * @param date 時刻
	 * @param sec 加算する秒数
	 * @return 計算結果の時刻
	 */
	public static Date calSecDate(Date date,int sec){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND,sec);
		return cal.getTime();
	}
	/**
	 * 時刻（分）計算.
	 * <br>Dateに対して、指定秒数を加算した時刻を求める。<br>
	 * 秒数＝負の値を指定することで過去の時刻を求める。<br>
	 * @since 1.3.0
	 * @param date 時刻
	 * @param sec 加算する秒数
	 * @return 計算結果の時刻
	 */
	public static Date calMinuteDate(Date date,int minute){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE,minute);
		return cal.getTime();
	}
	/**
	 * 時刻（Hour）計算.
	 * <br>Dateに対して、指定時間を加算した時刻を求める。<br>
	 * 時間＝負の値を指定することで過去の時刻を求める。<br>
	 * @since 1.3.0
	 * @param date 時刻
	 * @param hour 加算する時間
	 * @return 計算結果の時刻
	 */
	public static Date calHourDate(Date date,int hour){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR,hour);
		return cal.getTime();
	}

	/**
	 * 日付加算.
	 * <br>Dateに対して、指定日付を加算した時刻を求める。<br>
	 * 日付＝負の値を指定することで過去の時刻を求める。<br>
	 * @param date
	 * @param day
	 * @return 計算結果の時刻
	 */
	public static Date calDayDate(Date date,int day){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR,day * 24);
		return cal.getTime();
	}

	/**
	 * 月の末日を算出（Day 日を返す）.
	 * @since 1.1
	 * @param date 末日を算出したい月に該当する日付Date（java.util.Date）
	 * @return 月の末日のDay
	 */
	public static int getEndDay(Date date){
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, - cal.get(Calendar.DAY_OF_MONTH));
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	/**
	 * 月の末日を算出（Day 日を返す）.
	 * @since 1.1
	 * @param date 末日を算出したい月に該当する日付Date（java.time.LocalDate）
	 * @return 月の末日のDay
	 */
	public static int getEndDay(LocalDate date){
		return date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
	}

	/**
	 * 月の末日を算出（java.util.Date 日付を返す）.
	 * @since 1.1
	 * @param date 末日を算出したい月に該当する日付Date
	 * @return 月の末日のDate
	 */
	public static Date getEndDate(Date date){
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, - cal.get(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	/**
	 * 月の末日を算出（java.time.LocalDate 日付を返す）.
	 * @param date LocalDate
	 * @return 月の末日のLocalDate
	 */
	public static LocalDate getEndDate(LocalDate date){
		return date.with(TemporalAdjusters.lastDayOfMonth());
	}

	/**
	 * 指定日の月のカレンダー配列List を作成する
	 * @param date カレンダーを求めたい日付
	 * @param blank 該当しない曜日の表示文字、null を渡すと前月、翌月の日付けが格納される
	 * @return  List<String[]> String[] length = 7
	 */
	public static List<String[]> calendarList(Date date,String blank){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1);
		int firstWeek = c.get(Calendar.DAY_OF_WEEK);
		c.add(Calendar.MONTH, 1);
		c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1);
		c.add(Calendar.DAY_OF_MONTH, - c.get(Calendar.DAY_OF_MONTH));
		int endday = c.get(Calendar.DAY_OF_MONTH);
		int last = 31;
		if (blank==null){
			c.set(Calendar.DAY_OF_MONTH,1);
			c.add(Calendar.DAY_OF_MONTH,-1);
			last = c.get(Calendar.DAY_OF_MONTH);
		}
		List<String[]> list = new ArrayList<String[]>();
		int day = 1;
		int week = 0;
		String[] s = new String[7];
		int o = firstWeek - 1;
		if (blank==null){
			int x = last;
			for(int i=o-1;i >= 0;i--,x--){
				s[i] = Integer.toString(x);
				week++;
			}
		}else{
			for(;week < o;week++){
				s[week] = blank;
			}
		}
		for(int d=day;d <= endday;d++){
			if (week==7){
				list.add(s);
				s = new String[7];
				week = 0;
			}
			s[week] = Integer.toString(d);
			week++;
		}
		if (blank==null){
			int x = 1;
			for(;week < 7;week++,x++){
				s[week] = Integer.toString(x);
			}
		}else{
			for(;week < 7;week++){
				s[week] = blank;
			}
		}
		list.add(s);
		return list;
	}
	/**
	 * 指定日の月のカレンダー配列List を作成する（月曜始まり)
	 * @param date カレンダーを求めたい日付
	 * @param blank 該当しない曜日の表示文字、null を渡すと前月、翌月の日付けが格納される
	 * @return  List<String[]> String[] length = 7
	 */
	public static List<String[]> calendarListStartMonday(Date date,String blank){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1);
		int firstWeek = c.get(Calendar.DAY_OF_WEEK);
		c.add(Calendar.MONTH, 1);
		c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1);
		c.add(Calendar.DAY_OF_MONTH, - c.get(Calendar.DAY_OF_MONTH));
		int endday = c.get(Calendar.DAY_OF_MONTH);
		int last = 31;
		if (blank==null){
			c.set(Calendar.DAY_OF_MONTH,1);
			c.add(Calendar.DAY_OF_MONTH,-1);
			last = c.get(Calendar.DAY_OF_MONTH);
		}
		List<String[]> list = new ArrayList<String[]>();
		int day = 1;
		int week = 0;
		String[] s = new String[7];
		if (firstWeek==1){
			if (blank==null){
				int x = last;
				for(int i=5;i >= 0;i--,x--){
					s[i] = Integer.toString(x);
				}
			}else{
				for(int i=0;i < 6;i++){
					s[i] = blank;
				}
			}
			s[6] = "1";
			week = 7;
			day=2;
		}else{
			int o = firstWeek-2;
			if (blank==null){
				int x = last;
				for(int i=o-1;i >= 0;i--,x--){
					s[i] = Integer.toString(x);
					week++;
				}
			}else{
				for(;week < o;week++){
					s[week] = blank;
				}
			}
		}
		for(int d=day;d <= endday;d++){
			if (week==7){
				list.add(s);
				s = new String[7];
				week = 0;
			}
			s[week] = Integer.toString(d);
			week++;
		}
		if (blank==null){
			int x = 1;
			for(;week < 7;week++,x++){
				s[week] = Integer.toString(x);
			}
		}else{
			for(;week < 7;week++){
				s[week] = blank;
			}
		}
		list.add(s);
		return list;
	}

	/**
	 * java.util.Date の Stream生成.
	 * @param start java.util.Date 開始日
	 * @param end java.util.Date end 終了日
	 * @return 期間の java.util.Date の Stream
	 */
	public static Stream<Date> createDateStream(Date start, Date end){
		LocalDate startLocaldate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return IntStream.range(0, Period.between(startLocaldate, end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getDays() + 1)
				.mapToObj(i->startLocaldate.plusDays(i))
				.map(d->Date.from(d.atTime(LocalTime.of(0, 0)).atZone(ZoneId.systemDefault()).toInstant()));
	}

	/**
	 * java.util.Date期間→java.time.LocalDate のStream生成.
	 * @param start java.util.Date 開始日
	 * @param end java.util.Date end 終了日
	 * @return 期間の java.time.LocalDate の Stream
	 */
	public static Stream<LocalDate> createLocalDateStream(Date start, Date end){
		LocalDate startLocaldate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return IntStream.range(0, Period.between(startLocaldate, end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getDays() + 1)
				.mapToObj(i->startLocaldate.plusDays(i));
	}

	/**
	 * java.time.LocalDate期間→java.time.LocalDate のStream生成.
	 * @param start java.time.LocalDate 開始日
	 * @param end java.time.LocalDate 終了日
	 * @return 期間の java.time.LocalDate の Stream
	 */
	public static Stream<LocalDate> createLocalDateStream(LocalDate start, LocalDate end){
		return IntStream.range(0, Period.between(start, end).getDays() + 1).mapToObj(i->start.plusDays(i));
	}
}

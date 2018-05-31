package org.yipuran.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.yipuran.function.ThrowableFunction;

/**
 * DateUtil
 */
public final class DateUtil{
	/**
	 * private コンストラクタ.
	 */
	private DateUtil(){}

	/**
	 * 日付妥当性チェック.
	 * @param strDate 検査対象日付文字列
	 * @param pattern 日付書式
	 * @return true=OK
	 */
	public static boolean checkDate(String strDate, String pattern){
		DateFormat format = new SimpleDateFormat(pattern);
		format.setLenient(false);
		try{
			format.parse(strDate);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * 日付妥当性チェック→Optional&lt;LocalDate&gt;生成.
	 * LocalDate の Optional を取得して 厳密なチェック(lenient=false)のもと 異常であれば ParseException をスローする。
	 * null は、返却値 Optional の orElseThrow or orElse を処理するためのメソッド
	 * @param string 日付文字列
	 * @param pattern 日付書式
	 * @return Optional&lt;LocalDate&gt;
	 * @throws java.text.ParseException
	 */
	public static Optional<LocalDate> optionalDate(String string, String pattern){
		return Optional.ofNullable(string)
		.map(ThrowableFunction.of(e->{
			DateFormat f = new SimpleDateFormat(pattern);
			f.setLenient(false);
			f.parse(e);
			return e;}))
		.map(e->LocalDate.parse(e, DateTimeFormatter.ofPattern(pattern)));
	}

	/**
	 * 重複発生抽出.
	 * <PRE>
	 * 日付期間 DateSpan を実装のリストから、期間が重複している重複日と重複した要素のリストの BiConsumer を実行する。
	 * 例）
	 * 		DateUtil.booklist(list, (k, t)->{
	 * 			// k = 重複発生日
	 * 			// t = 重複発生した日、１日に該当する要素のリスト、２個以上のリスト
	 * 		});
	 * </PRE>
	 * @param list 重複検査対象のDateSpan実装リスト
	 * @param bookConsumer
	 */
	public static void booklist(List<? extends DateSpan> list, BiConsumer<LocalDate, List<? super DateSpan>> bookConsumer){
		AtomicInteger i = new AtomicInteger(0);
		Map<LocalDate, List<? super DateSpan>> map = new HashMap<>();
		list.stream().forEach(e->{
			list.subList(i.incrementAndGet(), list.size()).stream()
			.filter(t->e.isBook(t.start(), t.end()))
			.forEach(t->{
				SimpleEntry<LocalDate, LocalDate> key = new SimpleEntry<>(e.start().compareTo(t.start()) <= 0 && t.start().compareTo(e.end()) <= 0 ? t.start() : e.start()
						, e.start().compareTo(t.end()) <= 0 && t.end().compareTo(e.end()) <= 0 ? t.end() : e.end() );
				for(LocalDate dt=key.getKey(); dt.compareTo(key.getValue()) <= 0;dt=dt.plusDays(1)){
					List<? super DateSpan> plist = map.containsKey(dt) ? map.get(dt) : new ArrayList<>();
					plist.add(e);
					plist.add(t);
					map.put(dt, plist);
				}
			});
		});
		map.keySet().stream().sorted().forEach(k->{
			bookConsumer.accept(k, map.get(k).stream().distinct().collect(Collectors.toList()));
		});
	}
}

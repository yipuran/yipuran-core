package org.yipuran.time;

import java.time.LocalDate;
import java.time.Period;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 日付期間インターフェース. AbstractMap.SimpleEntry<LocalDate, LocalDate>；開始日と終了日とする期間に対する日付処理
 */
@FunctionalInterface
public interface DateSpan extends Supplier<AbstractMap.SimpleEntry<LocalDate, LocalDate>>{
	/**
	 * 期間内か否か？.
	 * @param date 検査対象日
	 * @return true=期間内である
	 */
	public default boolean inRange(LocalDate date){
		AbstractMap.SimpleEntry<LocalDate, LocalDate> pair = get();
		return pair.getKey().compareTo(date) <= 0 && date.compareTo(pair.getValue()) <= 0;
	}

	/**
	 * 期間と重複する？.
	 * @param start 検査対象開始日
	 * @param end 検査対象終了日
	 * @return true=重複する
	 */
	public default boolean isBook(LocalDate start, LocalDate end){
		AbstractMap.SimpleEntry<LocalDate, LocalDate> pair = get();
		return start.compareTo(pair.getValue()) <= 0 && end.compareTo(pair.getKey()) >= 0;
	}

	/**
	 * 重複日数を抽出.
	 * @param start 検査対象開始日
	 * @param end 検査対象終了日
	 * @return 重複した日数
	 */
	public default long bookDays(LocalDate start, LocalDate end){
		AbstractMap.SimpleEntry<LocalDate, LocalDate> pair = get();
		if (start.compareTo(pair.getValue()) <= 0 && end.compareTo(pair.getKey()) >= 0){
			LocalDate sdate = pair.getKey().compareTo(start) <= 0 && start.compareTo(pair.getValue()) <= 0 ? start : pair.getKey();
			LocalDate edate = pair.getKey().compareTo(end) <= 0 && end.compareTo(pair.getValue()) <= 0 ? end : pair.getValue();
			return Period.between(sdate, edate).getDays() + 1;
		}
		return 0;
	}

	/**
	 * 重複期間を抽出.
	 * @param start 検査対象開始日
	 * @param end 検査対象終了日
	 * @return 重複期間の開始日と終了の AbstractMap.SimpleEntry、重複がなければ null
	 */
	public default AbstractMap.SimpleEntry<LocalDate, LocalDate> bookPair(LocalDate start, LocalDate end){
		AbstractMap.SimpleEntry<LocalDate, LocalDate> pair = get();
		if (start.compareTo(pair.getValue()) <= 0 && end.compareTo(pair.getKey()) >= 0){
			return new AbstractMap.SimpleEntry<LocalDate, LocalDate>(
					pair.getKey().compareTo(start) <= 0 && start.compareTo(pair.getValue()) <= 0 ? start : pair.getKey()
					, pair.getKey().compareTo(end) <= 0 && end.compareTo(pair.getValue()) <= 0 ? end : pair.getValue());
		}
		return null;
	}

	/**
	 * 期間リスト→重複期間リストの抽出.
	 * @param targets 検査対象になる開始日と終了日の期間ペアのリスト
	 * @return 重複期間の開始日と終了の AbstractMap.SimpleEntryのリスト、重複がなければ size = 0
	 */
	public default List<SimpleEntry<LocalDate, LocalDate>> bookPair(List<SimpleEntry<LocalDate, LocalDate>> targets){
		AbstractMap.SimpleEntry<LocalDate, LocalDate> pair = get();
		return targets.stream().filter(e->e.getKey().compareTo(pair.getValue()) <= 0 && e.getValue().compareTo(pair.getKey()) >= 0)
		.map(e->new AbstractMap.SimpleEntry<LocalDate, LocalDate>(
				pair.getKey().compareTo(e.getKey()) <= 0 && e.getKey().compareTo(pair.getValue()) <= 0 ? e.getKey() : pair.getKey()
				, pair.getKey().compareTo(e.getValue()) <= 0 && e.getValue().compareTo(pair.getValue()) <= 0 ? e.getValue() : pair.getValue()))
		.collect(Collectors.toList());
	}

	/**
	 * 非重複期間リストを抽出.
	 * @param start 検査対象開始日
	 * @param end 検査対象終了日
	 * @return 重複でない期間を List で抽出
	 */
	public default List<SimpleEntry<LocalDate, LocalDate>> unbookPairs(LocalDate start, LocalDate end){
		List<AbstractMap.SimpleEntry<LocalDate, LocalDate>> list = new ArrayList<>();
		AbstractMap.SimpleEntry<LocalDate, LocalDate> pair = get();
		if (start.compareTo(pair.getValue()) <= 0 && end.compareTo(pair.getKey()) >= 0){
			if (start.compareTo(pair.getKey()) < 0){
				list.add(new SimpleEntry<LocalDate, LocalDate>(start, pair.getKey().minusDays(1)));
			}
			if (pair.getValue().compareTo(end) < 0){
				list.add(new SimpleEntry<LocalDate, LocalDate>(pair.getValue().plusDays(1), end));
			}
		}else{
			list.add(new SimpleEntry<>(start, end));
		}
		return list;
	}

	/**
	 * 期間リスト→非重複期間リストの抽出.
	 * @param targets 検査対象になる開始日と終了日の期間ペアのリスト
	 * @return 重複でない期間のリスト
	 */
	public default List<SimpleEntry<LocalDate, LocalDate>> unbookPairs(List<SimpleEntry<LocalDate, LocalDate>> targets){
		List<AbstractMap.SimpleEntry<LocalDate, LocalDate>> list = new ArrayList<>();
		AbstractMap.SimpleEntry<LocalDate, LocalDate> pair = get();
		targets.stream().forEach(e->{
			if (e.getKey().compareTo(pair.getValue()) <= 0 && e.getValue().compareTo(pair.getKey()) >= 0){
				if (e.getKey().compareTo(pair.getKey()) < 0){
					list.add(new SimpleEntry<LocalDate, LocalDate>(e.getKey(), pair.getKey().minusDays(1)));
				}
				if (pair.getValue().compareTo(e.getValue()) < 0){
					list.add(new SimpleEntry<LocalDate, LocalDate>(pair.getValue().plusDays(1), e.getValue()));
				}
			}else{
				list.add(new SimpleEntry<>(e.getKey(), e.getValue()));
			}
		});
		return list;
	}
	/**
	 * @return 期間開始日
	 */
	public default LocalDate start(){
		return get().getKey();
	}
	/**
	 * @return 期間終了日
	 */
	public default LocalDate end(){
		return get().getValue();
	}
}

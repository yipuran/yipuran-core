package org.yipuran.util.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
/**
 * 重複、ユニーク、終端操作Collectorの提供.
 * <PRE>
 * equals 、hashCode を実装しないクラスの Stream の終端操作としては処理できない。
 * </PRE>
 */
@FunctionalInterface
public interface CollectorHash<T> extends Serializable{
	/**
	 * 重複判定フィルタ
	 * @param t 要素
	 * @return true=重複
	 */
	boolean test(T t);

	/**
	 * 重複存在チェック終端操作
	 * @return boolean を返すCollector、true=重複あり
	 */
	public static <T> Collector<T,?,Boolean> duplicatedElements(){
		Set<T> set = new HashSet<>();
		return Collectors.reducing(true, set::add, Boolean::logicalXor);
	}
	/**
	 * 重複なしチェック終端操作
	 * @return boolean を返すCollector、true=重複なし
	 */
	public static <T> Collector<T,?,Boolean> uniqueElements(){
		Set<T> set = new HashSet<>();
		return Collectors.reducing(true, t->{ return set.add(t);}, Boolean::logicalAnd);
	}
	/**
	 * 重複リスト抽出終端操作
	 * @return 重複リストを返すCollector
	 */
	public static <T> Collector<T, ?, List<T>> duplicatedList(){
		Map<T, Integer> map = new HashMap<>();
		return Collectors.reducing(new ArrayList<T>(),
		t->{
			map.put(t, Optional.ofNullable(map.get(t)).map(i->i+1).orElse(1));
			return Arrays.asList(t);
		},(n1, n2)->{
			return map.entrySet().stream().filter(e->e.getValue() > 1)
				.map(e->e.getKey())
				.collect(Collectors.toList());
		});
	}
	/**
	 * 条件付き重複リスト抽出終端操作
	 * @param plusCheck 条件として対象要素１つを引数に trueを返す場合は重複にするラムダを指定する。
	 * @return 重複リストを返すCollector
	 */
	public static <T> Collector<T, ?, List<T>> duplicatedList(CollectorHash<T> plusCheck){
		Map<T, Integer> map = new HashMap<>();
		return Collectors.reducing(new ArrayList<T>(),
		t->{
			map.put(t, Optional.ofNullable(map.get(t)).map(i->plusCheck.test(t) ? i + 1 : i).orElse(1));
			return Arrays.asList(t);
		},(n1, n2)->{
			return map.entrySet().stream().filter(e->e.getValue() > 1)
				.map(e->e.getKey())
				.collect(Collectors.toList());
		});
	}
	/**
	 * 重複個数を条件とした重複リスト抽出終端操作
	 * @param count 個数を指定して、個数を超えた重複を対象にする。
	 * @return 重複リストを返すCollector
	 */
	public static <T> Collector<T, ?, List<T>> duplicateCountOverList(int count){
		Map<T, Integer> map = new HashMap<>();
		return Collectors.reducing(new ArrayList<T>(),
		t->{
			map.put(t, Optional.ofNullable(map.get(t)).map(i->i+1).orElse(1));
			return Arrays.asList(t);
		},(n1, n2)->{
			return map.entrySet().stream().filter(e->e.getValue() > count)
				.map(e->e.getKey())
				.collect(Collectors.toList());
		});
	}
	/**
	 * 重複なしのリスト抽出終端操作
	 * @return 重複ないリストを返すCollector
	 */
	public static <T> Collector<T, ?, List<T>> uniquedList(){
		Map<T, Integer> map = new HashMap<>();
		return Collectors.reducing(new ArrayList<T>(),
		t->{
			map.put(t, Optional.ofNullable(map.get(t)).map(i->i+1).orElse(1));
			return Arrays.asList(t);
		},(n1, n2)->{
			return map.entrySet().stream().filter(e->e.getValue() == 1)
				.map(e->e.getKey())
				.collect(Collectors.toList());
		});
	}
}

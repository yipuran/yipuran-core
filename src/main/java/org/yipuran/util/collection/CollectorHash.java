package org.yipuran.util.collection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
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
	public static <T> Collector<T,Set<T>,Boolean> uniqueElements(){
		return Collector.of(HashSet::new, Set::add,
			(a1, a2)->{
				a1.addAll(a2);
				return a1;
			},
			a->a.size()==1
		);
	}
	/**
	 * 集約数を返す終端操作.
	 * HashSet に格納チェックすることで集約数を求める Collectir を返す
	 * @return 集約数Integerを返すCollector
	 */
	public static <T> Collector<T, Set<T>, Integer> hashsetsize(){
		return Collector.of(HashSet::new, Set::add,
			(a1, a2)->{
				a1.addAll(a2);
				return a1;
			},
			a->a.size()
		);
	}
	/**
	 * 重複リスト抽出終端操作.
	 * @return 重複のみのリストを返すCollector
	 */
	public static <T> Collector<T, Map<T, Integer>, List<T>> duplicatedList(){
		return Collector.of(HashMap::new,
			(r, t)->r.put(t, Optional.ofNullable(r.get(t)).map(i->i+1).orElse(1)),
			(a1, a2)->{
				a1.entrySet().stream().forEach(e->{
					a2.put(e.getKey(), Optional.ofNullable(a2.get(e.getKey())).map(i->i+e.getValue()).orElse(e.getValue()));
				});
				return a2;
			},
			a->a.entrySet().stream().filter(e->e.getValue() > 1)
						.map(e->e.getKey())
						.collect(Collectors.toList()),
			Characteristics.CONCURRENT
		);
	}
	/**
	 * 重複リスト抽出終端操作（重複数指定）.
	 * @param n 重複数
	 * @return 重複のみのリストを返すCollecto
	 */
	public static <T> Collector<T, Map<T, Integer>, List<T>> duplicatedList(long n){
		return Collector.of(HashMap::new,
			(r, t)->r.put(t, Optional.ofNullable(r.get(t)).map(i->i+1).orElse(1)),
			(a1, a2)->{
				a1.entrySet().stream().forEach(e->{
					a2.put(e.getKey(), Optional.ofNullable(a2.get(e.getKey())).map(i->i+e.getValue()).orElse(e.getValue()));
				});
				return a2;
			},
			a->a.entrySet().stream().filter(e->e.getValue()==n)
						.map(e->e.getKey())
						.collect(Collectors.toList()),
			Characteristics.CONCURRENT
		);
	}
	/**
	 * 条件付き重複リスト抽出終端操作
	 * @param plusCheck 条件として対象要素１つを引数に trueを返す場合は重複にするラムダを指定する。
	 * @return 重複リストを返すCollector
	 */
	public static <T> Collector<T, Map<T, Integer>, List<T>> duplicatedList(CollectorHash<T> plusCheck){
		return Collector.of(HashMap::new,
				(r, t)->r.put(t, Optional.ofNullable(r.get(t)).map(i->plusCheck.test(t) ? i + 1 : i).orElse(1)),
				(a1, a2)->{
					a1.entrySet().stream().forEach(e->{
						a2.put(e.getKey(), Optional.ofNullable(a2.get(e.getKey()))
									.map(i->i+e.getValue()).orElse(e.getValue()));
					});
					return a2;
				},
				a->a.entrySet().stream().filter(e->e.getValue() > 1)
							.map(e->e.getKey())
							.collect(Collectors.toList()),
				Characteristics.CONCURRENT
			);
	}
	/**
	 * 重複個数を条件とした重複リスト抽出終端操作
	 * @param count 個数を指定して、個数を超えた重複を対象にする。
	 * @return 重複リストを返すCollector
	 */
	public static <T> Collector<T, Map<T, Integer>, List<T>> duplicateCountOverList(int count){
		return Collector.of(HashMap::new,
			(r, t)->r.put(t, Optional.ofNullable(r.get(t)).map(i->i+1).orElse(1)),
			(a1, a2)->{
				a1.entrySet().stream().forEach(e->{
					a2.put(e.getKey(), Optional.ofNullable(a2.get(e.getKey()))
								.map(i->i+e.getValue()).orElse(e.getValue()));
				});
				return a2;
			},
			a->a.entrySet().stream().filter(e->e.getValue() > count)
						.map(e->e.getKey())
						.collect(Collectors.toList()),
			Characteristics.CONCURRENT
		);
	}
	/**
	 * 重複なしのリスト抽出終端操作
	 * @return 重複ないリストを返すCollector
	 */
	public static <T> Collector<T, Map<T, Integer>, List<T>> uniquedList(){
		return Collector.of(HashMap::new,
			(r, t)->r.put(t, Optional.ofNullable(r.get(t)).map(i->i+1).orElse(1)),
			(a1, a2)->{
				a1.entrySet().stream().forEach(e->{
					a2.put(e.getKey(), Optional.ofNullable(a2.get(e.getKey()))
								.map(i->i+e.getValue()).orElse(e.getValue()));
				});
				return a2;
			},
			a->a.entrySet().stream().filter(e->e.getValue()==1)
						.map(e->e.getKey())
						.collect(Collectors.toList()),
			Characteristics.CONCURRENT
		);
	}
}

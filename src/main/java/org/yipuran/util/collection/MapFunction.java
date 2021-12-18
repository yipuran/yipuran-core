package org.yipuran.util.collection;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Map 未登録キーの場合にSupplierで提供する値を約束して値を取得する.
 * <PRE>
 * MapFunction＜String, List＜String＞＞ f = MapFunction.of(map, ArrayList::new);
 * f.apply("A").add("1");
 * </PRE>
 * @param <K>
 * @param <V>
 */
@FunctionalInterface
public interface MapFunction<K, V>{
	V apply(K k);

	/**
	 * Supplier 指定で、MapFunction インスタンスを生成
	 * @param map 対象Map
	 * @param s VのSupplier
	 * @return MapFunction
	 */
	static <K, V> MapFunction<K, V> of(Map<K, V> map, Supplier<V> s){
		return k->{
			if (!map.containsKey(k)) {
				map.put(k, s.get());
			}
			return map.get(k);
		};
	}
}

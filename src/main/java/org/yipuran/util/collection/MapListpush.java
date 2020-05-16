package org.yipuran.util.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * Map＜K, List＜V＞＞ key存在自動チェック登録.
 * <PRE>
 *
 * accept(key, value); でkey存在自動チェックしてvalueをList初期生成処理を記述することなく
 * List のマップを生成する。
 *
 * Map&lt;String, List&lt;String>> map = new HashMap&lt;>();
 * MapListpush<String, String> m = MapListpush.of(map);
 * m.accept("a", "A");
 * m.accept("a", "B");
 * </PRE>
 * @since 4.17
 */
@FunctionalInterface
public interface MapListpush<K, V> extends Serializable{

	void accept(K k, V v);

	/**
	 * MapListpush 生成
	 * @param m リストのマップ
	 * @return MapListpush
	 */
	public static <K, V> MapListpush<K, V> of(Map<K, List<V>> m){
		return (k, v)->{
			m.put(k, Optional.ofNullable(m.get(k))
						.map(e->listAddReturn(v).apply(e))
						.orElseGet(()->new ArrayList<V>(Arrays.asList(v))));
		};
	}

	static <T> UnaryOperator<List<T>> listAddReturn(T t){
		return l->{
			l.add(t);
			return l;
		};
	}
}

package org.yipuran.util.collection;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Map＜K, Set＜V＞＞ key存在自動チェック登録.
 * <PRE>
 *
 * accept(key, value); でkey存在自動チェックしてvalueをSet初期生成処理を記述することなく
 * Set のマップを生成する。
 *
 * Map&lt;String, Set&lt;String>> map = new HashMap<String, Set&lt;String>>();
 * MapSetpush&lt;String, String> m = MapSetpush.of(map, TreeSet&lt;String>::new);
 * m.accept("a", "A");
 * m.accept("a", "B");
 * m.accept("a", "A");
 * </PRE>
 * @since 4.17
 */
@FunctionalInterface
public interface MapSetpush<K, V> extends Serializable{

	void accept(K k, V v);

	public static <K, V> MapSetpush<K, V> of(Map<K, Set<V>> m){
		return (k, v)->{
			m.put(k, Optional.ofNullable(m.get(k))
						.map(e->setAddReturn(v).apply(e))
						.orElseGet(()->new HashSet<V>(Arrays.asList(v))));
		};
	}

	public static <K, V> MapSetpush<K, V> of(Map<K, Set<V>> m, Supplier<Set> newsupplier){
		return (k, v)->{
			m.put(k, Optional.ofNullable(m.get(k))
						.map(e->setAddReturn(v).apply(e))
						.orElse(new Function<V, Set<V>>(){
							@Override
							public Set<V> apply(V u){
								Set<V> s = newsupplier.get();
								s.add(u);
								return s;
							}
						}.apply(v))
			);
		};
	}

	static <T> UnaryOperator<Set<T>> setAddReturn(T t){
		return s->{
			s.add(t);
			return s;
		};
	}
}

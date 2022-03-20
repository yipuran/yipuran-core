package org.yipuran.util.collection;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Map＜K, V[]＞ key存在自動チェック登録.
 * <PRE>
 *
 * accept(key, value); でkey存在自動チェックしてvalueを配列生成処理を記述することなく
 * 配列のマップを生成する。
 *
 * Map<String, Integer[]> map = new HashMap<>();
 * MapArraypush<String, Integer> m = MapArraypush.of(map, 3);
 * m.accept("A", 1);
 * m.accept("A", 2);
 * m.accept("A", 3);
 * m.accept("A", 4);  // 配列長を超える場合は無視される。
 * </PRE>
 * @since 4.17
 */
@FunctionalInterface
public interface MapArraypush<K, V> extends Serializable{

	void accept(K k, V v);

	/**
	 * MapArraypush 生成
	 * @param m 配列のマップ
	 * @param length 配列最大長
	 * @return
	 */
	public static <K, V> MapArraypush<K, V> of(Map<K, V[]> m, int length){
		return (k, v)->{
			m.put(k, Optional.ofNullable(m.get(k))
						.map(e->arrayAddReturn(v).apply(e))
						.orElse(new Function<V, V[]>(){
							@SuppressWarnings("unchecked")
							@Override
							public V[] apply(V u){
								V[] ary = (V[])Array.newInstance(u.getClass(), length);
								ary[0] = u;
								return ary;
							}
						}.apply(v)));
		};
	}

	static <T> UnaryOperator<T[]> arrayAddReturn(T t){
		return a->{
			for(int i=0;i < a.length;i++){
				if (a[i]==null) {
					a[i] = t;
					break;
				}
			}
			return a;
		};
	}
}

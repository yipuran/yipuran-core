package org.yipuran.util.collection;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * CollectUtil.
 * <PRE>
 * unique() の使用サンプル
 *
 * if (Arrays.asList("A", "B", "C").stream().collect(CollectUtil.unique())){
 *    // 重複なし
 * }
 *
 * </PRE>
 * @since 4.12
 */
public final class CollectUtil{

	private CollectUtil(){
	}
	/**
	 * 重複なしの判定をする Collector を返す。
	 * @return  Collector<T, ?, Boolean> 全てユニークである場合、true を返す Collector
	 */
	public static<T> Collector<T, ?, Boolean> unique(){
	    Set<T> set = new HashSet<>();
	    return Collectors.reducing(true, set::add, Boolean::logicalAnd);
	}
	/**
	 * リストが全て同じ→ true.
	 * <PRE>
	 * equal メソッドで判定される。
	 * </PRE>
	 * @param list
	 * @return
	 */
	public static <T> boolean allMatch(List<T> list) {
		for(ListIterator<T> it = list.listIterator(); it.hasNext();){
			T t = it.next();
			if (it.hasPrevious()){
				T u = it.previous();
				if (it.hasPrevious()){
					u = it.previous();
					if (!u.equals(t)) return false;
					it.next();
				}
				it.next();
			}
		}
		return true;
	}
	/**
	 * 判定のBiPredicate指定でリストが全て同じ→ true.
	 * @param list
	 * @param p
	 * @return
	 */
	public static <T> boolean allMatch(List<T> list, BiPredicate<T, T> p) {
		for(ListIterator<T> it = list.listIterator(); it.hasNext();){
			T t = it.next();
			if (it.hasPrevious()){
				T u = it.previous();
				if (it.hasPrevious()){
					u = it.previous();
					if (!p.test(u, t)) return false;
					it.next();
				}
				it.next();
			}
		}
		return true;
	}
	/**
	 * 配列が全て同じ→ true.
	 * <PRE>
	 * equal メソッドで判定される。
	 * </PRE>
	 * @param ary
	 * @return
	 */
	public static <T> boolean allMatch(T[] ary) {
		if (ary.length < 2) return true;
		for(int i=0, j=1; j < ary.length; i++, j++) {
			if (!ary[i].equals(ary[j])) return false;
		}
		return true;
	}
	/**
	 * 判定のBiPredicate指定で配列が全て同じ→ true.
	 * @param ary
	 * @param p
	 * @return
	 */
	public static <T> boolean allMatch(T[] ary, BiPredicate<T, T> p) {
		if (ary.length < 2) return true;
		for(int i=0, j=1; j < ary.length; i++, j++) {
			if (!p.test(ary[i], ary[j])) return false;
		}
		return true;
	}
}

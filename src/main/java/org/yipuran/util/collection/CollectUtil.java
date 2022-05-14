package org.yipuran.util.collection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
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
	/**
	 * Listからカレント要素と前方要素の BiConsumer実行.
	 * <pre>
	 * リストを走査して BiConsumer＜T, T＞ カレントと前方を実行する。
	 * 先頭は、前方が null で実行される。
	 * </pre>
	 * @param list 走査対象List
	 * @param consumer BiConsumer １番目＝カレント要素、２番目＝前方の要素
	 * @since 4.32
	 */
	public static <T> void eachPrevious(List<T> list, BiConsumer<T, T> consumer){
		for(ListIterator<T> it = list.listIterator(); it.hasNext();) {
			T pre = null;
			if (it.hasPrevious()) {
				pre = it.previous();
				it.next();
			}
			consumer.accept(it.next(), pre);
		}
	}
	/**
	 * Listからカレント要素と前方要素の BiConsumer実行（先頭はConsumer）.
	 * @param list 走査対象List
	 * @param first 先頭だけ実行するConsumer
	 * @param consumer BiConsumer １番目＝カレント要素、２番目＝前方の要素
	 * @since 4.32
	 */
	public static <T> void eachPrevious(List<T> list, Consumer<T> first, BiConsumer<T, T> consumer){
		ListIterator<T> it = list.listIterator();
		first.accept(it.next());
		while(it.hasNext()) {
			T pre = it.previous();
			it.next();
			consumer.accept(it.next(), pre);
		}
	}
	/**
	 * Collection リレー Funtion 結果を求める。
	 * <pre>
	 * コレクションの用意で Funtion を返す。コレクション２番目以降は、前方のコレクション要素による結果と
	 * 合わせた BiFuntion で結果を求める
	 * </pre>
	 * @param collections Funtion実行対象のリスト等のコレクション
	 * @param first 先頭 Function
	 * @param func 2番目以降のBiFunction、コレクション要素と前方のFunction または、BiFunctionの結果で、結果を返す。
	 * @return Funtion実行結果、collectionsサイズ＝０は、null を返す。
	 * @since 4.33
	 */
	public static <T, R> R relayFunction(Collection<T> collections, Function<T, R> first, BiFunction<T, R, R> func){
		Iterator<T> it = collections.iterator();
		R r = it.hasNext() ? first.apply(it.next()) : null;
		while(it.hasNext()) {
			r = func.apply(it.next(), r);
		}
		return r;
	}
}

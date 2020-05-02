package org.yipuran.util.collection;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Refrain. 蓄積されたコレクション、マップから親子関係を辿るクラス
 * <PRE>
 * コレクションを対象とするかマップを対象とするか選択したインスタンス生成から親を求める callメソッド、順序リストを求める listメソッドを実行する。
 * 【前提条件】
 * ・対象のコレクションは、２つの要素から親子関係を判定するための BiPredicate を定義できること。
 * ・対象のマップは、key を親として参照できて、value から要素の親を指す key を求める Function を定義できること。
 * ・親子関係が永久ループにならず収束すること。
 *
 * 【コレクション記述例】
 * List&lt;Item&gt; list;
 * //
 * Refrain&lt;Item&gt; refrain = Refrain.of(list);
 * Optional&lt;Item&gt; root = refrain.call(item, (a, b)-&gt;b.parent != null && a.id==b.parent, e-&gt;e==null || e.parent==null);
 * List&lt;Item&gt; searchedlist = refrain.list(item, (a, b)-&gt;b.parent != null && a.id==b.parent, e-&gt;e==null || e.parent==null);
 * String path = searchedlist.stream().map(e-&gt;e.name).collect(Collectors.joining("/"));
 *
 * 【マップ記述例】
 * Map&lt;String, Item&gt; map;
 * //
 * Refrain&lt;Item&gt; refrain = Refrain.of(map);
 * Optional&lt;Entry&lt;String, Item&gt;&gt; entry = refrainmap.call("d", e-&gt;e.parent);
 * entry.ifPresentOrElse(e-&gt;{
 *    System.out.println("key = " + e.getKey() +"  name : " + e.getValue().getName() );
 * }, ()-&gt;{
 *    System.out.println("not Found");
 * });
 * List&lt;Entry&lt;String, Item&gt;&gt; searchedlist = refrain.list("d", e-&gt;e.parent);
 * searchedlist.stream().forEach(e-&gt;{
 *    System.out.println("key = " + e.getKey() +"  value : " + e.getValue().getName() );
 * });
 * String path = searchedlist.stream().map(e-&gt;e.getValue().getName()).collect(Collectors.joining("/"));
 *
 * </PRE>
 */
public class Refrain<T>{
	private List<T> list;
	private Map<?, T> map;
	private Refrain(List<T> list){
		this.list = list;
	}
	private Refrain(Map<?, T> map){
		this.map = map;
	}
	/**
	 * Collection&lt;T&gt; の Refrain取得
	 * @param collection T のコレクション、探索対象の List や Set を渡す
	 * @return Refrain&lt;T&gt;
	 */
	public static <T> Refrain<T> of(Collection<T> collection){
		return new Refrain<>(collection.stream().collect(Collectors.toList()));
	}
	/**
	 * Map&lt;K, T&gt; の Refrain取得
	 * @param map 探索対象である T のマップ
	 * @return Refrain&lt;T&gt;
	 */
	public static <K, T> Refrain<T> of(Map<K, T> map){
		return new Refrain<>(map);
	}

	/**
	 * コレクションから 可能な限りまたは、root まで辿った探索をして親を求める。返却値は、Optional
	 * @param t 検索開始の T オブジェクト
	 * @param f 検索において、参照する要素が走査の親であるか判定する BiPredicate ,
	 *  第１引数が走査中の T オブジェクトで、第２引数が、このメソッドの第１引数で「子」と判定する為のオブジェクト
	 * @param p root まで辿れたか、再帰探索を停止する為に判定する Predicate
	 * @return 探索結果 を Optional&lt;T&gt; で返却
	 */
	public Optional<T> call(T t, BiPredicate<T, T> f, Predicate<T> p){
		T r = list.stream().filter(e->f.test(e, t)).findAny().orElse(null);
		if (p.test(r)) return Optional.ofNullable(r);
		return call(r, f, p);
	}
	/**
	 * コレクションから 可能な限りまたは、root まで辿った root からの順序リスト返す.
	 * @param t 検索開始の T オブジェクト
	 * @param f 検索において、参照する要素が走査の親であるか判定する BiPredicate ,
	 *  第１引数が走査中の T オブジェクトで、第２引数が、このメソッドの第１引数で「子」と判定する為のオブジェクト
	 * @param p root まで辿れたか、再帰探索を停止する為に判定する Predicate
	 * @return root からの順序リスト返す、親が存在しないまたは、辿れない場合は探索を中断してその時点の走査結果リストを返す。
	 * このメソッド呼び出し時の検索開始の T オブジェクトの親が存在しなければ、検索開始の T オブジェクト１つだけのリストを返す。
	 */
	public List<T> list(T t, BiPredicate<T, T> f, Predicate<T> p){
		List<T> slist = new ArrayList<>();
		if (t==null) return slist;
		slist.add(t);
		return listing(()->slist, t, f, p);
	}
	private List<T> listing(Supplier<List<T>> s, T t, BiPredicate<T, T> f, Predicate<T> p){
		if (p.test(t)) return s.get();
		T tt = list.stream().filter(e->f.test(e, t)).findAny().orElse(null);
		return listing(()->{
			List<T> l = s.get();
			Optional.ofNullable(tt).ifPresent(e->l.add(0, e));
			return l;
		}, tt, f, p);
	}

	/**
	 * マップから可能な限りまたは、root まで辿った親を求める。返却値は、Optional.
	 * 【前提条件】マップの前提条件、親子関係の成立は親を示す Key がマップのキーで、Map の value から
	 * 親を示す Keyを参照するための Function を指定できること。
	 * @param k 検索開始の T オブジェクトを格納している Key オブジェクト
	 * @param f マップの value から親を示す Keyを取得する Function&lt;T, K&gt;
	 * @return 探索結果 を Optional&lt;T&gt; で返却、このメソッドに渡す keyオブジェクト(K)で最初から Mapに存在しなければ。Optional.empty()が返ってくる
	 */
	public <K> Optional<Entry<K, T>> call(K k, Function<T, K> f){
		if (!map.containsKey(k)) return Optional.empty();
		K n = f.apply(map.get(k));
		if (map.containsKey(n)) return call(n, f);
		return Optional.ofNullable(new SimpleEntry<K, T>(k, map.get(k)));
	}

	/**
	 * マップから可能な限りまたは、root まで辿ったroot からの順序リスト返す
	 * 【前提条件】マップの前提条件、親子関係の成立は親を示す Key がマップのキーで、Map の value から
	 * 親を示す Keyを参照するための Function を指定できること。
	 * @param k 検索開始の T オブジェクトを格納している Key オブジェクト
	 * @param f マップの value から親を示す Keyを取得する Function&lt;T, K&gt;
	 * @return root からの順序リスト返す、親が存在しないまたは、辿れない場合は探索を中断してその時点の走査結果リストを返す。
	 * このメソッド呼び出し時の検索開始の k で指定する Key がマップに存在しなければ、空リストを返す。
	 */
	public <K> List<Entry<K, T>> list(K k, Function<T, K> f){
		return listing(()->new ArrayList<>(), k, f);
	}
	private <K> List<Entry<K, T>> listing(Supplier<List<Entry<K, T>>> s, K k, Function<T, K> f){
		List<Entry<K, T>> l = s.get();
		if (map.get(k)==null) return l;
		l.add(0, new SimpleEntry<K, T>(k, map.get(k)));
		K n = f.apply(map.get(k));
		if (map.containsKey(n)) return listing(()->l, n, f);
		return l;
	}
}

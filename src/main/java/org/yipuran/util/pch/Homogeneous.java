package org.yipuran.util.pch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * nHr (Homogeneous) 重複あり組み合わせ 算出.
 * <PRE>
 * org.yipuran.util.Combinations が、要素の重複を許さない組み合わせであるのに対して、組み合わせる要素の重複を許可した組みあわせ算出を行う。
 *
 * （使用例）
 * Homogeneous&lt;String&gt; h = Homogeneous.of(list);
 * h.compute(3).stream().map(e->e.stream().collect(Collectors.joining(""))).forEach(System.out::println);
 *
 * Spliterator&lt;List&lt;String&gt;&gt; spliterator = Spliterators.spliteratorUnknownSize(h.iterator(3), 0);
 * Stream&lt;List&lt;String&gt;&gt; stream = StreamSupport.stream(spliterator, false);
 * stream.map(e->e.stream().collect(Collectors.joining(""))).forEach(System.out::println);
 * </PRE>
 * @since 4.11
 */
public class Homogeneous<T>{
	private List<T> list;
	private Homogeneous(List<T> list){
		this.list = list;
	}
	/**
	 * List→インスタンス生成.
	 * @param list List&lt;T&gt;生成前のリスト
	 * @return Homogeneous
	 */
	public static <T> Homogeneous<T> of(List<T> list){
		return new Homogeneous<T>(list);
	}
	/**
	 * 配列→インスタンス生成.
	 * @param larray T[] 生成前の配列
	 * @return Homogeneous
	 */
	public static <T> Homogeneous<T> of(T[] array){
		return new Homogeneous<T>(Arrays.asList(array));
	}
	/**
	 * 重複あり組み合わせ結果リスト抽出.
	 * @param len 組み合わせ数 nHr の r
	 * @return  List&lt;List&lt;T&gt;&gt;
	 */
	public List<List<T>> compute(int len){
		List<List<T>> combinations = new ArrayList<>();
		int n = list.size();
		int[] d = new int[n > len ? n+1 : len+1];
		for(int i=1; i <= n; i++) d[i] = 1;
		while(d[0] <= 0){
			if (d[d.length-1] != 0){
				List<T> lt = Arrays.stream(d).boxed().filter(e->e > 0).map(e->list.get(e-1)).limit(len).collect(Collectors.toList());
				if (lt.size()==len){
					combinations.add(lt);
				}
			}
			for(int j=len; 0 <= j; j--){
				d[j]++;
				for(int k=j+1; k <= len; k++) {
					d[k] = d[k-1];
				}
				if (d[j] <= n) break;
			}
		}
		return combinations;
	}
	/**
	 * Predicate指定結果Consumer実行
	 * @param len 組み合わせ数 nHr の r
	 * @param predivate 組み合わせ結果よりConsumer実行を判定 true=実行する
	 * @param consumer
	 */
	public void matchExecute(int len, Predicate<List<T>> predivate, Consumer<List<T>> consumer){
		int n = list.size();
		int[] d = new int[n > len ? n+1 : len+1];
		for(int i=1; i <= n; i++) d[i] = 1;
		while(d[0] <= 0){
			if (d[d.length-1] != 0){
				List<T> lt = Arrays.stream(d).boxed().filter(e->e > 0).map(e->list.get(e-1)).limit(len).collect(Collectors.toList());
				if (lt.size()==len){
					if (predivate.test(lt)) consumer.accept(lt);
				}
			}
			for(int j=len; 0 <= j; j--){
				d[j]++;
				for(int k=j+1; k <= len; k++) {
					d[k] = d[k-1];
				}
				if (d[j] <= n) break;
			}
		}
	}
	/**
	 * Predicate 最初に見つかる結果の取得
	 * @param len 組み合わせ数 nHr の r
	 * @param predivate 条件
	 * @return
	 */
	public List<T> firstMatch(int len, Predicate<List<T>> predivate){
		int n = list.size();
		int[] d = new int[n > len ? n+1 : len+1];
		for(int i=1; i <= n; i++) d[i] = 1;
		while(d[0] <= 0){
			if (d[d.length-1] != 0){
				List<T> lt = Arrays.stream(d).boxed().filter(e->e > 0).map(e->list.get(e-1)).limit(len).collect(Collectors.toList());
				if (lt.size()==len){
					if (predivate.test(lt)) return lt;
				}
			}
			for(int j=len; 0 <= j; j--){
				d[j]++;
				for(int k=j+1; k <= len; k++) {
					d[k] = d[k-1];
				}
				if (d[j] <= n) break;
			}
		}
		return null;
	}

	/**
	 * 重複あり組み合わせ結果List のイテレータを返す
	 * <PRE>
	 * Iterator を使用した例、
	 *
	 * Homogeneous&lt;String> homogeneous = new Homogeneous.of(Arrays.asList("A","B","C","D"));
	 *
	 * Spliterator&lt;List&lt;String>> spliterator = Spliterators.spliteratorUnknownSize(homogeneous.iterator(3), 0);
	 * Stream&lt;List&lt;String>> stream = StreamSupport.stream(spliterator, false);
	 * stream.map(e->e.stream().collect(Collectors.joining(""))).forEach(System.out::println);
	 * </PRE>
	 * @param len 組み合わせ数 nHr の r
	 * @return Iterator
	 */
	public Iterator<List<T>> iterator(int len){
		return this.compute(len).iterator();
	}
}

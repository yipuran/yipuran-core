package org.yipuran.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 順列組合せ nCr 算出.
 * <PRE>
 * インスタンス生成後、結果List&lt;List&lt;T&gt;&gt;を取得、または Iterator&lt;List&lt;T&gt;&gt;を iterator()で取得する。
 * 要素の重複はしない。
 * （使用例）
 * Combinations&lt;String&gt; c = Combinations.of(Arrays.asList("A", "B", "C", "D"));
 * c.compute(3).stream().map(e->e.stream().collect(Collectors.joining(""))).forEach(System.out::println);
 *
 * Spliterator&lt;List&lt;String&gt;&gt; spliterator = Spliterators.spliteratorUnknownSize(c.iterator(3), 0);
 * Stream&lt;List&lt;String&gt;&gt; stream = StreamSupport.stream(spliterator, false);
 * stream.map(e->e.stream().collect(Collectors.joining(""))).forEach(System.out::println);
 * </PRE>
 */
public class Combinations<T> {
	private List<List<T>> combinations;
	private List<T> list;
	private int[] index;
	private boolean[] visited;
	private int r;
	private boolean overHalf;

	/**
	 * インスタンス生成.
	 * @param list List&lt;T&gt;生成前のリスト
	 * @param r 組み合わせ数 nCr の r
	 */
	public static <T> Combinations<T> of(List<T> list){
		return new Combinations<T>(list);
	}

	/**
	 * インスタンス生成.
	 * @param array T[] 生成前の配列
	 * @param r 組み合わせ数 nCr の r
	 */
	public static <T> Combinations<T> of(T[] array){
		return new Combinations<T>(Arrays.asList(array));
	}

	private Combinations(List<T> list){
		if (list.size() < 1){
			throw new IllegalArgumentException();
		}
		this.list = list;
	}
	/**
	 * 組み合わせ結果リスト抽出.
	 * @param len nCr の r
	 * @return List&lt;List&lt;T&gt;&gt;
	 */
	public List<List<T>> compute(int len){
		if (len < 1 || list.size() < len){
			throw new IllegalArgumentException();
		}
		this.combinations = new ArrayList<List<T>>();
		this.r = len;
		if (this.r==list.size()){
			this.combinations.add(list);
		}else{
			if (this.r > list.size() / 2){
				this.r = list.size() - this.r;
				this.overHalf = true;
			}
			this.index = new int[this.r];
			this.visited = new boolean[list.size()];
			this._compute(list, 0);
		}

		return this.combinations;
	}
	/**
	 * イテレータ取得
	 * @param len nCr の r
	 * @return List&lt;T&gt;を返すイテレータ
	 */
	public Iterator<List<T>> iterator(int len){
		if (len < 1 || list.size() < len){
			throw new IllegalArgumentException();
		}
		this.combinations = new ArrayList<List<T>>();

		this.r = len;
		if (this.r==list.size()){
			this.combinations.add(list);
		}else{
			if (this.r > list.size() / 2){
				this.r = list.size() - this.r;
				this.overHalf = true;
			}
			this.index = new int[this.r];
			this.visited = new boolean[list.size()];
			this._compute(list, 0);
		}
		return this.combinations.iterator();
	}

	private void _compute(List<T> list, int n){
		if (n==this.r){
			List<T> combination = new ArrayList<T>();
			if (overHalf){
				for(int i=0;i < list.size();i++){
					boolean skip = false;
					for(int j=0;j < this.index.length;j++){
						if (i== this.index[j]){
							skip = true;
						}
					}
					if (skip){
						continue;
					}
					combination.add(list.get(i));
				}
			}else{
				for(int i=0;i < this.index.length;i++){
					combination.add(list.get(index[i]));
				}
			}
			this.combinations.add(combination);
		}else{
			for(int i=0;i < list.size();i++){
				if (n==0 || !this.visited[i] && index[n - 1] < i){
					this.visited[i] = true;
					this.index[n] = i;
					this._compute(list, n + 1);
					this.visited[i] = false;
				}
			}
		}
	}
}

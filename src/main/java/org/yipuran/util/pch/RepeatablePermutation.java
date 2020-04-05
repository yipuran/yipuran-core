package org.yipuran.util.pch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
/**
 * 重複順列.
 * <PRE>
 * 要素の重複有りの順列（Permutation）
 *
 * 使用例
 * RepeatablePermutation<Integer> r = RepeatablePermutation.of(ilist);
 * r.compute(2).stream().map(e->e.stream().map(i->i.toString()).collect(Collectors.joining(""))).forEach(System.out::println);
 *
 * RepeatablePermutation<String> rs = RepeatablePermutation.of(Arrays.asList("A", "B", "C"));
 * rs.compute(3).stream().map(e->e.stream().collect(Collectors.joining(""))).forEach(System.out::println);
 * </PRE>
 *
 * @since Ver 4.13
 */
public class RepeatablePermutation<T>{
	private int r, u;
	private int[] c;
	private int[] p;
	private List<List<T>> result;
	private List<T> list;

	private RepeatablePermutation(List<T> list){
		this.list = list;
		c = new int[list.size()];
		p = new int[list.size()];
		for(int i=0; i < c.length; i++) c[i] = i;
	}
	/**
	 * List→インスタンス生成.
	 * @param list List&lt;T&gt;生成前のリスト
	 * @return RepeatablePermutation
	 */
	public static <T> RepeatablePermutation<T> of(List<T> list){
		return new RepeatablePermutation<>(list);
	}
	/**
	 * 配列→インスタンス生成.
	 * @param larray T[] 生成前の配列
	 * @return RepeatablePermutation
	 */
	public static <T> RepeatablePermutation<T> of(T[] ary){
		return new RepeatablePermutation<>(Arrays.asList(ary));
	}
	/**
	 * 重複あり順列結果リスト抽出.
	 * @param len 順列 nPr の r
	 * @return  List&lt;List&lt;T&gt;&gt;
	 */
	public List<List<T>> compute(int len){
		if (len > list.size()) throw new IllegalArgumentException("list size over");
		result = new ArrayList<>();
		r = len;
		u = c[c.length-1] - len;
		for(int i=0; i <= c.length; i++)
			calc(0, i);
		return result;
	}
	private void calc(int i, int j){
		p[i] = j;
		if (i == r - 1){
			List<T> t = new ArrayList<>();
			for(int k=0; k < r; k++){
				if (p[k] < c.length)
					t.add(list.get(c[p[k]]));
			}
			if (t.size()==r)
				result.add(t);
		}
		if (i < r - 1){
			int k=0;
			for(; k <= r;k++)
				calc(i+1, k);
			if (u > 0){
				for(int n=0; n < u; n++)
					calc(i+1, c[k + n] );
			}
		}
	}
	/**
	 * 重複あり組み合わせ順列結果List のイテレータを返す
	 * @param len 順列 nPr の r
	 * @return Iterator<List<T>>
	 */
	public Iterator<List<T>> iterator(int len){
		return this.compute(len).iterator();
	}
}

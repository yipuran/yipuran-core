package org.yipuran.util.pch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 順列組合せ nCr 算出（要素の重複なし組み合わせ）Combination.
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
 * @since 4.11
 */
public class Combinations<T> {
	private List<T> list;

	/**
	 * List→インスタンス生成.
	 * @param list List&lt;T&gt;生成前のリスト
	 */
	public static <T> Combinations<T> of(List<T> list){
		return new Combinations<T>(list);
	}

	/**
	 * 配列→インスタンス生成.
	 * @param array T[] 生成前の配列
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
		return  StreamSupport.stream(iterable(len).spliterator(), false).collect(Collectors.toList());
	}
	/**
	 * 組み合わせ結果List のイテレータ取得
	 * @param len nCr の r
	 * @return List&lt;T&gt;を返すイテレータ
	 */
	public Iterator<List<T>> iterator(int len){
		if (len < 1 || list.size() < len){
			throw new IllegalArgumentException();
		}
		return iterable(len).iterator();
	}
	/**
	 * 組み合わせ総数を求める
	 * @param len
	 * @return
	 */
	public long size(int len) {
		return nCr(list.size(), len);
	}
	/**
	 * 組み合わせ結果 Iterable＜List＜T＞＞の生成
	 * @param len nCr の r
	 * @return Iterable&lt;List&lt;T&gt;&gt;
	 */
	public Iterable<List<T>> iterable(int len){
		return ()-> new Iterator<List<T>>() {
			int index = -1;
			long total = nCr(list.size(), len);
			int[] currCombination = new int[len];
			@Override
			public boolean hasNext() {
				index++;
				return index < total;
			}
			@Override
			public List<T> next(){
				if (index==0){
					for(int i=0; i < currCombination.length; i++){
						currCombination[i] = i + 1;
					}
				}else{
					currCombination = generateNextCombination(currCombination, list.size(), len);
				}
				List<T> result = new ArrayList<>();
				for(int aCurrCombination : currCombination) {
					result.add(list.get(aCurrCombination - 1));
				}
				return result;
			}
		};
	}
	/**
	 * Predicate で抑制した 組み合わせ結果 Iterable＜List＜T＞＞の生成
	 * @param len nCr の r
	 * @param pred Predicate&lt;List&lt;T&gt;&gt;
	 * @return Iterable&lt;List&lt;T&gt;&gt;
	 */
	public Iterable<List<T>> iterable(int len, Predicate<List<T>> pred){
		return () -> new Iterator<List<T>>() {
			Iterator<List<T>> sourceIterator = iterable(len).iterator();
			List<T> current;
			boolean hasCurrent = false;
			@Override
			public boolean hasNext(){
				while(!hasCurrent){
					if (!sourceIterator.hasNext()) {
						return false;
					}
					List<T> next = sourceIterator.next();
					if (pred.test(next)) {
						current = next;
						hasCurrent = true;
					}
				}
				return true;
			}
			@Override
			public List<T> next() {
				if (!hasNext()) throw new NoSuchElementException();
				hasCurrent = false;
				return current;
			}
		};
	}
	private long nCr(int n, int r){
		int rfact = 1, nfact = 1, nrfact = 1, temp1 = n - r, temp2 = r;
		if (r > n - r){
			temp1 = r;
			temp2 = n - r;
		}
		for(int i = 1; i <= n; i++){
			if (i <= temp2){
				rfact *= i;
				nrfact *= i;
			}else if(i <= temp1){
				nrfact *= i;
			}
			nfact *= i;
		}
		return (long)(nfact / (rfact * nrfact));
	}
	private int[] generateNextCombination(int[] temp, int n, int r){
		int m = r;
		int maxVal = n;
		while(temp[m - 1] == maxVal){
			m = m - 1;
			maxVal--;
		}
		temp[m-1]++;
		for(int j=m; j < r; j++){
			temp[j] = temp[j - 1] + 1;
		}
		return temp;
	}
}

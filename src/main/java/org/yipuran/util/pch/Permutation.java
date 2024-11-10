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
 * 順列.
 * <PRE>
 * 順列(Permutation) を生成するためのクラス、生成元のリスト、配列はユニークで null を含んでは意味がない。
 * </PRE>
 */
public class Permutation<T>{
	private List<T> list;

	/**
	 * インスタンス生成.
	 * @param list 対象List
	 * @return Permutation
	 */
	public static <T> Permutation<T> of(List<T> list){
		return new Permutation<>(list);
	}
	/**
	 * インスタンス生成.
	 * @param ary 対象配列
	 * @return Permutation
	 */
	public static <T> Permutation<T> of(T[] ary){
		return new Permutation<>(Arrays.asList(ary));
	}
	private Permutation(List<T> list){
		this.list = list;
	}
	/**
	 * 順列生成.
	 * @param len 生成する並びの長さ
	 * @return 順列リスト
	 */
	public List<List<T>> compute(int len){
		if (len < 1 || list.size() < len){
			throw new IllegalArgumentException();
		}
		return  StreamSupport.stream(iterable(len).spliterator(), false).collect(Collectors.toList());
	}
	/**
	 * 順列イテレータ
	 * @param len 生成する並びの長さ
	 * @return 順列リストイテレータ
	 */
	public Iterator<List<T>> iterator(int len){
		return iterable(len).iterator();
	}

	/**
	 * 順列生成する並びの長さ
	 * @param len 生成する並びの長さ
	 * @return
	 */
	public long size(int len) {
		long rfact = (long)factorial(len);
		return nCr(list.size(), len) * rfact;
	}

	/**
	 * 順列 Iterable＜List＜T＞＞の生成
	 * @param len 生成する並びの長さ
	 * @return Iterable&lt;List&lt;T&gt;&gt;
	 */
	public Iterable<List<T>> iterable(int len){
		long rfact = (long)factorial(len);
		long total = nCr(list.size(), len) * rfact;
		return ()-> new Iterator<List<T>>(){
			int index = -1;
			int permNo = 0;
			int[] currPermutation = new int[len];
			int[] currCombination = new int[len];
			@Override
			public boolean hasNext() {
				index++;
				return index < total;
			}
			@Override
			public List<T> next(){
				if (index==0){
					permNo = 0;
					for(int i=0; i < currCombination.length; i++){
						currCombination[i] = i + 1;
						currPermutation[i] = i + 1;
					}
				}else if(((permNo + 1) % rfact)==0){
					permNo++;
					currCombination = generateNextCombination(currCombination, list.size(), len);
					for(int i=0; i < currCombination.length; i++){
						currPermutation[i] = i + 1;
					}
				}else{
					permNo++;
					currPermutation = generateNextPermutation(currPermutation, len);
				}
				List<T> result = new ArrayList<>();
				for(int i=0; i < len; i++){
					result.add(list.get(currCombination[currPermutation[i] - 1] - 1));
				}
				return result;
			}
		};
	}
	/**
	 * Predicate で抑制した順列 Iterable＜List＜T＞＞の生成
	 * @param len nCr の r
	 * @param pred Predicate&lt;List&lt;T&gt;&gt;
	 * @return Iterable&lt;List&lt;T&gt;&gt;
	 */
	public Iterable<List<T>> iterable(int r, Predicate<List<T>> pred){
		return () -> new Iterator<List<T>>(){
			Iterator<List<T>> sourceIterator = iterable(r).iterator();
			List<T> current;
			boolean hasCurrent = false;
			@Override
			public boolean hasNext() {
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
	private double factorial(int n) {
		int nfact = 1;
		for(int i=1; i <= n; i++) {
			nfact *= i;
		}
		return nfact;
	}
	private long nCr(int n, int r){
		int rfact=1, nfact=1, nrfact=1, temp1=n - r, temp2=r;
		if (r > n - r){
			temp1 = r;
			temp2 = n - r;
		}
		for(int i=1; i <= n; i++){
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
	private int[] generateNextCombination(int[] temp, int n, int r) {
		int m = r;
		int maxVal = n;
		while(temp[m - 1]==maxVal){
			m = m - 1;
			maxVal--;
		}
		temp[m-1]++;
		for(int j=m; j < r; j++){
			temp[j] = temp[j-1] + 1;
		}
		return temp;
	}
	private int[] generateNextPermutation(int[] temp, int n){
		int m = n - 1;
		while(temp[m-1] > temp[m]) {
			m--;
		}
		int k=n;
		while(temp[m-1] > temp[k-1]){
			k--;
		}
		int swapVar;
		swapVar = temp[m-1];
		temp[m-1] = temp[k-1];
		temp[k-1] = swapVar;
		int p = m + 1;
		int q = n;
		while(p < q){
			swapVar = temp[p-1];
			temp[p-1] = temp[q-1];
			temp[q-1] = swapVar;
			p++;
			q--;
		}
		return temp;
	}
}
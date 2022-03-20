package org.yipuran.util.pch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 順列.
 * <PRE>
 * 順列(Permutation) を生成するためのクラス、生成元のリスト、配列はユニークで null を含んでは意味がない。
 * </PRE>
 */
public class Permutation<T>{
	private List<T> list;
	private int     number, list_size, searched, next_index;
	private int[][] perm_list;

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
		if (len > list.size()) throw new IllegalArgumentException("list size over");
		this.number     = len;
		this.list_size  = this.fact(len);
		this.searched   = 0;
		this.next_index = 0;
		this.perm_list  = new int[this.list_size][len];
		this.create(0, new int[len], new boolean[len]);
		List<List<T>> rtn = new ArrayList<List<T>>();
		while(isNext()){
			rtn.add(Arrays.stream(nextPerm()).boxed().map(e->list.get(e)).collect(Collectors.toList()));
		}
		return rtn;
	}
	/**
	 * 順列イテレータ
	 * @param len 生成する並びの長さ
	 * @return 順列リストイテレータ
	 */
	public Iterator<List<T>> iterator(int len){
		return compute(len).iterator();
	}
	private int[] nextPerm(){
		this.next_index++;
		return perm_list[this.next_index-1];
	}
	private boolean isNext(){
		if (this.next_index < this.list_size) {
			return true;
		}
		this.next_index = 0;
		return false;
	}
	private int fact(int n){
		return n == 0 ? 1 : n * fact(n-1);
	}
	private void create(int _num, int[] _list, boolean[] _flag) {
		if(_num == this.number) {
			copyArray(_list, perm_list[this.searched]);
			this.searched++;
		}
		for(int i=0;i < _list.length;i++) {
			if(_flag[i]) continue;
			_list[_num] = i;
			_flag[i] = true;
			this.create(_num+1, _list, _flag);
			_flag[i] = false;
		}
	}
	private void copyArray(int[] _from, int[] _to) {
		for(int i=0; i<_from.length; i++) _to[i] = _from[i];
	}
}
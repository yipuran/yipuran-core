package org.yipuran.util.sort;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import org.yipuran.util.Fieldgetter;

/**
 * Lis＜Ｔ[]＞ の Comparator生成
 * <PRE>
 * Comparable でないオブジェクト、比較の為のComparable をオブジェクト内属性名指定できる配列のリストを
 * ソートする為のComparatorを生成する。
 *
 * （例）Item クラスは、属性名 price を Integer で Not NULL で持っている。
 * List＜Item[]＞  list;
 *
 * Stream＜Item[]＞ list.stream().sorted(ListComparator.of(3).build("price"))
 *
 * </PRE>
 * @since Ver 4.16
 */
public class ArrayListComparator{

	private int size;

	private ArrayListComparator(int size) {
		this.size = size;
	}

	/**
	 * インスタンス生成.
	 * @param size リストの長さ
	 * @return
	 */
	public static ArrayListComparator of(int size) {
		return new ArrayListComparator(size);
	}

	/**
	 * Comparator生成.
	 * @param name 比較要素属性名
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> Comparator<T[]> build(String name){
		AtomicReference<Comparator<T[]>> cp
		= new AtomicReference<>(Comparator.comparing(t->(Comparable)Fieldgetter.of(e->name).apply(t[0])));
		IntStream.range(1, size).boxed().forEach(i->{
			cp.set(cp.get().thenComparing( Comparator.comparing(t->(Comparable) Fieldgetter.of(e->name).apply(t[i]))));
		});
		return cp.get();
	}

	/**
	 * Comparator生成（リバース指定）.
	 * @param name 比較要素属性名
	 * @param reverse true=降順、false=昇順
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> Comparator<T[]> build(String name, boolean reverse){
		AtomicReference<Comparator<T[]>> cp
		= new AtomicReference<>(Comparator.comparing(t->(Comparable)Fieldgetter.of(e->name).apply(t[0])));
		IntStream.range(1, size).boxed().forEach(i->{
			cp.set(cp.get().thenComparing( Comparator.comparing(t->(Comparable) Fieldgetter.of(e->name).apply(t[i]))));
		});
		if (reverse) cp.set(cp.get().reversed());
		return cp.get();
	}

}

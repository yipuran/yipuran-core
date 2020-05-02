package org.yipuran.util.sort;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.yipuran.util.Fieldgetter;

/**
 * 入れ子List（List＜List＜Ｔ＞＞）のComparator生成.
 * <PRE>
 * Comparable でないオブジェクト、比較の為のComparable をオブジェクト内属性名指定できるリストのリストを
 * ソートする為のComparatorを生成する。
 *
 * （例）Item クラスは、属性名 price を Integer で Not NULL で持っている。
 * List＜List＜Item＞＞  list;
 *
 * Stream＜List＜Item＞＞ list.stream().sorted(ListComparator.of(3).build0("price"))
 *
 * </PRE>
 * @since Ver 4.14
 */
public class NestedListComparator{

	private int size;

	private NestedListComparator(int size) {
		this.size = size;
	}

	/**
	 * インスタンス生成.
	 * @param size リストの長さ
	 * @return
	 */
	public static  NestedListComparator of(int size) {
		return new NestedListComparator(size);
	}

	/**
	 * Comparator生成.
	 * @param name 比較要素属性名
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> Comparator<List<T>> build0(String name){
		Comparator<List<T>> cp = Comparator.comparing(u->(Comparable)Fieldgetter.of(t->name).apply(u.get(0)));
		IntStream.range(1, size).boxed().forEach(i->{
			cp.thenComparing(Comparator.comparing(t->(Comparable) Fieldgetter.of(e->name).apply(t.get(i))));
		});
		return cp;
	}
}

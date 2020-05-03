package org.yipuran.util.sort;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
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
 * @since Ver 4.15
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
	@SuppressWarnings("unchecked")
	public <T> Comparator<List<T>> build(String name){
		AtomicReference<Comparator<List<T>>> cp = new AtomicReference<>(Comparator.comparing(t->(Comparable)Fieldgetter.of(e->name).apply(t.get(0))));
		IntStream.range(1, size).boxed().forEach(i->{
			cp.set(cp.get().thenComparing(Comparator.comparing(t->(Comparable)Fieldgetter.of(e->name).apply(t.get(i)))));
		});
		return cp.get();
	}
	/**
	 * Comparator生成（リバース指定）.
	 * @param name 比較要素属性名
	 * @param reverse true=降順、false=昇順
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Comparator<List<T>> build(String name, boolean reverse){
		AtomicReference<Comparator<List<T>>> cp = new AtomicReference<>(Comparator.comparing(u->(Comparable)Fieldgetter.of(t->name).apply(u.get(0))));
		IntStream.range(1, size).boxed().forEach(i->{
			cp.set(cp.get().thenComparing( Comparator.comparing(t->(Comparable) Fieldgetter.of(e->name).apply(t.get(i)))));
		});
		if (reverse) cp.set(cp.get().reversed());
		return cp.get();
	}

	/**
	 * キー複数指定Comparator生成.
	 * @param name 比較要素属性名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Comparator<List<T>> build(String...names){
		if (names.length==1) return build(names[0]);
		Comparator<List<T>> c = IntStream.range(0, names.length).boxed()
		.<Comparator<List<T>>>map(i->Comparator.comparing(t->(Comparable)Fieldgetter.of(e->names[i]).apply(t.get(0))))
		.collect(()->Comparator.comparing(t->(Comparable)Fieldgetter.of(e->names[0]).apply(t.get(0)))
		,(t, r)->r.thenComparing(t),(t, r)->{});
		AtomicReference<Comparator<List<T>>> cp = new AtomicReference<>(c);

		IntStream.range(1, size).boxed().forEach(i->{
			IntStream.range(0, names.length).boxed().forEach(ni->{
				cp.set(cp.get().thenComparing( Comparator.comparing(t->(Comparable)Fieldgetter.of(e->names[ni]).apply(t.get(i)))));
			});
		});
		return cp.get();
	}
	/**
	 * キー複数指定Comparator生成.
	 * @param name 比較要素属性名
	 * @param reverse true=降順、false=昇順
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> Comparator<List<T>> build(boolean reverse, String...names){
		if (names.length==1) return build(names[0]);
		Comparator<List<T>> c = IntStream.range(0, names.length).boxed()
		.<Comparator<List<T>>>map(i->Comparator.comparing(t->(Comparable)Fieldgetter.of(e->names[i]).apply(t.get(0))))
		.collect(()->Comparator.comparing(t->(Comparable)Fieldgetter.of(e->names[0]).apply(t.get(0)))
		,(t, r)->r.thenComparing(t),(t, r)->{});
		AtomicReference<Comparator<List<T>>> cp = new AtomicReference<>(c);

		IntStream.range(1, size).boxed().forEach(i->{
			IntStream.range(0, names.length).boxed().forEach(ni->{
				cp.set(cp.get().thenComparing( Comparator.comparing(t->(Comparable)Fieldgetter.of(e->names[ni]).apply(t.get(i)))));
			});
		});
		if (reverse) cp.set(cp.get().reversed());
		return cp.get();
	}

}

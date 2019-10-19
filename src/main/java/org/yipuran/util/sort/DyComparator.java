package org.yipuran.util.sort;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import org.yipuran.util.Fieldgetter;

/**
 * 複数要素Comparator生成.
 * <PRE>
 * 複数要素のソート処理 Comparator を生成する。
 * ソート対象オブジェクトの Comparableフィールド名称を並べて指定することで、
 * ソートの順序を指定する。
 * 以下３通りの昇順、降順を指定できる。
 *   ・全て昇順指定
 *   ・要素毎に昇順、降順の指定
 *   ・全て降順指定
 *
 * （使用例）
 *   // 全て昇順指定
 *   List<Foo> list = list.stream().sorted(DyComparator.of().build("width", "height","depth")).collect(Collectors.toList());
 *
 *   // 要素毎に昇順、降順の指定  ( true=降順、false=昇順 )
 *   List<Foo> list = list.stream().sorted(DyComparator.of().build(new String[]{ "width", "height","depth"}, new boolean[]{ true, false, true })).collect(Collectors.toList());
 *
 *   // 全て降順指定
 *   List<Foo> list = list.stream().sorted(DyComparator.of().reversed("width", "height","depth")).collect(Collectors.toList());
 *
 * </PRE>
 * @since 4.8
 */
public final class DyComparator<T>{
	/**
	 * コンストラクタ.
	 */
	public DyComparator(){
	}
	/**
	 * インスタンス生成.
	 * @return DyComparator
	 */
	public static <T> DyComparator<T> of(){
		return new DyComparator<T>();
	}

	/**
	 * 全て昇順指定 Comparatorの生成.
	 * @param sary フィールド名称、ソート順に並べた配列
	 * @return Comparator
	 */
	public Comparator<T> build(String...sary){
		AtomicReference<Comparator<T>> c = new AtomicReference<>(Comparator.comparing(Fieldgetter.of(u->sary[0])));
		IntStream.range(1, sary.length).boxed().forEach(e->{
			c.set(c.get().thenComparing(Comparator.comparing(Fieldgetter.of(u->sary[e]))) );
		});
		return c.get();
	}

	/**
	 * 昇順／降順、任意 Comparatorの生成.
	 * @param sary フィールド名称、ソート順に並べた配列
	 * @param revers 第１引数に合わせた、昇順・降順を配列で指定する。true=降順、false=昇順である。
	 * @return Comparator
	 */
	public Comparator<T> build(String[] sary, boolean[] revers){
		AtomicReference<Comparator<T>> c = new AtomicReference<>(Comparator.comparing(Fieldgetter.of(u->sary[0])));
		if (revers[0]) c.set(c.get().reversed());
		IntStream.range(1, sary.length).boxed().forEach(e->{
			if (revers[e]){
				c.set(c.get().thenComparing(Comparator.comparing(Fieldgetter.of(u->sary[e])).reversed()));
			}else {
				c.set(c.get().thenComparing(Comparator.comparing(Fieldgetter.of(u->sary[e]))) );
			}
		});
		return c.get();
	}

	/**
	 * 全て降順指定 Comparatorの生成.
	 * @param sary sary フィールド名称、ソート順に並べた配列
	 * @return Comparator
	 */
	public Comparator<T> reversed(String...sary){
		AtomicReference<Comparator<T>> c = new AtomicReference<>(Comparator.comparing(Fieldgetter.of(u->sary[0])));
		c.set(c.get().reversed());
		IntStream.range(1, sary.length).boxed().forEach(e->{
			c.set(c.get().thenComparing(Comparator.comparing(Fieldgetter.of(u->sary[e])).reversed()));
		});
		return c.get();
	}
}

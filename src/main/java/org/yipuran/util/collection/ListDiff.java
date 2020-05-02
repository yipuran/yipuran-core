package org.yipuran.util.collection;

import java.util.AbstractMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * リストの比較.
 * <PRE>
 * ListDiff.of で、比較する２つのリスト要素を等しいと判定する BiFunction を指定してインスタンスを生成
 * ３つのメソッドの振る舞いを登録した後、diff メソッドを実行する。
 *
 * leftOnly  : 左側（１番目指定）リストだけに存在する要素に対する Consumer
 * rightonly : 右側（２番目指定）リストだけに存在する要素に対する Consumer
 * match     ; 両方のリストに存在する要素に対する BiConsumer
 * </PRE>
 */
public final class ListDiff<T>{
	private BiFunction<T, T, Boolean> matchFunction;
	private Consumer<T> leftonly;
	private Consumer<T> rightonly;
	private BiConsumer<T, T> biConsumer;

	private ListDiff(BiFunction<T, T, Boolean> matchFunction){
		this.matchFunction = matchFunction;
	}
	/**
	 * ListDiffインスタンス生成.
	 * @param matchFunction 比較する２つのリスト要素を等しいと判定する BiFunction で Boolean(true=等しい) を返す。
	 * @return ListDiff<T>{
	 */
	public static <T> ListDiff<T> of(BiFunction<T, T, Boolean> matchFunction){
		 return new ListDiff<>(matchFunction);
	}
	/**
	 * 左側（１番目指定）リストだけに存在する要素に対する Consumerを登録.
	 * @param leftonly Consumer<T>
	 */
	public void leftOnly(Consumer<T> leftonly){
		this.leftonly = leftonly;
	}
	/**
	 * 右側（２番目指定）リストだけに存在する要素に対する Consumerを登録.
	 * @param rightonly Consumer<T>
	 */
	public void rightOnly(Consumer<T> rightonly){
		this.rightonly = rightonly;
	}
	/**
	 * 両方のリストに存在する要素に対する BiConsumerを登録.
	 * @param biConsumer BiConsumer<T, T>
	 */
	public void match(BiConsumer<T, T> biConsumer){
		this.biConsumer = biConsumer;
	}

	/**
	 * 比較の実行.
	 * @param leftList 左側（１番目指定）
	 * @param rightList 右側（２番目指定）
	 */
	public void diff(List<T> leftList, List<T> rightList){
		if (leftonly != null) leftList.stream().filter(e->rightList.stream().noneMatch(t->matchFunction.apply(t, e))).forEach(leftonly);
		if (rightonly != null) rightList.stream().filter(e->leftList.stream().noneMatch(t->matchFunction.apply(t, e))).forEach(rightonly);
		if (biConsumer != null)
			leftList.stream()
			.map(e->new AbstractMap.SimpleEntry<T, T>(e, rightList.stream().filter(t->matchFunction.apply(t, e)).findFirst().orElse(null)))
			.filter(e->e.getValue() != null).forEach(p->{
				biConsumer.accept(p.getKey(), p.getValue());
			});
	}
}

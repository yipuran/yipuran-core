package org.yipuran.function;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * NullableFunction. andThen 、compose の前方 apply 結果がnullの場合を対応する Function 拡張インターフェース.
 */
public interface NullableFunction<T, R> extends Function<T, R>{

	/** 前方Function NULL発生対応として null を返す andThen オーバーライド */
	@Override
	default <V> Function<T, V> andThen(Function<? super R, ? extends V> after){
		Objects.requireNonNull(after);
		return (T t) -> Optional.ofNullable(apply(t)).map(after::apply).orElse(null);
	}

	/**
	 * NULL発生時、結果値指定 andThen
	 * @param after 後続 Function
	 * @param v 前方 Function のapply結果がnullの場合、後続を実行しないで返す結果値
	 * @return andThenしたFunction
	 */
	default <V> Function<T, V> andThen(Function<? super R, V> after, V v){
		Objects.requireNonNull(after);
		return (T t) -> Optional.ofNullable(apply(t)).map(after::apply).orElse(v);
	}

	/**
	 * NULL発生時、Supplier指定 andThen
	 * @param after 後続 Function
	 * @param sup 前方 Function のapply結果がnullの場合、後続を実行しないで実行するSupplier
	 * @return andThenしたFunction
	 */
	default <V> Function<T, V> andThen(Function<? super R, V> after, Supplier<V> sup){
		Objects.requireNonNull(after);
		return (T t) -> Optional.ofNullable(apply(t)).map(after::apply).orElse(sup.get());
	}

	/** 前方Function NULL発生対応として null を返す compose オーバーライド */
	@Override
	default <V> Function<V, R> compose(Function<? super V, ? extends T> before){
		Objects.requireNonNull(before);
		return (V v) -> Optional.ofNullable(before.apply(v)).map(e->apply(e)).orElse(null);
	}

	/**
	 * NULL発生時、結果値指定 compose
	 * @param before 前方 Function
	 * @param r 前方 Function のapply結果がnullの場合、後続を実行しないで返す結果値
	 * @return composeしたFunction
	 */
	default <V> Function<V, R> compose(Function<? super V, ? extends T> before, R r){
		Objects.requireNonNull(before);
		return (V v) -> Optional.ofNullable(before.apply(v)).map(e->apply(e)).orElse(r);
	}

	/**
	 * NULL発生時、Supplier指定 compose
	 * @param before 前方 Function
	 * @param r 前方 Function のapply結果がnullの場合、後続を実行しないで実行するSupplier
	 * @return composeしたFunction
	 */
	default <V> Function<V, R> compose(Function<? super V, ? extends T> before, Supplier<R> sup){
		Objects.requireNonNull(before);
		return (V v) -> Optional.ofNullable(before.apply(v)).map(e->apply(e)).orElse(sup.get());
	}

	/**
	 * ２個の Function 実行を結合.前方Function結果が null の場合、null を返す。
	 * @param before
	 * @param after
	 * @return 結合した Function
	 */
	static <T,V,R> Function<T, R> bind(Function<T,V> before, Function<V,R> after){
		return (T t) -> Optional.ofNullable(before.apply(t)).map(after::apply).orElse(null);
	}

	/**
	 * ２個の Function 実行を結合.前方Function結果が null の場合、引数で指定した値を返す。
	 * @param before
	 * @param after
	 * @param r 前方Function結果が null の場合に返す値
	 * @return 結合した Function
	 */
	static <T,V,R> Function<T, R> bind(Function<T,V> before, Function<V,R> after, R r){
		return (T t) -> Optional.ofNullable(before.apply(t)).map(after::apply).orElse(r);
	}

	/**
	 * ２個の Function 実行を結合.前方Function結果が null の場合、Supplier引数の結果を返す。
	 * @param before
	 * @param after
	 * @param sup 前方Function結果が null の場合に実行するSupplier
	 * @return 結合した Function
	 */
	static <T,V,R> Function<T, R> bind(Function<T,V> before, Function<V,R> after, Supplier<R> sup){
		return (T t) -> Optional.ofNullable(before.apply(t)).map(after::apply).orElse(sup.get());
	}
}

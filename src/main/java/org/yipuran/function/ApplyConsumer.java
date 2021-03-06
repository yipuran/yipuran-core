package org.yipuran.function;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 複合BiConsumer.
 * Function apply の実行結果で Biconsumer accept を実行する。
 * <PRE>
 * 例）
 * ApplyConsumer.of(Base::getId, Item::setId).and(Base::getName, Item::setName).accept(base, item);
 * </PRE>
 * @param <T>
 * @param <U>
 */
@FunctionalInterface
public interface ApplyConsumer<T, U> extends Serializable{
	void accept(T t, U u);

	/**
	 * ApplyConsumer 生成.
	 * @param f Function
	 * @param b BiConsumer
	 * @return ApplyConsumer
	 */
	static <T, U, V> ApplyConsumer<T, U> of(Function<T, V> f, BiConsumer<U, V> b){
		return (t,u)->{
			b.accept(u, f.apply(t));
		};
	}
	/**
	 * ApplyConsumer を連結
	 * @param f Function
	 * @param b BiConsumer
	 * @return 連結したApplyConsumer
	 */
	default <V> ApplyConsumer<T, U> and(Function<T, V> f, BiConsumer<U, V> b){
		return (l, r) -> {
			accept(l, r);
			of(f, b).accept(l, r);
		};
	}
}

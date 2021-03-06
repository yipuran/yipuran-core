package org.yipuran.function;

import java.io.Serializable;
import java.util.function.Function;

import com.google.common.base.Supplier;

/**
 * 複合Function生成.
 * BiConsumer として accept 実行した結果を Function＜T,T＞として生成する。
 * <PRE>
 * 例）
 * Foo foo = Optional.of(new Foo())
 *          .map(AcceptFunction.of(Foo::setId, 1001))
 *          .map(AcceptFunction.of(Foo::setName, "Yip1"))
 *          .map(AcceptFunction.of(Foo::setLocate, "Tokyo"))
 *          .get();
 * </PRE>
 * @param <T>
 * @param <U>
 */
@FunctionalInterface
public interface AcceptFunction<T, U> extends Serializable{
	void accept(T t, U u);

	/**
	 * AcceptFunction 生成
	 * @param a
	 * @param u accept 対象オブジェクト
	 * @return
	 */
	public static <T, U> Function<T, T> of(AcceptFunction<T, U> a, U u){
		return t->{
			a.accept(t, u);
			return t;
		};
	}
	/**
	 * AcceptFunction 生成(Supplier)
	 * @param a
	 * @param s accept 対象を提供するSupplier
	 * @return
	 */
	public static <T, U> Function<T, T> of(AcceptFunction<T, U> a, Supplier<U> s){
		return t->{
			a.accept(t, s.get());
			return t;
		};
	}
	/**
	 * AcceptFunction 生成(Function)
	 * @param a
	 * @param func
	 * @param e
	 * @return
	 */
	public static <T, U, E> Function<T, T> of(AcceptFunction<T, U> a, Function<E, U> func, E e){
		return t->{
			a.accept(t, func.apply(e));
			return t;
		};
	}
}

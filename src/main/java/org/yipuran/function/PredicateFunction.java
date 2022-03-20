package org.yipuran.function;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Predicate検証→Function.
 * <PRE>
 * Predicate 実行して成功した場合に Function applyを実行する
 * </PRE>
 */
@FunctionalInterface
public interface PredicateFunction<T, R> extends Serializable{
	R apply(T t);
	/**
	 * Predicate → Function （false時は null）
	 * @param p Predicate
	 * @param f PredicateFunction
	 * @return
	 */
	static <T, R> Function<T, R> of(Predicate<T> p, PredicateFunction<T, R> f){
		return t-> p.test(t) ? f.apply(t) : null;
	}
	/**
	 * Predicate → Function （false時は Rを指定）
	 * @param p Predicate
	 * @param f PredicateFunction
	 * @param r false時の結果
	 * @return
	 */
	static <T, R> Function<T, R> of(Predicate<T> p, PredicateFunction<T, R> f, R r){
		return t-> p.test(t) ? f.apply(t) : r;
	}
	/**
	 * Predicate → Function （false時は Supplierで取得）
	 * @param p Predicate
	 * @param f PredicateFunction
	 * @param s false時の結果を返すSupplier
	 * @return
	 */
	static <T, R> Function<T, R> of(Predicate<T> p, PredicateFunction<T, R> f, Supplier<R> s){
		return t -> p.test(t) ? f.apply(t) : s.get();
	}
}

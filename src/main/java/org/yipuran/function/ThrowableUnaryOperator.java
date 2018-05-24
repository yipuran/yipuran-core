package org.yipuran.function;

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

/**
 * Exception 捕捉 UnaryOperator.
 *
 * <PRE>
 * Exception発生する UnaryOperator の処理でExceptionを捕捉処理する関数を同時に定義する。
 * to メソッドで生成する。
 *
 * 例)
 * stream.filter(ThrowableSupplier.to(e->{
 *     // 通常の判定
 *     return true;
 * }, (e, x)->false);
 *
 * </PRE>
 */
public interface ThrowableUnaryOperator<T> extends Serializable{
	T apply(T t) throws Exception;

	static <T> UnaryOperator<T> identity() {
		return t -> t;
	}
	/**
	 * ThrowableUnaryOperator 生成.
	 * @param u 例外スローする UnaryOperator<T>処理
	 * @param onCatch Exception捕捉処理 , 値を返さなければならない。
	 * @return UnaryOperator<T>
	 */
	public static <T> UnaryOperator<T> of(ThrowableUnaryOperator<T> u, BiFunction<T, Exception, T> onCatch){
		return  t->{
			try{
				return u.apply(t);
			}catch(Exception e){
				return onCatch.apply(t, e);
			}
		};
	}
	/**
	 * ThrowableUnaryOperator 生成（外に例外スロー）.
	 * @param u 例外スローする UnaryOperator<T>処理
	 * @return UnaryOperator<T>
	 */
	public static <T> UnaryOperator<T> of(ThrowableUnaryOperator<T> u){
		return  t->{
			try{
				return u.apply(t);
			}catch(Throwable ex){
				throw new RuntimeException(ex);
			}
		};
	}
}

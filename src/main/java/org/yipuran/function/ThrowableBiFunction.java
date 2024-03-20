package org.yipuran.function;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Exception 捕捉 BiFunction.
 *
 * <PRE>
 * Exception発生する BiFunction の処理でExceptionを捕捉処理する関数を同時に定義する。
 * of メソッドで生成する。
 *
 * 例)
 * ThrowableBiFunction.of((t, u)->{
 *     // 通常の処理
 *     return r;
 * }
 * , (e, x)->{
 *     // 例外捕捉処理、 x は Exception継承
 *     return null;
 * });
 *
 * </PRE>
 */
@FunctionalInterface
public interface ThrowableBiFunction<T, U, R> extends Serializable{

	R apply(T t, U u) throws Exception;

	default <V> BiFunction<T, U, V> andThen(Function<? super R, ? extends V> after, BiFunction<T, Exception, ? extends V> onCatch){
		Objects.requireNonNull(after);
		return (T t, U u)->{
			try{
				return after.apply(apply(t, u));
			}catch(Exception e){
				return onCatch.apply(t, e);
			}
		};
	}
	/**
	 * ThrowableBiFunction 生成.
	 * @param function 例外スローする BiFunction&lt;T, U, R&gt;処理
	 * @param onCatch Exception捕捉処理 , R を返すもしくは、null を返さなければならない。
	 * @return BiFunction&lt;T, U, R&gt;
	 */
	public static <T, U, R> BiFunction<T, U, R> of(ThrowableBiFunction<T, U, R> function, BiFunction<T, Exception, R> onCatch){
		return (t, u)->{
			try{
				return function.apply(t, u);
			}catch(Exception e){
				return onCatch.apply(t, e);
			}
		};
	}
	/**
	 * ThrowableBiFunction 生成.
	 * @param function 例外スローする BiFunction&lt;T, U, R&gt;処理
	 * @return BiFunction&lt;T, U, R&gt;
	 */
	public static <T, U, R> BiFunction<T, U, R> of(ThrowableBiFunction<T, U, R> function){
		return (t, u)->{
			try{
				return function.apply(t, u);
			}catch(Exception ex){
				throw new RuntimeException(ex.getMessage(), ex);
			}
		};
	}
}

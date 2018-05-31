package org.yipuran.function;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Exception 捕捉 Function.
 *
 * <PRE>
 * Exception発生する Function の処理でExceptionを捕捉処理する関数を同時に定義する。
 * of メソッドで生成する。
 *
 * 例)
 * stream.map(ThrowableFunction.of(e->{
 *     // 通常の処理
 *     return r;
 * }
 * , (e, x)->{
 *     // 例外捕捉処理、 x は Exception
 *     return null;
 * });
 *
 * </PRE>
 */
@FunctionalInterface
public interface ThrowableFunction<T, R> extends Serializable{
	R apply(T t) throws Exception;

	default <V> Function<V, R> compose(Function<? super V, ? extends T> before, BiFunction<V, Exception, R> onCatch){
		Objects.requireNonNull(before);
		return (V v)->{
			try{
				return apply(before.apply(v));
			}catch(Exception e){
				return onCatch.apply(v, e);
			}
		};
	}

	default <V> Function<T, V> andThen(Function<? super R, ? extends V> after, BiFunction<T, Exception,  ? extends V> onCatch){
		Objects.requireNonNull(after);
		return (T t)->{
			try{
				return after.apply(apply(t));
			}catch(Exception e){
				return onCatch.apply(t, e);
			}
		};
	}

	static <T> Function<T, T> identity(){
		return t -> t;
	}
	/**
	 * ThrowableFunction 生成.
	 * @param function 例外スローする Function&lt;T, R&gt;処理
	 * @param onCatch Exception捕捉処理 , R を返すもしくは、null を返さなければならない。
	 * @return Function&lt;T, R&gt;
	 */
	public static <T, R> Function<T, R> of(ThrowableFunction<T, R> function, BiFunction<T, Exception, R> onCatch){
		return  t->{
			try{
				return function.apply(t);
			}catch(Exception e){
				return onCatch.apply(t, e);
			}
		};
	}
	/**
	 * ThrowableFunction 生成（外に例外スロー）.
	 * @param function 例外スローする Function&lt;T, R&gt;処理
	 * @return Function&lt;T, R&gt;
	 */
	public static <T, R> Function<T, R> of(ThrowableFunction<T, R> function){
		return  t->{
			try{
				return function.apply(t);
			}catch(Throwable ex){
				throw new RuntimeException(ex);
			}
		};
	}

}


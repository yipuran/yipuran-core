package org.yipuran.function.number;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.yipuran.function.ThrowableFunction;
import org.yipuran.function.TriFunction;

/**
 * カウンタ付き Function.
 * <PRE>
 * BiFunction＜T, Long, R＞ でストリーム Function実行時のカウント(long）を認識して処理する。
 * Function を指定する箇所で CountFunction インスタンスを指定する。
 *
 * CountFunction.of((t, c)->{
 *    // t : 要素
 * 	// c : long カウンタ
 *
 * 	return r;
 * });
 *
 * ThrowableFunction を継承しており、例外捕捉もカウンタを取得した TriFunction 処理を指定できる。
 *
 * CountFunction.of((t, c)->{
 *    // t : 要素
 * 	// c : long カウンタ
 *
 * 	return r;
 * },(t, c, x)->{
 *    // t : 要素
 *    // c : long カウンタ
 *    // x : 例外 Throwable
 *
 *    return r;
 * });
 *
 * このインターフェースは、Serializable である
 * </PRE>
 * @since Ver4.17
 */
public interface CountFunction<T, R> extends ThrowableFunction<T, R>{


	default CountFunction<T, T> andThen(CountBiFunction<? super R, Long, ? extends T> after, BiFunction<T, Exception, ? extends T> onCatch){
		Objects.requireNonNull(after);
		AtomicLong c = new AtomicLong(1);
		return (T t)->{
			try{
				return after.apply(apply(t), c.getAndIncrement());
			}catch(Exception e){
				return onCatch.apply(t, e);
			}
		};
	}

	public static <T, R> Function<T, R> of(BiFunction<T, Long, R> function){
		AtomicLong c = new AtomicLong(1);
		return t->{
			try{
				return function.apply(t, c.getAndIncrement());
			}catch(Throwable ex){
				throw new RuntimeException(ex);
			}
		};
	}
	public static <T, R> Function<T, R> of(BiFunction<T, Long, R> function, TriFunction<T, Long, Throwable, R> onCatch){
		AtomicLong c = new AtomicLong(1);
		return t->{
			try{
				return function.apply(t, c.getAndIncrement());
			}catch(Throwable ex){
				return onCatch.apply(t, c.get(), ex);
			}
		};
	}
}

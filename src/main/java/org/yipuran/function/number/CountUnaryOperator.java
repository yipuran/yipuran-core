package org.yipuran.function.number;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import org.yipuran.function.ThrowableUnaryOperator;
import org.yipuran.function.TriFunction;

/**
 * カウンタ付き UnaryOperator.
 *
 * <PRE>
 * BiFunction＜T, Long, T＞ でストリーム UnaryOperator実行時のカウント(long）を認識して処理する。
 * UnaryOperator を指定する箇所で CountUnaryOperator インスタンスを指定する。
 *
 * CountUnaryOperator.of((t, c)->{
 *    // t : 要素
 * 	// c : long カウンタ
 *
 * 	return t;
 * });
 *
 * ThrowableUnaryOperator を継承しており、例外捕捉もカウンタを取得した TriFunction 処理を指定できる。
 *
 * CountUnaryOperator.of((t, c)->{
 *    // t : 要素
 * 	// c : long カウンタ
 *
 * 	return t;
 * },(t, c, x)->{
 *    // t : 要素
 *    // c : long カウンタ
 *    // x : 例外 Throwable
 *
 *    return t;
 * });
 *
 * このインターフェースは、Serializable である
 * </PRE>
 * @since Ver4.17
 */
public interface CountUnaryOperator<T> extends ThrowableUnaryOperator<T>{

	public static <T> UnaryOperator<T> of(BiFunction<T, Long, T> bifunction){
		AtomicLong c = new AtomicLong(1);
		return  t->{
			try{
				return bifunction.apply(t, c.getAndIncrement());
			}catch(Exception ex){
				throw new RuntimeException(ex);
			}
		};
	}

	public static <T> UnaryOperator<T> of(BiFunction<T, Long, T> bifunction, TriFunction<T, Long, Throwable, T> onCatch){
		AtomicLong c = new AtomicLong(1);
		return  t->{
			try{
				return bifunction.apply(t, c.getAndIncrement());
			}catch(Throwable ex){
				return onCatch.apply(t, c.get(), ex);
			}
		};
	}

}

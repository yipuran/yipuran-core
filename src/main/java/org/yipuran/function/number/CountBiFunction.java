package org.yipuran.function.number;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;

import org.yipuran.function.TetraFunction;
import org.yipuran.function.ThrowableBiFunction;
import org.yipuran.function.TriFunction;

/**
 * カウンタ付き BiFunction.
 * <PRE>
 * TriFunction＜T, U, Long＞ でストリーム BiFunction実行時のカウント(long）を認識して処理する。
 * BiFunction を指定する箇所で CountBiFunction インスタンスを指定する。
 *
 * CountBiConsumer.of((t, u, c)->{
 *    // t : 要素１
 *    // u : 要素２
 *    // c : long カウンタ
 *
 *    return r;
 * });
 *
 * ThrowableBiFunction を継承しており、例外捕捉もカウンタを取得した TetraFunction 処理を指定できる。
 *
 * CountBiFunction.of((t, u, c)->{
 *    // t : 要素１
 *    // u : 要素２
 *    // c : long カウンタ
 *
 *    return r;
 * }, (t, u, c, x)->{
 *    // t : 要素１
 *    // u : 要素２
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
public interface CountBiFunction<T, U, R> extends ThrowableBiFunction<T, U, R>{

	public static <T, U, R> BiFunction<T, U, R> of(TriFunction<T, U, Long, R> trifunction){
		AtomicLong c = new AtomicLong(1);
		return (t, u)->{
			try{
				return trifunction.apply(t, u, c.getAndIncrement());
			}catch(Throwable ex){
				throw new RuntimeException(ex);
			}
		};
	}
	public static <T, U, R> BiFunction<T, U, R> of(TriFunction<T, U, Long, R> trifunction, TetraFunction<T, U, Long, Throwable, R> tetrafunction){
		AtomicLong c = new AtomicLong(1);
		return (t, u)->{
			try{
				return trifunction.apply(t, u, c.getAndIncrement());
			}catch(Throwable ex){
				return tetrafunction.apply(t, u, c.get(), ex);
			}
		};
	}
}

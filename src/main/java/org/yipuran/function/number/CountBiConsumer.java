package org.yipuran.function.number;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

import org.yipuran.function.TetraConsumer;
import org.yipuran.function.ThrowableBiConsumer;
import org.yipuran.function.TriConsumer;

/**
 * カウンタ付き BiConsumer.
 * <PRE>
 * TriConsumer＜T, U, Long＞ でストリーム BiConsumer実行時のカウント(long）を認識して処理する。
 * BiConsumer を指定する箇所で CountBiConsumer インスタンスを指定する。
 *
 * CountBiConsumer.of((t, u, c)->{
 *    // t : 要素１
 *    // u : 要素２
 *    // c : long カウンタ
 * });
 *
 * ThrowableBiConsumer を継承しており、例外捕捉もカウンタを取得した TetraConsumer 処理を指定できる。
 *
 * CountBiConsumer.of((t, u, c)->{
 *    // t : 要素１
 *    // u : 要素２
 *    // c : long カウンタ
 *
 * }, (t, u, c, x)->{
 *    // t : 要素１
 *    // u : 要素２
 *    // c : long カウンタ
 *    // x : 例外 Throwable
 *
 * });
 *
 * このインターフェースは、Serializable である
 * </PRE>
 * @since Ver4.17
 */
public interface CountBiConsumer<T, U> extends ThrowableBiConsumer<T, U>{

	public static <T, U> BiConsumer<T, U> of(TriConsumer<T, U, Long> triconsumer){
		AtomicLong c = new AtomicLong(1);
		return (t, u)->{
			try{
				triconsumer.accept(t, u, c.getAndIncrement());
			}catch(Throwable ex){
				throw new RuntimeException(ex);
			}
		};
	}

	public static <T, U> BiConsumer<T, U>  of(TriConsumer<T, U, Long> triconsumer, TetraConsumer<T, U, Long, Throwable> onCatch){
		AtomicLong c = new AtomicLong(1);
		return (t, u)->{
			try{
				triconsumer.accept(t, u, c.getAndIncrement());
			}catch(Throwable ex){
				onCatch.accept(t, u, c.get(), ex);
			}
		};
	}

}

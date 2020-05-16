package org.yipuran.function.number;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.yipuran.function.ThrowableConsumer;
import org.yipuran.function.TriConsumer;
/**
 * カウンタ付き Consumer.
 * <PRE>
 * BiConsumer＜T, Long＞ でストリーム Consumer実行時のカウント(long）を認識して処理する。
 * Consumer を指定する箇所で CountConsumer インスタンスを指定する。
 *
 * CountConsumer.of((t, c)->{
 *    // t : 要素
 *    // c : long カウンタ
 * });
 *
 * ThrowableConsumer を継承しており、例外捕捉もカウンタを取得した TriConsumer 処理を指定できる。
 *
 * CountConsumer.of((t, c)->{
 *    // t : 要素
 *    // c : long カウンタ
 *
 * }, (t, c, x)->{
 *    // t : 要素
 * 	// c : long カウンタ
 *    // x : 例外 Throwable
 *
 * });
 *
 * このインターフェースは、Serializable である
 * </PRE>
 * @since Ver4.17
 */
public interface CountConsumer<T> extends ThrowableConsumer<T>{

	default CountConsumer<T> andThen(CountConsumer<? super T> after){
		Objects.requireNonNull(after);
		return t->{
			accept(t);
			after.accept(t);
		};
	}

	public static <T> Consumer<T> of(BiConsumer<T, Long> biconsumer){
		AtomicLong c = new AtomicLong(1);
		return t->{
			try{
				biconsumer.accept(t, c.getAndIncrement());
			}catch(Exception ex){
				throw new RuntimeException(ex);
			}
		};
	}
	public static <T> Consumer<T> of(BiConsumer<T, Long> biconsumer, TriConsumer<T, Long, Throwable> onCatch){
		AtomicLong c = new AtomicLong(1);
		return t->{
			try{
				biconsumer.accept(t, c.getAndIncrement());
			}catch(Throwable ex){
				onCatch.accept(t, c.get(), ex);
			}
		};
	}

}

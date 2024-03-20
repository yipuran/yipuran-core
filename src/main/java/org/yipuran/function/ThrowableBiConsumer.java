package org.yipuran.function;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Exception 捕捉 BiConsumer.
 *
 * <PRE>
 * Exception発生する Consumer の処理でExceptionを捕捉処理する関数を同時に定義する。
 * of メソッドで生成する。
 *
 * 例)
 * ThrowableBiConsumer.of((t, u)->{
 *     // 通常の処理
 * }
 * , (p, x)->{
 *     // 例外捕捉処理、 x は Exception
 * });
 *	  p は、t と u の AbstractMap.SimpleEntry&lt;T, U&gt;
 * </PRE>
 */
@FunctionalInterface
public interface ThrowableBiConsumer<T, U> extends Serializable{

	void accept(T t, U u) throws Exception;

	default BiConsumer<T, U> andThen(BiConsumer<? super T, ? super U> after, BiConsumer<AbstractMap.SimpleEntry<T, U>, Exception> onCatch){
		Objects.requireNonNull(after);
		return (t, u) -> {
			try{
				accept(t, u);
			}catch(Exception e){
				onCatch.accept(new AbstractMap.SimpleEntry<>(t, u), e);
			}
			after.accept(t, u);
		};
   }
	/**
	 * ThrowableBiConsumer 生成.
	 * @param consumer 例外スローする BiConsumer&lt;T, U&gt;処理
	 * @param onCatch Exception捕捉処理、BiConsumer&lt;AbstractMap.SimpleEntry&lt;T, U&gt;, Exception&gt;、
	 * @return BiConsumer&lt;T, U&gt;
	 */
	public static <T, U> BiConsumer<T, U> of(ThrowableBiConsumer<T, U> consumer, BiConsumer<AbstractMap.SimpleEntry<T, U>, Exception> onCatch){
		return (t, u)->{
			try{
				consumer.accept(t, u);
			}catch(Exception ex){
				onCatch.accept(new AbstractMap.SimpleEntry<T, U>(t, u), ex);
			}
		};
	}
	/**
	 * ThrowableBiConsumer 生成（外に例外スロー）.
	 * @param consumer 例外スローする BiConsumer&lt;T, U&gt;処理
	 * @return BiConsumer&lt;T, U&gt;
	 */
	public static <T, U> BiConsumer<T, U> of(ThrowableBiConsumer<T, U> consumer){
		return (t, u)->{
			try{
				consumer.accept(t, u);
			}catch(Throwable ex){
				throw new RuntimeException(ex.getMessage(), ex);
			}
		};
	}
}

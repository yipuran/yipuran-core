package org.yipuran.function;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Exception 捕捉 Consumer.
 *
 * <PRE>
 * Exception発生する Consumer の処理でExceptionを捕捉処理する関数を同時に定義する。
 * of メソッドで生成する。
 *
 * 例)
 * stream.forEach(ThrowableConsumer.of(e->{
 *     // 通常の処理
 * }
 * , (e, x)->{
 *     // 例外捕捉処理、 x は Exception
 * });
 *
 * </PRE>
 */
@FunctionalInterface
public interface ThrowableConsumer<T> extends Serializable{
	void accept(T t) throws Exception;

	default Consumer<T> andThen(Consumer<? super T> after, BiConsumer<T, Exception> onCatch){
		Objects.requireNonNull(after);
		return (T t)->{
			try{
				accept(t);
			}catch(Exception e){
				onCatch.accept(t, e);
			}
			after.accept(t);
		};
	}
	/**
	 * ThrowableConsumer 生成.
	 * @param consumer 例外スローする Consumer&lt;T&gt;処理
	 * @param onCatch Exception捕捉処理
	 * @return Consumer&lt;T&gt;
	 */
	public static <T> Consumer<T> of(ThrowableConsumer<T> consumer, BiConsumer<T, Exception> onCatch){
		return t->{
			try{
				consumer.accept(t);
			}catch(Exception ex){
				onCatch.accept(t, ex);
			}
		};
	}
	/**
	 * ThrowableConsumer 生成（外に例外スロー）.
	 * @param consumer 例外スローする Consumer&lt;T&gt;処理
	 * @return Consumer&lt;T&gt;
	 */
	public static <T> Consumer<T> of(ThrowableConsumer<T> consumer){
		return t->{
			try{
				consumer.accept(t);
			}catch(Throwable ex){
				throw new RuntimeException(ex.getMessage(), ex);
			}
		};
	}
}

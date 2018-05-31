package org.yipuran.function;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Exception 捕捉 Supplier.
 *
 * <PRE>
 * Exception発生する Supplier の処理でExceptionを捕捉処理する関数を同時に定義する。
 * to メソッドで生成する。
 *
 * 例)
 * stream.collect(ThrowableSupplier.to(()->{
 *     // 通常時
 *     return r;
 * }, exception->{
 * 	 // 例外発生時
 *     return r2;
 * }. accumulator, combiner );
 *
 * </PRE>
 */
@FunctionalInterface
public interface ThrowableSupplier<R> extends Serializable{

	R get() throws Exception;

	/**
	 * ThrowableSupplier 生成.
	 * @param supplier 例外スローする Supplier&lt;R&gt;処理
	 * @param onCatch Exception捕捉処理 , 値を返さなければならない。
	 * @return Supplier&lt;R&gt;
	 */
	public static <R> Supplier<R> to(ThrowableSupplier<? extends R> supplier, Function<Exception, R> onCatch){
		return ()->{
			try{
				return supplier.get();
			}catch(Exception e){
				return onCatch.apply(e);
			}
		};
	}
	/**
	 * ThrowableSupplier 生成（外に例外スロー）.
	 * @param supplier 例外スローする Supplier処理
	 * @return Supplier&lt;R&gt;
	 */
	public static <R> Supplier<R> to(ThrowableSupplier<? extends R> supplier){
		return ()->{
			try{
				return supplier.get();
			}catch(Throwable ex){
				throw new RuntimeException(ex);
			}
		};
	}
}
